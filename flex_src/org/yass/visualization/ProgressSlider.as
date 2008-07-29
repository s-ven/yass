package org.yass.visualization{
	import flash.events.Event;
	import flash.events.MouseEvent;

	import mx.controls.HSlider;
	import mx.events.SliderEvent;

	import org.yass.Yass;
	import org.yass.debug.log.Console;

	[Bindable]
	public class ProgressSlider extends HSlider{
		public function ProgressSlider(){
			Console.log("ProgressSlider : init");
			super();
			addEventListener(MouseEvent.MOUSE_DOWN, sliderChange);
			addEventListener(Event.ENTER_FRAME, onEnterFrame);
		}
		private function sliderChange(event:Event):void{
				Yass.player.skipTo(event.currentTarget.value);
		}
		private 	function onEnterFrame(event:Event):void{
			if(Yass.player.isPlaying){
				value = Yass.player.position;
				if(!Yass.player.isPaused && Yass.player.loadedTrack)
					maximum = Math.max(Yass.player.loadedTrack.length, Yass.player.loadedLength);
				else maximum = 0;
				visible = true;
			}
			else if (!Yass.player.isPaused){
				value = Yass.player.position;
				visible = false;
			}
		}
	}
}