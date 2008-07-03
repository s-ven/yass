package org.yass.main.interfaces.model
{
	import flash.events.IEventDispatcher;

	public interface INavigationModel extends IEventDispatcher
	{
		function savePlayList(id:String, name:String):void;
		function loadPlayList(id:String, type:String):void;
	}
}