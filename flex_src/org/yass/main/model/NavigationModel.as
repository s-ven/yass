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
	import org.yass.main.model.interfaces.IPlayListModel;

	public class NavigationModel extends EventDispatcher implements INavigationModel{
		private var httpService:HTTPService = new HTTPService();
		public function NavigationModel():void{
        	Console.log("model.NavigationModel :: init");
			httpService.url="/yass/jsp/navigation.jsp";
			httpService.resultFormat="e4x";
			httpService.addEventListener(ResultEvent.RESULT, serviceResultHandler);
			httpService.send();
		}
		
		public function savePlayList(id:String, name:String):void{
			Console.log("model.Navigation.savePlayList name=" + name + ", id=" + id);
			var svc:HTTPService = new HTTPService();
			svc.url = "/yass/playlist_save.do";
			var data:Object = new Object();
			data.id = id;
			data.name=name;
			svc.addEventListener(ResultEvent.RESULT, function():void{httpService.send();});
			svc.send(data);
		}
		public function loadPlayList(id:String, type:String):void{
			Console.group("model.Navigation.loadPlayList type=" + type + ", id=" + id);
			var playlist:IPlayListModel;
			try{
					if(Yass.player.loadedPlayList && Yass.player.loadedPlayList.playListId == id){
						Console.log("model.Navigation.loadPlayList :: Already playing PlayList" + Yass.player.loadedPlayList.trackIndex);
						playlist = Yass.player.loadedPlayList;
					}
					else{
						Console.log("model.Navigation.loadPlayList :: Fetching playList from server");
						playlist = new PlayListModel();
						(playlist as PlayListModel).playListId = id;
						var obj:Object = new Object();
						obj.id = id;
						var httpSvc:HTTPService = (playlist as PlayListModel).httpService; 
						httpSvc.addEventListener(ResultEvent.RESULT, function():void{
							if(httpSvc.lastResult.tracks)
								for each(var track:Object in httpSvc.lastResult.tracks.track)
									playlist.addItem(Yass.library.getTrack(track.id))
						});
						httpSvc.send(obj);
					}				 
			}finally{
				Console.groupEnd()
				dispatchEvent(new PlayListEvent(PlayListEvent.PLAYLIST_LOADED, null, playlist, type));
			}
		}	
		
		private function serviceResultHandler(event:ResultEvent):void{
			dispatchEvent(new PlayListEvent(PlayListEvent.REFRESH_PANE, event.result));
		}
	}
}