package org.yass.main.events
{
	import flash.events.Event;
	
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.debug.log.Console;

	public class PlayListEvent extends Event
	{
		public static const PLAYLIST_SAVED:String = "playlist_saved";
		public static const PLAYLIST_LOADED:String = "playlist_loaded";
		public static const TRACK_PLAY:String = "track_play";
		
		public var result:Object;
		public var playlist:IPlayListModel;
		public var playListType:String;
		public function PlayListEvent(type:String, _result:Object=null, _loader:IPlayListModel=null, _playListType:String=null):void{
			super(type);
			Console.log("event.PlayList.type=" + type);
			result = _result;
			playlist = _loader;
			playListType =_playListType;
		}
		
	}
}