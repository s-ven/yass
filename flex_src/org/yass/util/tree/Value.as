package org.yass.util.tree
{
	import mx.collections.ArrayCollection;
	
	import org.yass.util.Visitable;
	import org.yass.util.Visitor;
	[Bindable]
	public class Value implements Visitable	{
		public var childs:Array = new Array(); 
		public var id:int;
		public var value:String;
		public var type:String;
		public var parent:Value;
		public function Value(name:String, type:String, id:int):void{
			this.value = name;
			this.type = type;
			this.id = id;
		}
		
		public function addParent(parent:Value):void{
			this.parent = parent;			
		}
		
		public function hasParent(val:Value):Boolean{
			if(parent == val)
				return true;
			if(parent)
				return parent.hasParent(val);
			return false;
		}
			
		public function accept(visitor:Visitor):void{
			visitor.visit(this);
		}
		
		public function addChild(child:Value):void{
			childs.push(child);
			child.addParent(this);
		}
		public function toString():String{
			return value;
		}
		public function getArrayByType(type:String):ArrayCollection{
			var lvlExtr:TypeExtractor=  new TypeExtractor(type)
			this.accept(lvlExtr);
			return lvlExtr.extraction;
		}
		
		public function isChildOf(val:Value):Boolean{
			return val.hasParent(this)
		}
		
	}
}