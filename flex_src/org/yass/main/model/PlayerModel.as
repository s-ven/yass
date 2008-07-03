package org.yass.main.model
/*
    Player version 0.1

*/

{
    
    import flash.events.Event;
    import flash.events.IOErrorEvent;
    import flash.events.ProgressEvent;
    import flash.media.Sound;
    import flash.media.SoundChannel;
    import flash.media.SoundTransform;
    import flash.net.URLRequest;
    
    import mx.core.UIComponent;
    
    import org.yass.MP3;
    import org.yass.debug.log.Console;
    import org.yass.main.events.PlayerEvent;
    import org.yass.main.interfaces.model.IPlayListModel;
    import org.yass.main.interfaces.model.IPlayerModel;
    
    [Bindable]
    public class PlayerModel extends UIComponent implements IPlayerModel{
        public var _loadedPlaylist:IPlayListModel
        public var _loadedTrack;
        public var position:Number = 0;

		public var shuffle:Boolean;
		public var loop:Boolean;

        public var isPlaying:Boolean = false;
        private var _volume:Number                 = 1;
        private var soundInstance:Sound;
        private var soundChannelInstance:SoundChannel;
        
        public function  get isPaused():Boolean{
        	return position != 0 && !isPlaying;
        }
                
        private static var _instance:PlayerModel;
        public static function get instance():PlayerModel{
            if(_instance == null)
                _instance = new PlayerModel();
            return _instance;
        }
        public function PlayerModel():void{
        	Console.info("model.PlayerModel :: init");
            this.soundInstance = new Sound();
        }
        
        public function get volume():Number{
        	return _volume;
        }
        
        public function set volume(value:Number):void{
            this._volume = value;
            if(this.soundChannelInstance != null){
            	var transform:SoundTransform = soundChannelInstance.soundTransform;
            	transform.volume = this._volume;
            	this.soundChannelInstance.soundTransform = transform;
            }
        }
        /******************************************CONTROLS***********************************************/
        private function setupListeners():void{
            this.soundInstance.addEventListener(Event.COMPLETE, completeHandler);
            this.soundInstance.addEventListener(Event.OPEN, openHandler);
            this.soundInstance.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            this.soundInstance.addEventListener(ProgressEvent.PROGRESS, progressHandler);
            this.soundChannelInstance.addEventListener(Event.SOUND_COMPLETE, soundCompleteHandler);
            this.addEventListener( Event.ENTER_FRAME, enterFrame)
        }  
        
        public var loadedLengh:Number;
                        
        public function play():void{
            Console.log("model.PlayerModel.play");
	        this.isPlaying = true;
	        if(soundChannelInstance)
   				this.soundChannelInstance.stop();
	        this.soundChannelInstance = this.soundInstance.play(this.position); 
	    	this.setupListeners();
	    	var transform:SoundTransform = soundChannelInstance.soundTransform;
	    	transform.volume = 0;//////////////////////////////////
	    	this.soundChannelInstance.soundTransform = transform;
            this.dispatchEvent(new PlayerEvent(PlayerEvent.PLAYING));
	    }
        
        private function get  url():String{
        	return "/yass/play.do?UUID=" + loadedTrack.UUID;
        }
        
        public function set loadedTrack(track:Object):void{
        	this._loadedTrack = track;
            Console.group("model.PlayerModel.loadTrack " + url);
           	this.soundInstance = new Sound();
           	soundInstance.load(new URLRequest(url));
           	position = 0;
            Console.groupEnd();
            this.dispatchEvent(new PlayerEvent(PlayerEvent.LOADED));
        }
        public function get loadedTrack():Object{
        	return _loadedTrack;
        }

        public function setLoadedPlayList(val:Object):void{
        	
        }
        
        public function pause():void{
        	if(soundChannelInstance)
	            this.position = this.soundChannelInstance.position;
            this.stop();
            Console.log("model.PlayerModel.pause");
        }
        
        public function stop():void{
            this.isPlaying = false;
            if(soundChannelInstance){
            	this.dispatchEvent(new PlayerEvent(PlayerEvent.STOPPED));
   				this.soundChannelInstance.stop();
   			}
            Console.log("model.PlayerModel.stop");
        }
        
        public function get loadedPlayList():IPlayListModel{
        	return _loadedPlaylist;
        }
        public function set loadedPlayList(val:IPlayListModel):void{
        	 _loadedPlaylist = val;
        }
        public function skipTo(value:Number):void{
			Console.log("model.PlayerModel.skipTo value="+value);
			if(loadedTrack && loadedLengh >  value	&& value <= loadedTrack.length * 1000){
				position = value;	
				play();
			}
		}
		public function next():void{
			Console.log("model.PlayerModel.next");
       		_loadedPlaylist.getNextTrack(shuffle, loop);
       		loadedTrack = _loadedPlaylist.selectedTrack;
        	if(isPlaying)
        		play();
		}   
		public function previous():void{
			Console.log("model.PlayerModel.previous");
       		_loadedPlaylist.getPreviousTrack(shuffle, loop);
       		loadedTrack = _loadedPlaylist.selectedTrack;
        	if(isPlaying)
        		play();
		}   
		public function toogle():void{
			Console.log("model.PlayerModel.toogle");
			if(isPlaying)
				this.pause();
			else if(!isPaused){
				if(!loadedTrack){
        			_loadedPlaylist.getNextTrack(shuffle, loop);
        			loadedTrack = _loadedPlaylist.selectedTrack;
        		}
        		play();				
			}
		}
        
        /**
        * Event Handlers
        */
        private function completeHandler(event:Event):void {
            this.dispatchEvent(event);
        }        
        private function openHandler(event:Event):void {
            this.dispatchEvent(event);
        }
        public function soundCompleteHandler(event:Event):void {
            Console.log("model.PlayerModel.soundCompleteHandler");
            next();
        }        
        private function ioErrorHandler(event:IOErrorEvent):void {
           Console.log("model.PlayerModel. : in error");
        }
        private function progressHandler(event:ProgressEvent):void {
             this.dispatchEvent(event);
        }        
        private function enterFrame(event:Event):void {
			if(soundChannelInstance != null){
				if(isPlaying){
					position = soundChannelInstance.position;
					if(loadedTrack)
						loadedLengh  = Math.max(soundInstance.length, loadedTrack.length *1000);
				}
				else if(loadedTrack)
					loadedLengh = loadedTrack.length *1000;
			}
		}
    }
}