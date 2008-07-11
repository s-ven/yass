package org.yass.main.renderers
{
	import mx.controls.Image;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.yass.MP3;
	
	public class PlayingNowRenderer extends UIComponent implements IListItemRenderer{
		private var imgPlay:Image = new Image();
		private var imgLoaded:Image = new Image();
		private var _data:Object;
		private var _listData:BaseListData;
		public function PlayingNowRenderer() {
			super();
			this.imgPlay.source = '/assets/playlist_play.png';
			this.imgLoaded.source = '/assets/playlist_loaded.png';
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
		public function set listData(value:BaseListData):void{
			this._listData = DataGridListData(value);
		}
		public function get listData():BaseListData{
			return _listData
		}
		
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			imgLoaded.visible = data && data.isLoaded;		
			imgPlay.visible = imgLoaded.visible && MP3.player.isPlaying;
		}
	}
}