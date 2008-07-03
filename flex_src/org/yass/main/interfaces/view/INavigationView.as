package org.yass.main.interfaces.view
{
	import flash.events.IEventDispatcher;

	public interface INavigationView extends IEventDispatcher
	{
		function refreshNavigation(_dataProvider:Object):void;
		function getSelectedPlayListsView(type:String):IPlayListView
	}
}