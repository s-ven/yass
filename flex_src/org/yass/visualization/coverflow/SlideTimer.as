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
	import flash.utils.Timer;
	import flash.events.*;
	import flash.external.*;
	
	public class SlideTimer
	{
		/* D�claration des variables de la classe */
		private var JSCallback:String;
		private var displayTimer:Timer;
		private var currentSlide:Number = 1;
		private var slideList:Array;
		
		/* D�claration des donn�es publique */
		public var getCurrentSlide:Function;
		
		/* Fonction d'initialisation de la classe */
		function SlideTimer(time:Number, JSCallback:String)
		{
			/* On r�cup�re l'appel javascript */
			this.JSCallback = JSCallback;
			
			/* On initialise le timer */
			this.displayTimer = new Timer(time * 1000, 1);
			
			/* On d�fini la foncion a appeller une fois le temps �coul� */
			this.displayTimer.addEventListener("timer", displayTimerHandler);
		}
		
		/* On r�cup�re la liste des slides */
		public function setSlideList(list:Array):void
		{
			this.slideList = list;
		}

		/* Fonction d'initialisation du timer */
		public function startTimer(slideNumber:Number):void
		{
			if (this.currentSlide != slideNumber) {
				this.currentSlide = slideNumber;
				this.displayTimer.stop();
				this.displayTimer.start();
			}
		}

		/* Informe le timer de l'image qui est en cours d'affichage */
		public function updateNumberSlide(slideNumber:Number):void
		{
			this.displayTimer.stop();
			this.currentSlide = slideNumber;
		}

		/* Il est temps d'afficher les infos sur le timer */
		public function displayTimerHandler(event:TimerEvent):void
		{
			/* On stop le timer */
			this.displayTimer.stop();
			
			/* On donne l'ordre d'affichage */
			if (this.JSCallback != "") {
				var selectSlide:Slide = this.slideList[this.currentSlide - 1];
				try
				{
					ExternalInterface.call(this.JSCallback, selectSlide.getID());
				}
				catch(e:Error) {
					trace( e.toString() ) ;
				}
			}
		}		
	}
}