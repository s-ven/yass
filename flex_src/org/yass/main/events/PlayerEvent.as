package org.yass.main.events
{
	import flash.events.Event;
	
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.debug.log.Console;

	public class PlayerEvent extends Event
	{
		public static const NEXT:String = "next";
		public static const PREVIOUS:String = "previous";
		public static const TOOGLE:String = "toogle";
		public static const STOPPED:String = "stopped";
		public static const PLAYING:String = "playing";
		public static const LOADED:String = "loaded";

		public function PlayerEvent(type:String):void{
			super(type);
		}
		
	}
}