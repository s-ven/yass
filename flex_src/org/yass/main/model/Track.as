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
		private var _playCountText:String;
		public function Track(obj:XML):void		{
				_xml = obj
		}
		
		public  function get  id():int{
			return _xml.@id;
		}
		public  function get  trackNr():int{
			return _xml.@trackNr
		}
		public  function get  title():String{
			return _xml.@title
		}
		public  function get  track():String{
			return _xml.@track;
		}
		public  function get  length():int{
			return _xml.@length
		}
		public  function get  lastPlayed():Date{
			return new Date() // _xml.@lastPlayed
		}
		public  function get  playCount():int{
			return _xml.@playCount;
		}
		public function get playCountText():String{
			if(_playCountText)
				return _playCountText
			return _playCountText = _xml.@playCount == 0?"":_xml.@playCount;
		}
		public  function set  playCount(val:int){
			_xml.@playCount = val;
		}
		public  function set  lastPlayed(val:Date){
			_xml.@lastPlayed = val;
		}
		private var _allFields;
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
		public function get lengthText(){
			if(_lengthText)
				return _lengthText;
			return _lengthText = Yass.dateFormatter.format(new Date(0, 0, 0, 0, 0, 0, _xml.@length))
		}
		
		public function get isLoaded():Boolean{
			return Yass.player.loadedTrack == this 
		}
		public function get isPlaying():Boolean{
			return Yass.player.loadedTrack == this && Yass.player.isPlaying;
		}
		public function set rating(value:int):void{
			Console.log("model.Track.rating:" + value);
			_xml.@rating = value;
			save();
		}
		public function save():void{
			var service:HTTPService = new HTTPService();
			service.url = "/yass/track_save.do";
			service.send(this);
		}
		
		public function get rating():int{
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
		public function get bitrate():int{
			return _xml.@bitrate;
		}
		public function get year():int{
			return _xml.@year;
		}
		public function toString():String{
			return title;
		}
	}
}