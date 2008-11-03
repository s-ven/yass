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
	import flash.events.*;
	import flash.utils.*;
	import flash.net.*;

	public class Theme extends EventDispatcher
	{
		/* Déclaration des variables de la classe */
		private var error:String = "";
		private var versionXMLTheme:String = "1.0";
		private var Directory:String = "";
		private var XMLLoader:URLLoader = new URLLoader();
		private var XMLTheme:XML;
		private var BitmapLoader:Loader = new Loader();
		private var ValidTheme:Boolean = true;
		private var OffsetLoadBitmap:Number = 0;
		private var LoadedBitmap:Array = new Array();
		private var ListBitmap:Array = new Array("loading.png", "scroll_background.png", "scrollbar.png",
		                                         "scroll_array_left.png", "scroll_array_right.png");
		
		/* Paramètre d'un thème */
		public var themeVersion:String = "";
		public var themeAuthorName:String = "";
		public var themeAuthorURL:String = "";
		public var themeBackgroundColor:Number = 0xffffff;
		public var themeGalleryTop:Number = 10;
		public var themeStepTransition:Number = 4;
		public var themeSlideWidth:Number = 220;
		public var themeSlideHeight:Number = 165;
		public var themeSpaceBetweenSlide:Number = 40;
		public var themeSlideAutoSelect:Boolean = false;
		public var themeSlideTimeSelect:Number = 3;
		public var themeReflectHeight:Number = 80;
		public var themeReflectRatio:Number = 0.65;
		public var themeLoadingProgressTop:Number = 85;
		public var themeLoadingProgressLeft:Number = 39;
		public var themeLoadingProgressWidth:Number = 133;
		public var themeLoadingProgressHeight:Number = 10;
		public var themeLoadingProgressColor:Number = 0xff990a;
		public var themeScrollbarTop:Number = 200;
		public var themeScrollbarArrowWidth:Number = 22;
		public var themeScrollbarStepClick:Number = 6;
		public var animWidth:Number = 0;
		public var animHeight:Number = 0;

		/* Initialisation de la classe */
		function Theme(ThemeDirectory:String):void
		{
			/* On sauvegarde le chemin du r�pertoire */
			this.Directory = ThemeDirectory;
			
			/* On configure notre loader pour le flux XML */
			this.XMLLoader.dataFormat = "text";
			this.XMLLoader.addEventListener(Event.COMPLETE, onXMLComplete);
			this.XMLLoader.addEventListener(IOErrorEvent.IO_ERROR, onLoadError);
			this.XMLLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onLoadError);

			/* On configure notre loader de miniature */
			this.BitmapLoader.contentLoaderInfo.addEventListener(Event.COMPLETE, onBitmapLoadComplete);
			this.BitmapLoader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, onLoadError);
			this.BitmapLoader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onLoadError);

			/* On récupère notre flux XML */
			this.XMLLoader.load(new URLRequest(ThemeDirectory+"theme.xml"));
		}
		
		/* Cette fonction retourne le statu du thème : valide ou invalide */
		public function isValid():Boolean
		{
			return this.ValidTheme;
		}
		
		/* Cette fonction retourne le Sprite associé à l'image */
		public function getLoadedImage(name:String):Sprite
		{
			for (var index:Number = 0; index <= this.ListBitmap.length; index++) {
				if (this.ListBitmap[index] == name)
					return this.LoadedBitmap[index];
			}
			return null;
		}
		
		/* Retourne le message d'erreur */
		public function getError():String
		{
			return this.error;
		}
		
		/* Cette fonction est appelé lorsque l'on a récupère le flux XML */
		private function onXMLComplete(event:Event) : void
		{
			/* On récupère le contenu de notre fichier XML */
			this.XMLTheme = new XML(this.XMLLoader.data);
			
			try
			{
				/* On récupère les différentes valeur du fichier XML */
				if (checkParam(this.XMLTheme.attribute("version")))
					this.themeVersion = this.XMLTheme.attribute("version");
				if (checkParam(this.XMLTheme.Author.attribute("name")))
					this.themeAuthorName = this.XMLTheme.Author.attribute("name");
				if (checkParam(this.XMLTheme.Author.attribute("url")))
					this.themeAuthorURL = this.XMLTheme.Author.attribute("url");
				if (checkParam(this.XMLTheme.Application.attribute("backgroundColor")))
					this.themeBackgroundColor = Number(this.XMLTheme.Application.attribute("backgroundColor"));
				if (checkParam(this.XMLTheme.Application.Gallery.attribute("top")))
					this.themeGalleryTop = Number(this.XMLTheme.Application.Gallery.attribute("top"));
				if (checkParam(this.XMLTheme.Application.Gallery.attribute("stepTransition")))
					this.themeStepTransition = Number(this.XMLTheme.Application.Gallery.attribute("stepTransition"));
				if (checkParam(this.XMLTheme.Application.Slide.attribute("spaceBetweenUs")))
					this.themeSpaceBetweenSlide = Number(this.XMLTheme.Application.Slide.attribute("spaceBetweenUs"));
				if (checkParam(this.XMLTheme.Application.Slide.Dimension.attribute("width")))
					this.themeSlideWidth = Number(this.XMLTheme.Application.Slide.Dimension.attribute("width"));
				if (checkParam(this.XMLTheme.Application.Slide.Dimension.attribute("height")))
					this.themeSlideHeight = Number(this.XMLTheme.Application.Slide.Dimension.attribute("height"));
				if (checkParam(this.XMLTheme.Application.Slide.AutoSelect.attribute("enabled"))) {
					if (this.XMLTheme.Application.Slide.AutoSelect.attribute("enabled") == "yes")
						this.themeSlideAutoSelect = true;
					else
						this.themeSlideAutoSelect = false;
				}
				if (checkParam(this.XMLTheme.Application.Slide.AutoSelect.attribute("time")))
					this.themeSlideTimeSelect = Number(this.XMLTheme.Application.Slide.AutoSelect.attribute("time"));
				if (checkParam(this.XMLTheme.Application.Slide.Reflect.attribute("height")))
					this.themeReflectHeight = Number(this.XMLTheme.Application.Slide.Reflect.attribute("height"));
				if (checkParam(this.XMLTheme.Application.Slide.Reflect.attribute("ratio")))
					this.themeReflectRatio = Number(this.XMLTheme.Application.Slide.Reflect.attribute("ratio"));
				if (checkParam(this.XMLTheme.LoadingProgress.attribute("top")))
					this.themeLoadingProgressTop = Number(this.XMLTheme.LoadingProgress.attribute("top"));
				if (checkParam(this.XMLTheme.LoadingProgress.attribute("left")))
					this.themeLoadingProgressLeft = Number(this.XMLTheme.LoadingProgress.attribute("left"));
				if (checkParam(this.XMLTheme.LoadingProgress.attribute("width")))
					this.themeLoadingProgressWidth = Number(this.XMLTheme.LoadingProgress.attribute("width"));
				if (checkParam(this.XMLTheme.LoadingProgress.attribute("height")))
					this.themeLoadingProgressHeight = Number(this.XMLTheme.LoadingProgress.attribute("height"));
				if (checkParam(this.XMLTheme.LoadingProgress.attribute("color")))
					this.themeLoadingProgressColor = Number(this.XMLTheme.LoadingProgress.attribute("color"));
				if (checkParam(this.XMLTheme.Scrollbar.attribute("top")))
					this.themeScrollbarTop = Number(this.XMLTheme.Scrollbar.attribute("top"));
				if (checkParam(this.XMLTheme.Scrollbar.attribute("arrowWidth")))
					this.themeScrollbarArrowWidth = Number(this.XMLTheme.Scrollbar.attribute("arrowWidth"));
				if (checkParam(this.XMLTheme.Scrollbar.attribute("stepClick")))
					this.themeScrollbarStepClick = Number(this.XMLTheme.Scrollbar.attribute("stepClick"));
				
				/* On vérifie si la version du thème est la bonne */
				if (this.themeVersion != this.versionXMLTheme) {
					/* La version du fichier XML n'est pas la bonne */
					this.ValidTheme = false;
					this.error = "La version du thème est incorrect.\nVersion attendue "+this.versionXMLTheme+", version actuel "+this.themeVersion;
					this.loadThemeCompleted();
				} else {
					/* On lance le chargement des images */
					loadNextBitmap();
				}
			}
			catch(e:Error)
			{
				/* Le fichier XML n'est pas valide */
				this.ValidTheme = false;
				this.error = e.toString();
				this.loadThemeCompleted();
			}
		}
		
		/* Cette fonction vérifie que le paramètre est correctement rempli */
		private function checkParam(param:String):Boolean
		{
			if (param == "") {
				this.ValidTheme = false;
				this.error = "Le fichier XML contenant la description du thème n'est pas valide.";
				return false;
			}
			return true;
		}

		/* Cette fonction charge les images lié au thème les unes aprés les autres */
		private function loadNextBitmap():void
		{
			if (this.OffsetLoadBitmap < this.ListBitmap.length) {
				/* On lance le chargement du fichier suivant */
				this.BitmapLoader.load(new URLRequest(this.Directory + this.ListBitmap[this.OffsetLoadBitmap]));
			} else {
				/* On a fini de charger les images */
				this.loadThemeCompleted();
			}
		}
		
		/* Fonction appelée une fois que l'image a été chargée */
		private function onBitmapLoadComplete(event:Event):void
		{
			/* On récupère l'image */
			var loadedImage:Bitmap = Bitmap(this.BitmapLoader.content);
			var image:Sprite = new Sprite();
			image.graphics.beginBitmapFill(loadedImage.bitmapData);
			image.graphics.drawRect(0, 0, this.BitmapLoader.width, this.BitmapLoader.height);
			this.LoadedBitmap.push(image);

			/* On passe au chargement de l'image suivante */
			this.OffsetLoadBitmap++;
			loadNextBitmap();
		}

		/* Cette fonction est appelé lorsque qu'un fichier du thème n'est pas trouvé */
		private function onLoadError(event:Event) : void
		{
			/* Le theme n'est pas valide, le fichier theme.xml n'a pas été trouvé */
			this.ValidTheme = false;
			this.error = "Impossible de récupérer le fichier :\n"+this.ListBitmap[this.OffsetLoadBitmap];
			this.loadThemeCompleted();
		}
		
		/* On informe de la fin du chargement du thème */
		private function loadThemeCompleted():void
		{
			var eventLoaded:ThemeEvent = new ThemeEvent( ThemeEvent.LOADED );
			dispatchEvent(eventLoaded);			
		}
	}
}