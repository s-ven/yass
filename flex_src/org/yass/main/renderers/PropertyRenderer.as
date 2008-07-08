package org.yass.main.renderers
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.Label;
	import mx.controls.dataGridClasses.DataGridListData;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	import org.yass.debug.log.Console;
	import org.yass.main.LibraryBrowser;

	public class PropertyRenderer  extends UIComponent implements IListItemRenderer{
		private var label:Label = new Label()
		private var _listData:BaseListData;
		private var _data:Object;
		public var type:String = "artist";
		public function PropertyRenderer(){
			super();
			this.useHandCursor = true;
			this.mouseChildren = false;
			this.buttonMode = true;
			this.addEventListener(MouseEvent.MOUSE_OVER, over);
			this.addEventListener(MouseEvent.MOUSE_OUT, out);
			this.addEventListener(MouseEvent.CLICK, click);
		}

		public function get data():Object{
			return _data;
		}
		
		public function set data(value:Object):void{
	        _data = value;	    
			if(data)
				this.label.text = data[type].value;
	    	invalidateProperties();
	        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
	    }
	    override protected function createChildren() : void		{
			super.createChildren();
			addChild(label);
		}
		public function set listData(value:BaseListData):void{
			this._listData = DataGridListData(value);
		}
		public function get listData():BaseListData{
			return _listData
		}
		
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			label.move(0,0);
			label.setActualSize(unscaledWidth,unscaledHeight);
		}
		private function click(evt:Event):void{
			(parent.parent.parent.parent as LibraryBrowser).browserView.onClickPlayList(type, data[type])
		}
		protected function over(evt:Event) : void			{
			label.setStyle("textDecoration", "underline");
			label.setStyle("fontWeight", "bold");
		}
		protected function out(evt:Event) : void			{
			label.setStyle("textDecoration", "none");
			label.setStyle("fontWeight", "normal");
		}
	}
}