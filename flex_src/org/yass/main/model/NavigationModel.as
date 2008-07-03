package org.yass.main.model
{
	import flash.events.EventDispatcher;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.MP3;
	import org.yass.main.events.PlayListEvent;
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.main.interfaces.model.INavigationModel;
	import org.yass.debug.log.Console;

	public class NavigationModel extends EventDispatcher implements INavigationModel{
		private var httpService:HTTPService = new HTTPService();
		private var previousSelection:Object;
		public function NavigationModel():void{
        	Console.info("model.NavigationModel :: init");
			httpService.url="/flex/playlists.jsp";
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
				if(previousSelection != id && type == "user" && id != "-1"){
					if(MP3.player.loadedPlayList && MP3.player.loadedPlayList.playListId == id){
						Console.log("model.Navigation.loadPlayList :: Already playing PlayList" + MP3.player.loadedPlayList.trackIndex);
						playlist = MP3.player.loadedPlayList;
					}
					else{
							Console.log("model.Navigation.loadPlayList :: Fetching playList from server");
						playlist = new PlayListModel();
						(playlist as PlayListModel).playListId = id;
						var obj:Object = new Object();
						obj.id = id;
						(playlist as PlayListModel).httpService.send(obj);
					}				 
				}  
			}finally{
				Console.groupEnd()
				dispatchEvent(new PlayListEvent(PlayListEvent.PLAYLIST_LOADED, null, playlist, type));
				previousSelection = id;
			}
		}	
		
		private function serviceResultHandler(event:ResultEvent):void{
			dispatchEvent(new PlayListEvent(PlayListEvent.PLAYLIST_SAVED, event.result));
		}
	}
}