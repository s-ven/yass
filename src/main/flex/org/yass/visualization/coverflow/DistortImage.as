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
package org.yass.visualization.coverflow {
	import flash.geom.Matrix;
	import flash.display.BitmapData;
	import flash.display.*;

	public class DistortImage
	{
		// -- texture to distord
		public var texture:BitmapData;
		/* 
		* Constructor
		* @param mc MovieClip : the movieClip containing the distorded picture
		* @param symbolId String : th link name of the picture in the library
		* @param vseg Number : the vertical precision
		* @param hseg Number : the horizontal precision
		* @throws: An error in the second constructor parameter isn't a BitmapData or a MovieClip
		*/
		public function DistortImage( mc: Sprite, ptexture: *, vseg: Number, hseg: Number )
		{
			_mc = mc;
			if( ptexture is BitmapData )
			{
				texture = ptexture;
			}
			else if( ptexture is MovieClip )
			{
				texture = new BitmapData( ptexture._width, ptexture._height );
				texture.draw( ptexture );
			}
			else
			{
				throw new Error('Second argument in DistortImage class must be a BitmapData object or a Movieclip');
			}
			_vseg = vseg || 0;
			_hseg = hseg || 0;
			// --
			_w = texture.width ;
			_h = texture.height;
			// --
			_aMcs 	= new Array();
			_p 		= new Array();
			_tri 	= new Array();
			// --
			__init();
		}

		/** 
		* setTransform
		*
		* @param x0 Number the horizontal coordinate of the first point
		* @param y0 Number the vertical coordinate of the first point	
		* @param x1 Number the horizontal coordinate of the second point
		* @param y1 Number the vertical coordinate of the second point	
		* @param x2 Number the horizontal coordinate of the third point
		* @param y2 Number the vertical coordinate of the third point	
		* @param x3 Number the horizontal coordinate of the fourth point
		* @param y3 Number the vertical coordinate of the fourth point	 
		*
		* @description : Distord the bitmap to ajust it to those points.
		*/
		public function setTransform( x0:Number , y0:Number , x1:Number , y1:Number , x2:Number , y2:Number , x3:Number , y3:Number ): void
		{
			var w:Number = _w;
			var h:Number = _h;
			var dx30:Number = x3 - x0;
			var dy30:Number = y3 - y0;
			var dx21:Number = x2 - x1;
			var dy21:Number = y2 - y1;
			var l:Number = _p.length;
			while( --l > -1 )
			{
				var point:Object = _p[ l ];
				var gx:Number = ( point.x - _xMin ) / w;
				var gy:Number = ( point.y - _yMin ) / h;
				var bx:Number = x0 + gy * ( dx30 );
				var by:Number = y0 + gy * ( dy30 );

				point.sx = bx + gx * ( ( x1 + gy * ( dx21 ) ) - bx );
				point.sy = by + gx * ( ( y1 + gy * ( dy21 ) ) - by );
			}
			__render();
		}
		
		
		
		/////////////////////////
		///  PRIVATE METHODS  ///
		/////////////////////////
		
		private function __init(): void
		{
			_p = new Array();
			_tri = new Array();
			var ix:Number, iy:Number;
			var w2: Number = _w / 2;
			var h2: Number = _h / 2;
			_xMin = _yMin = 0;
			_xMax = _w; _yMax = _h;
			_hsLen = _w / ( _hseg + 1 );
			_vsLen = _h / ( _vseg + 1 );
			var x:Number, y:Number;
			var p0:Object, p1:Object, p2:Object;
			
			// -- we create the points
			for ( ix = 0 ; ix < _hseg + 2 ; ix++ )
			{
				for ( iy = 0 ; iy < _vseg + 2 ; iy++ )
				{
					x = ix * _hsLen;
					y = iy * _vsLen;
					_p.push( { x: x, y: y, sx: x, sy: y } );
				}
			}
			// -- we create the triangles
			for ( ix = 0 ; ix < _vseg + 1 ; ix++ )
			{
				for ( iy = 0 ; iy < _hseg + 1 ; iy++ )
				{
					p0 = _p[ iy + ix * ( _hseg + 2 ) ];
					p1 = _p[ iy + ix * ( _hseg + 2 ) + 1 ];
					p2 = _p[ iy + ( ix + 1 ) * ( _hseg + 2 ) ];
					__addTriangle( p0, p1, p2 );
					// --
					p0 = _p[ iy + ( ix + 1 ) * ( _vseg + 2 ) + 1 ];
					p1 = _p[ iy + ( ix + 1 ) * ( _vseg + 2 ) ];
					p2 = _p[ iy + ix * ( _vseg + 2 ) + 1 ];
					__addTriangle( p0, p1, p2 );
				}
			}
			__render();
		}
		
		private function __addTriangle( p0:Object, p1:Object, p2:Object ):void
		{
			var u0:Number, v0:Number, u1:Number, v1:Number, u2:Number, v2:Number;
			var tMat:Matrix = new Matrix();
			// --		
			u0 = p0.x; v0 = p0.y;
			u1 = p1.x; v1 = p1.y;
			u2 = p2.x; v2 = p2.y;
			tMat.tx = -v0*(_w / (v1 - v0));
			tMat.ty = -u0*(_h / (u2 - u0));
			tMat.a = tMat.d = 0;
			tMat.b = _h / (u2 - u0);
			tMat.c = _w / (v1 - v0);
			// --		
			_tri.push( [p0, p1, p2, tMat] );	
		}

		private function __render(): void
		{
			var vertices: Array;
			var p0:Object, p1:Object, p2:Object;
			var x0:Number, y0:Number;
			var ih:Number = 1/_h, iw:Number = 1/_w;
			var c:Sprite = _mc; c.graphics.clear();
			var a:Array;
			var sM:Matrix = new Matrix();
			var tM:Matrix = new Matrix();
			//--
			var l:Number = _tri.length;
			while( --l > -1 )
			{
				a 	= _tri[ l ];
				p0 	= a[0];
				p1 	= a[1];
				p2 	= a[2];
				tM = a[3];
				// --
				sM.a = ( p1.sx - ( x0 = p0.sx ) ) * iw;
				sM.b = ( p1.sy - ( y0 = p0.sy ) ) * iw;
				sM.c = ( p2.sx - x0 ) * ih;
				sM.d = ( p2.sy - y0 ) * ih;
				sM.tx = x0;
				sM.ty = y0;
				// --
				sM = __concat( sM, tM );
				c.graphics.beginBitmapFill( texture, sM, false, true );
				c.graphics.moveTo( x0, y0 );
				c.graphics.lineTo( p1.sx, p1.sy );
				c.graphics.lineTo( p2.sx, p2.sy );
				c.graphics.endFill();
			}
		}

		private function __concat( m1:Matrix , m2:Matrix ):Matrix 
		{	//Relies on the original triangles being right angled with p0 being the right angle. 
			//Therefore a = d = zero (before and after invert)
			var mat:Matrix = new Matrix();
			mat.a  = m1.c * m2.b;
			mat.b  = m1.d * m2.b;
			mat.c  = m1.a * m2.c;
			mat.d  = m1.b * m2.c;
			mat.tx = m1.a * m2.tx + m1.c * m2.ty + m1.tx;
			mat.ty = m1.b * m2.tx + m1.d * m2.ty + m1.ty;	
			return mat;
		}
		
		/////////////////////////
		/// PRIVATE PROPERTIES //
		/////////////////////////
		private var _mc:Sprite;
		private var _w:Number;
		private var _h:Number;
		private var _xMin:Number, _xMax:Number, _yMin:Number, _yMax:Number;
		// -- picture segmentation properties
		private var _hseg:Number;
		private var _vseg:Number;
		private var _hsLen:Number;
		private var _vsLen:Number;
		// -- arrays of differents datas types
		private var _p:Array;
		private var _tri:Array;
		private var _aMcs:Array;
		
	}
}