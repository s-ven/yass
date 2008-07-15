package org.yass.debug
{
	import flash.events.Event;
	import org.yass.Yass;
	
	import mx.containers.Canvas;
	import mx.controls.Text;

	public class DebugPane extends Canvas
	{
	
		private var txt:Text = new Text();
		
		public function DebugPane():void{
			super();
			txt.text = "Debug pane : NaN";
			this.addEventListener(Event.ENTER_FRAME, enterFrame);
		}
		
		override protected function commitProperties():void{
			addChild(txt);
		}
		
		private function enterFrame(event:Event):void{
				if(this.visible) 
				txt.text = Yass.state;
			}
		
	
	}
}