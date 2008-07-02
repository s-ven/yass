package org.yass.mp3
{
    import flash.events.Event;
    import flash.events.MouseEvent;
    
    import mx.collections.ListCollectionView;
    import mx.collections.Sort;
    import mx.collections.SortField;
    import mx.controls.DataGrid;
    import mx.core.IUIComponent;
    import mx.events.DataGridEvent;
    import mx.events.ListEvent;
    
    [Bindable]
    public class PlayList  extends DataGrid implements IUIComponent  {
				
		public var playListId:String;
		private var sortA:Sort;
		private var sortByTrackNr:SortField;
		private var sortByArtist:SortField;
		private var sortByAlbum:SortField;
		private var sortByTitle:SortField;
		private var sortByLength:SortField;
		private var oldColumn:String;
		public var loader:PlayListLoader;

 		public function PlayList(){
			Console.log("PlayList : init"); 			
			this.doubleClickEnabled=true;
			this.allowMultipleSelection=true; 
			this.dragEnabled=true;
			
			this.addEventListener(DataGridEvent.HEADER_RELEASE, headerRelease);
 			this.addEventListener(MouseEvent.CLICK, clickHandler,true);
 			this.addEventListener(MouseEvent.DOUBLE_CLICK, doubleClickHandler,true);
 			sortA = new Sort();
			sortByAlbum = new SortField("album", true);
			sortByArtist = new SortField("artist", true);
			sortByTitle = new SortField("title", true);
			sortByTrackNr = new SortField("trackNr", true, false, true);
			sortByLength = new SortField("length", true, false, true);
 		}
 		
 		
		private function headerRelease(event:DataGridEvent):void {
			Console.log("PlayList : Sorting column " + event.dataField.toString());
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
        
        public function doubleClickHandler(event:Event):void{
        	Console.log("PlayList : Doubleclick");
        	if(enabled){
				MP3.player.loader = loader;
				loader.selectedIndex = selectedIndex;
				MP3.player.loader.force();				
        	}
        }
        public function clickHandler(event:Event):void{
        	Console.log("PlayList : Click");
        	if(enabled){
	        	if(selectedIndex == -1)
	        		selectedIndex = 0;
	        	if(!loader.isPlaying && !loader.isPaused)
		            loader.currentTrack = providerCollection.getItemAt(selectedIndex);
         	}
        }
		public function autoPlay():void{
			Console.log("PlayList:AutoPlay : requested");
			addEventListener(Event.ENTER_FRAME, autoPlayDatagrid);			
		}
		private function autoPlayDatagrid(event:Event):void{
			if(enabled){
				MP3.player.loader = loader;
				Console.log("PlayList: AutoPlay : playing");
				removeEventListener(Event.ENTER_FRAME, autoPlayDatagrid);
				loader.stop();
				selectedIndex = -1;
				loader.getNextTrack(); 
				loader.play();
				Console.log("PlayList: AutoPlay : OK");
			}
		}
    }
}