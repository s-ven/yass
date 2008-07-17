package org.yass.main.model
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.events.BrowserEvent;
	import org.yass.util.tree.*;
	[Bindable]
	public class BrowserModel extends EventDispatcher{
		public var genreArray:ArrayCollection; 
		public var artistArray:ArrayCollection;
		public var albumArray:ArrayCollection; 
		public static var dict:Dictionary = new Dictionary();
		public var selectedArtists: Array = new Array();
		public var selectedGenres: Array = new Array();
		public var selectedAlbums: Array = new Array();
		private var tree:Tree;
		//private var httpService:HTTPService = new HTTPService();
		private var sort:Sort = new Sort();
		public function BrowserModel():void{
			Console.log("model.BrowserModel :: Init");
/* 			httpService.url = "/yass/library_get_tree.do";
			httpService.resultFormat = "e4x";
			httpService.addEventListener(ResultEvent.RESULT, populateTree);
			httpService.send(); */
			populateTree();
			sort.fields = [new SortField("value")];
		}
		private function populateTree():void{
			Console.group("model.BrowserModel.populateTree");
			tree = new Tree(new XML(Yass.libTreeData));
			genreArray = createArray("GENRE");
			Console.log("	genres.length:"+genreArray.length);
			artistArray = createArray("ARTIST");
			Console.log("	artists.length:"+artistArray.length);
			albumArray = createArray("ALBUM");
			Console.log("	albums.length:"+albumArray.length);
			Console.groupEnd();
			dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["genre","artist", "album"]));
		}
		private function createArray(type:String):ArrayCollection{
			var array:ArrayCollection = tree.getArrayByType(type);
			array.sort = sort;
			array.refresh();
			for(var i:Object in array){
				dict[array[i].type + "_" + array[i].id] = array[i]
			}
			return array;
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
		public function browseBy(type:String, selectedIndices:Array, selectedItems:Array):void{
			Console.group("model.BrowserModel.browseBy : type="+type);
			Console.log("Items : " + selectedItems);
			if(selectedItems[0].id == -1){
				if(type == "genre"){
					selectedGenres = new Array();
					selectedArtists = new Array();
					selectedAlbums = new Array();
					artistArray.filterFunction = null;
					artistArray.refresh();
					albumArray.filterFunction = null;
					albumArray.refresh();
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["artist", "album"]));			
				}else if(type == "artist"){
					selectedArtists = new Array();
					selectedAlbums = new Array();
					if(selectedGenres.length >1)
						filterChild(albumArray, selectedGenres);	
					else{
						albumArray.filterFunction = null;
						albumArray.refresh();
					}	
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["album"]));			
				} if(type == "album")
					selectedAlbums = new Array();
			}
			else{	
				if(type=="genre"){
					selectedGenres = selectedItems;
					selectedArtists = new Array();
					selectedAlbums = new Array();
					filterChild(artistArray, selectedItems);
					filterChild(albumArray, selectedItems);
					albumArray.refresh();;
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["artist", "album"]));
				} else if(type=="artist"){
					selectedArtists = selectedItems;
					selectedAlbums = new Array();
					var items:Array = new Array();
					for each(var item:Object in selectedItems)
						if(item is ValueMultiple){
							for each(var subItem:Value in item.values)
								if(selectedGenres.length > 0 && selectedGenres.lastIndexOf(subItem.parent) != -1)
									items.push(subItem);
								else if(selectedGenres.length ==0)
									items.push(subItem);
						}
						else
							items.push(item)
					filterChild(albumArray, items);
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["album"]));
				} else if(type == "album")
					selectedAlbums = selectedItems;
			}
			if(Yass.library){
				Yass.library.filterFunction = function(row:Object):Boolean{
							var ret : Boolean = true;
							if (selectedGenres.length != 0)
							 	ret = ret && selectedGenres.lastIndexOf(row.genre) != -1
							if (selectedAlbums.length != 0)
							 	ret = ret && selectedAlbums.lastIndexOf(row.album) != -1
							if (selectedArtists.length != 0)
							 	ret = ret && selectedArtists.lastIndexOf(row.artist) != -1
							return ret;
				}
				Yass.library.refresh();
				dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED_PLAYLIST));
			}
			Console.groupEnd();
		}
	}
}	