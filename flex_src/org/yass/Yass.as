package org.yass{
	import org.yass.main.model.BrowserModel;
	import org.yass.main.model.PlayListModel;
	import org.yass.main.model.PlayerModel;
	import org.yass.visualization.Display;
	public class Yass
	{
		public static var library:PlayListModel= new PlayListModel();
		public static var player:PlayerModel = new PlayerModel();
		public static var libTreeData:Object;
		public static var browser:BrowserModel;
		public static function get display():Display{
			return Display.instance;
		}
		
		public static function get state():String{
			// Playlist
			var str:String = "[Player.loadedPlayList]";
			if(player.loadedPlayList){
				str += "\n";
				str += " - length " + player.loadedPlayList.length +"\n";
				str += " - selectedIndex : " + player.loadedPlayList.trackIndex +"\n";
	 			if( player.loadedTrack){
					str += " - loadedTrack.length : " +player.loadedTrack.length +"\n";
					str += " - loadedTrack.id : " + player.loadedTrack.id +"\n";
	 			}
			}else
				str += " : NaN\n";
			// MP3 Player
			str += "\n[Player]";
			if(player){
				str += "\n";
				str += " - loop : " + player.loop +"\n";
				str += " - shuffle : " + player.shuffle +"\n";
				str += " - isPlaying : " + player.isPlaying +"\n";
				str += " - isPaused : " + player.isPaused +"\n";
				str += " - position : " + player.position +"\n";
				str += " - loadedLength : " + player.loadedLength +"\n";
			}else
				str += " : NaN\n";
			// Display
			str += "\n[Display]";
			if(display){
				str += "\n";
				str += " - progress.maximum : " + display.progress.maximum + "\n";
				str += " - progress.value : " + display.progress.value + "\n";
				str += " - scrollText.running : " + display.scrollText.running + "\n";
				str += " - scrollText.currentIndex : " + display.scrollText.currentIndex +"\n";
			}else
				str += " : NaN\n";			
			str += " - display.currentDisplay :" +display.currentDisplay +"\n";
			
		return str;
		}
	}
} 