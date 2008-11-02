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
	import flash.display.Loader;
	import flash.events.Event;
	import flash.net.URLRequest;

	[ SWF( backgroundColor='0xffffff', frameRate='40', width='660', height='290' ) ]

	public class CoverFlow extends Sprite
	{
		/* D�claration des variables de la classe */
		private var paramTheme:String = "themes/default/";
		private var paramJSFunction:String = "ATBRetrieveInfo";
		private var paramListCover:String = "CoversTest.xml";
		private var paramSelectSlide:Number = 1;
		private var theme:Theme;
		private var gallery:Gallery;
		private var loadingBitmap:Sprite;
		private var backgroundColor:Sprite;
		
		/* Fonction d'initialisation */
		function CoverFlow():void
		{
			/* On r�cup�re les param�tres pass� au Flash */
			var parms:Object = this.loaderInfo.parameters;
			if (parms.Theme) {
				this.paramTheme = parms.Theme;
				if (this.paramTheme.charAt(this.paramTheme.length - 1) != "/")
					this.paramTheme += "/";
			}
			if (parms.JSFunction) this.paramJSFunction = parms.JSFunction;
			if (parms.ListCover) this.paramListCover = parms.ListCover;
			if (parms.FirstSlide) this.paramSelectSlide = parms.FirstSlide;
			
			/* On lance la r�cup�ration du fichier Th�me */
			if (this.paramListCover != "") {
				this.theme = new Theme(this.paramTheme);
				this.theme.addEventListener(ThemeEvent.LOADED, onThemeLoaded);
			}
		}
		
		/* Fonction appel� une fois le th�me charg� */
		private function onThemeLoaded(event:ThemeEvent):void
		{
			/* Si le th�me a �t� correctement charg� */
			if (this.theme.isValid()) {
				/* On r�cup�re les dimensions de l'anim */
				this.theme.animWidth = stage.stageWidth;
				this.theme.animHeight = stage.stageHeight;
				stage.align = "LT"; 
				
				/* On instantie la gallery */
				this.gallery = new Gallery(this.paramListCover, this.theme, this.paramJSFunction);
				addChild(this.gallery);
			
				/* On r�cup�re la couleur de fond */
				this.backgroundColor = new Sprite();
				this.backgroundColor.graphics.beginFill(this.theme.themeBackgroundColor, 1);
				this.backgroundColor.graphics.drawRect(0, 0, this.theme.animWidth, this.theme.animHeight);
				addChild(this.backgroundColor);
				
				/* On affiche le bitmap de loading */
				this.loadingBitmap = theme.getLoadedImage("loading.png");
				this.loadingBitmap.x = (stage.stageWidth - loadingBitmap.width) / 2;
				this.loadingBitmap.y = (stage.stageHeight - loadingBitmap.height) / 2;
				addChild(this.loadingBitmap);

				/* Gestion de la barre de progression */
				this.addEventListener(Event.ENTER_FRAME, onEnterFrame);
			} else {
				var error:ErrorMsg = new ErrorMsg(this.theme.getError(), stage.stageWidth, stage.stageHeight);
				this.addChild(error);
			}
		}

		/* Cette fonction dessine l'avancement de la barre de progression */
		private function onEnterFrame(event:Event):void
		{
			var preloadValue:Number = this.gallery.getLoadingPercent();
			if (preloadValue < 100) {
				this.loadingBitmap.graphics.beginFill(this.theme.themeLoadingProgressColor, 1);
				this.loadingBitmap.graphics.drawRect(this.theme.themeLoadingProgressLeft, this.theme.themeLoadingProgressTop,
				                                     preloadValue * this.theme.themeLoadingProgressWidth / 100, this.theme.themeLoadingProgressHeight);
				this.loadingBitmap.graphics.endFill();
			} else {
				this.removeChild(this.loadingBitmap);
				this.removeEventListener(Event.ENTER_FRAME, onEnterFrame);
				if (this.gallery.getLoadingStatus() == false) {
					var error:ErrorMsg = new ErrorMsg(this.gallery.getError(), stage.stageWidth, stage.stageHeight);
					this.addChild(error);
				} else {
					swapChildren(this.backgroundColor, this.gallery);
				}
				/* On s�lectionne le slide � afficher */
				this.gallery.selectSlideNumber(this.paramSelectSlide);
			}
		}
	}
}