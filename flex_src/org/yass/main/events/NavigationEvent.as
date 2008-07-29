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

	public class NavigationEvent extends Event{
		public static const PLAYLIST_EDITED:String = "playlist_edited";
		public static const PLAYLIST_CLICKED:String = "playlist_clicked";
		public static const NAVIGATION_REFRESHED:String = "navigation_refreshed";
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