package org.yass.main.renderers
{
	import mx.controls.Image;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	
	public class PlayingNowRenderer extends UIComponent implements IListItemRenderer{
		[Embed(source="/assets/playlist_loaded.png")] 
		private static var loadedSource:Class;
		[Embed(source="/assets/playlist_play.png")] 
		private static var playSource:Class;
		private var imgPlay:Image = new Image();
		private var imgLoaded:Image = new Image();
		private var _data:Object;
		public function PlayingNowRenderer() {
			super();
			this.imgPlay.source = playSource;
			this.imgLoaded.source = loadedSource;
			this.imgPlay.visible = false;
			this.imgLoaded.visible = false;
			imgPlay.move(2,2);
			imgPlay.setActualSize(12,12);
			imgLoaded.move(2, 2); 	
			imgLoaded.setActualSize(12,12);
		}

		public function get data():Object{
			return _data;
		}
		
		public function set data(value:Object):void{
	        _data = value;	    
	    	invalidateProperties();
	        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
	    }
	    override protected function createChildren() : void		{
			super.createChildren();
			addChild(imgLoaded);
			addChild(imgPlay);
		}		
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			imgLoaded.visible = data && data.isLoaded;		
			imgPlay.visible = imgLoaded.visible && Yass.player.isPlaying;
		}
	}
}