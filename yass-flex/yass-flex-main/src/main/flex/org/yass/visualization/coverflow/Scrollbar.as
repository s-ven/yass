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
package org.yass.visualization.coverflow
{
	import flash.display.*;
	import flash.events.*;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.geom.Matrix;
	
	public class Scrollbar extends Sprite
	{
		/* D�claration des variables priv�e */
		private var nbStep:Number = 0;
		private var theme:Theme;
		private var scrollbarBackgroundDisplay:Sprite;
		private var scrollbarDisplay:Sprite;
		private var scrollbarImageSource:Sprite;
		private var scrollbarWidth:Number = 60;
		private var scrollbarSlideWidth:Number = 0;
		private var stepWidth:Number = 15;
		private var timer:SlideTimer;

		/* D�claration des donn�es publique */
		public var onMoveSlide:Function;
		
		/* Fonction d'initialisation de la classe */
		function Scrollbar(step:Number, theme:Theme, timer:SlideTimer)
		{
			/* On r�cup�re le nombre de slide � afficher */
			this.nbStep = step;
			
			/* On r�cup�re le th�me */
			this.theme = theme;
			
			/* On r�cup�re le timer */
			this.timer = timer;
			
			/* On dessine les �l�ments de base pour la scrollbar */
			drawDataScrollBar();
			
			/* On dessine l'ascenseur de la scrollbar */
			drawScrollBar();
			
			/* On ajoute la gestion du click sur la scrollbar  */
			scrollbarBackgroundDisplay.addEventListener( MouseEvent.CLICK, scrollbarGoto );
			scrollbarDisplay.addEventListener( MouseEvent.MOUSE_DOWN, scrollbarStartDrag );
		}
		
		/* Cette fonction repositionne la bar de scroll */
		public function setDisplayStep(step:Number) : void
		{
			/* On position le slide */
			var position:Number = (step - 1) * this.stepWidth + this.scrollbarBackgroundDisplay.x + this.theme.themeScrollbarArrowWidth
			this.scrollbarDisplay.x = Math.ceil(position);
			drawScrollBar();
			
			/* On indique au timer que l'on a choisi une nouvelle image */
			this.timer.updateNumberSlide(step);
		}
		
		/* Cette fonction dessine notre bare de scroll */
		private function drawDataScrollBar() : void
		{
			/* On affiche le bitmap de fond */
			this.scrollbarBackgroundDisplay = this.theme.getLoadedImage("scroll_background.png");
			this.scrollbarBackgroundDisplay.x = (this.theme.animWidth - this.scrollbarBackgroundDisplay.width) / 2;
			addChild(this.scrollbarBackgroundDisplay);
			
			/* On affiche le bitmap de la barre de scroll avec affichage par d�faut */
			this.scrollbarImageSource = theme.getLoadedImage("scrollbar.png");
			this.scrollbarDisplay = new Sprite();
			this.scrollbarDisplay.x = this.scrollbarBackgroundDisplay.x + this.theme.themeScrollbarArrowWidth;
			addChild(this.scrollbarDisplay);

			/* Affichage de la fleche gauche */
			var scrollArrayLeftDisplay:Sprite = theme.getLoadedImage("scroll_array_left.png");
			scrollArrayLeftDisplay.x = this.scrollbarBackgroundDisplay.x;
			scrollArrayLeftDisplay.addEventListener( MouseEvent.CLICK, scrollbarPrevItem );
			addChild(scrollArrayLeftDisplay);

			/* Affichage de la fleche droite */
			var scrollArrayRightDisplay:Sprite = theme.getLoadedImage("scroll_array_right.png");
			scrollArrayRightDisplay.x = this.scrollbarBackgroundDisplay.x + this.scrollbarBackgroundDisplay.width - scrollArrayRightDisplay.width;
			scrollArrayRightDisplay.addEventListener( MouseEvent.CLICK, scrollbarNextItem );
			addChild(scrollArrayRightDisplay);

			/* On calcul la taille de le scrollbar */
			this.scrollbarWidth = Math.round((this.scrollbarBackgroundDisplay.width - 2 * this.theme.themeScrollbarArrowWidth) - this.nbStep * Math.round(this.scrollbarBackgroundDisplay.width * 0.05));
			if (this.scrollbarWidth < 60) this.scrollbarWidth = 60;
			this.scrollbarSlideWidth = (this.scrollbarBackgroundDisplay.width - 2 * this.theme.themeScrollbarArrowWidth) - this.scrollbarWidth;
			this.stepWidth = this.scrollbarSlideWidth / (this.nbStep - 1);
		}
		
		/* Cette fonction redessine la bar de scroll */
		private function drawScrollBar() : void
		{
			/* On efface les pr�c�dentes informations */
			scrollbarDisplay.graphics.clear();

			/* On dessine la zone du milieu */
			var backgroundScrollbarMiddle:BitmapData = new BitmapData(20, this.scrollbarImageSource.height, true, 0);
			var translateMiddleMatrix:Matrix = new Matrix();
			translateMiddleMatrix.translate(-40, 0);
			backgroundScrollbarMiddle.draw(this.scrollbarImageSource, translateMiddleMatrix, null, null, new Rectangle(0, 0, 20, this.scrollbarImageSource.height));
			scrollbarDisplay.graphics.beginBitmapFill(backgroundScrollbarMiddle);
			scrollbarDisplay.graphics.drawRect(20, 0, this.scrollbarWidth - 40, this.scrollbarImageSource.height);
			scrollbarDisplay.graphics.endFill();

			/* On dessine la zone de gauche */
			var backgroundScrollbarLeft:BitmapData = new BitmapData(20, this.scrollbarImageSource.height, true, 0);
			var translateLeftMatrix:Matrix = new Matrix();
			if ((this.scrollbarDisplay.x - this.scrollbarBackgroundDisplay.x) <= this.theme.themeScrollbarArrowWidth)
				translateLeftMatrix.translate(-20, 0);
			else
				translateLeftMatrix.translate(0, 0);
			backgroundScrollbarLeft.draw(this.scrollbarImageSource, translateLeftMatrix, null, null, new Rectangle(0, 0, 20, this.scrollbarImageSource.height));
			scrollbarDisplay.graphics.beginBitmapFill(backgroundScrollbarLeft);
			scrollbarDisplay.graphics.drawRect(0, 0, 20, this.scrollbarImageSource.height);
			scrollbarDisplay.graphics.endFill();

			/* On dessine la zone de droite */
			var backgroundScrollbarRight:BitmapData = new BitmapData(20, this.scrollbarImageSource.height, true, 0);
			var translateRightMatrix:Matrix = new Matrix();
			if (this.scrollbarDisplay.x >= this.scrollbarBackgroundDisplay.x + this.theme.themeScrollbarArrowWidth + this.scrollbarSlideWidth)
				translateRightMatrix.translate(-60, 0);
			else
				translateRightMatrix.translate(-80, 0);
			backgroundScrollbarRight.draw(this.scrollbarImageSource, translateRightMatrix, null, null, new Rectangle(0, 0, 20, this.scrollbarImageSource.height));
			var translateDrawRightMatrix:Matrix = new Matrix();
			translateDrawRightMatrix.translate(this.scrollbarWidth % 20, 0);
			scrollbarDisplay.graphics.beginBitmapFill(backgroundScrollbarRight, translateDrawRightMatrix);
			scrollbarDisplay.graphics.drawRect(this.scrollbarWidth - 20, 0, 20, this.scrollbarImageSource.height);
			scrollbarDisplay.graphics.endFill();
		}
		
		/* Cette fonction retourne le num�ro du slide affich� */
		public function getCurrentSlide():Number
		{
			var offset:Number = this.scrollbarDisplay.x - this.scrollbarBackgroundDisplay.x - this.theme.themeScrollbarArrowWidth;
			var numberSlide:Number = Math.floor(offset / this.stepWidth) + 1;
			return numberSlide;
		}
		
		/* Cette fonction identifie l'endroit du click dans la scrollbar */
		private function scrollbarGoto(event:MouseEvent):void
		{
			/* On r�cup�re le num�ro du slide affich� */
			var currentSlide:Number = getCurrentSlide();
			if (event.localX < scrollbarDisplay.x)
				currentSlide -= this.theme.themeScrollbarStepClick;
			else
				currentSlide += this.theme.themeScrollbarStepClick;
			if (currentSlide < 1) currentSlide = 1;
			if (currentSlide > this.nbStep) currentSlide = this.nbStep;
			
			/* On redessine la bar de scroll */
			var position:Number = (currentSlide - 1) * this.stepWidth + this.scrollbarBackgroundDisplay.x + this.theme.themeScrollbarArrowWidth
			this.scrollbarDisplay.x = Math.ceil(position);
			drawScrollBar();
			
			/* On d�place les slides */
			onMoveSlide.call(null, currentSlide);
			if (this.theme.themeSlideAutoSelect)
				this.timer.startTimer(currentSlide);
		}
		
		/* Cette fonction commence le scroll */
		private function scrollbarStartDrag(event:MouseEvent) : void
		{
			stage.addEventListener( MouseEvent.MOUSE_UP, scrollbarStopDrag );
			stage.addEventListener( MouseEvent.MOUSE_MOVE, scrollbarMove );
			scrollbarDisplay.startDrag(false, new Rectangle(this.scrollbarBackgroundDisplay.x + this.theme.themeScrollbarArrowWidth, 0, this.scrollbarSlideWidth, 0));
		}

		/* Cette fonction termine le scroll */
		private function scrollbarStopDrag( event:MouseEvent ) : void
		{
			scrollbarDisplay.stopDrag();
			stage.removeEventListener( MouseEvent.MOUSE_UP, scrollbarStopDrag);
			stage.removeEventListener( MouseEvent.MOUSE_MOVE, scrollbarMove );
			drawScrollBar();
		}
		
		/* Cette fonction redessine la barre de scroll quand elle arrive dans les coins */
		private function scrollbarMove( event:Event ) : void
		{
			/* On r�cup�re le num�ro du slide affich� */
			var currentSlide:Number = getCurrentSlide();
			
			/* On redessine la bar de scroll */
			drawScrollBar();
			
			/* On d�place les slides */
			onMoveSlide.call(null, currentSlide);
			if (this.theme.themeSlideAutoSelect)
				this.timer.startTimer(currentSlide);
		}
		
		/* On se d�place vers l'�l�ment suivant */
		private function scrollbarNextItem( event:MouseEvent ) : void
		{
			selectNewSlide(1);
		}
		
		/* On se d�place vers l'�l�ment pr�c�dent */
		private function scrollbarPrevItem( event:MouseEvent ) : void
		{
			selectNewSlide(-1);
		}
		
		/* Select new slide */
		private function selectNewSlide( step:Number ) : void
		{
			/* On r�cup�re le num�ro du slide affich� */
			var currentSlide:Number = getCurrentSlide();
			
			/* On se d�place dans l'ordre du slide */
			var newSlide:Number = currentSlide + step;
			if (newSlide < 1) newSlide = 1;
			if (newSlide > this.nbStep) newSlide = this.nbStep;
			
			/* On positionne la barre de scroll */
			var position:Number = (newSlide - 1) * this.stepWidth + this.scrollbarBackgroundDisplay.x + this.theme.themeScrollbarArrowWidth
			this.scrollbarDisplay.x = Math.ceil(position);
			drawScrollBar();
			
			/* On d�place les slides */
			onMoveSlide.call(null, newSlide);
			if (this.theme.themeSlideAutoSelect) {
				this.timer.updateNumberSlide(newSlide);
				this.timer.displayTimerHandler(null);
			}
		}
	}
}