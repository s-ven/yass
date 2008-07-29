package org.yass.visualization{
	import flash.display.Shape;
	import flash.events.Event;
	import flash.events.MouseEvent;

	import mx.controls.Text;
	import mx.core.UIComponent;

	import org.yass.Yass;
	import org.yass.debug.log.Console;

	[Style(name="fontSize",type="Number",inherit="yes")]
	[Style(name="fontWeight",type="String",inherit="yes",value="bold")]
	[Style(name="fontFamily",type="String",inherit="yes")]
	[Style(name="color",type="Number",format="Color",inherit="yes")]


	/**
	 * This component displays a string of moving text. The text may move left to right, right to left,
	 * bottom to top, or top to bottom.
	 *
	 * When the text reaches the edge in its direction of travel, a second copy of the text begins to
	 * scroll along the same path.
	 *
	 * The String to scroll is set using this component's <code>text</code> property. Constants can
	 * set the <code>direction</code> of the scroll as well as the <code>speed</code>.
	 *
	 * <pre>
	 * <ScrollingText text="Hello World" direction="rightToLeft" speed="5" width="100%" />
	 * </pre>
	 */
	public class ScrollingText extends UIComponent{
		private var cache:Array;
		private var clipMask:Shape;
		public var currentIndex:int = 0;
		private var secondIndex:int = 1;
		public static var instance:ScrollingText;
		/**
		 * Constructor
		 */
		public function ScrollingText(){
			super();
			Console.log("ScrollingText : init");
			instance = this;
			addEventListener(MouseEvent.CLICK, mouseClick);
		}

		/**
		 * Creates the objects that make this component work. Two Labels are placed into a cache
		 * where they are used to scroll the text. As one label disappears the other appears.
		 *
		 * A mask is also created for the component to clip the text flowing out of the component's
		 * boundaries as UIComponent provides no clipping of its own.
		 */
		override protected function createChildren():void{
			super.createChildren();
			cache = [new Text(),new Text()];
			(cache[0] as Text).selectable = false;
			(cache[1] as Text).selectable = false;
			addChild(cache[0]);
			addChild(cache[1]);
			clipMask = new Shape();
			addChild(clipMask);
			mask = clipMask;
		}
		/**
		 * If true, the text is flowing; false if the text is not flowing.
		 */
		public var running:Boolean = false;

		/**
		 * This function begins the text scrolling.
		 */
		public function start() : void{
			var oldRunning:Boolean = running;
			running = true;
			if( !oldRunning )
				addEventListener( Event.ENTER_FRAME, moveText );
		}

		/**
		 * This function stops the text scrolling.
		 */
		public function stop() : void{
			running = false;
		}

		/**
		 * This Flex framework function is called once all of the properties have been set (or if
		 * invalidateProperties has been called). The Labels are given their text values and th
		 * orientation is set, if necessary, to comply with the direction.
		 */
		override protected function commitProperties() : void{
			// calling validateNow will help with determining the actual size of the Labels in
			// the measure method.
			cache[0].validateNow();
			cache[1].validateNow();
			invalidateSize();
			invalidateDisplayList();
				addEventListener( Event.ENTER_FRAME, setText  );
		}
		/**
		 * The job of measure() is to given reasonable values to measuredWidth and measuredHeight. The measure()
		 * framework function is called only if an explicit size cannot be determined.
		 */
 		override protected function measure() : void{
			super.measure();
			measuredWidth = 0;
			measuredHeight= 0;
			// For each Label, set its actual size and then modified the measured width
			// and height.
			for(var i:int=0; i < cache.length; i++){
				var l:Text = cache[i] as Text;
				l.setActualSize( l.getExplicitOrMeasuredWidth(), l.getExplicitOrMeasuredHeight() );
				measuredWidth = Math.max(measuredWidth,l.getExplicitOrMeasuredWidth());
				measuredHeight = Math.max(measuredHeight,l.getExplicitOrMeasuredHeight());
			}
		}
		/**
		 * The updateDisplayList function is called whenever the display list has become invalid. Here,
		 * the initial positions of the labels are determined and the clipping mask is made.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			var label0:Text = cache[currentIndex] as Text;
			var label1:Text = cache[secondIndex] as Text;
			label0.move((unscaledWidth - label0.width)/2,0);
			label1.move((unscaledWidth - label1.width)/2,label0.height);
			pauseScroll = true;
			laststop = new Date().time;
			clipMask.graphics.clear();
			clipMask.graphics.beginFill(0);
			clipMask.graphics.drawRect(0,0,unscaledWidth,unscaledHeight);
			clipMask.graphics.endFill();
		}
		private var timeout:Number = 6000;
		private var pauseScroll:Boolean = false;
		private var laststop:Number = 0;
		/**
		 * This function is the ENTER_FRAME event handler and it moves the text by measuredSpeed amounts.
		 *
		 * The direction determines where the text is positioned and moved. When one label
		 * reaches its destination (eg, RIGHT_TO_LEFT reaching the left edge), the second label begins its
		 * journey. When that label reaches the destination, the first label starts, etc.
		 */
		private function moveText( event:Event ) : void{
			if((cache[currentIndex].y == 0 || cache[secondIndex].y ==0)){
				if(!pauseScroll){
					laststop = new Date().time;
					pauseScroll = true;
				}
				if(!running)
					removeEventListener( Event.ENTER_FRAME, moveText );
			}
			pauseScroll = !(pauseScroll && new Date().time - laststop > timeout);
			if(!pauseScroll){
				cache[currentIndex].y -= 1;
				if( cache[currentIndex].y <= 0 )
					cache[secondIndex].y -= 1;
				if( cache[currentIndex].y+cache[currentIndex].height <= 0 ) {
					cache[currentIndex].y = cache[currentIndex].parent.measuredHeight;
					currentIndex = 1 - currentIndex;
					secondIndex  = 1 - secondIndex;
				}
			}
		}

		private function mouseClick(event:Event):void{
			event.stopPropagation();
			laststop = new Date().time;
			currentIndex = 1 - currentIndex;
			secondIndex  = 1 - secondIndex;
			cache[currentIndex].y = 0;
			cache[secondIndex].y -= cache[currentIndex].parent.measuredHeight;
		}

		private function setText(event:Event):void{
			if(Yass.player.loadedTrack){
				cache[0].text = Yass.player.loadedTrack.artist;
				cache[1].text = Yass.player.loadedTrack.album;
			}
		}
	}
}