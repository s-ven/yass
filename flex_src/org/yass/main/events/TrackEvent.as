package org.yass.main.events
{
	import flash.events.Event;
	
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.debug.log.Console;

	public class TrackEvent extends Event
	{
		public static const TRACK_PLAY:String = "track_play";
		public static const TRACK_SELECTED:String = "track_selected";
		public static const TRACK_CLICK:String = "track_click";
		public var trackIndex:Number;
		public var loader:IPlayListModel;
		public function TrackEvent(type:String, _trackIndex:Number, _loader:IPlayListModel):void{
			super(type);
			Console.log("event.Track.type=" + type);
			this.trackIndex = _trackIndex;
			this.loader = _loader;
		}
		
	}
}