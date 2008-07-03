package org.yass.main.interfaces.view
{
	import flash.events.IEventDispatcher;
	
	import org.yass.main.interfaces.model.IPlayListModel;
	
	public interface IPlayListView extends IEventDispatcher 
	{
		function selectTrack(trackIndex:Number, loader:IPlayListModel):void;
		function get model():IPlayListModel;
		function set model(loader:IPlayListModel):void;
		function set playListId(value:String):void;
	}
}