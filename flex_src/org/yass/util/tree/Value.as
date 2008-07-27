package org.yass.util.tree
{
	import mx.collections.ArrayCollection;
	
	import org.yass.util.Visitable;
	import org.yass.util.Visitor;
	public class Value implements Visitable	{
		public var childs:Array = new Array(); 
		public var parent:Value;
		public var xml:XML;
		private var _lowerCaseValue:String;
		public function Value(node:XML):void{
			xml=node;
		}
		
		public function get lowerCaseValue():String{
			if(_lowerCaseValue)
				return _lowerCaseValue;
			return _lowerCaseValue= xml.@value.toLowerCase();
		}
		public function get type():String{
			return xml.@type;
		}
		public function get value():String{
			return xml.@value;
		}
		
		public function get id():int{
			return xml.@id;
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
			accept(lvlExtr);
			return lvlExtr.extraction;
		}
		
		public function isChildOf(val:Value):Boolean{
			return hasParent(val)
		}
		
	}
}