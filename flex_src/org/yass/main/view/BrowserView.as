/* 
 Copyright (c) 2008 Sven Duzont sven.duzont@gmail.com> All rights reserved. 
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), 
 to deal in the Software without restriction, including without limitation 
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished 
 to do so, subject to the following conditions: The above copyright notice 
 and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", 
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.yass.main.view
{
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.controls.DataGrid;
	import mx.events.ListEvent;
	
	import org.yass.debug.log.Console;
	import org.yass.main.events.BrowserEvent;
	import org.yass.main.model.BrowserModel;

    [Bindable]
	public class BrowserView extends EventDispatcher
	{
		private var genres:DataGrid;
		private var artists:DataGrid;
		private var albums:DataGrid; 
		private var model:BrowserModel;
		private var playlistView:PlayListView;
		public function BrowserView(genres:DataGrid, artists:DataGrid, albums:DataGrid, playlistView:PlayListView){
			Console.log("view.BrowserView :: Init " + playlistView);
			this.genres = genres;
			this.artists = artists;  
			this.albums = albums; 
			model = new BrowserModel();
			this.playlistView = playlistView; 
			this.playlistView.model = model.playlistModel;
			genres.addEventListener(ListEvent.ITEM_CLICK, onItemClick);
			artists.addEventListener(ListEvent.ITEM_CLICK, onItemClick);
			albums.addEventListener(ListEvent.ITEM_CLICK, onItemClick);
			model.addEventListener(BrowserEvent.REFRESHED, onRefreshed);
			model.addEventListener(BrowserEvent.REFRESHED_PLAYLIST, onRefreshedPlayList);
		}
		private function onItemClick(evt:ListEvent):void{
			Console.log("view.BrowserView.onItemClick id:"+evt.currentTarget.id);
			model.browseBy(evt.currentTarget.id, evt.currentTarget.selectedIndices, evt.currentTarget.selectedItems);
		}
		private function onRefreshed(evt:BrowserEvent):void{
			Console.log("view.BrowserView.onRefreshed types:" + evt.types);
			if(evt.containsType("genres"))
				genres.dataProvider = createAllRow("genres", model.genres);
			if(evt.containsType("artists"))
				artists.dataProvider = createAllRow("artists", model.artists);
			if(evt.containsType("albums"))
				albums.dataProvider = createAllRow("albums", model.albums);
		}
		private function onRefreshedPlayList(evt:BrowserEvent):void{
			Console.log("view.BrowserView.onRefreshedPlayList types:" + evt.types);
			model.playlistModel.bindDataProvider(playlistView);
		}
		private function createAllRow(label:String, arrCol:ArrayCollection):Array{
			var arr:Array = new Array();
			if(arrCol.length > 1){
				var allRow = new Object();
				allRow.name = "All (" + arrCol.length + " " + label+")";
				allRow.id = -1;
				arr.push(allRow)
			}
				
			arr = arr.concat(arrCol.toArray());
			return arr;
		}
		
	}
} 	