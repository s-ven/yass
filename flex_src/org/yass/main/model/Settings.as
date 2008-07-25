package org.yass.main.model{
	import mx.events.PropertyChangeEvent;
	import mx.rpc.http.HTTPService;
	
	import org.yass.Yass;
	import org.yass.debug.log.Console;
	import org.yass.util.tree.Value;
	public class Settings {
		public var shuffle:Boolean = false;  
		public var loop:Boolean = false;
		public var showRemaining:Boolean = true;
		public var displayMode:int=0; 
		public var loadedTrack:Track;
		public var artistSelected: Array = new Array();
		public var genreSelected: Array = new Array();
		public var albumSelected: Array = new Array();
		public var stopFadeout:int = 1000;
		public var skipFadeout:int = 3000;
		public var nextFadeout:int = 6000;
		public var loadedTrackId:int = 0;
		public var trackInfoIds:Array;
		public function Settings(obj:Object):void{
			Yass.settings = this;
			var xml:XML = new XML(obj);
			if(xml.attributes().length() >0){ 
				shuffle = (xml.@shuffle =="true") ;
				loop = (xml.@loop =="true");
				showRemaining = (xml.@showRemaining =="true");
				displayMode = xml.@displayMode;
				stopFadeout = xml.@stopFadeout;
				skipFadeout = xml.@skipFadeout;
				nextFadeout = xml.@nextFadeout;
				for(var nodeName:Object in xml.trackInfoIds.trackInfo){
					var trackInfo:Value = LibraryModel.trackInfos[xml.trackInfoIds.trackInfo[nodeName].@id+""];
					this[trackInfo.type+"Selected"].push(trackInfo);					
				} 
				if(xml.@loadedTrackId != 0)
					Yass.player.loadedTrack = Yass.library.getTrack(xml.@loadedTrackId)
				Yass.library.filteredText = "";
			}
		}
		
		public function save(evt:PropertyChangeEvent):void{
			if(evt.property != "artistSelected" && evt.property != "genreSelected"){
				Console.log("model.Settings.save");
				var svc:HTTPService = new HTTPService();
				loadedTrackId = loadedTrack?loadedTrack.id:0;
				trackInfoIds = new Array();
				artistSelected.forEach(function(val:Value, index:int, arr:Array):void{trackInfoIds.push(val.id)});
				genreSelected.forEach(function(val:Value, index:int, arr:Array):void{trackInfoIds.push(val.id)});
				albumSelected.forEach(function(val:Value, index:int, arr:Array):void{trackInfoIds.push(val.id)});
				svc.url = "/yass/settings_save.do"
				svc.method = "POST";
				svc.send(this);
			}
		}
	}
}