package org.yass.main.renderers
{
	import mx.controls.DataGrid;
	import mx.controls.Label;
	import mx.controls.listClasses.IListItemRenderer;
	
	import org.yass.debug.log.Console;

	public class LabelDataGridRenderer  extends Label implements IListItemRenderer{
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(owner is DataGrid){
				setStyle("paddingLeft",  (owner as DataGrid).getStyle("paddingLeft"));
	            text = data[(owner as DataGrid).columns[listData.columnIndex].dataField];
				if((owner as DataGrid).isItemSelected(data))
					setStyle("color", (owner as DataGrid).getStyle("textSelectedColor"));
				else
					setStyle("color", (owner as DataGrid).getStyle("color"));
		}}
	}
}