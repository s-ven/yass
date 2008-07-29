package org.yass.util.tree{
	import org.yass.debug.log.Console;

	[Bindable]
	public class ValueMultiple extends Value{
		public var values:Array = new Array();
		public function ValueMultiple(ex:Value){
			super(ex.xml);
			values.push(ex);
		}
		override public function hasParent(parent:Value):Boolean{
			for each(var subbVal:Value in values)
				if (subbVal.hasParent(parent))
					return true;
			return false;
		}
		override public function isChildOf(parent:Value):Boolean{
			for each(var subbVal:Value in values)
				if (subbVal.isChildOf(parent))
					return true;
			return false;
		}
	}
}