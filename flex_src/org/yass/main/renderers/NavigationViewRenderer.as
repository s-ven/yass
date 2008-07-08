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
        
        override protected function createChildren():void{
            super.createChildren();
		}
        
        override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void{
            var treeListData:TreeListData=TreeListData(listData);
            super.updateDisplayList(unscaledWidth,unscaledHeight);
			//updateDisplay(treeListData.item)
			var obj:Object = super.data; 

        }
        override public function set data(obj:Object):void   {
        
        	super.data = obj;
        	if(obj){
				var __tree:Tree = Tree(this.parent.parent);
	        	if(obj.@type == "void"){
	               	if(icon)
	                this.enabled=false;
	                this.setStyle("fontWeight","bold");
	               // this.disclosureIcon.x = this.disclosureIcon.x  = 5;;
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
		}
	}
}