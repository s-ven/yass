package org.yass.mp3
{
    import flash.events.Event;
    import flash.events.MouseEvent;
    
    import mx.binding.utils.BindingUtils;
    import mx.collections.ArrayCollection;
    import mx.collections.ListCollectionView;
    import mx.collections.Sort;
    import mx.collections.SortField;
    import mx.core.IUIComponent;
    import mx.events.DataGridEvent;
    import mx.rpc.events.ResultEvent;
    
    [Bindable]
    public class PlayList extends HTTPDataGrid implements IUIComponent  {
    	
		public var currentTrack:Object; 
		public var shuffle:Boolean;
        
		public var loop:Boolean;
		public var tracksLoaded:Boolean= true;;
        public var shuffledTracks:ArrayCollection;
        public var shuffledListPosition:int; 
				
		private var sortA:Sort;
		private var sortByTrackNr:SortField;
		private var sortByArtist:SortField;
		private var sortByAlbum:SortField;
		private var sortByTitle:SortField;
		private var sortByLength:SortField;
		private var oldColumn:String;

 		public function PlayList(){
			MP3.info("PlayList : init");
 			
			this.doubleClickEnabled=true;
			this.allowMultipleSelection=true; 
			this.dragEnabled=true;
														
 			MP3.player.playList = this;
			this.addEventListener(DataGridEvent.HEADER_RELEASE, headerRelease);
 			this.addEventListener(MouseEvent.CLICK, click,true);
 			this.addEventListener(MouseEvent.DOUBLE_CLICK, doubleClick,true);
 			shuffledTracks = new ArrayCollection();
 			httpService.url = "/yass/library_playlist.do";
 			httpService.addEventListener(ResultEvent.RESULT, resultEvent 	);		
 			
 			sortA = new Sort();
			sortByAlbum = new SortField("album", true);
			sortByArtist = new SortField("artist", true);
			sortByTitle = new SortField("title", true);
			sortByTrackNr = new SortField("trackNr", true, false, true);
			sortByLength = new SortField("length", true, false, true);
 		}
 		
 		
		private function headerRelease(event:DataGridEvent):void {
			MP3.info("PlayList : Sorting column " + event.dataField.toString());
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
		private function resultEvent (event:Event):void{
			BindingUtils.bindProperty(this, "dataProvider", httpService,  ["lastResult", "tracks", "track"]);
			this.enabled = true;
 			tracksLoaded= true;
 			
    	
	 	}
	 	
	 	public function get length():Number{
	 		if(providerCollection)
    			return providerCollection.length;
			return 0;
		}

        private function getPreviousShuffledTrack():Number{
        	if(shuffledTracks.length > 1 && shuffledListPosition > 1)
        		return shuffledTracks.getItemAt((shuffledListPosition -= 1) -1) as Number;
        	stop();
        	return selectedIndex;
    	}
 
        private function getNextShuffledTrack():Number{
        	if(!(shuffledTracks.length > 1 && shuffledListPosition < shuffledTracks.length))
        		shuffledTracks.addItem(Math.ceil( 
        		( 1 - Math.random()) * length) - 1);
        	shuffledListPosition += 1;
        	return shuffledTracks.getItemAt(shuffledListPosition-1) as Number;
    	}
    	
    	public function get isPaused():Boolean{
    		return MP3.player.isPaused; 
    	}
    	public function get isPlaying():Boolean{
    		return MP3.player.isPlaying; 
    	}
    	    	
        public function play():void{   
            this.stop();      
            MP3.player.playUrl();
                
        }
        
        public function stop():void{
        	MP3.player.stop();
        }
        
        private function get providerCollection():ListCollectionView{
        	if(dataProvider)
        	   	return (dataProvider as ListCollectionView);
        	else return null;
        }
        
        public function getNextTrack():void{
            if(shuffle)
	           	selectedIndex = getNextShuffledTrack();
	        else{
	            if(selectedIndex < length - 1)
	            	selectedIndex += 1;
	            else
	            	if(loop)
	                	selectedIndex = 0;
	                else 
	                	MP3.player.stop();           
	        } 
            this.currentTrack = providerCollection.getItemAt(selectedIndex);
			MP3.player.position = 0;
	        if(isPlaying)
				play();
            scrollToIndex(selectedIndex)
        }
        
        public function getPreviousTrack():void{
        	if(shuffle)
        		selectedIndex = getPreviousShuffledTrack();
        	else
        	   	if(selectedIndex > 0)
               		selectedIndex -= 1;
            	else
            		if(loop)
                		selectedIndex = length -1;
                	else
                		MP3.player.stop();
            this.currentTrack = providerCollection.getItemAt(selectedIndex);
			MP3.player.position = 0;
			if(isPlaying)
				play();
            scrollToIndex(selectedIndex)
		}
		
        public function tooglePlay():void{
        	if(selectedIndex == -1)
        		selectedIndex = 0;
            scrollToIndex(selectedIndex)
        	if(isPlaying)
        		MP3.player.pause();
        	else{
        		if(isPaused)
        			MP3.player.play();
        		else {
	            	this.currentTrack = providerCollection.getItemAt(selectedIndex);
        			play();
        		}
        	}
        }
        
        public function doubleClick(event:Event):void{
        	if(enabled){
		        this.currentTrack = providerCollection.getItemAt(selectedIndex);
	        	this.play();
	        	if(shuffle){
	        		while(shuffledTracks.length> shuffledListPosition)
	        			shuffledTracks.removeItemAt(shuffledListPosition);
	        		shuffledListPosition +=1;
	        		shuffledTracks.addItem(selectedIndex);
	        	}
        	}
            this.dispatchEvent(event);
        }
        public function click(event:Event):void{
        	if(enabled){
	        	if(selectedIndex == -1)
	        		selectedIndex = 0;
	        	if(!isPlaying && !isPaused)
		            this.currentTrack = providerCollection.getItemAt(selectedIndex);
         	}
	        this.dispatchEvent(event);
        }
	   
		public function autoPlay():void{
			MP3.info("PlayList:AutoPlay : requested");
			addEventListener(Event.ENTER_FRAME, autoPlayDatagrid);			
		}
		private function autoPlayDatagrid(event:Event):void{
			if(tracksLoaded == true){
				MP3.info("PlayList: AutoPlay : playing");
				removeEventListener(Event.ENTER_FRAME, autoPlayDatagrid);
				stop();
				selectedIndex = -1;
				getNextTrack(); 
				play();
				MP3.info("PlayList: AutoPlay : OK");
			}
		}
    }
}