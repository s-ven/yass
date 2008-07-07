package org.yass.main.model
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.debug.log.Console;
	import org.yass.main.events.BrowserEvent;
	import org.yass.util.tree.*;
	public class BrowserModel extends EventDispatcher
	{
		public var genres:ArrayCollection; 
		public var artists:ArrayCollection;
		public var albums:ArrayCollection; 
		public var playlistModel:PlayListModel = new PlayListModel();
		private var selectedArtists: Array = new Array();
		private var selectedGenres: Array = new Array();
		private var selectedAlbums: Array = new Array();
		private var tree:Tree;
		private var httpService:HTTPService = new HTTPService();
		private var sort:Sort = new Sort();
		public function BrowserModel():void{
			Console.log("model.BrowserModel :: Init");
			httpService.url = "/lib_tree.xml";
			httpService.resultFormat = "e4x";
			httpService.addEventListener(ResultEvent.RESULT, populateTree);
			httpService.send();
			this.playlistModel = playlistModel as PlayListModel;
			this.playlistModel.httpService.url = "/yass/library_browse.do";
			sort.fields = [new SortField("name")];
		}
		private function populateTree(evt:Event):void{
			Console.group("model.BrowserModel.populateTree");
			tree = new Tree(httpService.lastResult as XML);
			genres = createArray("genre");
			Console.log("	genres.length:"+genres.length);
			artists = createArray("artist");
			Console.log("	artists.length:"+artists.length);
			albums = createArray("album");
			Console.log("	albums.length:"+albums.length);
			Console.groupEnd();
			dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["genres","artists", "albums"]));
		}
		private function createArray(type:String):ArrayCollection{
			var array:ArrayCollection = tree.getArrayByType(type);
			array.sort = sort;
			array.refresh();
			return array;
		}
		private function filterSub(sub:ArrayCollection,selectedItems:Array):void{
			sub.filterFunction = 	function(rowVal:Object):Boolean{
									if(rowVal.id !=-1){
										for each(var toFilter:Value in selectedItems)
											if (rowVal.hasParent(toFilter))
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
				if(type == "genres"){
					selectedGenres = new Array();
					selectedArtists = new Array();
					selectedAlbums = new Array();
					artists = createArray("artist");
					albums = createArray("album");		
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["artists", "albums"]));			
				}else if(type == "artists"){
					selectedArtists = new Array();
					selectedAlbums = new Array();
					filterSub(albums, selectedGenres);					
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["albums"]));			
				}
			}
			else{	
				if(type=="genres"){
					selectedGenres = selectedItems;
					selectedArtists = new Array();
					selectedAlbums = new Array();
					filterSub(artists, selectedItems);
					filterSub(albums, selectedItems);
					albums.refresh();;
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["artists", "albums"]));
				} else if(type=="artists"){
					selectedArtists = selectedItems;
					selectedAlbums = new Array();
					var items:Array = new Array();
					for each(var item:Object in selectedItems)
						if(item is Value)
							items.push(item)
						else
							for each(var subItem:Value in item.values)
								if(selectedGenres.lastIndexOf(subItem.parent) != -1)
									items.push(subItem);
					
					filterSub(albums, items);
					dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED, ["albums"]));
				} else if(type == "albums")
					selectedAlbums = selectedItems;
			}
			var obj:Object = new Object();
			obj.genres = createRequestParam(selectedGenres);
			obj.albums = createRequestParam(selectedAlbums)
			obj.artists = createRequestParam(selectedArtists)
			playlistModel.httpService.cancel();
			playlistModel.httpService.send(obj);
			dispatchEvent(new BrowserEvent(BrowserEvent.REFRESHED_PLAYLIST));
			Console.groupEnd();
		}
		private function createRequestParam(selectedArr:Array):Array{
			var arr:Array = new Array();
			for each(var val:Object in selectedArr)
				arr.push(val.name);
			return arr;
		}
	}
}