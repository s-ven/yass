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
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.formatters.DateFormatter;
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.events.PlayerEvent;
	import org.yass.main.events.TrackEvent;
	import org.yass.main.model.interfaces.IPlayListModel;
	
	public class PlayListModel extends ArrayCollection implements IPlayListModel{
        public var shuffledTracks:ArrayCollection= new ArrayCollection();
        public var shuffledListPosition:int; 
		public var playListId:String;
		
		private var _trackIndex:Number = -1;
   		private var _datas:ArrayCollection;
		private var _sortA:Sort = new Sort();
		private var _sortByTrackNr:SortField = new SortField("trackNr", true, false, true);
		private var _sortByArtist:SortField = new SortField("artist", true);
		private var _sortByAlbum:SortField = new SortField("album", true);
		private var _sortByTitle:SortField = new SortField("title", true);
		private var _sortByGenre:SortField = new SortField("genre", true);
		private var _sortByLength:SortField = new SortField("length", true, false, true);
		private var _sortByRating:SortField = new SortField("rating", true, false, true);
		private var _sortByPlayCount:SortField = new SortField("playCount", true, false, true);
		private var _oldColumn:String;
		private var _selectedTrack:Track;
			
		public function PlayListModel(){			
        	Console.log("model.PlayListModel :: Init");
		}
		
		public function removeEventListeners():void{
			removeEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange);
			Yass.player.removeEventListener(PlayerEvent.TRACK_LOADED, onPlayerEvent);
			Yass.player.removeEventListener(PlayerEvent.PLAYING, onPlayerEvent);
			Yass.player.removeEventListener(PlayerEvent.STOPPED, onPlayerEvent);
		}
		public function setEventListeners():void{
			addEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionChange);
			Yass.player.addEventListener(PlayerEvent.TRACK_LOADED, onPlayerEvent);
			Yass.player.addEventListener(PlayerEvent.PLAYING, onPlayerEvent);
			Yass.player.addEventListener(PlayerEvent.STOPPED, onPlayerEvent);
		}
		
    	public function set trackIndex(value:Number):void{
    		_trackIndex = value;
    	}
		
        public function get selectedTrack():Track{
        	if(trackIndex !=-1 && trackIndex < length){
        		_selectedTrack = getItemAt(trackIndex) as Track
	        	return _selectedTrack;
	        }
        	return null;
        }
        
 		public function get trackIndex():Number{
 			return _trackIndex;
 		}
        private function getPreviousShuffledTrack():Number{
        	if(shuffledTracks.length > 1 && shuffledListPosition > 1)
        		return shuffledTracks.getItemAt((shuffledListPosition -= 1) -1) as Number;
        	return trackIndex;
    	}     
        private function getNextShuffledTrack():Number{
        	if(!(shuffledTracks.length > 1 && shuffledListPosition < shuffledTracks.length))
        		shuffledTracks.addItem(Math.ceil( 
        		( 1 - Math.random()) * length) - 1);
        	shuffledListPosition += 1;
        	return shuffledTracks.getItemAt(shuffledListPosition-1) as Number;
    	}
        public function getNextTrack():Track{
        	Console.log("model.PlayList.getNextTrack");
            if(Yass.settings.shuffle)
	           	trackIndex = getNextShuffledTrack();
	        else{
				if(trackIndex < length - 1)
					trackIndex += 1;
				else if(Yass.settings.loop)
					trackIndex = 0;
				else
					return null;
	        }
			return selectedTrack;
        }
        
        public function getPreviousTrack():Track{
        	Console.log("model.PlayList.getPreviousTrack");
        	if(Yass.settings.shuffle)
        		trackIndex = getPreviousShuffledTrack();
        	else
        	   	if(trackIndex > 0)
               		trackIndex -= 1;
            	else if(Yass.settings.loop)
                	trackIndex = length -1;
				else
					return null;
			return selectedTrack;
		}        
  		/**
  		 *  This will play the request track,
  		 */
  		public function playTrack(_trackIndex:Number):void{ 
			this.trackIndex = _trackIndex;
  			Console.group("group.PlayList.playTrack trackIndex:"  +trackIndex+ ", playListId:"+ playListId);
			var wasPlaying:Boolean = Yass.player.isPlaying;
			// loads the player with the current PlayList and track
				loadToPlayer()
			Yass.player.playTrack(selectedTrack)
			// If the Player is shuffling, will add the selected track to the random list 
 	       	if(Yass.settings.shuffle){
	       		while(shuffledTracks.length > shuffledListPosition)
	       			shuffledTracks.removeItemAt(shuffledListPosition);
	       		shuffledListPosition +=1;
	      		shuffledTracks.addItem(trackIndex);
			} 
			Console.groupEnd();
		}	  		
  		public function selectTrack(_trackIndex:Number):void{
  			Console.group("model.PlayList.selectTrack trackIndex:"  +_trackIndex+ ", playListId:"+ playListId);
  			this.trackIndex = _trackIndex;
  			// If a track is selected and the current player is not playing, wll cause the display to be updated
  			if(!Yass.player.loadedPlayList)
  				loadToPlayer()
			if(Yass.player.loadedPlayList.playListId == playListId && !Yass.player.isPlaying && !Yass.player.isPaused)
				Yass.player.loadedTrack = selectedTrack;
			Console.groupEnd();
		}
		/**
		 * Adds a listener to the player and set this to be the loader
		 */
		private function loadToPlayer():void{
			Console.log("model.PlayList.loadToPlayer");
  			Yass.player.loadedPlayList = this;
		} 
		/**
		 * Will dispatch a TrackEvent in order to refresh the view (Plate, PlayList) 
		 */
		protected function onPlayerEvent(evt:PlayerEvent):void{
			if(Yass.player.loadedPlayList == this)
				this.dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, trackIndex, this));
			else if (this.getItemIndex(Yass.player.loadedTrack) != -1)
				this.dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, this.getItemIndex(Yass.player.loadedTrack), this));
			
		}
		public function onCollectionChange(evt:CollectionEvent):void{
			if(evt.kind == CollectionEventKind.REFRESH){
				Console.group("model.PlayList.onCollectionChange");
				this.trackIndex = getItemIndex(_selectedTrack);
				if(trackIndex != -1)
					this.dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, trackIndex, this, true));
				else if (this.getItemIndex(Yass.player.loadedTrack) != -1)
					this.dispatchEvent(new TrackEvent(TrackEvent.TRACK_SELECTED, this.getItemIndex(Yass.player.loadedTrack), this));
				Console.groupEnd();
			}
		}
 		/**
 		 * Called when a sort has occured, 
 		 * The sort logic :
 		 *  - sorted by artist, group the results by albums, then track number
 		 *  - sorted by albums, group the results by artists then track number
 		 *  - sorted by track number, group the results by artists then album
 		 *  TODO :: Move this to the Model
 		 */
		public function sortColumn(columnName:String):void{
			Console.group("model.PlayList.sortColumn name:"+columnName);
			if (columnName=="trackNr") {
			    if(_oldColumn == "trackNr")
			    	_sortByTrackNr.reverse();
				_sortA.fields=[_sortByTrackNr, _sortByArtist, _sortByAlbum, ];
			} else if (columnName=="album") {
			    if(_oldColumn == "album")
			    	_sortByAlbum.reverse();
				_sortA.fields=[_sortByAlbum, _sortByArtist, _sortByTrackNr];
			} else if (columnName=="artist") {
			    if(_oldColumn == "artist")
			    	_sortByArtist.reverse();
				_sortA.fields=[_sortByArtist, _sortByAlbum, _sortByTrackNr];
			} else if (columnName=="genre") {
			    if(_oldColumn == "genre")
			    	_sortByGenre.reverse();
				_sortA.fields=[_sortByGenre,_sortByArtist, _sortByAlbum, _sortByTrackNr];
			} else if (columnName=="title") {
			    if(_oldColumn == "title")
			    	_sortByTitle.reverse();
			   _sortA.fields=[_sortByTitle, _sortByArtist, _sortByAlbum];
			}else if (columnName=="lengthText") {
			    if(_oldColumn == "lengthText")
			    	_sortByLength.reverse();
			   _sortA.fields=[_sortByLength, _sortByTitle, _sortByArtist, _sortByAlbum];
			} else if (columnName=="rating") {
			    if(_oldColumn == "rating")
			    	_sortByRating.reverse(); 
			   _sortA.fields=[_sortByRating];
			} else if (columnName=="playCount") {
			    if(_oldColumn == "playCount")
			    	_sortByPlayCount.reverse();
			   _sortA.fields=[_sortByPlayCount];
			} else if (columnName=="genre") {
			    if(_oldColumn == "genre")
			    	_sortByGenre.reverse();
			   _sortA.fields=[_sortByGenre];
			} 
			_oldColumn = columnName;
			sort = _sortA
			refresh()
			sort = null
			Console.groupEnd();
 		}
	}    
}