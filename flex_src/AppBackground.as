/*
    RepeatedBackground
    
    Repeated Background.....just like that!
    
    Created by Maikel Sibbald
    info@flexcoders.nl
    http://labs.flexcoders.nl
    
    Free to use.... just give me some credit
*/
package{
    import flash.display.Bitmap;
    import flash.display.BitmapData;
    import flash.display.Graphics;
    import flash.display.Loader;
    import flash.events.Event;
    import flash.events.IOErrorEvent;
    import flash.geom.Matrix;
    import flash.net.URLRequest;
    import mx.controls.Image;
    import mx.core.BitmapAsset;
    import mx.graphics.RectangularDropShadow;
    import mx.skins.RectangularBorder;
    public class AppBackground extends RectangularBorder{
        private var tile:BitmapData;
        [Embed(source="/assets/app-bkg.png")]
        public var imgCls:Class;
        public function AppBackground():void{
            var background:BitmapAsset = BitmapAsset(new imgCls());
            tile =  background.bitmapData;
        }
        
        override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
            super.updateDisplayList(unscaledWidth, unscaledHeight);
            
            var transform: Matrix = new Matrix();

            // Finally, copy the resulting bitmap into our own graphic context.
            graphics.clear();
            graphics.beginBitmapFill(tile, transform, true);
            graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
        }
    }  
}