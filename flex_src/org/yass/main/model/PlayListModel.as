package org.yass.main.model
{
	import flash.events.EventDispatcher;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayCollection;
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
			if(!MP3.player.loadedPlayList){
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
		public function tooglePlay():void{
        	if(trackIndex == -1){
        		trackIndex = 0;
				dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, trackIndex, this));
			}
        	if(isPlaying)
        		MP3.player.pause();
        	else{
        		if(isPaused)
        			MP3.player.play();
        	}
        }
        
        public function get selectedTrack():Object{
        	if(httpService.lastResult && trackIndex !=-1)
	        	return httpService.lastResult.tracks.track[trackIndex];
        	return null;
        }
        public function get length():Number{
        	if(httpService.lastResult)
        		return httpService.lastResult.tracks.track.length;
        	return 0;
        }
        
        
 		public function get trackIndex():Number{
 			return _trackIndex;
 		}
   		
  		public function bindDataProvider(obj:Object):void{
  			Console.log("model.PlayList.bindDataProvider :: Binding dataProvider");
			BindingUtils.bindProperty(obj, "dataProvider", httpService,  ["lastResult", "tracks", "track"]);
			httpService.addEventListener(ResultEvent.RESULT, function():void{
  				Console.log("model.PlayList.bindDataProvider :: HTTP Results " + httpService.lastResult);
				BindingUtils.bindProperty(obj, "dataProvider", httpService,  ["lastResult", "tracks", "track"]);
				obj.enabled = true;
	 		});
  		}
  		public function playTrack(_trackIndex:Number):void{
			this.trackIndex = _trackIndex;
  			Console.log("model.PlayList.playTrack trackIndex="  +trackIndex+ ", playListId="+ playListId);
			MP3.player.loadedPlayList = this;
			MP3.player.stop();
			MP3.player.loadedTrack = selectedTrack;
			MP3.player.play();
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
			if(MP3.player.loadedPlayList.playListId == playListId && !MP3.player.isPlaying && !MP3.player.isPaused)
				MP3.player.loadedTrack = selectedTrack;
		}	
	}    
}