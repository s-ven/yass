package org.yass.mp3
/*
    Player version 0.1

*/

{
    import com.airlogger.AirLoggerDebug;
    
    import flash.events.Event;
    import flash.events.IOErrorEvent;
    import flash.events.ProgressEvent;
    import flash.media.Sound;
    import flash.media.SoundChannel;
    import flash.media.SoundTransform;
    import flash.net.URLRequest;
    
    import mx.core.UIComponent;
    [Bindable]
    public class Player extends UIComponent{
        public var playList:PlayList
        public var position:Number                 = 0;
        

        public var isPlaying:Boolean = false;
        private var _volume:Number                 = 1;
        private var soundInstance:Sound;
        private var soundChannelInstance:SoundChannel;
        
        public function  get isPaused():Boolean{
        	return position != 0 && !isPlaying;
        }
                
        private static var _instance:Player;
        public static function get instance():Player{
            if(_instance == null)
                _instance = new Player();
            return _instance;
        }
        public function Player():void{
        	AirLoggerDebug.info("Player : init");
            this.soundInstance = new Sound();
            // this.setupListeners();
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
            Console.log("MP3Player : Starting play...");
            startHdlr();
	        this.isPlaying = true;if(soundChannelInstance)
   			this.soundChannelInstance.stop();
	        this.soundChannelInstance = this.soundInstance.play(this.position); 
	    	this.setupListeners();
	    	var transform:SoundTransform = soundChannelInstance.soundTransform;
	    	transform.volume = this._volume;
	    	this.soundChannelInstance.soundTransform = transform;
	      //  this.pausePosition = 0;
            Console.log("MP3Player : Playing...");
	    }
        
        private function get  url():String{
        	return "/yass/play.do?UUID=" + playList.currentTrack.UUID;
        }
        
        public function playUrl():void{
            Console.log("MP3Player : Playing URL " + url);
            this.soundInstance = new Sound();
            soundInstance.load(new URLRequest(url));
            position = 0;
    		this.play();
        }

        
        private function startHdlr():void{
	        MP3.controller.playButton.styleName = "PlayButtonStarted";
	        MP3.display.scrollText.start();
		}
		private function stopHdlr():void{
			MP3.controller.playButton.styleName = "PlayButtonStopped";
			MP3.display.scrollText.stop();
		}
        
        public function pause():void{
        	if(soundChannelInstance)
	            this.position = this.soundChannelInstance.position;
            this.stop();
            Console.log("MP3Player : Paused ");
        }
        
        public function stop():void{
            this.isPlaying = false;
            if(soundChannelInstance){
            	stopHdlr();
   				this.soundChannelInstance.stop();
   			}
            Console.log("MP3Player : Stopped ");
        }
        
        
        /******************************************HANDLERS***********************************************/
        private function completeHandler(event:Event):void {
            this.dispatchEvent(event);
        }
        
        private function openHandler(event:Event):void {
            this.dispatchEvent(event);
        }

        public function soundCompleteHandler(event:Event):void {
            Console.log("MP3Player : Track Finished ");
            playList.getNextTrack();
        }
        
        private function ioErrorHandler(event:IOErrorEvent):void {
           Console.log('MP3Player : in error');
        }


        private function progressHandler(event:ProgressEvent):void {
             this.dispatchEvent(event);
        }
        
        private function enterFrame(event:Event):void {
			if(soundChannelInstance != null){
				if(isPlaying){
					position = soundChannelInstance.position;
					loadedLengh  = Math.max(soundInstance.length, playList.currentTrack.length *1000);
				}
				else loadedLengh = playList.currentTrack.length *1000;
			}
		}
        public function skipTo(value:Number):void{
			Console.log("ProgressSlider : Click " + isPaused + ", " + isPlaying + " value="+value);
			if(loadedLengh >  value	&& value <= playList.currentTrack.length * 1000){
				position = value;	
				play();
				}
			}
    }
}