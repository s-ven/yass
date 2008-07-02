package org.yass.mp3
{
	import flash.events.MouseEvent;
	import flash.net.URLRequestMethod;
	import flash.utils.Dictionary;
	
	import mx.binding.utils.BindingUtils;
	import mx.collections.ListCollectionView;
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.controls.DataGrid;
	import mx.controls.dataGridClasses.DataGridColumn;
	import mx.events.ListEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	[Bindable]
	public class FilterPane extends HTTPDataGrid
	{
		public var headerName:String;
		public var childPane:String;
		
		public static var panes:Dictionary = new Dictionary(false);
		
		public function FilterPane(){
			super();
			this.verticalScrollPolicy = "on";
			this.allowMultipleSelection = true;
			this.doubleClickEnabled= true;
			this.styleName="FilterPane";
			this.percentHeight=100;
			this.percentWidth=100;
			this.addEventListener(MouseEvent.DOUBLE_CLICK, function():void{
				MP3.playList.autoPlay(); 
			});
			this.addEventListener(ListEvent.CHANGE, filterChildPanes);
		}
		
 		public function search(searchKeywords:String, keywordsSearchField:String):void{
		    httpService.cancel();
		    var params:Object = new Object(); 
		    params.keywords = searchKeywords;
		    params.keywordsFields = keywordsSearchField;
		    httpService.send(params);
		}
		/**
		 */
		override protected function commitProperties():void{
			var dgc:DataGridColumn = new DataGridColumn(headerName);
			dgc.headerText=headerName; 
			dgc.dataField="name"; 
			dgc.draggable=false;
			dgc.sortable=false;
			columns = [dgc];
			httpService = new HTTPService();
			httpService.url = "/yass/library_" + id + "s.do";
			httpService.method = URLRequestMethod.POST;
			this.httpService.addEventListener(ResultEvent.RESULT, filterChildPanes);
			panes[id] = this;
            super.commitProperties();
		}
		
		private function cancelLoad():void{
			this.httpService.cancel();
		}
				
		private function get datas():ListCollectionView{
			return (dataProvider as ListCollectionView);
		}
		
		private function get child():HTTPDataGrid{
			var child:HTTPDataGrid = panes[childPane] as FilterPane;
			if(!child)
				child = MP3.playList;
			return child;
		}
		private function disableChilds():void{
			var ch:FilterPane = panes[childPane] as FilterPane;
			if(ch){
				ch.enabled = false;
				ch.disableChilds();
			}
			else
				MP3.playList.enabled = false;
		}
		/**
		 */
		 private function filterChildPanes(event:Event):void{
			this.removeEventListener(ListEvent.CHANGE, filterChildPanes);
			this.enabled = true;
		 	if(event is ResultEvent){
				BindingUtils.bindProperty(this, "dataProvider", httpService,  ["lastResult", id+"s", id]);
				if(datas.length >1)
					datas.addItemAt("All ("+datas.length + " " + headerName+"s)", 0);
           		selectedIndex =0;
			} 
            MP3.info("FilterPane-" + id + " : Loaded " + dataProvider.length);
			MP3.playList.tracksLoaded = false; 
			var childService:HTTPService = child.httpService;
			childService.cancel();
			disableChilds();			
		 	if(selectedIndex ==0 && datas.length >1){
			   	var toPost:Object=new Object();
			   	toPost.refresh = true;
			   	childService.send(toPost);
		 	}else if(selectedItems && selectedItems.length >0){
			   	var toPost:Object=new Object();
			   	toPost[id+"s"] = getNames(selectedItems);
			   	childService.send(toPost);
			}
		 	else
		 		childService.send();
            MP3.info("FilterPane-" + id + " : ChildPane refreshed " + childService.url);
			this.addEventListener(ListEvent.CHANGE, filterChildPanes);
		 }
		
		public static function hideAll():void{
			if(FilterPane._visible){
				MP3.info("FilterPane : hideAll");
				FilterPane._oldminHeight = (panes["genre"] as FilterPane).height;
				FilterPane._percentHeight = 0;
				FilterPane._visible=false; 
				FilterPane._verticalGap = 0;
			}
		}
		public static var _parentcnv:Canvas;
		public static var _parenthbox:HBox;
		public static var _oldminHeight:Number;
		
		public function set parentcnv(cnv:Canvas):void{
			_parentcnv = cnv;
		}
		public function set parenthbox(hbx:HBox):void{
			_parenthbox = hbx;
		}
		
		public static var _percentHeight:Number = 300;
		public static var _minHeight:int = 70;
		public static var _visible:Boolean=true; 
		public static var _verticalGap :Number= 5;
		public static function showAll():void{
			if(!FilterPane._visible){
				MP3.info("FilterPane : showAll");
				FilterPane._percentHeight = _oldminHeight;
				FilterPane._visible=true; 
				FilterPane._verticalGap = 5;
			}
		}
		private function getNames(selectedItems:Array):Array {
			var names:Array = new Array(); 
			for(var i:Object in selectedItems){
				var item:Object = selectedItems[i]
				names[i] = item.name;
			}
			return names;
		}
		
		public function refresh():void{
			var obj:Object = new Object();
			obj.keywoords="";
			httpService.send(); 
		}

	}
}