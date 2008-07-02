package org.yass.mp3
{
	import com.airlogger.AirLoggerDebug;
		
	public class MP3
	{
		public function MP3():void{
			
		}
		public static var player:Player = Player.instance;

		public static function info(o:Object):void{
			AirLoggerDebug.info(o);
		}
		public static function get playList():PlayList{
			return player.playList;
		}
		public static function get display():Display{
			return Display.instance;
		}
		public static function get controller():Controller{
			return Controller.instance ;
		}
		public static function get state():String{
			var str:String = "[Playlist]";
			if(playList){
				str += "\n";
				str += " - length " + playList.length +"\n";
				str += " - tracksLoaded : " + playList.tracksLoaded +"\n";
				str += " - loop : " + playList.loop +"\n";
				str += " - shuffle : " + playList.shuffle +"\n";
				str += " - selectedIndex : " + playList.selectedIndex +"\n";
				str += " - shuffledListPosition : " + playList.shuffledListPosition +"\n";
	 			str += " - shuffledTracks.length : " + playList.shuffledTracks.length +"\n";
	 			if( playList.currentTrack){
					str += " - currentTrack.length : " + playList.currentTrack.length * 1000 +"\n";
					str += " - currentTrack.UUID : " + playList.currentTrack.UUID +"\n";
	 			}
			}else
				str += " : NaN\n";
			str += "\n[MP3Player]";
			if(player){
				str += "\n";
				str += " - isPlaying : " + player.isPlaying +"\n";
				str += " - isPaused : " + player.isPaused +"\n";
				str += " - position : " + player.position +"\n";
		//		str += " - pausedPosition : " + player.pausePosition +"\n";
				str += " - loadedLength : " + player.loadedLengh +"\n";
			}else
				str += " : NaN\n";
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
			str += " - display.currentDisplay :" +display.currentDisplay +"\n";
			
		return str;
		}
	}
} 