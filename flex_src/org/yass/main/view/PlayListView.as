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
 package org.yass.main.view{
    import flash.events.Event;
    import flash.utils.Dictionary;
    import flash.utils.setTimeout;
    
    import mx.controls.DataGrid;
    import mx.controls.dataGridClasses.DataGridColumn;
    import mx.events.CollectionEvent;
    import mx.events.FlexEvent;
    import mx.events.ListEvent;
    
    import org.yass.Yass;
    import org.yass.debug.log.Console;
    import org.yass.main.controller.PlayListController;
    import org.yass.main.events.TrackEvent;
    import org.yass.main.model.interfaces.IPlayListModel;
    
    public class PlayListView  extends DataGrid   {
				
		private var _model:IPlayListModel;
		private var playListColumns:Dictionary = new Dictionary();
		private var controller:PlayListController;

 		public function PlayListView(){	
 			super();
 			Console.log("view.PlayList :: Init");
			this.doubleClickEnabled=true;
			this.allowMultipleSelection=true; 
			this.dragEnabled=true;
 			this.addEventListener(ListEvent.ITEM_CLICK, onClick);
 			this.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, onDoubleClick);
 			this.addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
 		}
 		
 		override protected function commitProperties():void{
 			super.commitProperties();
 		}
 		
 		/*
 		* The playList model associated with this view.
 		* called when a playlist have been returned from the Model after a server call
 		*/
		public function set model(value:IPlayListModel):void{
			if(model)
				_model.removeEventListener(TrackEvent.TRACK_SELECTED, onTrackSelected);
			this._model = value;
			dataProvider = _model; 
			_model.addEventListener(TrackEvent.TRACK_SELECTED, onTrackSelected);
			// Remove the eventLoaders for a potentially previous controller
			if(controller)
				controller.destroy();
			this.controller = new PlayListController(this, value);
		}
		public function get model():IPlayListModel{ 
			return _model;
		}		
	 	

        /*
        * Called when a click has occured on the playlist, 
        * will dispatch a TrackEvent.TRACK_PLAY event to the MVC Controller 
        */
        public function onDoubleClick(event:Event):void{
			Console.group("view.PlayList.onDoubleClick");
		  	if(enabled){
				dispatchEvent(new TrackEvent(TrackEvent.TRACK_PLAY, selectedIndex, _model));
				this.collectionChangeHandler(new CollectionEvent(CollectionEvent.COLLECTION_CHANGE));
		  	}
			Console.groupEnd();
        }
        /*
        * Called when a click has occured on the playlist, 
        * will dispatch a TrackEvent.TRACK_CLICK event to the MVC Controller 
        */
        public function onClick(event:Event):void{
			Console.group("view.PlayList.onClick");
        	if(enabled)
        		dispatchEvent(new TrackEvent(TrackEvent.TRACK_CLICK, selectedIndex, _model));
			Console.groupEnd();
        }
        // TODO : Move this to the model
		public function autoPlay():void{
			Console.group("view.PlayList.autoPlay :: requested");
			addEventListener(Event.ENTER_FRAME, function autoPlayAfterRefresh():void{
				if(enabled){
					Yass.player.loadedPlayList = _model;
					Console.log("view.PlayList.autoPlayDatagrid");
					removeEventListener(Event.ENTER_FRAME, autoPlayAfterRefresh);
					Yass.player.stop();
					Yass.player.loadedPlayList = model;
					Yass.player.play();
					this.collectionChangeHandler(new CollectionEvent(CollectionEvent.COLLECTION_CHANGE));
					Console.log("view.PlayList.autoPlayDatagrid :: AutoPlay : OK");
				}
			});		
			Console.groupEnd();	
		}
		/**
		 * Called when an event occured from the Model (eg end of track, previous track
		 * Will cause the previously selected track to be displayed 
		 */
		public function onTrackSelected(evt:TrackEvent):void{
			Console.group("view.PlayList.onTrackSelected trackIndex=" + evt.trackIndex);
			if(evt.playList == this.model){
				this.collectionChangeHandler(new CollectionEvent(CollectionEvent.COLLECTION_CHANGE));
				setTimeout(function ():void{scrollToIndex(evt.trackIndex);}, 0);
			}	
			Console.groupEnd();
		}
		/**
		 * 
		 */
		private function onCreationComplete(evt:FlexEvent):void{
			Console.group("view.PlayList.onCreationComplete");
			for each(var column:DataGridColumn in columns){
				if(column.dataField){
					Console.log(" Saving " +column.dataField);
					playListColumns[column.dataField] = column
				}
			}
			Console.groupEnd();
			
			
		}
    }
}