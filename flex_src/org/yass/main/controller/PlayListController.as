package org.yass.main.controller
{
	import org.yass.main.events.TrackEvent;
	import org.yass.main.interfaces.model.IPlayListModel;
	import org.yass.main.interfaces.view.IPlayListView;
	import org.yass.debug.log.Console;
	
	public class PlayListController
	{
		private var view:IPlayListView;
		private var model:IPlayListModel;
		public function PlayListController(_view:IPlayListView, _model:IPlayListModel)
		{
			this.view = _view;
			this.model = _model;
			view.addEventListener(TrackEvent.TRACK_PLAY, onTrackPlay);
			view.addEventListener(TrackEvent.TRACK_CLICK, onTrackClick);
			model.addEventListener(TrackEvent.TRACK_SELECTED, onTrackSelected);
		}
		
		private function onTrackSelected(evt:TrackEvent):void{
			var  loader:IPlayListModel = evt.loader;
			Console.log("controller.PlayListController.onTrackSelected trackIndex=" +evt.trackIndex);
			view.selectTrack(evt.trackIndex, loader);	
		}
		
		private function onTrackPlay(evt:TrackEvent):void{
			Console.log("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			Console.log("controller.PlayListController.onTrackPlay trackIndex=" +evt.trackIndex);
			evt.loader.playTrack(evt.trackIndex);			
		}
		private function onTrackClick(evt:TrackEvent):void{
			Console.log("controller.PlayListController.onTrackCick trackIndex=" +evt.trackIndex);
			model.selectTrack(evt.trackIndex);			
		}
	}
}