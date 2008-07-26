package org.yass.visualization
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.profiler.showRedrawRegions;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.CheckBox;
	import mx.controls.Label;
	import mx.core.ScrollPolicy;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;

	public class Display extends Canvas
	{
		public var trackLabel:Label = new Label();
		public var scrollText:ScrollingText = new ScrollingText();
		public var progress:ProgressSlider = new ProgressSlider();
		public var loop:CheckBox = new CheckBox();
		public var shuffle:CheckBox = new CheckBox();
		public var elapsed:Label = new Label();
		public var remaining:Label = new Label();
		public var spectrumAnalyzer:SpectrumAnalyzer = new SpectrumAnalyzer();
		public var vuMeters:Canvas = new Canvas();
		public var songInfo:Canvas = new Canvas();
		
		public static var _instance:Display;
		 
		public static function get instance():Display{
			if(!_instance)
				_instance = new Display();
			return _instance
		}
		public function Display()		{
			Console.log("Display : init");
			super();
			addEventListener(Event.ENTER_FRAME, handlerEnterFrame);
			this.setStyle("fontThickness", 200);
			this.setStyle("fontAntiAliasType", "advanced");
			horizontalScrollPolicy = ScrollPolicy.OFF;
			verticalScrollPolicy = ScrollPolicy.OFF;
			songInfo.percentWidth = 100;
			songInfo.percentHeight = 100;
			songInfo.horizontalScrollPolicy = ScrollPolicy.OFF;
			songInfo.verticalScrollPolicy = ScrollPolicy.OFF;
			songInfo.setStyle("verticalAlign", "middle");
			songInfo.setStyle("horizontalAlign", "center");
			vuMeters.percentWidth = 100;
			vuMeters.percentHeight = 100;
			vuMeters.setStyle("left", 0);
			vuMeters.setStyle("right", 0);
			vuMeters.setStyle("top", 0);
			vuMeters.setStyle("bottom", 0);
			vuMeters.addChild(spectrumAnalyzer);
			addChild(vuMeters);
			addChild(songInfo);
			_instance = this;
		}
		
		private function formatPos(pos:Number):String {
			return Yass.dateFormatter.format(new Date(0, 0, 0, 0, 0, 0, pos));
		}         
		override protected function commitProperties():void{
			super.commitProperties();
			var vbox:VBox = new VBox();
			vbox.setStyle("verticalGap", 0);
			vbox.setStyle("verticalAlign", "middle");
			vbox.setStyle("horizontalAlign", "center");
			vbox.percentWidth = 100;	
			vbox.percentHeight = 100;	
			trackLabel.height = 15	
			vbox.addChild(trackLabel);	
			scrollText.percentWidth = 100;
			scrollText.height=15
			vbox.addChild(scrollText);			
			// The progress Slide etc
			var hbox:HBox= new HBox();
			hbox.percentWidth=100;
			hbox.height=15;
			hbox.setStyle("horizontalCenter", 0);
			hbox.setStyle("horizontalAlign","center");
			hbox.setStyle("verticalAlign","middle");
			hbox.setStyle("horizontalGap",0);
			// Loop Button
			var canv1:Canvas = new Canvas();
			canv1.setStyle("verticalCenter", -2);
			canv1.setStyle("horizontalAlign", "right");
			canv1.height = 15;
			canv1.percentWidth = 5;
			canv1.minWidth=25;
			loop. styleName="LoopCheckBox";
			canv1.addChild(loop);
			hbox.addChild(canv1);
			// Position 
			var canv2:Canvas = new Canvas();
			canv2.height = 15;
			canv2.percentWidth = 5;
			canv2.minWidth=40;
			elapsed.setStyle("verticalCenter", 1);
			elapsed.setStyle("right", 0);
			canv2.addChild(elapsed);
			hbox.addChild(canv2);
			// Progress Slider 
			var canv3:Canvas = new Canvas();
			canv3.setStyle("borderStyle", "solid");
			canv3.setStyle("borderColor", "#0B333C");
			canv3.setStyle("verticalCenter", -5);
			canv3.height = 10;
			canv3.percentWidth = 100;
			progress.id="ProgressSlider" 
			progress.sliderThumbClass=ProgressThumbClass;
			progress.percentWidth=100;
			progress.styleName="ProgressSlider";
			canv3.addChild(progress);
			hbox.addChild(canv3);
			// Remaining 
			var canv4:Canvas = new Canvas();
			canv4.height = 15;
			canv4.percentWidth = 5;
			remaining.setStyle("verticalCenter", 1);
			remaining.setStyle("left", 0);
			canv4.addChild(remaining);
			canv4.minWidth=40;
			hbox.addChild(canv4);			
			// Loop Button
			var canv5:Canvas = new Canvas();
			canv5.setStyle("verticalCenter", -2);
			canv5.setStyle("horizontalAlign", "left");
			canv5.height = 15;
			canv5.percentWidth = 5;
			canv5.minWidth=25;
			shuffle. styleName="ShuffleCheckBox";
			canv5.addChild(shuffle);
			hbox.addChild(canv5);
			vbox.addChild(hbox);
			songInfo.addChild(vbox);
			setEventListeners();
			refresh();
		}
		private function handlerEnterFrame(e:Event):void {
			if(Yass.player.loadedTrack){
				trackLabel.text = Yass.player.loadedTrack.title;
				elapsed.text = formatPos(Yass.player.position);
				remaining.text = Yass.settings.showRemaining?"-" + formatPos(Yass.player.loadedLength - Yass.player.position):formatPos(Yass.player.loadedLength);
			}
		}

		private function plateClick(evt:Event):void{
			if((Yass.settings.displayMode+=1) ==3) 
				Yass.settings.displayMode=0;
			refresh();
		}
		public function refresh(){
			shuffle.selected = Yass.settings.shuffle;
			loop.selected = Yass.settings.loop;
			switch (Yass.settings.displayMode){
			case(0):
				spectrumAnalyzer.styleName = "dimmed";
				spectrumAnalyzer.visible = false;
				songInfo.visible = true;
				break;
			case(1):
				spectrumAnalyzer.styleName = "dimmed";
				spectrumAnalyzer.visible = true;
				songInfo.visible = true;
				break;
			case(2):
				spectrumAnalyzer.styleName = "visible";
				spectrumAnalyzer.visible = true;
				songInfo.visible = false;
				break;
			}
		}
		private function loopClick(evt:Event):void{
			evt.stopPropagation();
			Yass.settings.loop = loop.selected;
		}
		private function remainingClick(evt:Event):void{
			evt.stopPropagation();
			Yass.settings.showRemaining = !Yass.settings.showRemaining;
		}
		private function shuffleClick(evt:Event):void{
			evt.stopPropagation();
			Yass.settings.shuffle = shuffle.selected;
		}
		private function setEventListeners():void{			
			loop.addEventListener(MouseEvent.CLICK , loopClick);
			remaining.addEventListener(MouseEvent.CLICK , remainingClick);
			shuffle.addEventListener(MouseEvent.CLICK , shuffleClick);
			this.addEventListener(MouseEvent.CLICK , plateClick);
		}
	}
}