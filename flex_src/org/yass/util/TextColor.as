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
package org.yass.util{
	import flash.filters.ColorMatrixFilter;

	import mx.controls.TextInput;

	public class TextColor {

		private static const byteToPerc:Number = 1 / 0xff;

		private var textField:TextInput;
		private var textColor:uint;
		private var selectedColor:uint;
		private var selectionColor:uint;
		private var colorMatrixFilter:ColorMatrixFilter;

		public function TextColor(ttt:TextInput, textColor:uint = 0x000000, selectionColor:uint = 0x000000, selectedColor: uint = 0x000000) {

			this.textField = ttt;

			colorMatrixFilter = new ColorMatrixFilter();
			textColor = textColor;
			selectionColor = selectionColor;
			selectedColor = selectedColor;
			updateFilter();
		}


		private function updateFilter():void {

			textField.setStyle("color", 0xff0000);

			var o:Array = splitRGB(selectionColor);
			var r:Array = splitRGB(textColor);
			var g:Array = splitRGB(selectedColor);

			var ro:int = o[0];
			var go:int = o[1];
			var bo:int = o[2];

			var rr:Number = ((r[0] - 0xff) - o[0]) * byteToPerc + 1;
			var rg:Number = ((r[1] - 0xff) - o[1]) * byteToPerc + 1;
			var rb:Number = ((r[2] - 0xff) - o[2]) * byteToPerc + 1;

			var gr:Number = ((g[0] - 0xff) - o[0]) * byteToPerc + 1 - rr;
			var gg:Number = ((g[1] - 0xff) - o[1]) * byteToPerc + 1 - rg;
			var gb:Number = ((g[2] - 0xff) - o[2]) * byteToPerc + 1 - rb;

			colorMatrixFilter.matrix = [rr, gr, 0, 0, ro, rg, gg, 0, 0, go, rb, gb, 0, 0, bo, 0, 0, 0, 1, 0];

			textField.filters = [colorMatrixFilter];

		}

		private static function splitRGB(color:uint):Array {

			return [color >> 16 & 0xff, color >> 8 & 0xff, color & 0xff];
		}
	}
}