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
package org.yass.main.renderers{
	import mx.controls.Image;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;

	import org.yass.Yass;
	import org.yass.debug.log.Console;

	public class PlayingNowRenderer extends UIComponent implements IListItemRenderer{
		[Embed(source="/assets/playlist_loaded.png")]
		private static const loadedSource:Class;
		[Embed(source="/assets/playlist_play.png")]
		private static const playSource:Class;
		private var imgPlay:Image = new Image();
		private var imgLoaded:Image = new Image();
		private var _data:Object;
		public function PlayingNowRenderer() {
			imgPlay.source = playSource;
			imgLoaded.source = loadedSource;
			imgPlay.visible = false;
			imgLoaded.visible = false;
		}

		public function get data():Object{
			return _data;
		}

		public function set data(value:Object):void{
			_data = value;
			invalidateProperties();
			dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
		}
		override protected function createChildren() : void{
			addChild(imgLoaded);
			addChild(imgPlay);
		}
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			imgPlay.move(2,2);
			imgPlay.setActualSize(12,12);
			imgLoaded.move(2, 2); 
			imgLoaded.setActualSize(12,12);
			imgLoaded.visible = data && data.isLoaded;
			imgPlay.visible = imgLoaded.visible && Yass.player.isPlaying;
		}
	}
}