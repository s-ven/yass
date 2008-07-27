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
package org.yass.main.model{
    
    import mx.core.UIComponent;
    
    import org.yass.Yass;
    import org.yass.debug.log.Console;
    import org.yass.main.events.PlayerEvent;
    import org.yass.main.model.interfaces.IPlayerModel;
    
    [Bindable]
    public class PlayerModel extends UIComponent implements IPlayerModel{
        public var loadedPlayList:PlayListModel
        
        private var _volume:Number = 1;
        private var _soundHandler:SoundHandler;
        
                
        public function PlayerModel():void{
        	Console.log("model.PlayerModel :: init");
        }
        
        public function get volume():Number{
        	return _volume;
        }
        public function get isPlaying():Boolean{
        	return _soundHandler && _soundHandler.isPlaying;
        }
        public function get loadedLength():Number{
        	if(_soundHandler)
	        	return _soundHandler.loadedLength;
	        return 0;
        }
        public function get position():Number{
        	if(_soundHandler)
	        	return _soundHandler.position;
	        return 0;
        }
        public function  get isPaused():Boolean{
        	return _soundHandler && _soundHandler.position != 0 && !_soundHandler.isPlaying;
        }
        
        public function set volume(value:Number):void{
            _volume = value;
            if(_soundHandler != null)
            	_soundHandler.volume = value;
            
        }    
        public function set loadedTrack(track:Track):void{
        	Console.group("model.Player.loadedTrack title:"+track);
			if(isPlaying)
				_soundHandler.fadeOut(Yass.settings.skipFadeout);
			Yass.settings.loadedTrack = track;
        	if(track){
				_soundHandler = new SoundHandler(track as Track, volume);
				dispatchEvent(new PlayerEvent(PlayerEvent.TRACK_LOADED, track));
			}
			Console.groupEnd();
        }
        
        public function get loadedTrack():Track{
        	return Yass.settings.loadedTrack;
        }
        public function skipTo(value:Number):void{
        	if(_soundHandler && value != 0){
				Console.group("model.PlayerModel.skipTo value:"+value);
				_soundHandler.skipTo(value);
				Console.groupEnd();
			}
            dispatchEvent(new PlayerEvent(isPlaying?PlayerEvent.PLAYING:PlayerEvent.STOPPED));
		}
		public function next():void{
			Console.group("model.PlayerModel.next");
			var wasPlaying:Boolean = isPlaying;
       		loadedTrack = loadedPlayList.getNextTrack();
       		if(wasPlaying && loadedTrack)
				_soundHandler.play();
			Console.groupEnd();
		}   
		public function previous():void{
			Console.group("model.PlayerModel.previous");
			var wasPlaying:Boolean = isPlaying;
       		loadedTrack = loadedPlayList.getPreviousTrack();
       		if(wasPlaying && loadedTrack)
				_soundHandler.play();
        	else
        		stop();
			Console.groupEnd();
		}   
		public function toogle():void{
			Console.group("model.PlayerModel.toogle");
			if(isPlaying){
				_soundHandler.pause();
	            dispatchEvent(new PlayerEvent(PlayerEvent.STOPPED));
			}
			else {
				if(!loadedTrack)
        			loadedTrack = loadedPlayList.getNextTrack();
        		_soundHandler.play();				
         	   dispatchEvent(new PlayerEvent(PlayerEvent.PLAYING, loadedTrack));
			}
			Console.groupEnd();
		}
		public function stop():void{
			if(isPlaying)
				_soundHandler.fadeOut(Yass.settings.stopFadeout);
            Console.log("model.PlayerModel.stop");
            dispatchEvent(new PlayerEvent(PlayerEvent.STOPPED));
		}
		
		public function playTrack(track:Track):void{
        	Console.group("model.Player.loadedTrack title:"+track.title);
			if(loadedTrack != track || !isPlaying){
				if(isPlaying)
					_soundHandler.fadeOut(Yass.settings.stopFadeout)	
				loadedTrack = track;
				if(!isPlaying){
					_soundHandler.play();
	            	dispatchEvent(new PlayerEvent(PlayerEvent.PLAYING, loadedTrack));
	   			}
   			}
			Console.groupEnd();
		}        
    }
}