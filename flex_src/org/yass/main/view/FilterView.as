package org.yass.main.view
{
	import flash.net.URLRequestMethod;
	import flash.utils.Dictionary;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ListCollectionView;
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.events.FlexEvent;
	import mx.events.ListEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.debug.log.Console;
	import org.yass.main.model.PlayListModel;
	[Bindable]
	
	/**
	 * This component contains the logic for the Library browser filters pane (genre, artist, album)
	 * Each FilterPane object have a child associated so that an event on it
	 * will refresh recursively all the childs.
	 * Note that the childs will be also de activated until they're tottaly loaded;
	 * 
	 * The last pane (album) will not have a child associated in the mxml file.
	 * That will cause it to refresh the playlist
	 */
	public class FilterView extends DataGrid
	{
		private var httpService:HTTPService = new HTTPService();
		// The Header Text
		public var headerName:String;
		// Name of the child pane
		public var childPane:String; 
		// Playlist to refresh
		public var playList:Object;
		// Containair for FilterPane
		public static var panes:Dictionary = new Dictionary(false);
		// Load the pane when the Pane is first ?
		public var autoRefresh:Boolean = false;
		
		public function FilterView(){
			super();
			this.verticalScrollPolicy = "on";
			this.allowMultipleSelection = true;
			this.doubleClickEnabled= true;
			this.styleName="FilterPane";
			this.percentHeight=100;
			this.percentWidth=100;
			this.addEventListener(ListEvent.ITEM_DOUBLE_CLICK, function():void{playList.autoPlay();});
			this.addEventListener(ListEvent.ITEM_CLICK, clickPane);
		}
		
		// TODO : Move this somewhere else
 		public function search(searchKeywords:String, keywordsSearchField:String):void{
		    httpService.cancel();
		    var params:Object = new Object(); 
		    params.keywords = searchKeywords;
		    params.keywordsFields = keywordsSearchField;
		    httpService.send(params);
		}
		override protected function commitProperties():void{
			Console.log("view.FilterView :: Init " + id);
            super.commitProperties();
			var dgc:DataGridColumn = new DataGridColumn(headerName);
			dgc.headerText=headerName; 
			dgc.dataField="name"; 
			dgc.draggable=false;
			dgc.sortable=false;
			columns = [dgc];
			// TODO : remove of the trailing s
			// ex : /yass/library_genres.do
			httpService.url = "/yass/library_" + id + "s.do";
			httpService.method = URLRequestMethod.POST;
			httpService.addEventListener(ResultEvent.RESULT, filterChildPanes);
			if(autoRefresh)
				addEventListener(FlexEvent.CREATION_COMPLETE, function(){
					var obj:Object = new Object();
					obj.keywords="";
					obj.refresh=true;
					httpService.send(); 
					});
			panes[id] = this;
		}
				
		private function get datas():ListCollectionView{
			return (dataProvider as ListCollectionView);
		}
		
		/**
		 * Returns the child object, if no childName was given, will return the playList object
		 */
		private function get child():DataGrid{
			var child:FilterView = panes[childPane] as FilterView;
			// if no child associated : return the playList object
			if(!child)
				return playList as DataGrid;
			return child;
		}
		
		/**
		 * Recursive disabling of all childs
		 */ 
		private function disableChilds():void{
			var ch:FilterView = panes[childPane] as FilterView;
			if(ch){
				ch.enabled = false;
				ch.disableChilds();
			}
			else
				playList.enabled = false;
		}
		/**
		 * Called when the associated httpService object have returned results.
		 * Will display these results
		 */
		 private function filterChildPanes(event:Event):void{
		 	// The list was disables, have to enable it
			this.enabled = true;
			// Bind the dataProvider th the results
			BindingUtils.bindProperty(this, "dataProvider", httpService,  ["lastResult", id+"s", id]);
			// If multiple items, will add a 'All of' on first row
			if(datas.length >1)
				datas.addItemAt("All ("+datas.length + " " + headerName+"s)", 0);
      		Console.log("FilterView-" + id + " : Loaded " + dataProvider.length);
			// Now starts filtering child panes
			clickPane(event);
           	selectedIndex = 0;
		 }
		 /**
		 * Will call the childrens httpService field causing it to reload.
		 */ 
		 private function clickPane(event:Event):void{
			var childService:HTTPService; 			
			if(child is FilterView)
				childService = (child as FilterView).httpService;
			else{
				// Refreshing the playlist pane with a new playlistController object
				var model:PlayListModel = new PlayListModel();
				childService = model.httpService;
				model.bindDataProvider(child);
				(child as Object).model = model;
				// Will reload the Player with the playlist content only if a user playing is not loaded
/////////////////				if(MP3.player.loader && (!MP3.player.loader.playListId || MP3.player.loader.playListId==""))
				////////////////	MP3.player.loader = loader;
			}
			// Cancel the eventualy previouus request
			childService.cancel();
			// recursively disable all childs
			disableChilds();			
			// Il the 'Show All' is selected, force the refresh of child pane (will display all child's possible values)
		 	if(selectedIndex == 0 && datas.length >1){
			   	var toPost:Object=new Object();
			   	toPost.refresh = true;
			   	childService.send(toPost);
		 	}else if(selectedItems && selectedItems.length > 0){
		 		// FilterViews with the selected value
			   	var toPost:Object=new Object();
			   	toPost[id+"s"] = getNames(selectedItems);
			   	childService.send(toPost);
			}
		 	else
		 		childService.send();
            Console.log("FilterView-" + id + " : ChildPane refreshed " + childService.url);
   		}

		/*
		* Returns the selected values into an array
		*/
		private function getNames(selectedItems:Array):Array {
			var names:Array = new Array(); 
			for(var i:Object in selectedItems){
				
				var item:Object = selectedItems[i]
				names[i] = item.name;
			}
			return names;
		}
		
		public function refresh():void{

		}

	}
}