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
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.formatters.DateFormatter;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.MP3;
	import org.yass.debug.log.Console;
	import org.yass.main.events.TrackEvent;
	import org.yass.main.interfaces.model.IPlayListModel;
	
	public class PlayListModel extends EventDispatcher implements IPlayListModel{
        public var shuffledTracks:ArrayCollection= new ArrayCollection();
        public var shuffledListPosition:int; 
		public var _playListId:String;
		private var _trackIndex:Number = -1;
		public var httpService : HTTPService = new HTTPService();
		
		public function PlayListModel(){			
        	Console.info("model.PlayListModel :: Init");
        	
        	// TODO :: Find another way !!!!!!!!!!!!!
			var df:DateFormatter	 = new DateFormatter();
			df.formatString="HH:NN:SS";
			_playListId = df.format(new Date());
        	// If the player is not yet currently plaiying, loading this playlist to it
        	// Otherwise, the loaded playlist will keep on reading, being paused
			if(!(MP3.player.isPlaying && !MP3.player.isPaused)){
				Console.log("model.PlayListModel.loadPlayList :: MP3 player seems to be blank, loading it");
				MP3.player.loadedPlayList = this;
			}
 			httpService.url = "/yass/library_playlist.do";
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
		
        private function getPreviousShuffledTrack():Number{
        	if(shuffledTracks.length > 1 && shuffledListPosition > 1)
        		return shuffledTracks.getItemAt((shuffledListPosition -= 1) -1) as Number;
        	stop();
        	return trackIndex;
    	}
 		    	
    	public function get isPaused():Boolean{
    		return MP3.player.isPaused; 
    	}
    	public function get isPlaying():Boolean{
    		return MP3.player.isPlaying; 
    	}
    	    	        
        public function stop():void{
        	MP3.player.stop();
        }
        private function getNextShuffledTrack():Number{
        	if(!(shuffledTracks.length > 1 && shuffledListPosition < shuffledTracks.length))
        		shuffledTracks.addItem(Math.ceil( 
        		( 1 - Math.random()) * length) - 1);
        	shuffledListPosition += 1;
        	return shuffledTracks.getItemAt(shuffledListPosition-1) as Number;
    	}
        public function getNextTrack(shuffle:Boolean, loop:Boolean):void{
        	Console.log("model.PlayList.getNextTrack");
            if(MP3.player.shuffle)
	           	trackIndex = getNextShuffledTrack();
	        else{
	            if(trackIndex < length - 1)
	            	trackIndex += 1;
	            else if(loop)
	                	trackIndex = 0;      
	        }
			dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, trackIndex, this));
        }
        
        public function getPreviousTrack(shuffle:Boolean, loop:Boolean):void{
        	Console.log("model.PlayList.getPreviousTrack");
        	if(shuffle)
        		trackIndex = getPreviousShuffledTrack();
        	else
        	   	if(trackIndex > 0)
               		trackIndex -= 1;
            	else if(loop)
                	trackIndex = length -1;
			dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, trackIndex, this));
		}        
        public function get selectedTrack():Object{
        	if(httpService.lastResult && trackIndex !=-1)
	        	return httpService.lastResult.tracks.track[trackIndex];
        	return null;
        }
        public function get length():Number{
        	return datas.length;
        }
        
        
 		public function get trackIndex():Number{
 			return _trackIndex;
 		}
   		   		
   		private function setDatas():void{
			datas = new ArrayCollection();
			if(httpService.lastResult.tracks)
				if(httpService.lastResult.tracks.track is ArrayCollection)
					 for(var i:Object in httpService.lastResult.tracks.track)
					 	datas.addItem(httpService.lastResult.tracks.track[i]);
				else
					datas.addItem(httpService.lastResult.tracks.track);
   		}
   		
   		public var _datas:ArrayCollection;
   		public function get datas():ArrayCollection{
   			return _datas;
   		}
   		public function set datas(val:ArrayCollection){
   			this._datas = val;
   		}
  		public function bindDataProvider(obj:Object):void{
  			Console.log("model.PlayList.bindDataProvider :: Binding dataProvider");
			obj.dataProvider = datas;
  			// add an event listener so that the dada object will be populated when the request is back
			httpService.addEventListener(ResultEvent.RESULT, function bind():void{
  				setDatas();
  				Console.log("model.PlayList.bindDataProvider :: Loaded " + datas.length + " tracks");
				obj.dataProvider = datas;
				obj.enabled = true;
				httpService.removeEventListener(ResultEvent.RESULT, bind);
	 		});
  		}
  		/**
  		 *  This will play the request track,
  		 */
  		public function playTrack(_trackIndex:Number):void{ 
			this.trackIndex = _trackIndex;
  			Console.log("model.PlayList.playTrack trackIndex="  +trackIndex+ ", playListId="+ playListId);
			MP3.player.stop();
			// loads the player with the current PlayList and track
			MP3.player.loadedPlayList = this;
			MP3.player.loadedTrack = selectedTrack;
			MP3.player.play();
			// If the Player is shuffling, will add the selected track to the random list 
 	       	if(MP3.player.shuffle){
	       		while(shuffledTracks.length > shuffledListPosition)
	       			shuffledTracks.removeItemAt(shuffledListPosition);
	       		shuffledListPosition +=1;
	      		shuffledTracks.addItem(trackIndex);
			} 
		}	
  		public function selectTrack(_trackIndex:Number):void{
  			Console.log("model.PlayList.selectTrack trackIndex="  +_trackIndex+ ", playListId="+ playListId);
  			this.trackIndex = _trackIndex;
  			// If a track is selected and the current player is not playing, wll cause the display to be updated
			if(MP3.player.loadedPlayList && MP3.player.loadedPlayList.playListId == playListId && !MP3.player.isPlaying && !MP3.player.isPaused)
				MP3.player.loadedTrack = selectedTrack;
		}	
	}    
}