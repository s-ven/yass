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
	import mx.formatters.DateFormatter;
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
	public class FilterView extends DataGrid{
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
				addEventListener(FlexEvent.CREATION_COMPLETE, function():void{
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
      		Console.log("view.FilterView.filterChildPanes :: " + id + " : Loaded " + dataProvider.length);
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
				model.bindDataProvider(child);
				childService = model.httpService;
				(child as Object).model = model; 
				
/* 				var df:DateFormatter = new DateFormatter();
				df.formatString="HH:NN:SS";
				var model:PlayListModel = new PlayListModel();
				var df:DateFormatter = new DateFormatter();
				df.formatString="HH:NN:SS";
				model.playListId = df.format(new Date());
				Console.log(df.format(new Date()));
				childService = model.httpService;
				model.bindDataProvider(child);
				(child as Object).model = model; */
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
            Console.log("view.FilterView.clickPane :: " + id + " : ChildPane refreshed " + childService.url);
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