package org.yass.main.model
{
	import flash.events.Event;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	
	import org.yass.Yass;
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
			if(_filteredText && _filteredText.length > 0)
				filterFunction = function(row:Object):Boolean{
							var ret : Boolean = true;
							if (genreSelected.length != 0)
							 	ret = ret && genreSelected.lastIndexOf(row.genre) != -1
							if (albumSelected.length != 0)
							 	ret = ret && albumSelected.lastIndexOf(row.album) != -1
							if (artistSelected.length != 0)
							 	ret = ret && artistSelected.lastIndexOf(row.artist) != -1
							 if(ret)
								return ret && _filteredText.every(function(obj:Object, index:int, arr:Array):Boolean{return row.allFields.indexOf(obj) != -1});
							return ret;
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
				}
			refresh();
			Console.groupEnd();
			if(toDispatch)
				dispatchEvent(toDispatch)
		}
		private var _filteredText:Array;
		private var _genreFiltered:Array;
		private var _albumFiltered:Array;
		private var _artistFiltered:Array;
		public function set filteredText(txt:String):void{
			_filteredText = txt.toLowerCase().split(/\W/);;
			Console.group("model.Library.set filteredText:" + _filteredText);
			_genreFiltered = new Array();
			_artistFiltered = new Array();
			_albumFiltered = new Array();
			if(_filteredText.length > 0){
				Console.log("Non empty text");
				filterFunction = function(row:Object):Boolean{
					var ret:Boolean= true
					_filteredText.forEach(function(obj:Object, index:int, arr:Array):void{ret = ret && row.allFields.indexOf(obj) != -1});
						if(ret){
							if(_genreFiltered.indexOf(row.genre) == -1)
								_genreFiltered.push(row.genre)
							if(_artistFiltered.indexOf(row.artist) == -1)
								_artistFiltered.push(row.artist)
							if(_albumFiltered.indexOf(row.album) == -1)
								_albumFiltered.push(row.album)
						}
					return ret
				}
			}
			else {
				Console.log("Empty text");
				filterFunction = function(row:Object):Boolean{
					if(_genreFiltered.indexOf(row.genre) == -1)
						_genreFiltered.push(row.genre)
					if(_artistFiltered.indexOf(row.artist) == -1)
						_artistFiltered.push(row.artist)
					if(_albumFiltered.indexOf(row.album) == -1)
						_albumFiltered.push(row.album)
					return true
				}; 
			}
			refresh()
			Console.log("list : " + length)
			if(checkSelected("album"));
			else if(checkSelected("artist"));
			else if(checkSelected("genre"));
			populateTree()
			Console.groupEnd();
		}
		private function checkSelected(type:String):Boolean{
			var filtered:Array= this["_"+type+"Filtered"];
			if(this[type+"Selected"].length > 0 && this[type+"Selected"].every(
						function(obj:Object, index:int, arr:Array):Boolean{
							return arr.indexOf(obj) != -1
							}))	{
				browseBy(type, this[type+"Selected"]);
				return true;
			}
			return false;
		}
	}
}	