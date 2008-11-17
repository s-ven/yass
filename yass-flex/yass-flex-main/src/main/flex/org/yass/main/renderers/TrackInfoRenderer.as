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
	import flash.events.Event;
	import flash.events.MouseEvent;

	import mx.controls.DataGrid;

	import org.yass.Yass;
	import org.yass.main.MainPane;

	public class TrackInfoRenderer  extends TrackRenderer{

		public function TrackInfoRenderer(){
			addEventListener(MouseEvent.MOUSE_OVER, over);
			addEventListener(MouseEvent.MOUSE_OUT, out);
			addEventListener(MouseEvent.CLICK, click);
		}
		private function click(evt:Event):void{
			var dataField:String = (owner as DataGrid).columns[listData.columnIndex].dataField;
			(parent.parent.parent.parent as MainPane).playList.model = Yass.library;
			(parent.parent.parent.parent as MainPane).currentState = null;
			(parent.parent.parent.parent as MainPane).browserView.onClickPlayList(dataField, data[dataField])
		}
		protected function over(evt:Event) : void{
			setStyle("textDecoration", "underline");
			setStyle("fontThickness", 200);
		}
		protected function out(evt:Event) : void{
			setStyle("textDecoration", "none");
			setStyle("fontThickness", data.isLoaded?200:0);
		}
	}
}