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
package org.yass.main.model
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.formatters.DateFormatter;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.utils.ObjectProxy;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.events.PlayerEvent;
	import org.yass.main.events.TrackEvent;
	import org.yass.main.model.interfaces.IPlayListModel;
	
	public class PlayListModel extends EventDispatcher implements IPlayListModel{
        public var shuffledTracks:ArrayCollection= new ArrayCollection();
        public var shuffledListPosition:int; 
		public var httpService : HTTPService = new HTTPService();
		
		private var _playListId:String;
		private var _trackIndex:Number = -1;
   		private var _datas:ArrayCollection;
   		private var _datasProxy:ObjectProxy;
		public function PlayListModel(){			
        	Console.log("model.PlayListModel :: Init");
        	// TODO :: Find another way to generate time based GUI!!!!!!!!!!!!!
			var df:DateFormatter	 = new DateFormatter();
			df.formatString="HH:NN:SS";
			_playListId = df.format(new Date());
 			httpService.method = "POST";
		}
		
   		public function get datas():Object{
   			return _datas;
   		}
   		public function set datas(val:Object):void{
   			_datas = new ArrayCollection();
			if(val is XMLList)
				for(var i:Object in val)
					datas.addItem(new ObjectProxy(new XMLTrack(val[i])));
			else if(val is ArrayCollection)
				for(var i:Object in val)
					datas.addItem(new ObjectProxy(new XMLTrack(val[i])));
			else
				datas.addItem(new ObjectProxy(new XMLTrack(val as XML)));
   		}
    	public function set trackIndex(value:Number):void{
    		_trackIndex = value;
    	}
		
		public function set playListId(val:String):void{
			this._playListId = val; 
 			httpService.url = "/yass/playlist_show.do";
		}
		public function get playListId():String{
			return _playListId;
		}
        public function get selectedTrack():Object{
        	if(datas && trackIndex !=-1 && trackIndex < datas.length)
	        	return (datas[trackIndex] as ObjectProxy).valueOf() as XMLTrack;
        	return null;
        }
        public function get length():Number{
        	return datas.length;
        }
        
 		public function get trackIndex():Number{
 			return _trackIndex;
 		}
        private function getPreviousShuffledTrack():Number{
        	if(shuffledTracks.length > 1 && shuffledListPosition > 1)
        		return shuffledTracks.getItemAt((shuffledListPosition -= 1) -1) as Number;
        	return trackIndex;
    	}     
        private function getNextShuffledTrack():Number{
        	if(!(shuffledTracks.length > 1 && shuffledListPosition < shuffledTracks.length))
        		shuffledTracks.addItem(Math.ceil( 
        		( 1 - Math.random()) * length) - 1);
        	shuffledListPosition += 1;
        	return shuffledTracks.getItemAt(shuffledListPosition-1) as Number;
    	}
        public function getNextTrack(shuffle:Boolean, loop:Boolean):Object{
        	Console.log("model.PlayList.getNextTrack");
            if(Yass.player.shuffle)
	           	trackIndex = getNextShuffledTrack();
	        else{
				if(trackIndex < length - 1)
					trackIndex += 1;
				else if(loop)
					trackIndex = 0;
				else
					return null;
	                	 
	        }
			return selectedTrack;
        }
        
        public function getPreviousTrack(shuffle:Boolean, loop:Boolean):Object{
        	Console.log("model.PlayList.getPreviousTrack");
        	if(shuffle)
        		trackIndex = getPreviousShuffledTrack();
        	else
        	   	if(trackIndex > 0)
               		trackIndex -= 1;
            	else if(loop)
                	trackIndex = length -1;
				else
					return null;
			return selectedTrack;
		}        
  		/**
  		 *  This will play the request track,
  		 */
  		public function playTrack(_trackIndex:Number):void{ 
			this.trackIndex = _trackIndex;
  			Console.group("group.PlayList.playTrack trackIndex:"  +trackIndex+ ", playListId:"+ playListId);
			var wasPlaying = Yass.player.isPlaying;
			// loads the player with the current PlayList and track
			loadToPlayer()
			Yass.player.playTrack(selectedTrack)
			// If the Player is shuffling, will add the selected track to the random list 
 	       	if(Yass.player.shuffle){
	       		while(shuffledTracks.length > shuffledListPosition)
	       			shuffledTracks.removeItemAt(shuffledListPosition);
	       		shuffledListPosition +=1;
	      		shuffledTracks.addItem(trackIndex);
			} 
			Console.groupEnd();
		}	  		
  		public function selectTrack(_trackIndex:Number):void{
  			Console.group("model.PlayList.selectTrack trackIndex:"  +_trackIndex+ ", playListId:"+ playListId);
  			this.trackIndex = _trackIndex;
  			// If a track is selected and the current player is not playing, wll cause the display to be updated
  			if(!Yass.player.loadedPlayList)
  				loadToPlayer()
			if(Yass.player.loadedPlayList.playListId == playListId && !Yass.player.isPlaying && !Yass.player.isPaused)
				Yass.player.loadedTrack = selectedTrack;
			Console.groupEnd();
		}
		/**
		 * Adds a listener to the player and set this to be the loader
		 */
		private function loadToPlayer():void{
			Console.log("model.PlayList.loadToPlayer");
			if(Yass.player.loadedPlayList)
				(Yass.player.loadedPlayList as PlayListModel).unLoadFromPlayer();
			Yass.player.addEventListener(PlayerEvent.TRACK_LOADED, onPlayerEvent);
			Yass.player.addEventListener(PlayerEvent.PLAYING, onPlayerEvent);
			Yass.player.addEventListener(PlayerEvent.STOPPED, onPlayerEvent);
  			Yass.player.loadedPlayList = this;
		} 
		private function unLoadFromPlayer():void{
			Console.log("model.PlayList.unLoadFromPlayer");
			Yass.player.removeEventListener(PlayerEvent.TRACK_LOADED, onPlayerEvent);
			Yass.player.removeEventListener(PlayerEvent.PLAYING, onPlayerEvent);
			Yass.player.removeEventListener(PlayerEvent.STOPPED, onPlayerEvent);
		}
		/**
		 * Will dispatch a TrackEvent in order to refresh the view (Plate, PlayList) 
		 */
		private function onPlayerEvent(evt:Event):void{
			if(Yass.player.loadedPlayList == this)
				this.dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, trackIndex, this));
		}
	}    
}