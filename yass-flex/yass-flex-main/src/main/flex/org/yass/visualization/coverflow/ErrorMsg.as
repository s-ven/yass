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
package org.yass.visualization.coverflow {
	import flash.display.*;
	import flash.text.*;
	
	public class ErrorMsg extends Sprite
	{
		/* Initialisation de la classe */
		function ErrorMsg(description:String, animWidth:Number, animHeight:Number)
		{
			/* On défni le style du message */
			var errorStyle:Object = new Object();
			errorStyle.color = "#ffffff"
			errorStyle.fontWeight = "bold";
			errorStyle.textAlign = "center";
			errorStyle.fontFamily = "sans-serif";
			
			var style:StyleSheet = new StyleSheet();
			style.setStyle(".error", errorStyle);

			/* On défini le texte du message */
			var errorMsg:TextField = new TextField();
			errorMsg.width = 250;
			errorMsg.wordWrap = true;
			errorMsg.selectable = false;
			errorMsg.autoSize = TextFieldAutoSize.CENTER;
			errorMsg.antiAliasType = AntiAliasType.ADVANCED;
			errorMsg.styleSheet = style;
            errorMsg.htmlText = "<span class='error'>"+description+"</span>";
			errorMsg.x = Math.round((animWidth - errorMsg.width) / 2);
			errorMsg.y = Math.round((animHeight - errorMsg.height) / 2);
			
			/* On dessine le message en arrière plan */
			var background:Sprite = new Sprite();
			background.graphics.beginFill(0xff0000, 0.8);
			background.graphics.drawRoundRect(0, 0, errorMsg.width+60, errorMsg.height+40, 25, 25);
			background.x = Math.round((animWidth - background.width) / 2);
			background.y = Math.round((animHeight - background.height) / 2);
			addChild(background);

			/* On ajoute le message d'erreur */
			addChild(errorMsg);
		}
	}
}