package org.yass.mp3
{
	import mx.binding.utils.BindingUtils;
	import mx.collections.ArrayCollection;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	public class PlayListLoader{
		public var currentTrack:Object; 
		public var shuffle:Boolean;
		public var loop:Boolean;
        public var shuffledTracks:ArrayCollection= new ArrayCollection();
        public var shuffledListPosition:int; 
        public var _playList:PlayList;
		public var _playListId:String;
		public var _selectedIndex:Number = -1;
		
		
 		public function get selectedIndex():Number{
 			if(_playList.selectedIndex != -1)
 				return (_selectedIndex = _playList.selectedIndex );
 			return _selectedIndex;
 		}
		
    	
    	public function set selectedIndex(val:Number):void{
    		if(playList.playListId == _playListId)
    			playList.selectedIndex = val;
    		_selectedIndex = val;
    	}
		
		public var httpService : HTTPService = new HTTPService();
		
		public function PlayListLoader(){			
 			httpService.url = "/yass/library_playlist.do";
 			if(MP3.player.loader == null)
 				MP3.player.loader = this;
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
        	return selectedIndex;
    	}
 		    	
    	public function get isPaused():Boolean{
    		return MP3.player.isPaused; 
    	}
    	public function get isPlaying():Boolean{
    		return MP3.player.isPlaying; 
    	}
    	    	
        public function play():void{   
            this.stop();      
            MP3.player.playUrl();          
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
        public function getNextTrack():void{
            if(shuffle)
	           	selectedIndex = getNextShuffledTrack();
	        else{
	            if(selectedIndex < length - 1)
	            	selectedIndex += 1;
	            else
	            	if(loop)
	                	selectedIndex = 0;
	                else 
	                	MP3.player.stop();          
	        } 
           	currentTrack = getItemAt(selectedIndex);
			MP3.player.position = 0;
	        if(isPlaying)
				play();
            playList.scrollToIndex(selectedIndex)
        }
        
        public function getPreviousTrack():void{
        	if(shuffle)
        		selectedIndex = getPreviousShuffledTrack();
        	else
        	   	if(selectedIndex > 0)
               		selectedIndex -= 1;
            	else
            		if(loop)
                		selectedIndex = length -1;
                	else
                		MP3.player.stop();
            currentTrack = getItemAt(selectedIndex);
			MP3.player.position = 0;
			if(isPlaying)
				play();
            playList.scrollToIndex(selectedIndex)
		}
		
        public function tooglePlay():void{
        	if(selectedIndex == -1)
        		selectedIndex = 0;
            playList.scrollToIndex(selectedIndex)
        	if(isPlaying)
        		MP3.player.pause();
        	else{
        		if(isPaused)
        			MP3.player.play();
        		else {
	            	currentTrack = getItemAt(selectedIndex);
        			play();
        		}
        	}
        }
        
        public function force():void{
        	currentTrack = getItemAt(selectedIndex);
	       	this.play();
	       	if(shuffle){
	       		while(shuffledTracks.length > shuffledListPosition)
	       			shuffledTracks.removeItemAt(shuffledListPosition);
	       		shuffledListPosition +=1;
	      		shuffledTracks.addItem(selectedIndex);
			}
        }
        
        public function get length():Number{
        	if(httpService.lastResult)
        		return httpService.lastResult.tracks.track.length;
        	return 0;
        }
        
        public function getItemAt(index:Number):Object{
        	if(httpService.lastResult)
	        	return httpService.lastResult.tracks.track[index];
        	return null;
        }
        
		public function set playList(val:PlayList):void{
			this._playList = val;
			_playList.loader = this;
			_playList.playListId = this._playListId;
			BindingUtils.bindProperty(_playList, "dataProvider", httpService,  ["lastResult", "tracks", "track"]);
			//	Console.log(httpService.lastResult.tracks.track.length);
			httpService.addEventListener(ResultEvent.RESULT, function():void{
				Console.log("PlayList : HTTP Results " + httpService.lastResult);
				BindingUtils.bindProperty(_playList, "dataProvider", httpService,  ["lastResult", "tracks", "track"]);
 				_playList.enabled = true;
	 		});
  		}
		public function get playList():PlayList{
			return _playList;
		}
	}    
}