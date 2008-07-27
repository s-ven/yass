package org.yass.main.model
{
	import flash.events.Event;
	import flash.events.IOErrorEvent;
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
        
        private var _sound:Sound = new Sound();
        private var _soundChannel:SoundChannel;
        private var _loadedTrack:Track;
        private var _volume:Number;
		private var _fadeoutDuration:int; 
        private var _fadeoutStartTime:Number;
        private var _initialVolume:Number = 0;
		public function SoundHandler(track:Track, volume:Number)		{
            Console.log("model.SoundHandler :: Init title:" +track.title);
			_loadedTrack = track;
			this.volume = volume;
            _sound.addEventListener(IOErrorEvent.IO_ERROR, onIoError);
            addEventListener( Event.ENTER_FRAME, onEnterFrame)
		}

        public function set volume(value:Number):void{
        	_volume = value;
        	if(_soundChannel){
        		var transform:SoundTransform = _soundChannel.soundTransform;
        		transform.volume = value;
        		_soundChannel.soundTransform = transform;
        	}
        }
        
        public function pause():void{
        	if(_soundChannel)
	            position = _soundChannel.position;
            stop();
            Console.log("model.SoundHandler.pause title:" + _loadedTrack.title);
        }
        
        public function stop():void{
        	isPlaying = false;
        	if(_soundChannel){
        		_soundChannel.removeEventListener(Event.SOUND_COMPLETE, onSoundComplete)
				_soundChannel.stop();
        	}
        }       
        public function play():void{
        	isPlaying = true;
            Console.log("model.SoundHandler.play title:" + _loadedTrack.title);
	        if (position == 0){
				_sound.load(new URLRequest("/yass/track_play.do?id=" + _loadedTrack.id), new SoundLoaderContext(15000, false));
	        	_loadedTrack.playCount ++;
	        	_loadedTrack.lastPlayed = new Date();
	        	_loadedTrack.save();
	        }
	        _soundChannel = _sound.play(position);
            _soundChannel.addEventListener(Event.SOUND_COMPLETE, onSoundComplete);
	    	volume = _volume;
	    }
        public function skipTo(value:Number):void{
			if(value != 0 && loadedLength >  value	&& value <= _loadedTrack.length){
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
            Console.log("model.SoundHandler.soundCompleteHandler title:" + _loadedTrack.title);
            if(_soundChannel.position+10 >= _sound.length)
				Yass.player.next();
			else 
				skipTo(_soundChannel.position + 10)
			
        }        
        private function onIoError(event:IOErrorEvent):void {
           Console.log("model.SoundHandler.ioErrorHandler event.text:" + event.text);
        }
        private function onEnterFrame(event:Event):void {
			if(_soundChannel != null){
				if(isPlaying){
					position = _soundChannel.position;
					if(_loadedTrack)
						loadedLength  = Math.max(_sound.length, _loadedTrack.length);
					if(Yass.settings.nextFadeout != 0 && position > loadedLength - Yass.settings.nextFadeout){
						fadeOut(Yass.settings.nextFadeout);
						Yass.player.next();
					}
				}
				else if(_loadedTrack)
					loadedLength = _loadedTrack.length;					
			}
		}
		public function fadeOut(duration:Number):void{
			if(_initialVolume == 0){
				removeEventListener(Event.ENTER_FRAME, onEnterFrame);
				_fadeoutDuration = duration;
				Console.log("model.SoundHandler.fadeOut duration:" + duration +", title:" + _loadedTrack.title);
				_initialVolume = _volume;
				_fadeoutStartTime = new Date().time;
				addEventListener(Event.ENTER_FRAME, fadeOutHandler);
				_soundChannel.removeEventListener(Event.SOUND_COMPLETE, onSoundComplete);
			}
		}
		private function fadeOutHandler(evt:Event):void{
			volume = Math.max(0, _initialVolume * (_fadeoutDuration - new Date().time + _fadeoutStartTime) / _fadeoutDuration);
			if(Math.round(_volume * 100) == 0){
				Console.log("model.SoundHandler.fadeOut over title:" + _loadedTrack.title);
				removeEventListener(Event.ENTER_FRAME, fadeOutHandler);
				stop();
				_soundChannel = null;
				_sound = null;
			}
		}
	}
}