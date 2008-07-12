package org.yass.main.renderers
{
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.yass.debug.log.Console;
	
	public class RatingRenderer extends UIComponent implements IListItemRenderer{
		private var img:Image = new Image();
		private var _data:Object;
		private var _listData:BaseListData;
		public function RatingRenderer() {
			super();
			img.addEventListener(MouseEvent.CLICK, click);
		}

		public function get data():Object{
			return _data;
		}
		
		public function set data(value:Object):void{
	        _data = value;	    
			if(data)
				this.img.source = '/assets/rating_'+ data.rating + ".png";
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
			img.move(unscaledWidth/2 -35, unscaledHeight/2-6);
			img.setActualSize(69,12);
		}
		protected function click(evt:MouseEvent) : void{
			var oldRating:int = data.rating;
			this.data.rating = Math.floor(evt.localX / 14) +1;
			if(oldRating == this.data.rating && oldRating == 1)
				this.data.rating = 0;
			evt.stopPropagation();
		}
	}
}