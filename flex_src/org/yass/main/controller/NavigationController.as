package org.yass.main.controller
{
	import org.yass.main.events.PlayListEvent;
	import org.yass.main.events.NavigationEvent;
	import org.yass.main.interfaces.model.INavigationModel;
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.main.interfaces.view.INavigationView;
	import org.yass.main.interfaces.view.IPlayListView;
	import org.yass.debug.log.Console;
	
	public class NavigationController
	{
		private var view:INavigationView;
		private var model:INavigationModel;
		
		public function NavigationController(_view: INavigationView, _model:INavigationModel):void{
			model = _model;
			view = _view;
			view.addEventListener(NavigationEvent.PLAYLIST_EDITED, onEditPlayList);
			view.addEventListener(NavigationEvent.PLAYLIST_CLICKED, onClickPlayList);
			model.addEventListener(PlayListEvent.PLAYLIST_SAVED, onSavePlayList);
			model.addEventListener(PlayListEvent.PLAYLIST_LOADED, onLoadPlayList);
		}
		
		// Model event handlers;
		private function onEditPlayList(evt : NavigationEvent):void{
			Console.log("controller.Navigation.onEditPlayList id=" +evt.id + ", name=" + evt.name);
			model.savePlayList(evt.id, evt.name);
		}
		private function onClickPlayList(evt : NavigationEvent):void{
			Console.log("controller.Navigation.onClickPlayList id=" + evt.id + ", playListType=" + evt.playListType);
			model.loadPlayList(evt.id, evt.playListType);
		}
		
		// Model event handlers;  
		private function onSavePlayList(evt:PlayListEvent):void{
			Console.log("controller.Navigation.onSavePLayList result=" +evt.result);
			view.refreshNavigation(evt.result);
		} 
		private function onLoadPlayList(evt:PlayListEvent):void{
			Console.log("controller.Navigation.onLoadPlayList playListType=" +evt.playListType);
			var plv:IPlayListView = view.getSelectedPlayListsView(evt.playListType);
			Console.log(evt.playlist);
			if(evt.playListType=="user")
				plv.model = evt.playlist;
			}
		
	}
}