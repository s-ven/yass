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
package org.yass.main.model{
	import mx.events.PropertyChangeEvent;
	import mx.rpc.http.HTTPService;

	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.util.tree.Value;
	public class Settings {
		public var shuffle:Boolean = false;
		public var loop:Boolean = false;
		public var showRemaining:Boolean = true;
		public var displayMode:int=0;
		public var loadedTrack:Track;
		public var artistSelected: Array = new Array();
		public var genreSelected: Array = new Array();
		public var albumSelected: Array = new Array();
		public var stopFadeout:int = 1000;
		public var skipFadeout:int = 3000;
		public var nextFadeout:int = 6000;
		public var loadedTrackId:int = 0;
		public var trackInfoIds:Array;
		private var _init:Boolean=false;
		public function Settings(obj:Object):void{
			Yass.settings = this;
			var xml:XML = new XML(obj);
			if(xml.attributes().length() >0){
				shuffle = (xml.@shuffle =="true") ;
				loop = (xml.@loop =="true");
				showRemaining = (xml.@showRemaining =="true");
				displayMode = xml.@displayMode;
				stopFadeout = xml.@stopFadeout;
				skipFadeout = xml.@skipFadeout;
				nextFadeout = xml.@nextFadeout;
				Console.time("model.settings.init : context init")
				for(var nodeName:Object in xml.trackInfoIds.trackInfo){
					var trackInfo:Value = LibraryModel.trackInfos[xml.trackInfoIds.trackInfo[nodeName].@id+""];
					if(trackInfo)
						this[trackInfo.type+"Selected"].push(trackInfo);
				}
				if(artistSelected.length > 0 || genreSelected.length > 0 || albumSelected.length >0)
					Yass.library.filteredText = "";
				Console.timeEnd("model.settings.init : context init")
				Console.time("model.settings.init : load track")
				if(xml.@loadedTrackId != 0)
					Yass.player.loadedTrack = Yass.library.getTrack(xml.@loadedTrackId)
				Console.timeEnd("model.settings.init : load track")
			}
			_init = true;
		}

		public function save(evt:PropertyChangeEvent):void{
			if(_init && evt.property != "artistSelected" && evt.property != "genreSelected"){
				Console.log("model.Settings.save");
				var svc:HTTPService = new HTTPService();
				loadedTrackId = loadedTrack?loadedTrack.id:0;
				trackInfoIds = new Array();
				artistSelected.forEach(function(val:Value, index:int, arr:Array):void{trackInfoIds.push(val.id)});
				genreSelected.forEach(function(val:Value, index:int, arr:Array):void{trackInfoIds.push(val.id)});
				albumSelected.forEach(function(val:Value, index:int, arr:Array):void{trackInfoIds.push(val.id)});
				svc.url = "/yass/settings_save.do"
				svc.method = "POST";
				svc.send(this);
			}
		}
	}
}