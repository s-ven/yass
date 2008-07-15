package org.yass.main.model
{
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.media.Sound;
	import flash.media.SoundChannel;
	import flash.media.SoundLoaderContext;
	import flash.media.SoundTransform;
	import flash.net.URLRequest;
	
	import mx.core.UIComponent;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	public class SoundHandler extends UIComponent	{
        public var position:Number = 0;
        public var loadedLength:Number;
        public var isPlaying:Boolean = false;
        
        private var sound:Sound = new Sound();
        private var soundChannel:SoundChannel;
        private var loadedTrack:XMLTrack;
        private var _volume:Number;
        private var fadeoutDuration:Number = 10000;
        private var fadeoutStartTime:Number;
        private var initialVolume:Number = 0;
		public function SoundHandler(track:XMLTrack, volume:Number, fadeOutDuration:Number=10000)		{
            Console.log("model.SoundHandler :: Init title:" +track.title);
			this.loadedTrack = track;
			this.volume = volume;
			this.fadeoutDuration = fadeOutDuration;
            this.sound.addEventListener(IOErrorEvent.IO_ERROR, ioErrorHandler);
            this.addEventListener( Event.ENTER_FRAME, enterFrame)
		}

        public function set volume(value:Number):void{
        	_volume = value;
        	if(soundChannel){
        		var transform:SoundTransform = soundChannel.soundTransform;
        		transform.volume = value;
        		this.soundChannel.soundTransform = transform;
        	}
        }
        
        public function pause():void{
        	if(soundChannel)
	            this.position = this.soundChannel.position;
            this.stop();
            Console.log("model.SoundHandler.pause title:" + loadedTrack.title);
        }
        
        public function stop():void{
        	isPlaying = false;
        	if(soundChannel){
        		this.soundChannel.removeEventListener(Event.SOUND_COMPLETE, soundCompleteHandler)
				this.soundChannel.stop();
        	}
        }       
        public function play():void{
        	isPlaying = true;
            Console.log("model.SoundHandler.play title:" + loadedTrack.title);
	        if (position == 0){
	        	
				sound.load(new URLRequest("/yass/track_play.do?id=" + loadedTrack.id), new SoundLoaderContext(15000, false));
	        	loadedTrack.playCount ++;
	        	loadedTrack.lastPlayed = new Date();
	        	loadedTrack.save();
	        }
	        this.soundChannel = this.sound.play(this.position);
            this.soundChannel.addEventListener(Event.SOUND_COMPLETE, soundCompleteHandler);
	    	volume = _volume;
	    }
        public function skipTo(value:Number):void{
			if(value != 0 && loadedLength >  value	&& value <= loadedTrack.length){
				Console.group("model.SoundHandler.skipTo value:"+value);
				var wasPlaying:Boolean = isPlaying;
				stop();
				position = value;
				if(wasPlaying)		
					play();
				Console.groupEnd();
			}
		}
		
        public function soundCompleteHandler(event:Event):void {
            Console.log("model.SoundHandler.soundCompleteHandler title:" + loadedTrack.title);
            if(soundChannel.position >= loadedLength)
				Yass.player.next();
			else 
				skipTo(soundChannel.position + 10)
			
        }        
        private function ioErrorHandler(event:IOErrorEvent):void {
           Console.log("model.SoundHandler.ioErrorHandler event.text:" + event.text);
        }
        private function get fadeout():Boolean{
        	return fadeoutDuration != 0 && position > loadedLength - fadeoutDuration;
        }
        private function get fadeoutvolume():Number{
        	return Math.max(0, initialVolume * (fadeoutDuration - new Date().time + fadeoutStartTime) / fadeoutDuration)
        }
        private function enterFrame(event:Event):void {
			if(soundChannel != null){
				if(isPlaying){
					position = soundChannel.position;
					if(loadedTrack)
						loadedLength  = Math.max(sound.length, loadedTrack.length);
					if(fadeout){
						fadeOut(fadeoutDuration);
						Yass.player.next();
					}
				}
				else if(loadedTrack)
					loadedLength = loadedTrack.length;					
			}
		}
		public function fadeOut(duration:Number):void{
			if(initialVolume == 0){
				this.removeEventListener(Event.ENTER_FRAME, enterFrame);
				fadeoutDuration = duration;
				Console.log("model.SoundHandler.fadeOut duration:" + duration +", title:" + loadedTrack.title);
				initialVolume = _volume;
				fadeoutStartTime = new Date().time;
				this.addEventListener(Event.ENTER_FRAME, fadeOutHandler);
				soundChannel.removeEventListener(Event.SOUND_COMPLETE, soundCompleteHandler);
			}
		}
		private function fadeOutHandler(evt:Event):void{
			volume = fadeoutvolume;
			if(Math.round(_volume * 100) ==0){
				Console.log("model.SoundHandler.fadeOut over title:" + loadedTrack.title);
				this.removeEventListener(Event.ENTER_FRAME, fadeOutHandler);
				stop();
				soundChannel = null;
				sound = null;
			}
		}
	}
}