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
package org.yass.main.controller{
	import flash.events.IEventDispatcher;
	
	import mx.events.DataGridEvent;
	
	import org.yass.debug.log.Console;
	import org.yass.main.events.TrackEvent;
	import org.yass.main.model.interfaces.IPlayListModel;
	
	public class PlayListController{
		private var view:IEventDispatcher;
		private var model:IPlayListModel;
		public function PlayListController(_view:IEventDispatcher, _model:IPlayListModel){
			this.view = _view;
			this.model = _model;
			view.addEventListener(TrackEvent.TRACK_PLAY, onTrackPlay);
			view.addEventListener(TrackEvent.TRACK_CLICK, onTrackClick);
			view.addEventListener(DataGridEvent.HEADER_RELEASE, onHeaderRelease);
		}
		public function destroy():void{
			view.removeEventListener(TrackEvent.TRACK_PLAY, onTrackPlay);
			view.removeEventListener(TrackEvent.TRACK_CLICK, onTrackClick);
			
		}
		
		private function onTrackPlay(evt:TrackEvent):void{
			Console.log("controller.PlayList.onTrackPlay trackIndex=" +evt.trackIndex);
			model.playTrack(evt.trackIndex);			
		}
		private function onTrackClick(evt:TrackEvent):void{
			Console.log("controller.PlayList.onTrackCick trackIndex=" +evt.trackIndex);
			model.selectTrack(evt.trackIndex);			
		}
		private function onHeaderRelease(event:DataGridEvent):void {
			Console.log("controller.PlayList.headerRelease column=" + event.dataField.toString());
			model.sortColumn(event.dataField.toString());
			event.preventDefault();

		}
	}
}