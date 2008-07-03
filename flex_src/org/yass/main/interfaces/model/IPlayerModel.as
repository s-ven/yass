package org.yass.main.interfaces.model
{
	import flash.events.Event;
	import flash.events.IEventDispatcher;

	public interface IPlayerModel extends IEventDispatcher{
		function next():void;
		function previous():void;
		function toogle():void;
	}
}