package org.yass.main.controller
{
	import org.yass.debug.log.Console;
	import org.yass.main.events.PlayerEvent;
	import org.yass.main.interfaces.model.IPlayerModel;
	import org.yass.main.interfaces.view.IPlayerView;;
	
	public class PlayerController
	{
		private var model:IPlayerModel;
		private var view:IPlayerView;
		public function PlayerController(_view:IPlayerView, _model:IPlayerModel)
		{
			this.model = _model;
			this.view = _view;
			view.addEventListener(PlayerEvent.NEXT, onNext);
			view.addEventListener(PlayerEvent.PREVIOUS, onPrevious);
			view.addEventListener(PlayerEvent.TOOGLE, onToogle);
			model.addEventListener(PlayerEvent.PLAYING, onPlaying);
			model.addEventListener(PlayerEvent.STOPPED, onStopped);
		}
		
		private function onNext(evt:PlayerEvent):void{
			Console.log("controller.PlayerController.onNext");
			model.next();	
		} 
		
		private function onPrevious(evt:PlayerEvent):void{
			Console.log("controller.PlayerController.onPrevious");
			model.previous();			
		}
		private function onToogle(evt:PlayerEvent):void{
			Console.log("controller.PlayerController.onToogle");
			model.toogle();			
		}
		private function onStopped(evt:PlayerEvent):void{
			Console.log("controller.PlayerController.onStopped");
			view.stopped();			
		}
		private function onPlaying(evt:PlayerEvent):void{
			Console.log("controller.PlayerController.onPlaying");
			view.playing();			
		}
	}
}