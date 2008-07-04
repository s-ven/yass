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
package org.yass.main.events{
	import flash.events.Event;
	
	import org.yass.debug.log.Console;
	import org.yass.main.model.interfaces.IPlayListModel;

	public class PlayListEvent extends Event	{
		public static const REFRESH_PANE:String = "refresh_pane";
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