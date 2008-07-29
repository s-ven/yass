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