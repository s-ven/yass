package org.yass.main.view
{
	import flash.events.MouseEvent;
	import flash.utils.setTimeout;
	
	import mx.controls.Tree;
	import mx.core.ClassFactory;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.events.ListEvent;
	import mx.managers.DragManager;
	import mx.rpc.http.HTTPService;
	
	import org.yass.debug.log.Console;
	import org.yass.main.MainPane;
	import org.yass.main.controller.NavigationController;
	import org.yass.main.events.NavigationEvent;
	import org.yass.main.interfaces.model.INavigationModel;
	import org.yass.main.interfaces.view.INavigationView;
	import org.yass.main.interfaces.view.IPlayListView;
	import org.yass.main.model.NavigationModel;
	import org.yass.main.NavigationViewRenderer;
	[Bindable]
	public class NavigationView extends Tree implements INavigationView{	 
		[Embed(source="../assets/small-tree-lib.png")] private var libIcon:Class;  
 		[Embed(source="../assets/small-tree-spl.png")] private var smartPlIcon:Class;  
		[Embed(source="../assets/small-tree-upl.png")] private var userPlIcon:Class;  
		[Embed(source="../assets/small-tree-light.png")] private var lightPlIcon:Class;  
		 
		private var model:INavigationModel = new NavigationModel();
		private var controller:NavigationController;

		public var mainPane:MainPane;
		public function NavigationView()		{
			controller = new NavigationController(this, model);
			Console.log("PlayListsView : init()");
			super();
			labelField="@name";
			showRoot=false;
			this.addEventListener(DragEvent.DRAG_ENTER, dragEnterHandler);
			this.addEventListener(DragEvent.DRAG_OVER, dragOverHandler);
			this.addEventListener(DragEvent.DRAG_DROP, dragDropHandler);
			this.addEventListener(DragEvent.DRAG_EXIT, dragExitHandler);
			this.addEventListener(ListEvent.ITEM_CLICK, mouseClickHandler);
			this.addEventListener(ListEvent.ITEM_EDIT_END, onEditItem);
			dropEnabled = true;
	        setStyle("folderClosedIcon", null);
	        setStyle("folderOpenIcon", null); 
	        itemRenderer = new ClassFactory(NavigationViewRenderer);
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
		
		override protected  function dragDropHandler(event:DragEvent):void{
			var uids:Array = new Array();
			var selected:Object = (event.dragInitiator as PlayListView).selectedItems;
			for(var i:Object in selected){
				var item = selected[i]
				if(item)
					uids.push(item.UUID);
			}
			Console.log("Dropped : " + uids);
			var svc:HTTPService = new HTTPService();
			svc.url = "/yass/playlist_addto.do";
			var data:Object=new Object();
			data.UUIDs = uids
			data.id= event.currentTarget.selectedItem.@id;
			svc.send(data);
		}
		private function onEditItem(event:ListEvent):void{
			var oldName:String=selectedItem.@name;
			setTimeout(saveItem,250, selectedItem, oldName);
			this.editable = false;
		}
		private function saveItem(obj:Object, oldName:String):void	{	
			if(obj.@name != oldName){			
			Console.log("view.Navigation.saveItem name=" + obj.@name + ",oldName=" +  oldName);
				this.dispatchEvent(new NavigationEvent(NavigationEvent.PLAYLIST_EDITED, obj.@id, obj.@name, obj.@type));		
			}
		}
		override protected function mouseClickHandler(event:MouseEvent):void{
			var item:Object= event.currentTarget.selectedItem		
			Console.log("view.Navigation.mouseClickHandler type=" + item.@type + ", id=" +item.@id);
			dispatchEvent(new NavigationEvent(NavigationEvent.PLAYLIST_CLICKED,  item.@id, null, item.@type));
		}		
		
		public function refreshNavigation(_dataProvider:Object):void{			
			Console.log("view.Navigation.refreshNavigation");
			this.dataProvider = _dataProvider;
			if(dataProvider){
				var treeData:XML = dataProvider[0];
				openItems = treeData.elements();
			}
		}
		public function getSelectedPlayListsView(type:String):IPlayListView{		
			Console.log("view.Navigation.getSelectedPlayListsView type=" + type);
			if(type == "library"){
				mainPane.currentState = "libraryBrowser";
				return mainPane.libraryBrowser.playList;
			}
			mainPane.currentState = "playListBrowser";
			return mainPane.playListBrowser.playList;
		
		}
		
	}
	
}