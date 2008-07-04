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
	import flash.events.IEventDispatcher;
	
	import org.yass.debug.log.Console;
	import org.yass.main.events.NavigationEvent;
	import org.yass.main.events.PlayListEvent;
	import org.yass.main.model.interfaces.INavigationModel;
	
	public class NavigationController{
		private var view:IEventDispatcher;
		private var model:INavigationModel;
		
		public function NavigationController(_view: IEventDispatcher, _model:INavigationModel):void{
			model = _model;
			view = _view;
			view.addEventListener(NavigationEvent.PLAYLIST_EDITED, onEditPlayList);
			view.addEventListener(NavigationEvent.PLAYLIST_CLICKED, onClickPlayList);
		}
		
		private function onEditPlayList(evt : NavigationEvent):void{
			Console.log("controller.Navigation.onEditPlayList id=" +evt.id + ", name=" + evt.name);
			model.savePlayList(evt.id, evt.name);
		}
		private function onClickPlayList(evt : NavigationEvent):void{
			Console.log("controller.Navigation.onClickPlayList id=" + evt.id + ", playListType=" + evt.playListType);
			model.loadPlayList(evt.id, evt.playListType);
		}
		
	}
}