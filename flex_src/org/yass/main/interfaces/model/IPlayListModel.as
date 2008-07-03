package org.yass.main.interfaces.model
{
	import flash.events.Event;
	import flash.events.IEventDispatcher;

	public interface IPlayListModel extends IEventDispatcher
	{
		function bindDataProvider(obj:Object):void;
		function getNextTrack(shuffle:Boolean, loop:Boolean):void;
		function getPreviousTrack(shuffle:Boolean, loop:Boolean):void;
		function set trackIndex(value:Number):void;
		function get trackIndex():Number;
		function get selectedTrack():Object;
		function get playListId():String;
		function playTrack(trackIndex:Number):void;
		function selectTrack(trackIndex:Number):void;
		function get length():Number;
	}
}