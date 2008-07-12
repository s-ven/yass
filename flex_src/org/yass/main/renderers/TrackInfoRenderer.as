package org.yass.main.renderers
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.controls.DataGrid;
	import mx.controls.listClasses.IListItemRenderer;
	
	import org.yass.main.LibraryBrowser;

	public class TrackInfoRenderer  extends LabelDataGridRenderer implements IListItemRenderer{
		
		public function TrackInfoRenderer(){
			super();
			this.useHandCursor = true;
			this.mouseChildren = false;
			this.buttonMode = true;
			this.addEventListener(MouseEvent.MOUSE_OVER, over);
			this.addEventListener(MouseEvent.MOUSE_OUT, out);
			this.addEventListener(MouseEvent.CLICK, click);
		}
		private function click(evt:Event):void{
			var dataField:String = (owner as DataGrid).columns[listData.columnIndex].dataField;
			(parent.parent.parent.parent as LibraryBrowser).browserView.onClickPlayList(dataField, data[dataField])
		}
		protected function over(evt:Event) : void			{
			setStyle("textDecoration", "underline");
			setStyle("fontWeight", "bold");
		}
		protected function out(evt:Event) : void			{
			setStyle("textDecoration", "none");
			setStyle("fontWeight", "normal");
		}
	}
}