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
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.events.*;
	import flash.external.*;
	
	public class Slide extends Sprite
	{
		/* D�claration des variables priv�e */
		private var theme:Theme;
		private var thumbWidth:Number = 220;
		private var thumbHeight:Number = 165;
		private var reflet:Boolean = false;
		private var refletSize:Number = 80;
		private var refletRatio:Number = 0.65;
		private var maxStepMove:Number = 4;
		private var originalBitmapData:BitmapData;
		private var reflectedBitmapData:BitmapData;
		private var bitmapData:BitmapData;
		private var order:Number = 1;
		private var currentNumber:Number = 1;
		private var displayNumber:Number = 1;
		private var stepMove:Number = 0;
		private var distortImage:DistortImage;
		private var publicID:Number = -1;
		private var JSCallback:String;
		
		/* D�claration des donn�es publique */
		public var onMoveSlide:Function;
		public var onUpdateScrollbar:Function;
		
		/* Constructeur de la classe */
		function Slide(bitmapData:BitmapData, theme:Theme, JSCallback:String)
		{
			/* On sauvegarde notre th�me */
			this.theme = theme;
			
			/* On met � jour le nombre de transition */
			this.maxStepMove = this.theme.themeStepTransition;

			/* On sauvegarde la fonction JS a appel� */
			this.JSCallback = JSCallback;
			
			/* On met � jour nos variables de classe */
			this.thumbWidth = this.theme.themeSlideWidth;
			this.thumbHeight = this.theme.themeSlideHeight;
			this.refletSize = this.theme.themeReflectHeight;
			this.refletRatio = this.theme.themeReflectRatio;

			/* On r�cup�re l'image et on en fait une copie */
			this.originalBitmapData = createThumb(bitmapData);

			/* On met � jour la taille du reflet */
			if (this.originalBitmapData.height * this.refletRatio < this.refletSize)
				this.refletSize = this.originalBitmapData.height * this.refletRatio;
			
			/* On ajoute notre reflet*/
			addReflect();
			this.bitmapData = reflectedBitmapData.clone();
			
			/* On d�forme notre image */
			this.distortImage = new DistortImage(this, this.bitmapData, 5, 5);
			this.distortImage.texture = this.bitmapData;
			drawPicture();
			
			/* On clique sur le slide */
			this.addEventListener(MouseEvent.CLICK, onThumbClick);
			this.addEventListener(MouseEvent.DOUBLE_CLICK, onThumbClick);
		}
		
		/* On met � jour l'ID public */
		public function setID(newID:Number) : void
		{
			this.publicID = newID;
		}
		
		/* On retourne l'ID public */
		public function getID(): Number
		{
			return this.publicID;
		}
		
		/* On d�fini l'ordre du slide */
		public function setOrder(order:Number) : void
		{
			this.order = order;
		}

		/* On r�cup�re l'ordre du slide */
		public function getOrder() : Number
		{
			return this.order;
		}

		/* On d�fini le slide en premi�re place */
		public function setDisplayNumber(displayNumber:Number) : void
		{
			this.currentNumber = this.displayNumber;
			this.displayNumber = displayNumber;
			drawPicture();
		}

		/* On d�fini le slide en premi�re place */
		public function setStepMove(step:Number, redraw:Boolean=true) : void
		{
			this.stepMove = step;
			if (redraw)
				drawPicture();
		}

		/* Cette fonction redessine le slide dans sa position actuelle */
		public function redraw(): void
		{
			drawPicture(true);
		}
		
		/* On redimensionne l'image */
		private function createThumb(bitmapData:BitmapData):BitmapData
		{
			/* On v�rifie si l'on a besoin de redimensionner ou pas l'image */
			if (bitmapData.width > this.thumbWidth || bitmapData.height > this.thumbHeight) {
				/* On calcul le ratio de redimensionnement */
				var ratio:Number = Math.min(this.thumbWidth / bitmapData.width, this.thumbHeight / bitmapData.height);
				var bitmap:Bitmap = new Bitmap(bitmapData);
				var resultBD:BitmapData = new BitmapData(bitmapData.width * ratio, bitmapData.height * ratio);
				var scaleMatrix:Matrix = new Matrix();
				scaleMatrix.scale(ratio, ratio);
				resultBD.draw(bitmap, scaleMatrix, null, null, null, true);
				return resultBD;
			}
			/* On a pas eu besoin de redimensionner l'image */
			return bitmapData;
		}
		
		/* Fonction pour ajouter le reflet � notre image */
		private function addReflect() : void
		{
			var refletBD:BitmapData = getSwapedImage();
			var alphaBitmap:BitmapData = getAlphaReflect(this.refletSize);
			var sprite:Sprite = new Sprite();
			sprite.graphics.lineStyle(1, 0x000000);
			sprite.graphics.drawRect(0, 0, originalBitmapData.width - 1, originalBitmapData.height - 1);
			bitmapData = new BitmapData(thumbWidth, originalBitmapData.height + this.refletSize, false, this.theme.themeBackgroundColor);
			bitmapData.copyPixels(originalBitmapData, originalBitmapData.rect, new Point(0, 0));
			bitmapData.draw(sprite);
			var reflectPos:Point = new Point(0 , originalBitmapData.height);
			bitmapData.copyPixels(refletBD, refletBD.rect, reflectPos, alphaBitmap, new Point(), true);
			var finalBitmapData:BitmapData = new BitmapData(originalBitmapData.width, thumbHeight + refletSize, true, 0x000000);
			finalBitmapData.copyPixels(bitmapData, bitmapData.rect, new Point(0, thumbHeight - originalBitmapData.height));
			this.reflectedBitmapData = finalBitmapData;
		}
		
		/* On r�cup�re une image invers�e de notre image */
		private function getSwapedImage() : BitmapData
		{
			var bitmap:Bitmap = new Bitmap(originalBitmapData);
			var resultBD:BitmapData = new BitmapData(originalBitmapData.width, originalBitmapData.height);
			var scaleMatrix:Matrix = new Matrix(1, 0, 0, -1 * this.refletRatio, 0, originalBitmapData.height * this.refletRatio );
			resultBD.draw(bitmap, scaleMatrix);
			return resultBD;
		}
		
		/* On r�cup�re notre d�grad� pour la taille du reflet */
		private function getAlphaReflect(size:Number) : BitmapData
		{
			var resultBD:BitmapData = new BitmapData(originalBitmapData.width, size, true, 0);
			var sprite:Sprite = new Sprite();
			var matr:Matrix = new Matrix();
  			matr.createGradientBox(size, size, Math.PI/2, 0, 0);
			sprite.graphics.beginGradientFill(GradientType.LINEAR, [ 0, 0 ], [ 0.6, 0 ], [ 0x00, 0xFF ], matr);
			sprite.graphics.drawRect(0, 0, originalBitmapData.width, size);
			resultBD.draw(sprite);
			return resultBD;					
		}

		/* Cette fonction dessine l'image en fonction de sa position (stepMove) */
		private function drawPicture(forceTransform:Boolean = false) : void
		{
			/* On r�cup�re les dimensions de notre image originale */
			var width:Number = this.reflectedBitmapData.width;
			var height:Number = this.reflectedBitmapData.height;
			
			/* Valeur pour le redimensionnement des images */
			var dimWidth:Number = 0.45;
			var dimHeight:Number = 0.12 * this.originalBitmapData.width / this.thumbWidth;
			
			/* On cherche a d�terminer si l'image sans de sens */
			var changeDirection:Boolean = true;
			if (this.order < this.currentNumber && this.order < this.displayNumber) changeDirection = false;
			if (this.order > this.currentNumber && this.order > this.displayNumber) changeDirection = false;
			
			/* Si l'image sans de direction, on calcul l'interpolation */
			if (changeDirection == true || forceTransform == true) {
				/* On calcul la position de d�part */
				var p1_start : Point = new Point(); /* Point sup�rieur gauche */
				var p2_start : Point = new Point(); /* Point sup�rieur droit */
				var p3_start : Point = new Point(); /* Point inf�rieur droit */
				var p4_start : Point = new Point(); /* Point inf�rieur gauche */
				if (this.order < this.currentNumber) {
					p1_start.x = 0; p1_start.y = height * 0.15;
					p2_start.x = width * dimWidth; p2_start.y = height * (dimHeight + 0.15);
					p3_start.x = width * dimWidth; p3_start.y = height * (0.85 - dimHeight);
					p4_start.x = 0; p4_start.y = height * 0.85;				
				}
				if (this.order > this.currentNumber) {
					p1_start.x = this.thumbWidth - (width * dimWidth); p1_start.y = height * (dimHeight + 0.15);
					p2_start.x = this.thumbWidth; p2_start.y = height * 0.15;
					p3_start.x = this.thumbWidth; p3_start.y = height * 0.85;
					p4_start.x = this.thumbWidth - (width * dimWidth); p4_start.y = height * (0.85 - dimHeight);
				}
				if (this.order == this.currentNumber) {
					p1_start.x = (this.thumbWidth - width) / 2; p1_start.y = 0;
					p2_start.x = (this.thumbWidth - width) / 2 + width; p2_start.y = 0;
					p3_start.x = (this.thumbWidth - width) / 2 + width; p3_start.y = height;
					p4_start.x = (this.thumbWidth - width) / 2; p4_start.y = height;
				}

				/* On calcul la position d'arriv�e */
				var p1_end : Point = new Point(); /* Point sup�rieur gauche */
				var p2_end : Point = new Point(); /* Point sup�rieur droit */
				var p3_end : Point = new Point(); /* Point inf�rieur droit */
				var p4_end : Point = new Point(); /* Point inf�rieur gauche */
				if (this.order < this.displayNumber) {
					p1_end.x = 0; p1_end.y = height * 0.15;
					p2_end.x = width * dimWidth; p2_end.y = height * (dimHeight + 0.15);
					p3_end.x = width * dimWidth; p3_end.y = height * (0.85 - dimHeight);
					p4_end.x = 0; p4_end.y = height * 0.85;				
				}
				if (this.order > this.displayNumber) {
					p1_end.x = this.thumbWidth - (width * dimWidth); p1_end.y = height * (dimHeight + 0.15);
					p2_end.x = this.thumbWidth; p2_end.y = height * 0.15;
					p3_end.x = this.thumbWidth; p3_end.y = height * 0.85;
					p4_end.x = this.thumbWidth - (width * dimWidth); p4_end.y = height * (0.85 - dimHeight);
				}
				if (this.order == this.displayNumber) {
					p1_end.x = (this.thumbWidth - width) / 2; p1_end.y = 0;
					p2_end.x = (this.thumbWidth - width) / 2 + width; p2_end.y = 0;
					p3_end.x = (this.thumbWidth - width) / 2 + width; p3_end.y = height;
					p4_end.x = (this.thumbWidth - width) / 2; p4_end.y = height;
				}
				
				/* On calcul l'interpolation de l'image */
				var p1:Point = Point.interpolate( p1_start, p1_end, (maxStepMove - this.stepMove) / maxStepMove );
				var p2:Point = Point.interpolate( p2_start, p2_end, (maxStepMove - this.stepMove) / maxStepMove );
				var p3:Point = Point.interpolate( p3_start, p3_end, (maxStepMove - this.stepMove) / maxStepMove );
				var p4:Point = Point.interpolate( p4_start, p4_end, (maxStepMove - this.stepMove) / maxStepMove );

				/* On affiche l'image */
				distortImage.setTransform( p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y );
			}
			
			/* On d�place l'image vers la gauche ou la droite */
			/* 1) Position de d�part */
			var offet_x_start:Number = 0;
			if (this.order < this.currentNumber) offet_x_start = (this.order - this.currentNumber - 0.5) * this.theme.themeSpaceBetweenSlide;
			if (this.order > this.currentNumber) offet_x_start = (this.order - this.currentNumber + 0.5) * this.theme.themeSpaceBetweenSlide;

			/* 2) Position d'arriv� */
			var offet_x_end:Number = 0;
			if (this.order < this.displayNumber) offet_x_end = (this.order - this.displayNumber - 0.5) * this.theme.themeSpaceBetweenSlide;
			if (this.order > this.displayNumber) offet_x_end = (this.order - this.displayNumber + 0.5) * this.theme.themeSpaceBetweenSlide;

			/* 3) On calcul la position en mouvement */
			this.x = (this.theme.animWidth - this.theme.themeSlideWidth) / 2 + offet_x_start +
			         (offet_x_end - offet_x_start) * this.stepMove / maxStepMove;
		}

		/* On demande � la classe gallery d'envoyer les ordres de d�placement � tous les slides */
		private function onThumbClick(event:Event) : void
		{
			onMoveSlide.call(null, this.order);
			onUpdateScrollbar.call(null, this.order);
			if (this.JSCallback != "") {
				try
				{
					ExternalInterface.call(this.JSCallback, this.publicID);
				}
				catch(e:Error) {
					trace( e.toString() ) ;
				}
			}
		}
	}
}