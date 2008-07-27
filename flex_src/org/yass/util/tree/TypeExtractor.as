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
package org.yass.util.tree
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	
	import org.yass.util.Visitable;
	import org.yass.util.Visitor;

	public class TypeExtractor implements Visitor{
		public var extraction:ArrayCollection = new ArrayCollection();
		public var dict:Dictionary = new Dictionary();
		private var type:String;
		public function TypeExtractor(type:String){
			this.type = type;
		}

		public function visit(visitable:Visitable):void{
			var val:Value = (visitable as Value);
			var ex:Value = dict["id_"+val.id]
			if(val.type == type && ex == null){
				dict["id_"+val.id] = val;
				extraction.addItem(val);
			}
			else if (val.type == type && ex != null){
				if(!(ex is ValueMultiple)){
					extraction.removeItemAt(extraction.getItemIndex(ex));
					ex = new ValueMultiple(ex);
					dict["id_"+val.id] = ex;
					extraction.addItem(ex);
				}
				(ex as ValueMultiple).values.push(val);
			}
			else
				for each(var child:Value in val.childs)
					visit(child);
		}		
	}
}