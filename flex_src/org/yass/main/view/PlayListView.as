package org.yass.main.view
{
    import flash.events.Event;
    
    import mx.collections.ListCollectionView;
    import mx.collections.Sort;
    import mx.collections.SortField;
    import mx.controls.DataGrid;
    import mx.events.DataGridEvent;
    import mx.events.ListEvent;
    
    import org.yass.MP3;
    import org.yass.debug.log.Console;
    import org.yass.main.controller.PlayListController;
    import org.yass.main.events.TrackEvent;
    import org.yass.main.interfaces.model.IPlayListModel;
    import org.yass.main.interfaces.view.IPlayListView;
    
    [Bindable]
    public class PlayListView  extends DataGrid implements IPlayListView  {
				
		public var playListId:String;
		private var sortA:Sort;
		private var sortByTrackNr:SortField;
		private var sortByArtist:SortField;
		private var sortByAlbum:SortField;
		private var sortByTitle:SortField;
		private var sortByLength:SortField;
		private var oldColumn:String;
		public var _model:IPlayListModel;
		
		public function set model(value:IPlayListModel):void{
			this._model = value;
			Console.log("view.PlayListView.setLoader : " + model);
			model.bindDataProvider(this);
			playListId = model.playListId;
			this.controller = new PlayListController(this, value);
		}
		public function get model():IPlayListModel{
			return _model;
		}
		
		private var controller:PlayListController;

 		public function PlayListView(){	
			this.doubleClickEnabled=true;
			this.allowMultipleSelection=true; 
			this.dragEnabled=true;
			
			this.addEventListener(DataGridEvent.HEADER_RELEASE, headerRelease);
 			this.addEventListener(ListEvent.ITEM_CLICK, onClick);
 			this.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, onDoubleClick);
 			sortA = new Sort();
			sortByAlbum = new SortField("album", true);
			sortByArtist = new SortField("artist", true);
			sortByTitle = new SortField("title", true);
			sortByTrackNr = new SortField("trackNr", true, false, true);
			sortByLength = new SortField("length", true, false, true);
 		}
 		
 		
		private function headerRelease(event:DataGridEvent):void {
			Console.log("view.PlayList.headerRelease column=" + event.dataField.toString());
			if (event.dataField.toString()=="trackNr") {
			    if(oldColumn == "trackNr")
			    	sortByTrackNr.reverse();
			 sortA.fields=[sortByTrackNr, sortByArtist, sortByAlbum, ];
			} else if (event.dataField.toString()=="album") {
			    if(oldColumn == "album")
			    	sortByAlbum.reverse();
			 sortA.fields=[sortByAlbum, sortByTrackNr, sortByArtist];
			} else if (event.dataField.toString()=="artist") {
			    if(oldColumn == "artist")
			    	sortByArtist.reverse();
			 sortA.fields=[sortByArtist, sortByAlbum, sortByTrackNr];
			   } else if (event.dataField.toString()=="title") {
			    if(oldColumn == "title")
			    	sortByTitle.reverse();
			   sortA.fields=[sortByTitle, sortByArtist, sortByAlbum];
			}else if (event.dataField.toString()=="length") {
			    if(oldColumn == "length")
			    	sortByLength.reverse();
			   sortA.fields=[sortByLength, sortByTitle, sortByArtist, sortByAlbum];
			} 
			oldColumn = event.dataField.toString();
			providerCollection.sort=sortA; 
			providerCollection.refresh();
			event.preventDefault();
		}

	 	
	 	public function get length():Number{
	 		if(providerCollection)
    			return providerCollection.length;
			return 0;
		}

        private function get providerCollection():ListCollectionView{
        	if(dataProvider)
        	   	return (dataProvider as ListCollectionView);
        	else return null;
        }
        
        public function onDoubleClick(event:Event):void{
			Console.log("view.PlayList.onDoubleClick");
			Console.log("eeeeeeeeeeeeeeeeee");
		  	if(enabled)
				dispatchEvent(new TrackEvent(TrackEvent.TRACK_PLAY, selectedIndex, _model));
			Console.log("fsdfsd");
        }
        public function onClick(event:Event):void{
			Console.log("view.PlayList.onClick");
        	if(enabled)
        		dispatchEvent(new TrackEvent(TrackEvent.TRACK_CLICK, selectedIndex, _model));
        }
		public function autoPlay():void{
			Console.log("view.PlayList.autoPlay :: requested");
			addEventListener(Event.ENTER_FRAME, autoPlayDatagrid);			
		}
		private function autoPlayDatagrid(event:Event):void{
			if(enabled){
				MP3.player.loadedPlayList = _model;
				Console.log("view.PlayList.autoPlayDatagrid");
				removeEventListener(Event.ENTER_FRAME, autoPlayDatagrid);
				MP3.player.stop();
				MP3.player.loadedPlayList = model;
				MP3.player.play();
				Console.log("view.PlayList.autoPlayDatagrid :: AutoPlay : OK");
			}
		}
		public function selectTrack(trackIndex:Number, loader:IPlayListModel):void{
			Console.log("view.PlayList.selectTrack trackIndex=" +trackIndex);
			if(loader.playListId == this.playListId){
				this.selectedIndex = trackIndex;
				this.scrollToIndex(trackIndex);
			}
		}
    }
}