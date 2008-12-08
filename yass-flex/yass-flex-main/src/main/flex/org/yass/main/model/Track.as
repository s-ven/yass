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
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.util.tree.Value;
	public class Track{
		private var _xml:XML;
		private var _genre:Value;
		private var _artist:Value;
		private var _album:Value;
		private var _lengthText:String;
		private var _bitrateText:String;
		private var _lastModifiedText:String;
		public function Track(obj:XML):void{
			_xml = obj
		}

		public  function get  id():uint{
			return _xml.@id;
		}
		public  function get  trackNr():uint{
			return _xml.@trackNr
		}
		public  function get  title():String{
			return _xml.@title
		}
		public  function get  track():String{
			return _xml.@track;
		}
		public  function get  length():uint{
			return _xml.@length
		}
		public  function get  lastPlayed():Number{
			return _xml.@lastPlayed;
		}
		public function get lastPlayedText():String{
			return _xml.@lastPlayed!=0?Yass.dateFormatter.format(new Date(_xml.@lastPlayed)):"";
		}
		public  function get  playCount():uint{
			return _xml.@playCount;
		}
		public function get playCountText():String{
			return _xml.@playCount == 0?"":_xml.@playCount;
		}
		public  function set  playCount(val:uint):void{
			_xml.@playCount = val;
		}
		public  function set  lastPlayed(val:Number):void{
			_xml.@lastPlayed = val;
		}
		private var _allFields:String;
		public  function get  allFields():String{
			if(_allFields)
				return _allFields;
			return _allFields = (artist.value + " " + album.value +" " + _xml.@title).toLocaleLowerCase()
		}
		public  function get  lowerCaseTitle():String{
			return _xml.@title.toLocaleLowerCase();
		}
		public function get artist():Value{
			if(_artist)
				return _artist
			return _artist = LibraryModel.trackInfos[_xml.@artist+""];
		}
		public function get genre():Value{
			if(_genre)
				return _genre
			return _genre = LibraryModel.trackInfos[_xml.@genre+""];
		}
		public function get album():Value{
			if(_album)
				return _album
			return _album = LibraryModel.trackInfos[_xml.@album+""];
		}
		public function get lengthText():String{
			if(_lengthText)
				return _lengthText;
			return _lengthText = Yass.trackDurationFormatter.format(new Date(_xml.@length))
		}

		public function get isLoaded():Boolean{
			return Yass.player.loadedTrack == this
		}
		public function get isPlaying():Boolean{
			return Yass.player.loadedTrack == this && Yass.player.isPlaying;
		}
		public function set rating(value:uint):void{
			Console.log("model.Track.rating:" + value);
			_xml.@rating = value;
			save();
		}
		public function save():void{
			var service:HTTPService = new HTTPService();
			service.url = "/yass/rest/users/" + Yass.userId + "/libraries/" + Yass.library.id + "/tracks/" + id;
			service.method = "POST";
			service.headers["X-Method-Override"] = "PUT";
			service.headers["X-HTTP-Method-Override"] = "PUT";
			service.send(this);
		}

		public function get rating():uint{
			return _xml.@rating;
		}
		public function get yearText():String{
			return _xml.@year ==0?"":_xml.@year;
		}
		public function get bitrateText():String{
			if(_bitrateText)
				return _bitrateText;
			return _bitrateText = ((_xml.@vbr == "true")?(_xml.@bitrate + " kbps (VBR)"):(_xml.@bitrate + " kbps"));
		}
		public function get bitrate():uint{
			return _xml.@bitrate;
		}
		public function get year():uint{
			return _xml.@year;
		}
		public function get lastModified():Number{
			return _xml.@lastModified;
		}
		public function get lastModifiedText():String{
			if(_lastModifiedText)
				return _lastModifiedText;
			return _lastModifiedText = Yass.dateFormatter.format(new Date(_xml.@lastModified))
		}
		public function toString():String{
			return title;
		}
	}
}