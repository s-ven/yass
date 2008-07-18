package org.yass.visualization
{
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
		private var _vuTops:Array = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
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
		public function SpectrumAnalyzer(){
			super();
        }
        
        override protected function createChildren():void{
			addChild(_clipMask);
			mask = _clipMask;
		}
		override protected function commitProperties() : void		{
			styleName = "dimmed";
			addEventListener( Event.ENTER_FRAME, onEnterFrame );
		}
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number ) : void{
			_colorTop = getStyle("colorTop");
			_vuCount = getStyle("vuCount");
			_vuLevels = getStyle("vuLevels") * 2;
			_colorMax = getStyle("colorMax");
			_colorActive =  getStyle("colorActive");
			_colorInactive =  getStyle("colorInactive");
			_alphaTop = getStyle("alphaTop");
			_alphaMax = getStyle("alphaMax");
			_alphaActive = getStyle("alphaActive");
			_alphaInactive =  getStyle("alphaInactive");
			}		
		private function onEnterFrame( event : Event ) : void{
			_vuWidth = ((parent.width-4)/2 ) / _vuCount -1 ;
			if (visible){
				try{
					SoundMixer.computeSpectrum(_spectrumArr, false,1);
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
				var vuStep:int = 256/_vuCount;
				for(var i:uint = 0; i < _vuCount *2; i++){
					_spectrumArr.position = i * vuStep; 
					var vuMax:int = _spectrumArr.readFloat()*_vuLevels
					var xVu:int = parent.width / 2 - (i+1) * (_vuWidth+1) - 3;
					if(i>=_vuCount)
						xVu = parent.width/2 +2   + (i-_vuCount) * (_vuWidth +1);
					graphics.lineStyle(1, _colorActive,_alphaActive);
					// draws active vu
					for(var j:int = 0; j<vuMax; j +=2)
						graphics.drawRect(xVu, parent.height - j,_vuWidth,0);
					// draws max vu
					graphics.lineStyle(1, _colorMax,_alphaMax);
					graphics.drawRect(xVu, parent.height - vuMax,_vuWidth,0);		
					// draws inactive vu
					graphics.lineStyle(1, _colorInactive, _alphaInactive);
					for(var j:int = vuMax + 2; j<_vuLevels; j +=2)
						graphics.drawRect(xVu, parent.height - j,_vuWidth,0);
					// draws top vu
					graphics.lineStyle(1,_colorTop,_alphaTop);
					graphics.drawRect(xVu, parent.height - top(i, vuMax),_vuWidth-1,0);	
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
				return _vuTops[i] = compare;
			return _vuTops[i] = --top
		}
	}
}