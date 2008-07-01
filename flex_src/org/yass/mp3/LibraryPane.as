package org.yass.mp3
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.setTimeout;
	
	import mx.controls.Tree;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.events.ListEvent;
	import mx.managers.DragManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	[Bindable]
	public class LibraryPane extends Tree{	
		[Embed(source="../../../assets/small-tree-lib.png")] private var libIcon:Class;  
		[Embed(source="../../../assets/small-tree-spl.png")] private var smartPlIcon:Class;  
		[Embed(source="../../../assets/small-tree-upl.png")] private var userPlIcon:Class;  
		[Embed(source="../../../assets/small-tree-light.png")] private var lightPlIcon:Class;  

		private static var httpService:HTTPService = new HTTPService();
		
		public function LibraryPane()		{
			trace("PlaylistPane : init()");
			super();
			httpService.url="/flex/playlists.jsp";
			httpService.resultFormat="e4x";
			httpService.addEventListener(ResultEvent.RESULT, serviceResultHandler);
			httpService.send();
			labelField="@name";
			showRoot=false;
			this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
			this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
			this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
			this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
			this.addEventListener(MouseEvent.CLICK, mouseClickHandler);
			httpService.addEventListener(ResultEvent.RESULT, expand);
			this.addEventListener(ListEvent.ITEM_EDIT_END, editItem);
			dropEnabled = true;
	        setStyle("folderClosedIcon", null);
	        setStyle("folderOpenIcon", null);
		}
		
		private function expand(event:Event):void{
			if(dataProvider){
				var treeData:XML = dataProvider[0];
				openItems = treeData.elements();
			}
		}

		private var previousSelection:Object;
		private var service:HTTPService = new HTTPService();
		override protected function mouseClickHandler(event:MouseEvent):void{
			var item = event.currentTarget.selectedItem
			if(previousSelection != event.currentTarget.selectedItem){
						this.editable = false;
				if(item.@type == "void"){
					selectedItem = previousSelection;
					event.stopImmediatePropagation();
					return; 
				}
				trace("PlayListPane : click " + item.@type)
				var type:String = item.@type;
				if(type == "library" && previousSelection){
					MP3.playList.httpService.url = "/yass/library_playlist.do";
					MP3.playList.httpService.send();
					FilterPane.showAll();
				}else if(type == "smart"){
					FilterPane.hideAll()
				}else if(type == "user"){
					if(item.@id != "-1"){
						MP3.playList.httpService.url = "/yass/playlist_show.do?id="+item.@id;
						MP3.playList.httpService.send();
						FilterPane.hideAll();
					}
					else{
					this.editable = true;
					return;
					}
				}
				previousSelection = item;
			}
		}
		
		private function hideFilterPanes():void{
		}
		
		private function serviceResultHandler(event:ResultEvent):void{
			this.dataProvider =  httpService.lastResult;
			}
		
		override protected function dragEnterHandler(event:DragEvent):void{
            DragManager.acceptDragDrop(UIComponent(event.currentTarget)); 
		}
		
		override protected  function dragExitHandler(event:DragEvent):void{			
		}		
		
		override protected  function dragCompleteHandler(event:DragEvent):void{	
		}
		override protected  function dragOverHandler(event:DragEvent):void{
                var dropTarget:Tree = Tree(event.currentTarget);
                var r:int = dropTarget.calculateDropIndex(event);
                this.selectedIndex = r;
                var node:XML = this.selectedItem as XML;
                if( node.@type == "user" ) {
                    DragManager.showFeedback(DragManager.COPY);
                    return;
                }
                DragManager.showFeedback(DragManager.NONE);
		
		}
		
		private function editItem(event:ListEvent):void{
			var oldName : String=selectedItem.@name;
			setTimeout(saveItem,250, selectedItem, oldName);
			this.editable = false;
			selectedItem = previousSelection;
		}
		private function saveItem(obj:Object, oldName:String):void	{	
			trace("LibraryPane : Edited " + obj.@name + " " +  oldName);
			if(obj.@name != oldName){
				var svc:HTTPService = new HTTPService();
				svc.url = "/yass/playlist_save.do";
				var data:Object = new Object();
				data.id = "NO_ID";
				data.name=obj.@name;
				svc.addEventListener(ResultEvent.RESULT, function(){
					httpService.send();
				});
				svc.send(data);
				selectedItem = previousSelection;
			}
		
		}
		override protected  function dragDropHandler(event:DragEvent):void{
			var uids:Array = new Array();
			var selected:Object = (event.dragInitiator as PlayList).selectedItems;
			for(var i:Object in selected){
				var item = selected[i]
				if(item){  
					uids.push(item.UUID);
					trace(item.UUID);
				}
			}
			trace("Dropped : " + uids);
			var svc:HTTPService = new HTTPService();
			svc.url = "/yass/playlist_addto.do";
			var data:Object=new Object();
			data.UUIDs = uids
			data.id= event.currentTarget.selectedItem.@id;
			svc.send(data);
			selectedItem = previousSelection;
			
		}
	}
	
}