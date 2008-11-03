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
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.external.*;

	public class Gallery extends Sprite
	{
		/* Déclaration des variables de la classe */
		private var theme:Theme;
		private var XMLLoader:URLLoader = new URLLoader();
		private var SlideLoader:Loader = new Loader();
		private var XMLImages:XML;
		private var nbSlide:Number = 0;
		private var nbLoadedSlide:Number = 0;
		private var loadingSlide:Array = new Array();
		private var tempLoadID:Number = -1;
		private var loadingPercent:Number = 0;
		private var needRefresh:Boolean = false;
		private var isSliding:Boolean = false;
		private var stepMove:Number = 0;
		private var scrollbar:Scrollbar;
		private var JSCallback:String;
		private var timer:SlideTimer;
		private var succesLoading:Boolean = true;
		private var errorLoading:String;
		
		/* Initialisation de la classe */
		function Gallery(XMLFile:String, theme:Theme, JSCallback:String)
		{
			/* On sauvegarde notre thème */
			this.theme = theme;

			/* On sauvegarde la fonction JS a appelé */
			this.JSCallback = JSCallback;
			
			/* On configure notre loader pour le flux XML */
			XMLLoader.dataFormat = "text";
			XMLLoader.addEventListener(Event.COMPLETE, onXMLComplete);
			XMLLoader.addEventListener(IOErrorEvent.IO_ERROR, onXMLError);
			XMLLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onXMLError);

			/* On configure notre loader de miniature */
			SlideLoader.contentLoaderInfo.addEventListener(Event.COMPLETE, onSlideComplete);
			SlideLoader.contentLoaderInfo.addEventListener(IOErrorEvent.IO_ERROR, onSlideError);
			SlideLoader.contentLoaderInfo.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSlideError);
			
			/* On récupère notre flux XML */
			XMLLoader.load(new URLRequest(XMLFile));
			
			/* On gére l'evenement de présence de la sourie */
			this.addEventListener( Event.ENTER_FRAME, onEnterFrame );
			
			/* On initialise le timer */
			if (this.theme.themeSlideAutoSelect) {
				this.timer = new SlideTimer(this.theme.themeSlideTimeSelect, this.JSCallback);
				this.timer.setSlideList(loadingSlide);
			}
			
			/* Declaration de la fonction Javascript Externe */
			try  {
				ExternalInterface.addCallback("SelectSlide", selectSlideNumber);
			}
			catch(e:Error) {
				trace( e.toString() ) ;
			}
		}

		/* Retourne le pourcentage de preload */
		public function getLoadingPercent() : Number
		{
			return this.loadingPercent;
		}

		/* Retourne le statu concernant le loading de la gallery */
		public function getLoadingStatus():Boolean
		{
			return this.succesLoading;
		}
		
		/* Retourne le message d'erreur */
		public function getError():String
		{
			return this.errorLoading;
		}
		
		/* Sélectionne le slide passé en paramètre. Numéro du slide (à partir de 1) et non ID du slide. */
		public function selectSlideNumber(offset:Number):void
		{
			var nbSlide:Number = loadingSlide.length;
			
			if (offset < 1) offset = 1;
			if (offset > nbSlide) offset = nbSlide;
			onMoveSlide(offset);
			onUpdateScrollbar(offset);
		}
		
		/* Cette fonction est appelé lorsque l'on a récupérer le flux XML */
		private function onXMLComplete(event:Event) : void
		{
			/* On récupère le contenu de notre fichier XML */
			this.XMLImages = new XML(XMLLoader.data);
			
			try
			{
				/* On récupère le nombre d'image et lance le téléchargement */
				this.nbSlide = this.XMLImages.children().length(); 
				loadNextSlide();
			}
			catch(e:Error)
			{
				trace( e.toString() ) ;			
			}			
		}
		
		/* Cette fonction est appelé lorsque le fichier XML n'a pas été téléchargé */
		private function onXMLError(event:Event):void
		{
			this.succesLoading = false;
			this.errorLoading = "Le fichier XML contenant la liste des images n'a pu être téléchargé.";
			this.loadingPercent = 100;
		}

		/* Cette fonction télécharge la miniature suivante */
		private function loadNextSlide() : void
		{
			if (this.nbLoadedSlide < this.nbSlide) {
				this.tempLoadID = this.XMLImages.image[this.nbLoadedSlide].attribute("id");
				var filename:String = this.XMLImages.image[this.nbLoadedSlide].path ;
				this.SlideLoader.load(new URLRequest(filename));
			} else {
				/* On positionne les slides */
				initSlidePosition();
				
				/* On envoi la commande d'affichage d'info sur le 1er slide */
				if (this.JSCallback != "") {
					var firstSlide:Slide = this.loadingSlide[0];
					try
					{
						ExternalInterface.call(this.JSCallback, firstSlide.getID());
					}
					catch(e:Error) {
						trace( e.toString() ) ;
					}
				}

				/* On notifie le fait que le chargement est terminé */
				this.loadingPercent = 100;
			}
		}
		
		/* Cette fonction position positionne les slides dans la position de départ */
		private function initSlidePosition() : void
		{
			var slide:Slide;
			var cpt:Number = loadingSlide.length;
			
			/* On donne leur nouvelle position à chaque slide */
			for each (slide in loadingSlide) {
				swapChildrenAt( --cpt , this.getChildIndex( slide ));
				slide.redraw();
			}			

			/* Dégradé sur le coté gauche */
			var matrLeft:Matrix = new Matrix();
			matrLeft.createGradientBox(60, this.theme.animHeight, 0, 0, 0);
			var leftDegrade:Sprite = new Sprite();
			leftDegrade.graphics.beginGradientFill(GradientType.LINEAR, [ this.theme.themeBackgroundColor, this.theme.themeBackgroundColor ], [ 1, 0 ], [ 0x00, 0xFF ], matrLeft);
			leftDegrade.graphics.drawRect(0, 0, 60, this.theme.animHeight);
			leftDegrade.mouseEnabled = false;
			addChild(leftDegrade);				
			
			/* Dégradé sur le coté droit */
			var matrRight:Matrix = new Matrix();
			matrRight.createGradientBox(60, this.theme.animHeight, 0, 0, 0);
			var rightDegrade:Sprite = new Sprite();
			rightDegrade.graphics.beginGradientFill(GradientType.LINEAR, [ this.theme.themeBackgroundColor, this.theme.themeBackgroundColor ], [ 0, 1 ], [ 0x00, 0xFF ], matrRight);
			rightDegrade.graphics.drawRect(0, 0, 60, this.theme.animHeight);
			rightDegrade.x = this.theme.animWidth - 60;
			rightDegrade.mouseEnabled = false;
			addChild(rightDegrade);				
			
			/* On affiche la barre de scroll si il y a plus d'un slide */
			if (this.nbSlide > 1) {
				scrollbar = new Scrollbar(this.nbSlide, this.theme, this.timer);
				scrollbar.onMoveSlide = onMoveSlide;
				scrollbar.y = this.theme.themeScrollbarTop;
				this.addChild(scrollbar);
			}
		}

		/* Cette fonction est appelé lorsque l'on a fini de télécharger une miniature */
		private function onSlideComplete(event:Event) : void
		{
			/* On récupère l'image */
			var loadedImage:Bitmap = Bitmap(this.SlideLoader.content);
			var bitmapData:BitmapData = new BitmapData(this.SlideLoader.width, this.SlideLoader.height);
			bitmapData.draw(this.SlideLoader);
			
			/* On instancie un nouveau slide */
			var slide:Slide = new Slide(bitmapData, this.theme, this.JSCallback);
			slide.y = this.theme.themeGalleryTop;
			slide.cacheAsBitmap = true;
			slide.setID(this.tempLoadID);
			slide.setOrder(this.nbLoadedSlide + 1);
			slide.setDisplayNumber(1);
			slide.onMoveSlide = onMoveSlide;
			slide.onUpdateScrollbar = onUpdateScrollbar;
			addChild( slide );

			/* On sauvegarde notre instance et on passe au suivant */
			loadingSlide.push(slide);
			this.nbLoadedSlide++;
			loadNextSlide();
			
			/* On met à jour le pourcentege de chargement */
			this.loadingPercent = Math.max (this.loadingPercent, this.nbLoadedSlide / (this.nbSlide + 1) * 100);
		}
		
		/* Cette fonction est appellé lorsqu'une miniature n'a pu être téléchargée*/
		private function onSlideError(event:Event):void
		{
			/* On passe au suivant, l'info est logé dans le fichier flashlog */
			trace("Une erreur c'est produite lors du chargement du fichier : "+this.XMLImages.image[this.nbLoadedSlide].path);
			this.nbLoadedSlide++;
			loadNextSlide();
		}

		/* Cette fonction donne l'ordre de se redissiner à tout les slides */
		public function onMoveSlide(displayNumber:Number) : void
		{
			/* Si un slide est en cour d'affichage on attend */
			if (this.isSliding == false) {
				/* On notifie la gallery qu'un slide est en cour d'affichage */
				this.isSliding = true;
				
				/* Initialisation des variables */
				var slide:Slide;
				var orderedSlide:Array = new Array();
				
				/* On donne leur nouvelle position à chaque slide */
				for each (slide in loadingSlide) {
					slide.setStepMove(0, false);
					slide.setDisplayNumber(displayNumber);
					orderedSlide.push( {theSlide:slide, dist:Math.abs( slide.getOrder() - displayNumber )} );
				}
				
				/* On tri les slides pour la position des claques */
				var obj:Object; var cpt:Number = 0;
				orderedSlide.sortOn( "dist", Array.DESCENDING | Array.NUMERIC );
				for each (obj in orderedSlide) {
					swapChildrenAt( cpt++ , this.getChildIndex( obj.theSlide ) );
				}
				
				/* On lance le déplacement des slides */
				this.stepMove = 0;
				this.needRefresh = true;
			}
		}

		/* Cette fonction met à jour la position de la barre de scroll */
		public function onUpdateScrollbar(displayNumber:Number) : void
		{
			this.scrollbar.setDisplayStep(displayNumber);
		}
				
		/* Cette fonction permet de définir l'ordre de transition de l'image */
		private function onEnterFrame( event:Event ) : void
		{
			if (this.needRefresh)
			{
				this.stepMove++;
				if (this.stepMove > this.theme.themeStepTransition) {
					this.stepMove = 0;
					this.needRefresh = false;
					this.isSliding = false;
				} else {
					var slide:Slide;
					for each (slide in loadingSlide) {
						slide.setStepMove(this.stepMove);
					}
				}
			}			
		}
	}
}