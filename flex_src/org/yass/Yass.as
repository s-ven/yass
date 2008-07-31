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
package org.yass{
	import flash.events.Event;

	import mx.events.PropertyChangeEvent;
	import mx.formatters.DateFormatter;
	import mx.utils.ObjectProxy;

	import org.yass.debug.log.Console;
	import org.yass.main.model.LibraryModel;
	import org.yass.main.model.PlayerModel;
	import org.yass.main.model.Settings;
	import org.yass.visualization.Display;
	public class Yass{
		public static var library:LibraryModel;
		public static var player:PlayerModel = new PlayerModel();

		private static var _trackDurationFormatter:DateFormatter;
		private static var _dateFormatter:DateFormatter;
		private static var _settings:ObjectProxy;

		public static function set settings(settings:Object):void{
			_settings = new ObjectProxy(settings);
			_settings.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, settings.save);
		}

		public static function get settings():Object{
			return _settings;
		}


		public static function get display():Display{
			return Display.instance;
		}

		public static function get trackDurationFormatter():DateFormatter{
			if(!_trackDurationFormatter){
				_trackDurationFormatter = new DateFormatter();
				_trackDurationFormatter.formatString = "N:SS";
			}
			return _trackDurationFormatter;
		}
		public static function get dateFormatter():DateFormatter{
			if(!_dateFormatter){
				_dateFormatter = new DateFormatter();
				_dateFormatter.formatString = "MM/DD/YYYY LL:NN A";
			}
			return _dateFormatter;
		}
	}
} 