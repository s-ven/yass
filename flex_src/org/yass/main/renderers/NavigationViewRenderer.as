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
			var obj:Object = super.data; 
        	if(obj){
				var __tree:Tree = Tree(this.parent.parent);
	        	if(obj.@type == "void"){
	               	if(icon)
	                this.enabled=false;
	                this.setStyle("fontWeight","bold");
	                this.label.x = this.label.x  = 20;;
	            }
	            else{
	            	this.useHandCursor = true;
	            	this.buttonMode = true;
					this.enabled=true;
	                this.setStyle("color","#000000");
	                this.setStyle("fontWeight","normal");
	                this.label.x = this.label.x  = 35;
					if(obj.@type == "smart")
						__tree.setItemIcon(obj, smartPlIcon, smartPlIcon);
					else if(obj.@type == "library")
						__tree.setItemIcon(obj, libIcon, libIcon);
					else if(obj.@type == "user")
						__tree.setItemIcon(obj, userPlIcon, userPlIcon);
		    	}
	    	}
            super.updateDisplayList(unscaledWidth,unscaledHeight);

        }
	}
}