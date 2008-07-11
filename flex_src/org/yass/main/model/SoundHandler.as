package org.yass.main.model
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
	public class SoundHandler extends UIComponent	{
        public var position:Number = 0;
        public var loadedLengh:Number;
        public var isPlaying:Boolean = false;
        
        private var soundInstance:Sound = new Sound();
        private var soundChannelInstance:SoundChannel;
        private var loadedTrack:Track;
        private var _volume:Number;
        private var fadeoutDuration:Number = 10000;
        private var fadeoutStartTime:Number;
        private var initialVolume:Number = 0;
		public function SoundHandler(track:Track, volume:Number, fadeOutDuration:Number=10000)		{
			this.loadedTrack = track;
			this.volume = volume;
			this.fadeoutDuration = fadeOutDuration;
            this.soundInstance.addEventListener(Event.COMPLETE, completeHandler);
            this.soundInstance.addEventListener(Event.OPEN, openHandler);
            this.soundInstance.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            this.soundInstance.addEventListener(ProgressEvent.PROGRESS, progressHandler);
            this.addEventListener( Event.ENTER_FRAME, enterFrame)
		}

        public function set volume(value:Number):void{
        	_volume = value;
        	if(soundChannelInstance){
        		var transform:SoundTransform = soundChannelInstance.soundTransform;
        		transform.volume = value;
        		this.soundChannelInstance.soundTransform = transform;
        	}
        }
        
        public function pause():void{
        	if(soundChannelInstance)
	            this.position = this.soundChannelInstance.position;
            this.stop();
            Console.log("model.SoundHandler.pause");
        }
        
        public function stop():void{
        	isPlaying = false;
        	if(soundChannelInstance)
				this.soundChannelInstance.stop();
        }       
        public function play():void{
        	isPlaying = true;
            Console.log("model.SoundHandler.play");
	        if (position == 0){
				soundInstance.load(new URLRequest("/yass/play.do?id=" + loadedTrack.id));
	        	loadedTrack.playCount ++;
	        	loadedTrack.lastPlayed = new Date();
	        }
	        this.soundChannelInstance = this.soundInstance.play(this.position);
            this.soundChannelInstance.addEventListener(Event.SOUND_COMPLETE, soundCompleteHandler);
	    	volume = _volume;
	    }
        public function skipTo(value:Number):void{
			Console.log("model.SoundHandler.skipTo value="+value);
			if(loadedLengh >  value	&& value <= loadedTrack.length){
				stop();
				position = value;	
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
            Console.log("model.SoundHandler.soundCompleteHandler " + loadedTrack.title);
            MP3.player.next();
        }        
        private function ioErrorHandler(event:IOErrorEvent):void {
           Console.log("model.SoundHandler : in error" + event.text);
        }
        private function progressHandler(event:ProgressEvent):void {
             this.dispatchEvent(event);
        }        
        private function get fadeout():Boolean{
        	return fadeoutDuration != 0 && position > loadedLengh - fadeoutDuration;
        }
        private function get fadeoutvolume():Number{
        	return Math.max(0, initialVolume * (fadeoutDuration - new Date().time + fadeoutStartTime) / fadeoutDuration)
        }
        private function enterFrame(event:Event):void {
			if(soundChannelInstance != null){
				if(isPlaying){
					position = soundChannelInstance.position;
					if(loadedTrack)
						loadedLengh  = Math.max(soundInstance.length, loadedTrack.length);
					if(fadeout){
						fadeOut(fadeoutDuration);
						MP3.player.next();
					}
				}
				else if(loadedTrack)
					loadedLengh = loadedTrack.length;					
			}
		}
		
		public function fadeOut(duration:Number):void{
			if(initialVolume ==0){
				this.removeEventListener(Event.ENTER_FRAME, enterFrame);
				fadeoutDuration = duration;
				Console.log("model.SoundHandler.enterFrame: Fading Out " + duration);
				initialVolume = _volume;
				fadeoutStartTime = new Date().time;
				this.addEventListener(Event.ENTER_FRAME, fadeOutHandler);
				soundChannelInstance.removeEventListener(Event.SOUND_COMPLETE, soundCompleteHandler);
			}
		}
		private function fadeOutHandler(evt:Event):void{
			volume = fadeoutvolume;
			if(Math.round(_volume * 100) ==0){
				Console.log("model.SoundHandler.fadeOut over");
				this.removeEventListener(Event.ENTER_FRAME, fadeOutHandler);
				soundChannelInstance.stop();
				soundChannelInstance = null;
				soundInstance = null;
			}
		}
	}
}