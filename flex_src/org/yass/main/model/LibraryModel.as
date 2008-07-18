package org.yass.main.model
{
	import flash.events.Event;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.yass.debug.log.Console;
	import org.yass.main.events.BrowserEvent;
	import org.yass.util.tree.*;
	public class LibraryModel extends PlayListModel{
		public var genreArray:ArrayCollection; 
		public var artistArray:ArrayCollection;
		public var albumArray:ArrayCollection; 
		public static var dict:Dictionary = new Dictionary();
		public var artistSelected: Array = new Array();
		public var genreSelected: Array = new Array();
		public var albumSelected: Array = new Array();
		private var _tree:Tree;
		private var _sort:Sort = new Sort();
		private var _filteredText:Array;
		private var _genreFiltered:Array;
		private var _albumFiltered:Array;
		private var _artistFiltered:Array;
		private var _searchScope:String=SearchScope.ALL;
		public function LibraryModel(libTreeData:Object, libraryData:Object):void{
			Console.log("model.Library :: Init");
			_tree = new Tree(new XML(libTreeData));
			populateTree();
			_sort.fields = [new SortField("value")];
			datas = new XML(libraryData).children()
		}
		private function populateTree():void{
			Console.group("model.Library.populateTree");
			createArray("genre");
			createArray("artist");
			createArray("album");
			Console.groupEnd();
			dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["genre","artist", "album"]));
		}
		private function createArray(type:String):void{
			var array:ArrayCollection;
			if(_filteredText == null){
				array = this[type+"Array"] = _tree.getArrayByType(type);
				for(var i:Object in array)
					dict[array[i].type + "_" + array[i].id] = array[i]
			}
			else 
				array = this[type+"Array"] = new ArrayCollection(this["_" + type + "Filtered"]);
			array.sort = _sort;
			array.refresh();
			Console.log("type:"+type+", length:"+array.length);
		}
		private function filterChild(sub:ArrayCollection,selectedItems:Array):void{
			Console.log("Filtering child with : " + selectedItems);
			sub.filterFunction = 	function(rowVal:Value):Boolean{
									if(rowVal.id !=-1){
										for each(var toFilter:Value in selectedItems)
											if (toFilter.isChildOf(rowVal))
												return true;
										return false; }
									return true;
									};
			sub.refresh();
		}
		public function browseBy(type:String, selectedItems:Array):void{
			Console.group("model.Library.browseBy : type="+type);
			Console.log("Items : " + selectedItems);
			var toDispatch:Event;
			if(selectedItems[0].id == -1){
				if(type == "genre"){
					genreSelected = new Array();
					artistSelected = new Array();
					albumSelected = new Array();
					artistArray.filterFunction = null;
					artistArray.refresh();
					albumArray.filterFunction = null;
					albumArray.refresh();
					toDispatch = new BrowserEvent(BrowserEvent.REFRESHED, ["artist", "album"]);			
				}else if(type == "artist"){
					artistSelected = new Array();
					albumSelected = new Array();
					if(genreSelected.length >1)
						filterChild(albumArray, genreSelected);	
					else{
						albumArray.filterFunction = null;
						albumArray.refresh();
					}	
					toDispatch = new BrowserEvent(BrowserEvent.REFRESHED, ["album"]);			
				}
				if(type == "album")
					albumSelected = new Array();
			}
			else{	
				if(type=="genre"){
					genreSelected = selectedItems;
					artistSelected = new Array();
					albumSelected = new Array();
					filterChild(artistArray, selectedItems);
					filterChild(albumArray, selectedItems);
					albumArray.refresh();;
					toDispatch = new BrowserEvent(BrowserEvent.REFRESHED, ["artist", "album"]);
				} else if(type=="artist"){
					artistSelected = selectedItems;
					albumSelected = new Array();
					var items:Array = new Array();
					for each(var item:Object in selectedItems)
						if(item is ValueMultiple){
							for each(var subItem:Value in item.values)
								if(genreSelected.length > 0 && genreSelected.lastIndexOf(subItem.parent) != -1)
									items.push(subItem);
								else if(genreSelected.length ==0)
									items.push(subItem);
						}
						else
							items.push(item)
					filterChild(albumArray, items);
					toDispatch = new BrowserEvent(BrowserEvent.REFRESHED, ["album"]);
				}
				else if(type == "album")
					albumSelected = selectedItems;
			}
			createBrowseByFilterFunction()
			refresh();
			Console.groupEnd();
			if(toDispatch)
				dispatchEvent(toDispatch)
		}
		public function set filteredText(txt:String):void{
			_filteredText = txt.toLowerCase().split(/\W/).filter(function(row:Object, index:int, arr:Array){return row && row != "";})
			filterText();
		}
		private function filterText():void{
			Console.group("model.Library.set filteredText:" + _filteredText);
			_genreFiltered = new Array();
			_artistFiltered = new Array();
			_albumFiltered = new Array();
			createTextFilterFunction();
			refresh()
			Console.log("list : " + length)
			populateTree()
			Console.groupEnd();
		}
		public function set searchScope(scope:String):void{
			_searchScope = scope;
			if(_filteredText && _filteredText.length !=0)
				filterText();
		}
		public function get searchScope():String{
			return _searchScope;
		}
		private function createTextFilterFunction():void{
			var selFilterFunction:Function = function(row:Object):Boolean{
				return 	(genreSelected.length == 0 || genreSelected.indexOf(row.genre) !=-1) &&
						(artistSelected.length == 0 || artistSelected.indexOf(row.artist) !=-1) &&
						(albumSelected.length == 0 || albumSelected.indexOf(row.album) !=-1);
				};
			if(_filteredText != null && _filteredText.length > 0){
				var ffunction:Function = getFilterFunction();
				filterFunction = function(row:Object):Boolean{
						if(_filteredText.every(ffunction, row)){
							if(_genreFiltered.indexOf(row.genre) == -1)
								_genreFiltered.push(row.genre)
							if(_artistFiltered.indexOf(row.artist) == -1)
								_artistFiltered.push(row.artist)
							if(_albumFiltered.indexOf(row.album) == -1)
								_albumFiltered.push(row.album)
							return selFilterFunction(row);
						}
					return false;
				}
			}
			else 
				filterFunction = function(row:Object):Boolean{
					if(_genreFiltered.indexOf(row.genre) == -1)
						_genreFiltered.push(row.genre)
					if(_artistFiltered.indexOf(row.artist) == -1)
						_artistFiltered.push(row.artist)
					if(_albumFiltered.indexOf(row.album) == -1)
						_albumFiltered.push(row.album)
					return selFilterFunction(row);
				}; 
		}
		
		private function getFilterFunction():Function{
			switch(searchScope){
				case SearchScope.ALL :
					return function(obj:Object, index:int, arr:Array):Boolean{return this.allFields.indexOf(obj) != -1};
					break;
				case SearchScope.ARTISTS :
					return function(obj:Object, index:int, arr:Array):Boolean{return this.artist.lowerCaseValue.indexOf(obj) != -1};
					break;
				case SearchScope.ALBUMS :
					return function(obj:Object, index:int, arr:Array):Boolean{return this.album.lowerCaseValue.indexOf(obj) != -1};
					break;
				case SearchScope.TITLE :
					return function(obj:Object, index:int, arr:Array):Boolean{return this.lowerCaseTitle.indexOf(obj) != -1};
					break;
				};
			return null;
		}
		
		private function createBrowseByFilterFunction():void{
			if(_filteredText != null  && _filteredText.length > 0){
				var ffunction:Function = getFilterFunction();
				filterFunction = function(row:Object):Boolean{
							var ret : Boolean = true;
							if (genreSelected.length != 0)
							 	ret = ret && genreSelected.lastIndexOf(row.genre) != -1
							if (albumSelected.length != 0)
							 	ret = ret && albumSelected.lastIndexOf(row.album) != -1
							if (artistSelected.length != 0)
							 	ret = ret && artistSelected.lastIndexOf(row.artist) != -1
							 if(ret)
								return ret && _filteredText.every(ffunction, row);
							return ret;
				}
			}
			else
				filterFunction = function(row:Object):Boolean{
							var ret : Boolean = true;
							if (genreSelected.length != 0)
							 	ret = ret && genreSelected.lastIndexOf(row.genre) != -1
							if (albumSelected.length != 0)
							 	ret = ret && albumSelected.lastIndexOf(row.album) != -1
							if (artistSelected.length != 0)
							 	return ret && artistSelected.lastIndexOf(row.artist) != -1
							return ret;
				};
		}
	}
}	