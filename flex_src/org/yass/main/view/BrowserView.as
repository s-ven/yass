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
	import flash.utils.setTimeout;
	
	import mx.collections.ArrayCollection;
	import mx.controls.DataGrid;
	import mx.events.ListEvent;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.events.LibraryEvent;
	import org.yass.main.model.LibraryModel;
	import org.yass.util.tree.Value;

	public class BrowserView extends EventDispatcher{
		public var model:LibraryModel;
		private var _genre:DataGrid;
		private var _artist:DataGrid;
		private var _album:DataGrid; 
		public function BrowserView(genre:DataGrid, artist:DataGrid, album:DataGrid, playlistView:PlayListView){
			Console.log("view.BrowserView :: Init " + playlistView);
			model = Yass.library;
			init("genre", genre)
			init("artist", artist)
			init("album", album)
			playlistView.model = Yass.library;
			model.addEventListener(LibraryEvent.REFRESHED, onRefreshed);
		} 
		private function init(type:String, dg:DataGrid):void{
			this["_"+type] = dg;
			createAllRow(type);
			this["_"+type].addEventListener(ListEvent.ITEM_CLICK, onItemClick);
		}
		private function onItemClick(evt:ListEvent):void{
			Console.log("view.BrowserView.onItemClick id:"+evt.currentTarget.id);
			model.browseBy(evt.currentTarget.id, evt.currentTarget.selectedItems);
		}
		private function onRefreshed(evt:LibraryEvent):void{
			Console.group("view.BrowserView.onRefreshed types:" + evt.types);
			for each(var type:String in evt.types)
				createAllRow(type)
			Console.groupEnd();
		}
		private function createAllRow(type:String):void{
			Console.group("type:" + type);
			var arrCol:ArrayCollection = model[type+"Array"]
			var arr:Array = new Array();
			if(arrCol.length > 1){
				var allRow:Object = new Object();
				allRow.value = "All (" + arrCol.length + " " + type+"s)";
				allRow.id = -1;
				arr.push(allRow)
			}
			this["_" + type].dataProvider = arr.concat(arrCol.toArray());
			Console.log("length:"+this["_" + type].dataProvider.length)
			this["_"+type].selectedItems = model[type +"Selected"]
			if(this["_"+type].selectedIndex != -1){
				Console.log("selected index : " + this["_"+type].selectedIndex);
				setTimeout(function(zis:Object, type:String):void{zis["_" + type].scrollToIndex(zis["_"+type].selectedIndex)},200, this, type);
			}
			Console.groupEnd();
		}
		public function onClickPlayList(type:String, val:Value):void{
			Console.group("view.BrowserView.onClickPlayList type:"+type+  ", val:"+ val);
			model.browseBy(type, [val])	
			createAllRow(type);
			Console.groupEnd();
		}
	}
} 	