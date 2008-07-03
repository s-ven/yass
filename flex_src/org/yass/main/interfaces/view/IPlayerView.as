package org.yass.main.interfaces.view
{
	import flash.events.IEventDispatcher;

	public interface IPlayerView extends IEventDispatcher
	{
		function stopped():void;
		function playing():void;
	}
}