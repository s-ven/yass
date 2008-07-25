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
	[Bindable]
	public class Track{
		public var id:int
		public var trackNr:int
		public var title:String
		public var track:String
		public var artist:Value;
		public var genre:Value;
		public var album:Value;
		public var length:int;
		public var lastPlayed:Date;
		public var playCount:int = 0;
		public var allFields:String;
		public var lowerCaseTitle:String;
		private var _rating:int = 0;
		public function Track(obj:XML):void		{
				this.id = obj.@id;
				this.trackNr = obj.@trackNr;
				this.title = obj.@title;
				this.lowerCaseTitle = title.toLocaleLowerCase();
				this.track = obj.@track;
				this.length = obj.@length;
				if(obj.rating)
					this._rating = obj.@rating;
				//this.lastPlayed = obj.lastPlayed;
				this.playCount = obj.@playCount;
				this.artist = LibraryModel.trackInfos[obj.@artist+""];
				this.genre = LibraryModel.trackInfos[obj.@genre+""];
				this.album = LibraryModel.trackInfos[obj.@album+""];
				this.allFields = (artist.value + " " + album.value +" " + title).toLocaleLowerCase();
		}
		
		public function get isLoaded():Boolean{
			return Yass.player.loadedTrack == this 
		}
		public function get isPlaying():Boolean{
			return Yass.player.loadedTrack == this && Yass.player.isPlaying;
		}
		public function set rating(value:int):void{
			Console.log("model.Track.rating:" + value);
			_rating = value;
			save();
		}
		public function save():void{
			var service:HTTPService = new HTTPService();
			service.url = "/yass/track_save.do";
			service.send(this);
		}
		
		public function get rating():int{
			return _rating;
		}
		
		public function toString():String{
			return this.title;
		}
	}
}