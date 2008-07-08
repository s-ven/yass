package org.yass.main.renderers
{
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.yass.MP3;
	import org.yass.debug.log.Console;
	
	public class PlayingNowRenderer extends UIComponent implements IListItemRenderer{
		private var img:Image = new Image();
		private var _data:Object;
		private var _listData:BaseListData;
		public function PlayingNowRenderer() {
			super();
			this.img.source = '/assets/playlst_play.png';
			this.img.visible = false;
			img.move(2, 2);
			img.setActualSize(12,12);
		}

		public function get data():Object{
			return _data;
		}
		
		public function set data(value:Object):void{
	        _data = value;	    
			if(data);
	    	invalidateProperties();
	        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
	    }
	    override protected function createChildren() : void		{
			super.createChildren();
			addChild(img);
		}
		public function set listData(value:BaseListData):void{
			this._listData = DataGridListData(value);
		}
		public function get listData():BaseListData{
			return _listData
		}
		
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(data && data.valueOf() == MP3.player.loadedTrack && MP3.player.isPlaying)
				img.visible = true;
			else
				img.visible = false;		
		}
	}
}