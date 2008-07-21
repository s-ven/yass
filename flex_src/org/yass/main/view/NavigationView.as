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
	import flash.events.MouseEvent;
	import flash.utils.setTimeout;
	
	import mx.controls.Tree;
	import mx.core.ClassFactory;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.events.ListEvent;
	import mx.managers.DragManager;
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.MainPane;
	import org.yass.main.controller.NavigationController;
	import org.yass.main.events.NavigationEvent;
	import org.yass.main.events.PlayListEvent;
	import org.yass.main.model.NavigationModel;
	import org.yass.main.model.interfaces.INavigationModel;
	import org.yass.main.renderers.NavigationViewRenderer;
	public class NavigationView extends Tree {	 
		[Embed(source="/assets/small-tree-lib.png")] private var libIcon:Class;  
 		[Embed(source="/assets/small-tree-spl.png")] private var smartPlIcon:Class;  
		[Embed(source="/assets/small-tree-upl.png")] private var userPlIcon:Class;  
		[Embed(source="/assets/small-tree-light.png")] private var lightPlIcon:Class;  
		 
		private var model:INavigationModel = new NavigationModel();
		private var controller:NavigationController;

		public var mainPane:MainPane;
		public function NavigationView()		{
			controller = new NavigationController(this, model);
			model.addEventListener(PlayListEvent.REFRESH_PANE, onRefreshPane);
			model.addEventListener(PlayListEvent.PLAYLIST_LOADED, onLoadPlayList);
			Console.log("view.NavigationView :: Init");
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
				var item:Object = selected[i]
				if(item)
					uids.push(item.id);
			}
			Console.log("view.NavigationView.gragDropHandler :: " + uids);
			var svc:HTTPService = new HTTPService();
			svc.url = "/yass/playlist_addto.do";
			var data:Object=new Object();
			data.trackIds = uids
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
			if(item.@type == "user" ||item.@type == "smart")
				if(item.@id == 0) 
					this.editable = true;
				else		{
					dispatchEvent(new NavigationEvent(NavigationEvent.PLAYLIST_CLICKED,  item.@id, null, item.@type));
					this.editable = false;
				}
			else if(item.@type=="library"){
				mainPane.currentState = null;
				mainPane.playList.model = Yass.library;
			}
			else
				event.stopImmediatePropagation();
		}		
		
		public function onRefreshPane(evt:PlayListEvent):void{			
			Console.log("view.Navigation.refreshNavigation");
			this.dataProvider = evt.result;
			if(dataProvider){
				var treeData:XML = dataProvider[0];
				openItems = treeData.elements();
			}
		}
		public function onLoadPlayList(evt:PlayListEvent):void{		
			Console.log("view.Navigation.getSelectedPlayListsView type=" + evt.playListType);
			mainPane.currentState = "playListState";
			mainPane.playList.model = evt.playlist
		}	
	}
}