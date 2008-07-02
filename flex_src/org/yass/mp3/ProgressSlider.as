package org.yass.mp3
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.HSlider;
	import mx.events.SliderEvent;

	[Bindable]
	public class ProgressSlider extends HSlider{
				
		
		public function ProgressSlider()
		{
			MP3.info("ProgressSlider : init");
			super();
			this.addEventListener(SliderEvent.CHANGE, sliderChange,false,1);
			this.addEventListener(Event.ENTER_FRAME, onEnterFrame);
		}		
		

		private function sliderChange(event:SliderEvent):void{
				MP3.player.skipTo(event.value);
		}
		
		private 	function onEnterFrame(event:Event):void{
			if(MP3.player.isPlaying){
				value = MP3.player.position;
				if(MP3.player.isPlaying && !MP3.player.isPaused)
					maximum = Math.max(MP3.player.playList.currentTrack.length * 1000, MP3.player.loadedLengh);
				else maximum = 0;
				this.visible = true;
			}
			else if (!MP3.player.isPaused){
				value = MP3.player.position;
				this.visible = false;
			}
		}
		
		
	}
}