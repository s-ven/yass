/* 
 Copyright (c) 2008 Sven Duzont sven.duzont@gmail.com> All rights reserved. 
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), 
 to deal in the Software without restriction, including without limitation 
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished 
 to do so, subject to the following conditions: The above copyright notice 
 and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", 
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.yass.main.mp3
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.HSlider;
	import mx.controls.Image;
	import mx.events.SliderEvent;
	
	import org.yass.main.MP3;
	import org.yass.debug.log..Console;

	public class ControllerView extends HBox
	{
		public var backButton:Button = new Button();
		public var playButton:Button = new Button()
		public var forwardButton:Button = new Button()
		public var volumeDownBtn:Button = new Button();
		public var volumeUpBtn : Button = new Button();
		public var volumeSlider : HSlider = new HSlider();
		[Embed(source="../../../assets/volume-track-left.png")] private var leftImgCl:Class;  
		[Embed(source="../../../assets/volume-track-right.png")] private var rightImgCl:Class;  
		public static var instance:ControllerView;
		public function ControllerView(){
			instance = this;
			super();
		}
		
		override protected function commitProperties():void{
			super.commitProperties();
			backButton.styleName="BackButton";
			playButton.styleName="PlayButtonStopped";
			forwardButton.styleName="ForwardButton";
			addChild(backButton);
			addChild(playButton);
			addChild(forwardButton);
			backButton.addEventListener(MouseEvent.CLICK, previousTrack);
			playButton.addEventListener(MouseEvent.CLICK, tooglePlay);
			forwardButton.addEventListener(MouseEvent.CLICK, nextTrack);
			
			// The Volume Slider
			var hb: HBox= new HBox();
			hb.percentHeight = 100;
			hb.width = 160;
			hb.setStyle("horizontalAlign", "right");
			hb.setStyle("verticalAlign", "middle");
			addChild(hb);
			volumeDownBtn.autoRepeat = true;
			volumeDownBtn.styleName = "VolDownButton";
			volumeDownBtn.addEventListener(MouseEvent.CLICK, volDown);
			hb.addChild(volumeDownBtn);
			var cnv :Canvas = new Canvas();
			cnv.percentHeight = 100;
			hb.addChild(cnv);
			var leftImg :Image = new Image();
			leftImg.setStyle("verticalCenter", 0);
			leftImg.setStyle("left", 1);
			leftImg.source = leftImgCl;
			cnv.addChild(leftImg);
			var rightImg :Image = new Image();
			rightImg.setStyle("verticalCenter", 0);
			rightImg.setStyle("horizontalCenter", 50);
			rightImg.source = rightImgCl;
			cnv.addChild(rightImg);
			volumeSlider.value=100;
			volumeSlider.addEventListener(SliderEvent.CHANGE, volumeSlideChange);
			volumeSlider.sliderThumbClass = VolumeThumbClass; 
			volumeSlider.maximum = 100;
			volumeSlider.percentHeight = 100;
			volumeSlider.width=100;
			volumeSlider.snapInterval = 1;
			volumeSlider.dataTipFormatFunction = formatVolume;
			volumeSlider.liveDragging=true	;
			volumeSlider.styleName="VolumeSlider"
			cnv.addChild(volumeSlider);
			volumeUpBtn.autoRepeat = true;
			volumeUpBtn.styleName = "VolumeUpButton";
			volumeUpBtn.addEventListener(MouseEvent.CLICK, volUp);
			hb.addChild(volumeUpBtn);
		}
		private function formatVolume(val:String):String {
		    return "Volume : " + String(val)+"%";
		}
		private function volumeSlideChange(evt:Event){
			MP3.player.volume = volumeSlider.value/100;
		}
		private function volDown(evt:Event):void{
			volumeSlider.value = volumeSlider.value -1; 
			MP3.player.volume =volumeSlider.value/100;
		}
		private function volUp(evt:Event):void{
			volumeSlider.value = volumeSlider.value + 1; 
			MP3.player.volume =volumeSlider.value/100;
		}
		private function nextTrack(evt:Event):void{
			Console.log("view.ControllerView.nextTrack");
			MP3.player.loadedPlaylist.getNextTrack()
		}
		private function previousTrack(evt:Event):void{
			Console.log("view.ControllerView.previousTrack");
			MP3.player.loadedPlaylist.getPreviousTrack();
		}
		private function tooglePlay(evt:Event):void{
			Console.log("view.ControllerView.tooglePlay");
			MP3.player.loadedPlaylist.tooglePlay()
		}
	}
}