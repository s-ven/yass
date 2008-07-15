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
package org.yass.main.view
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.HSlider;
	import mx.controls.Image;
	import mx.events.SliderEvent;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.controller.PlayerController;
	import org.yass.main.events.PlayerEvent;
	import org.yass.main.model.interfaces.IPlayerModel;

	public class PlayerView extends HBox
	{
		public var backButton:Button = new Button();
		public var playButton:Button = new Button()
		public var forwardButton:Button = new Button()
		public var volumeDownBtn:Button = new Button();
		public var volumeUpBtn : Button = new Button();
		public var volumeSlider : HSlider = new HSlider();
		private var controller:PlayerController;
		[Embed(source="/assets/volume-track-left.png")] private var leftImgCl:Class;  
		[Embed(source="/assets/volume-track-right.png")] private var rightImgCl:Class;
		
		public function PlayerView(){
			Console.log("view.PlayerView :: Init");
			controller = new PlayerController(this, Yass.player as IPlayerModel);
			Yass.player.addEventListener(PlayerEvent.PLAYING, onPlaying);
			Yass.player.addEventListener(PlayerEvent.STOPPED, onStopped);
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
			backButton.addEventListener(MouseEvent.CLICK, onPreviousTrackClick);
			playButton.addEventListener(MouseEvent.CLICK, onPlayPauseClick);
			forwardButton.addEventListener(MouseEvent.CLICK, onNextTrackClick);
			
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
		private function volumeSlideChange(evt:Event):void{
			Yass.player.volume = volumeSlider.value/100;
		}
		private function volDown(evt:Event):void{
			volumeSlider.value = volumeSlider.value -1; 
			Yass.player.volume =volumeSlider.value/100;
		}
		/**
		 * Called when the next track button has been clicked, 
		 * Will cause the model to go to next track
		 */ 
		private function volUp(evt:Event):void{
			volumeSlider.value = volumeSlider.value + 1; 
			Yass.player.volume =volumeSlider.value/100;
		}
		/**
		 * Called when the next track button has been clicked, 
		 * Will cause the model to go to next track
		 */ 
		private function onNextTrackClick(evt:Event):void{
			Console.log("view.Player.nextTrack");
			dispatchEvent(new PlayerEvent(PlayerEvent.NEXT));
		}
		/**
		 * Called when the previous track button has been clicked, 
		 * Will cause the model to go to prévious track
		 */ 
		private function onPreviousTrackClick(evt:Event):void{
			Console.log("view.Player.previousTrack");
			dispatchEvent(new PlayerEvent(PlayerEvent.PREVIOUS));
		}
		/**
		* Caled when the user has clicked on the play or pause button
		* Will toogle the  state of the PlayerModel between play and pause
		*/
		private function onPlayPauseClick(evt:Event):void{
			Console.log("view.Player.tooglePlay");
			dispatchEvent(new PlayerEvent(PlayerEvent.TOOGLE));
		}
		/**
		 * Called after an start event from the model 
		 * Will cause the play button to be refreshed to pause
		 * And the Artist/album scrolling text to start
		 */
		private function onPlaying(evt:PlayerEvent):void{
			Console.log("view.Player.onPlaying");
			playButton.styleName = "PlayButtonStarted";
	        Yass.display.scrollText.start();
		}
		/**
		 * Called after an start event from the model 
		 * Will cause the play play to be refreshed to play
		 * And the Artist/album scrolling text to stop
		 */
		private function onStopped(evt:PlayerEvent):void{
			Console.log("view.Player.onStopped");
			playButton.styleName = "PlayButtonStopped";
			Yass.display.scrollText.stop();
		}
	}
}