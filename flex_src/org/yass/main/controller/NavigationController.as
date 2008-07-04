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
package org.yass.main.controller{
	import org.yass.main.events.PlayListEvent;
	import org.yass.main.events.NavigationEvent;
	import org.yass.main.interfaces.model.INavigationModel;
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.main.interfaces.view.INavigationView;
	import org.yass.main.interfaces.view.IPlayListView;
	import org.yass.debug.log.Console;
	
	public class NavigationController{
		private var view:INavigationView;
		private var model:INavigationModel;
		
		public function NavigationController(_view: INavigationView, _model:INavigationModel):void{
			model = _model;
			view = _view;
			view.addEventListener(NavigationEvent.PLAYLIST_EDITED, onEditPlayList);
			view.addEventListener(NavigationEvent.PLAYLIST_CLICKED, onClickPlayList);
			model.addEventListener(PlayListEvent.REFRESH_PANE, onRefreshPane);
			model.addEventListener(PlayListEvent.PLAYLIST_LOADED, onLoadPlayList);
		}
		
		private function onEditPlayList(evt : NavigationEvent):void{
			Console.log("controller.Navigation.onEditPlayList id=" +evt.id + ", name=" + evt.name);
			model.savePlayList(evt.id, evt.name);
		}
		private function onClickPlayList(evt : NavigationEvent):void{
			Console.log("controller.Navigation.onClickPlayList id=" + evt.id + ", playListType=" + evt.playListType);
			model.loadPlayList(evt.id, evt.playListType);
		}
		private function onRefreshPane(evt:PlayListEvent):void{
			Console.log("controller.Navigation.onRefreshPane");
			view.refreshNavigation(evt.result);
		} 
		private function onLoadPlayList(evt:PlayListEvent):void{
			Console.log("controller.Navigation.onLoadPlayList playListType=" +evt.playListType);
			// TODO :: Move this to the view
			var plv:IPlayListView = view.getSelectedPlayListsView(evt.playListType);
			if(evt.playListType=="user")
				plv.model = evt.playlist;
		}
		
	}
}