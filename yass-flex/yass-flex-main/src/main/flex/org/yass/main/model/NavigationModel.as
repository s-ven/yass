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
package org.yass.main.model{
	import flash.events.EventDispatcher;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.main.events.PlayListEvent;
	import org.yass.main.model.interfaces.INavigationModel;

	public class NavigationModel extends EventDispatcher implements INavigationModel{
		private var httpService:HTTPService;
		private var _playlist:PlayListModel;
		public function NavigationModel():void{
		}
		public function load():void{
			Console.log("model.NavigationModel :: init");
			httpService  = new HTTPService();
			httpService.url="/yass/rest/users/" + Yass.userId + "/playlists/";
			httpService.resultFormat="e4x";
			httpService.addEventListener(ResultEvent.RESULT, serviceResultHandler);
			httpService.send();
		}
		public function savePlayList(id:String, name:String):void{
			Console.log("model.Navigation.savePlayList name=" + name + ", id=" + id);
			var data:Object = new Object();
			data.userId = Yass.userId;			
			data.id = id;
			data.name=name;
			var svc:HTTPService = new HTTPService();
			svc.url = "/yass/playlist_save.do";
			svc.addEventListener(ResultEvent.RESULT, function():void{httpService.send();});
			svc.send(data);
		}
		public function loadPlayList(id:String, type:String):void{
			Console.group("model.Navigation.loadPlayList type=" + type + ", id=" + id);
			if(type != "library"){
				if(Yass.player.loadedPlayList && Yass.player.loadedPlayList.playListId == id)
					_playlist = Yass.player.loadedPlayList as PlayListModel;
				else
					_playlist = new PlayListModel();
				_playlist.removeEventListeners();
				_playlist.playListId = id;
				var svc : HTTPService = new HTTPService();
	 			svc.url = "/yass/rest/users/" + Yass.userId + "/playlists/" +id;
				svc.addEventListener(ResultEvent.RESULT, function():void{
					if(svc.lastResult.playlist){
						_playlist.removeAll();
						for each(var track:Object in svc.lastResult.playlist.track)
							_playlist.addItem(Yass.library.getTrack(track.id))
						dispatchEvent(new PlayListEvent(PlayListEvent.PLAYLIST_LOADED, null, _playlist, type));
					}
				});
				_playlist.setEventListeners();
				svc.send();
				Console.groupEnd();
				return ;
			}
			if(_playlist && Yass.player.loadedPlayList && Yass.player.loadedPlayList.playListId != _playlist.playListId)
				_playlist.removeEventListeners();
			_playlist = Yass.library
			Console.groupEnd()
			dispatchEvent(new PlayListEvent(PlayListEvent.PLAYLIST_LOADED, null, _playlist, type));
		}
		public var data:XML;
		private function serviceResultHandler(event:ResultEvent):void{
			data = event.result as XML;
			dispatchEvent(new PlayListEvent(PlayListEvent.REFRESH_PANE, data));
		}
	}
}