package org.yass.main.model
{
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.util.tree.Value;
	[Bindable]
	public class Track{
		public var id:String
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
				this.artist = LibraryModel.dict["artist_" + obj.@artist];
				this.genre = LibraryModel.dict["genre_" + obj.@genre];
				this.album = LibraryModel.dict["album_" + obj.@album];
				this.allFields = (artist.value + " " + album.value +" " + title).toLocaleLowerCase();
		}
		
		public function get isLoaded():Boolean{
			return Yass.player.loadedTrack == this
		}
		public function get isPlaying():Boolean{
			return Yass.player.loadedTrack == this && Yass.player.isPlaying;
		}
		public function set rating(value:Number):void{
			Console.log("model.Track.rating:" + value);
			_rating = value;
			save();
		}
		public function save():void{
			var service:HTTPService = new HTTPService();
			service.url = "/yass/track_save.do";
			service.send(this);
		}
		
		public function get rating():Number{
			return _rating;
		}
		
		
	}
}