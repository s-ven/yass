package org.yass.mp3
{
	import flash.display.Shape;
	import flash.events.Event;
	import flash.media.SoundMixer;
	import flash.utils.ByteArray;
	
	import mx.controls.Text;
	import mx.core.UIComponent;

	[Style(name="colorTop",type="Number",format="Color")]
	[Style(name="colorMax",type="Number",format="Color")]
	[Style(name="colorActive",type="Number",format="Color")]
	[Style(name="colorInactive",type="Number",format="Color")]
	[Style(name="alphaTop",type="Number")]
	[Style(name="alphaMax",type="Number")]
	[Style(name="alphaActive",type="Number")]
	[Style(name="alphaInactive",type="Number")]
	
	
	
	public class SpectrumAnalyzer extends UIComponent{

		public function SpectrumAnalyzer(){
			super();
        }
        
        override protected function createChildren():void{
			super.createChildren();	
			addChild(clipMask);
			mask = clipMask;
		}
		override protected function commitProperties() : void		{
			styleName = "dimmed";
			this.addEventListener( Event.ENTER_FRAME, onEnterFrame );

		}
		private var clipMask:Shape = new Shape();
		public var ba:ByteArray = new ByteArray();
		private var vuwidth:int;
			
			
		override public function get height():Number{
				return parent.height;
		}	
			
		override public function get width():Number{
				return parent.width;
		}	
			
			
		public function get vuStep():int{
			return 256/vuCount;
		}
		private function onEnterFrame( event : Event ) : void{
			if ( this.visible){
				event.stopImmediatePropagation();
				vuwidth = ((width-4)/2 ) / vuCount ;
				try{
					SoundMixer.computeSpectrum(ba, false,1);
				}
				catch(e:Error){
					this.visible=false;
					Console.log(e);
					return;
				}
				finally{}
				this.graphics.clear();
				this.graphics.beginFill(0,1)
				for(var i:uint = 0; i < vuCount *2; i++){
					ba.position = i * vuStep; 
					var spectrum:Number =  ba.readFloat()*vuLevels;
					this.drawVuMeter(i, spectrum);
				}
				clipMask.graphics.clear();
				clipMask.graphics.beginFill(0);
				clipMask.graphics.drawRect(0,0,width,height);
				clipMask.graphics.endFill();
			}
		}
		private function drawVuMeter(vuId:int, vuMax:Number):void{
			var vuTop:int = top(vuId, vuMax).value;
			var xVu:int = (width -4) / 2 - (vuId+1) * vuwidth -1;
			if(vuId>=vuCount)
				xVu = width/2 +2   + (vuId-vuCount) * vuwidth;
			this.graphics.lineStyle(1, colorActive,alphaActive);
			// draws active vu
			for(var j:int = 0; j<vuMax; j++)
				this.graphics.drawRect(xVu, unscaledHeight - j*2,vuwidth-1,0);
			// draws max vu
			this.graphics.lineStyle(1, colorMax,alphaMax);
			this.graphics.drawRect(xVu, unscaledHeight - vuMax*2,vuwidth-1,0);		
			// draws inactive vu
			this.graphics.lineStyle(1, colorInactive, alphaInactive);
			for(var j:int = vuMax + 1; j<vuLevels; j++)
				this.graphics.drawRect(xVu, unscaledHeight - j*2,vuwidth-1,0);
			// draws top vu
			this.graphics.lineStyle(1,colorTop,alphaTop);
			this.graphics.drawRect(xVu, unscaledHeight - vuTop*2,vuwidth-1,0);		
		} 

		public function get colorTop():Number{
			return getStyle("colorTop");
		}	
		public function get vuCount():Number{
			return getStyle("vuCount");
		}	
		public function get vuLevels():Number{
			return getStyle("vuLevels");
		}	
		public function get colorMax():Number{
			return getStyle("colorMax");
		}	
		public function get colorActive():Number{
			return getStyle("colorActive");
		}	
		public function get colorInactive():Number{
			return getStyle("colorInactive");
		}	
		public function get alphaTop():Number{
			return getStyle("alphaTop");
		}	
		public function get alphaMax():Number{
			return getStyle("alphaMax");
		}	
		public function get alphaActive():Number{
			return getStyle("alphaActive");
		}	
		public function get alphaInactive():Number{
			return getStyle("alphaInactive");
		}	
		
		private var tops:Array = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
		null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
		private function top(i:int, compare:Number):VuTop{
			var top:VuTop = tops[i] as VuTop;
			if(!top || top.value < compare){
				top = new VuTop();
				top.value = compare;
			tops[i] = top;
			}				
			else
				top.value = Math.round(top.value) - 1;
			return top;
		}
	}
}