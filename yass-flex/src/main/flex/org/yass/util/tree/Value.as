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
package org.yass.util.tree{
	import mx.collections.ArrayCollection;
	
	import org.yass.util.Visitable;
	import org.yass.util.Visitor;
	public class Value implements Visitable{
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
			return (xml.name() as QName).localName;
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