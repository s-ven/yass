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
        private var loadedTrack:Track;
        private var _volume:Number;
        private var fadeoutStartTime:Number;
        private var initialVolume:Number = 0;
		public function SoundHandler(track:Track, volume:Number)		{
            Console.log("model.SoundHandler :: Init title:" +track.title);
			this.loadedTrack = track;
			this.volume = volume;
            this.sound.addEventListener(IOErrorEvent.IO_ERROR, onIoError);
            this.addEventListener( Event.ENTER_FRAME, onEnterFrame)
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
        		this.soundChannel.removeEventListener(Event.SOUND_COMPLETE, onSoundComplete)
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
            this.soundChannel.addEventListener(Event.SOUND_COMPLETE, onSoundComplete);
	    	volume = _volume;
	    }
        public function skipTo(value:Number):void{
			if(value != 0 && loadedLength >  value	&& value <= loadedTrack.length){
				Console.group("model.SoundHandler.skipTo value:"+value);
				var wasPlaying:Boolean = isPlaying; //192872.90249433107 193228 //192914.2857142857
				stop();
				position = value;
				if(wasPlaying)		
					play();
				Console.groupEnd();
			}
		}
        public function onSoundComplete(event:Event):void {
            Console.log("model.SoundHandler.soundCompleteHandler title:" + loadedTrack.title);
            if(soundChannel.position+10 >= sound.length)
				Yass.player.next();
			else 
				skipTo(soundChannel.position + 10)
			
        }        
        private function onIoError(event:IOErrorEvent):void {
           Console.log("model.SoundHandler.ioErrorHandler event.text:" + event.text);
        }
        private function onEnterFrame(event:Event):void {
			if(soundChannel != null){
				if(isPlaying){
					position = soundChannel.position;
					if(loadedTrack)
						loadedLength  = Math.max(sound.length, loadedTrack.length);
					if(Yass.settings.nextFadeout != 0 && position > loadedLength - Yass.settings.nextFadeout){
						fadeOut(Yass.settings.nextFadeout);
						Yass.player.next();
					}
				}
				else if(loadedTrack)
					loadedLength = loadedTrack.length;					
			}
		}
		public function fadeOut(duration:Number):void{
			if(initialVolume == 0){
				this.removeEventListener(Event.ENTER_FRAME, onEnterFrame);
				Yass.settings.nextFadeout = duration;
				Console.log("model.SoundHandler.fadeOut duration:" + duration +", title:" + loadedTrack.title);
				initialVolume = _volume;
				fadeoutStartTime = new Date().time;
				this.addEventListener(Event.ENTER_FRAME, fadeOutHandler);
				soundChannel.removeEventListener(Event.SOUND_COMPLETE, onSoundComplete);
			}
		}
		private function fadeOutHandler(evt:Event):void{
			volume = Math.max(0, initialVolume * (Yass.settings.nextFadeout - new Date().time + fadeoutStartTime) / Yass.settings.nextFadeout);
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