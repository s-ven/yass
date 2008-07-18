package org.yass.main.renderers
{
	import mx.collections.*;
	import mx.controls.Tree;
	import mx.controls.treeClasses.*;  
	  
	public class NavigationViewRenderer extends TreeItemRenderer {
		[Embed(source="assets/small-tree-lib.png")] private var libIcon:Class;  
		[Embed(source="assets/small-tree-spl.png")] private var smartPlIcon:Class;  
		[Embed(source="assets/small-tree-upl.png")] private var userPlIcon:Class;  
		[Embed(source="assets/small-tree-light.png")] private var lightPlIcon:Class;  

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