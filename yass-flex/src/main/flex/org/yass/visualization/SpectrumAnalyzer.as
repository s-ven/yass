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
package org.yass.visualization{
	import flash.display.Shape;
	import flash.events.Event;
	import flash.media.SoundMixer;
	import flash.utils.ByteArray;

	import mx.core.UIComponent;

	import org.yass.debug.log.Console;

	[Style(name="colorTop",type="Number",format="Color")]
	[Style(name="colorMax",type="Number",format="Color")]
	[Style(name="colorActive",type="Number",format="Color")]
	[Style(name="colorInactive",type="Number",format="Color")]
	[Style(name="alphaTop",type="Number")]
	[Style(name="alphaMax",type="Number")]
	[Style(name="alphaActive",type="Number")]
	[Style(name="alphaInactive",type="Number")]

	public class SpectrumAnalyzer extends UIComponent{

		private var _clipMask:Shape = new Shape();
		private var _spectrumArr:ByteArray = new ByteArray();
		private var _vuWidth:int;
		private var _miWidth:int;
		private var _vuTops:Array = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
		private var _vuTopsTimes:Array = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
		private var _vuCount:int;
		private var _vuLevels:int;
		private var _colorTop:Number;
		private var _colorMax:Number;
		private var _colorActive:Number
		private var _colorInactive:Number
		private var _alphaTop:Number = 1;
		private var _alphaMax:Number = 1;
		private var _alphaInactive:Number = 0.5
		private var _alphaActive:Number = 1;
		private var _vuStep:int;
		private var _bottom:int;
		public var active:Boolean =true;
		public function SpectrumAnalyzer(){
			super();
			Console.log("vizu.SpectrumAnalyzer :: Init");
		}

		override protected function createChildren():void{
			addChild(_clipMask);
			mask = _clipMask;
			styleName = "dimmed";
			addEventListener( Event.ENTER_FRAME, onEnterFrame );
		}
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number ) : void{
			Console.log("vizu.SpectrumAnalyzer.updateDisplayList styleName:"+styleName+", visible:"+visible);
			_colorTop = getStyle("colorTop");
			_vuCount = getStyle("vuCount");
			_vuLevels = getStyle("vuLevels");
			_colorMax = getStyle("colorMax");
			_colorActive =  getStyle("colorActive");
			_colorInactive =  getStyle("colorInactive");
			_alphaTop = getStyle("alphaTop");
			_alphaMax = getStyle("alphaMax");
			_alphaActive = getStyle("alphaActive");
			_alphaInactive =  getStyle("alphaInactive");
			_vuStep = 256/_vuCount;
			_colorArrays = buildStyleArray(_colorTop, _colorMax, _colorActive, _colorInactive);
			_alphaArrays = buildStyleArray(_alphaTop, _alphaMax, _alphaActive, _alphaInactive);
			_bottom = parent.height / 2 + _vuLevels;
		}
		private function buildStyleArray(__top:Number, __max:Number, __active:Number, __inactive:Number):Array{
			var arr:Array = new Array(_vuLevels);
			for(var localTop:int=_vuLevels; localTop >=0 ; --localTop){
				arr[localTop] = new Array(localTop);
				for(var localMx:int=localTop; localMx >=0; localMx--){
					arr[localTop][localMx] = new Array(_vuLevels);
					for(var k:int=0; k< _vuLevels; k++)
						if(k == localTop)
							arr[localTop][localMx][k] = __top;
						else if(k == localMx)
							arr[localTop][localMx][k] = __max;
						else if(k > localMx)
							arr[localTop][localMx][k] = __inactive;
						else if(k < localTop || k < localMx)
							arr[localTop][localMx][k] = __active;
					arr[localTop][localMx] = arr[localTop][localMx]
				}
			}
			return arr;
		}

		private var _colorArrays:Array;
		private var _alphaArrays:Array
		private var _current : Number = 0;

		private function onEnterFrame( event : Event ) : void{
			if (visible){
				_miWidth = parent.width / 2
				_vuWidth = (_miWidth -10) / _vuCount -1 ;
				try{
					SoundMixer.computeSpectrum(_spectrumArr, true);
				}
				catch(e:Error){
					visible=false;
					Console.log(e);
					Console.log("SpectrumAnalyzer : ERROR");
					return;
				}
				finally{}
				graphics.clear();
				graphics.beginFill(0,1)
				for(var i:uint = 0; i < _vuCount * 2; i++){
					_current = 0;
					for (j = 0; j < _vuStep ; j++)
						_current = Math.max(_current, _spectrumArr.readFloat());
					var vuMax:int = Math.min( _current, 1 )* (_vuLevels -1)
					var localTop:int = top(i, vuMax)
					var xVu:int = _miWidth - (i+1) * (_vuWidth+1) - 3;
					if(i>=_vuCount)
						xVu = _miWidth + (i-_vuCount) * (_vuWidth+1) + 2;
					for(var j:int = 0; j<_vuLevels;j++){
						graphics.lineStyle(1, _colorArrays[localTop][vuMax][j],_alphaArrays[localTop][vuMax][j]);
						graphics.drawRect(xVu, _bottom - j * 2,_vuWidth,0);
 					}
				}
				_clipMask.graphics.clear();
				_clipMask.graphics.beginFill(0);
				_clipMask.graphics.drawRect(0,0,parent.width,parent.height);
				_clipMask.graphics.endFill();
			}
		}
		private function top(i:int, compare:Number):int{
			var top:int = _vuTops[i];
			if(!top || top < compare)
				_vuTops[i] = compare;
			return _vuTops[i]--;
		}
	}
}