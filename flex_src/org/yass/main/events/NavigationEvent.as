package org.yass.main.events
{
	import flash.events.Event;
	
	import org.yass.debug.log.Console;

	public class NavigationEvent extends Event
	{
		public static const PLAYLIST_EDITED:String = "playlist_edited";
		public static const PLAYLIST_CLICKED:String = "playlist_clicked";
		public var id:String;
		public var name:String;
		public var playListType:String;

		
		public function NavigationEvent(_type:String, id:String, name:String=null, playListType:String=null){
			super(_type);
			Console.log("event.Navigation.type=" + type);
			this.id = id;
			this.name = name;
			this.playListType = playListType;
		}
		
	}
}