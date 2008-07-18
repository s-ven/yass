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
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.events.BrowserEvent;
	import org.yass.main.model.BrowserModel;
	import org.yass.util.tree.Value;

    [Bindable]
	public class BrowserView extends EventDispatcher{
		public var model:BrowserModel;
		private var _genre:DataGrid;
		private var _artist:DataGrid;
		private var _album:DataGrid; 
		private var _playlistView:PlayListView;
		public function BrowserView(genre:DataGrid, artist:DataGrid, album:DataGrid, playlistView:PlayListView){
			Console.log("view.BrowserView :: Init " + playlistView);
			this._genre = genre;
			this._artist = artist;  
			this._album = album; 
			model = Yass.browser;
			genre.dataProvider = createAllRow("genre", model.genreArray);
			artist.dataProvider = createAllRow("artist", model.artistArray);
			album.dataProvider = createAllRow("album", model.albumArray);
			this._playlistView = playlistView; 
			this._playlistView.model = Yass.library;
			genre.addEventListener(ListEvent.ITEM_CLICK, onItemClick);
			artist.addEventListener(ListEvent.ITEM_CLICK, onItemClick);
			album.addEventListener(ListEvent.ITEM_CLICK, onItemClick);
			model.addEventListener(BrowserEvent.REFRESHED, onRefreshed);
		}
		private function onItemClick(evt:ListEvent):void{
			Console.log("view.BrowserView.onItemClick id:"+evt.currentTarget.id);
			model.browseBy(evt.currentTarget.id, evt.currentTarget.selectedIndices, evt.currentTarget.selectedItems);
		}
		private function onRefreshed(evt:BrowserEvent):void{
			Console.log("view.BrowserView.onRefreshed types:" + evt.types);
			if(evt.containsType("genre"))
				_genre.dataProvider = createAllRow("genre", model.genreArray);
			if(evt.containsType("artist"))
				_artist.dataProvider = createAllRow("artist", model.artistArray);
			if(evt.containsType("album"))
				_album.dataProvider = createAllRow("album", model.albumArray);
		}
		private function createAllRow(label:String, arrCol:ArrayCollection):Array{
			var arr:Array = new Array();
			if(arrCol.length > 1){
				var allRow:Object = new Object();
				allRow.value = "All (" + arrCol.length + " " + label+"s)";
				allRow.id = -1;
				arr.push(allRow)
			}
			arr = arr.concat(arrCol.toArray());
			return arr;
		}
		public function onClickPlayList(type:String, val:Value):void{
			Console.group("view.BrowserView.onClickPlayList type:"+type+  ", val:"+ val);
			model.browseBy(type, this._artist.selectedIndices, [val])	
			this["_" + type].dataProvider = createAllRow(type, model[type+"Array"]);
			this["_" + type].selectedItem = val;
			this["_" + type].scrollToIndex(this["_"+type].selectedIndex)
			Console.groupEnd()		
		}
	}
} 	