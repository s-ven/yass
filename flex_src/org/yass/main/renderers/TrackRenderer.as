package org.yass.main.renderers
{
	import org.yass.debug.log.Console;
	
	public class TrackRenderer extends LabelDataGridRenderer
	{
		public function TrackRenderer()
		{
			super();
		}
		override protected function updateDisplayList( unscaledWidth:Number, unscaledHeight:Number ) : void{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(data.isLoaded)
				setStyle("fontThickness", 200);
			else
				setStyle("fontThickness", 0);
		}
		
	}
}