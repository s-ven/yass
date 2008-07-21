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
	import mx.controls.DataGrid;
	import mx.controls.Label;
	import mx.controls.listClasses.IListItemRenderer;
	
	import org.yass.debug.log.Console;

	public class LabelDataGridRenderer  extends Label implements IListItemRenderer{
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(owner is DataGrid){
	            text = data[(owner as DataGrid).columns[listData.columnIndex].dataField];
				if((owner as DataGrid).isItemHighlighted(data))
					setStyle("color", (owner as DataGrid).getStyle("textRollOverColor"));
				else if((owner as DataGrid).isItemSelected(data))
					setStyle("color", (owner as DataGrid).getStyle("textSelectedColor"));
				else
					setStyle("color", (owner as DataGrid).getStyle("color"));
			}
		}
	}
}