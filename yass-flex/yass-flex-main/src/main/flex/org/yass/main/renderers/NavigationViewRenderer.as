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
*/package org.yass.main.renderers{
	import mx.collections.*;
	import mx.controls.Tree;
	import mx.controls.treeClasses.*;

	public class NavigationViewRenderer extends TreeItemRenderer {
		[Embed(source="/assets/small-tree-lib.png")] private var libIcon:Class;
		[Embed(source="/assets/small-tree-spl.png")] private var smartPlIcon:Class;
		[Embed(source="/assets/small-tree-upl.png")] private var userPlIcon:Class;
		[Embed(source="/assets/small-tree-light.png")] private var lightPlIcon:Class;

		public function NavigationViewRenderer(){
			 super();
		}
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			if(data){
				var tree:Tree = Tree(parent.parent);
				if(data.@type == "void"){
					enabled=false;
					setStyle("fontThickness",200);
					label.x = 20;;
				}
				else{
					useHandCursor = true;
					buttonMode = true;
					enabled = true;
					setStyle("color","#000000");
					label.x = 36;
					icon.x = 20
					if(data.@type == "smart")
						tree.setItemIcon(data, smartPlIcon, smartPlIcon);
					else if(data.@type == "library")
						tree.setItemIcon(data, libIcon, libIcon);
					else if(data.@type == "user")
						tree.setItemIcon(data, userPlIcon, userPlIcon);
				}
			}

		}
	}
}