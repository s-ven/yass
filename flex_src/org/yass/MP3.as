package org.yass{
	import org.yass.main.interfaces.model.IPlayerModel;
	import org.yass.main.model.PlayListModel;
	import org.yass.main.model.PlayerModel;
	import org.yass.visualization.Display;
	public class MP3
	{
		public function MP3():void{			
		}
		public static var player:PlayerModel = PlayerModel.instance;

		public static function get display():Display{
			return Display.instance;
		}
		
		public static function get state():String{
			// Playlist
			var str:String = "[Player.loader]";
			if(player.loadedPlayList){
				str += "\n";
				str += " - length " + player.loadedPlayList.length +"\n";
				str += " - selectedIndex : " + player.loadedPlayList.trackIndex +"\n";
	 			if( player.loadedTrack){
					str += " - currentTrack.length : " +player.loadedTrack.length * 1000 +"\n";
					str += " - currentTrack.UUID : " + player.loadedTrack.UUID +"\n";
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
				str += " - loadedLength : " + player.loadedLengh +"\n";
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
			str += " - display.spectrumAnalyzer.width :" +display.spectrumAnalyzer.width +"\n";
			str += " - display.spectrumAnalyzer.height :" +display.spectrumAnalyzer.height +"\n";
			str += " - display.spectrumAnalyzer.width :" +display.spectrumAnalyzer.width +"\n";
			str += " - display.spectrumAnalyzer.alphaActive :" +display.spectrumAnalyzer.alphaActive +"\n";
			str += " - display.spectrumAnalyzer.alphaInactive :" +display.spectrumAnalyzer.alphaInactive +"\n";
			str += " - display.spectrumAnalyzer.alphaMax :" +display.spectrumAnalyzer.alphaMax +"\n";
			str += " - display.spectrumAnalyzer.alphaTop :" +display.spectrumAnalyzer.alphaTop +"\n";
			str += " - display.spectrumAnalyzer.colorActive :" +display.spectrumAnalyzer.colorActive +"\n";
			str += " - display.spectrumAnalyzer.colorInactive :" +display.spectrumAnalyzer.colorInactive +"\n";
			str += " - display.spectrumAnalyzer.colorMax :" +display.spectrumAnalyzer.colorMax +"\n";
			str += " - display.spectrumAnalyzer.colorTop :" +display.spectrumAnalyzer.colorTop +"\n";
			str += " - display.spectrumAnalyzer.visible :" +display.spectrumAnalyzer.visible +"\n";
			str += " - display.spectrumAnalyzer.vuCount :" +display.spectrumAnalyzer.vuCount +"\n";
			str += " - display.spectrumAnalyzer.vuLevels :" +display.spectrumAnalyzer.vuLevels +"\n";
			str += " - display.spectrumAnalyzer.vuStep :" +display.spectrumAnalyzer.vuStep +"\n";
			
			str += " - display.currentDisplay :" +display.currentDisplay +"\n";
			
		return str;
		}
	}
} 