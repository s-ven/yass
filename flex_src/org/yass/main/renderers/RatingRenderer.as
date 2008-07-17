package org.yass.main.renderers
{
	import flash.events.MouseEvent;
	
	import mx.controls.Image;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	public class RatingRenderer extends UIComponent implements IListItemRenderer{
		[Embed(source="/assets/rating_0.png")] 
		private static var rating0:Class;
		[Embed(source="/assets/rating_1.png")] 
		private static var rating1:Class;
		[Embed(source="/assets/rating_2.png")] 
		private static var rating2:Class;
		[Embed(source="/assets/rating_3.png")] 
		private static var rating3:Class;
		[Embed(source="/assets/rating_4.png")] 
		private static var rating4:Class;
		[Embed(source="/assets/rating_5.png")] 
		private static var rating5:Class;
		
		private var img:Image = new Image();
		private var _data:Object;
		public function RatingRenderer() {
			super();
			img.addEventListener(MouseEvent.CLICK, click);
			img.setActualSize(69,12);
		}

		public function get data():Object{
			return _data;
		}
		
		public function set data(value:Object):void{
			if(value){
	        	_data = value;	    
				switch(data.rating){
					case 0:
						img.source = rating0;
						break;
					case 1:
						img.source = rating1;
						break;
					case 2:
						img.source = rating2;
						break;
					case 3:
						img.source = rating3;
						break;
					case 4:
						img.source = rating4;
						break;
					case 5:
						img.source = rating5;
						break;
				}
		    	invalidateProperties();
		        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
			}
	    }
	    override protected function createChildren() : void		{
			super.createChildren();
			addChild(img);
		}
		
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			img.move(unscaledWidth/2 -35, unscaledHeight/2-6);
		}
		protected function click(evt:MouseEvent) : void{
			var oldRating:int = data.rating;
			this.data.rating = Math.floor(evt.localX / 14) +1;
			if(oldRating == this.data.rating && oldRating == 1)
				this.data.rating = 0;
			evt.stopPropagation();
		}
	}
}