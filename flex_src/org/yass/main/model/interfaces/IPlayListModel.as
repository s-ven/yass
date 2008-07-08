/* 
 Copyright (c) 2008 Sven Duzont sven.duzont@gmail.com> All rights reserved. 
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), 
 to deal in the Software without restriction, including without limitation 
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished 
 to do so, subject to the following conditions: The above copyright notice 
 and this permission notice shall be included in all
 copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", 
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package org.yass.main.model.interfaces{
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	
	import org.yass.main.model.Track;

	public interface IPlayListModel extends IEventDispatcher	{
		function bindDataProvider(obj:Object):void;
		function getNextTrack(shuffle:Boolean, loop:Boolean):Object;
		function getPreviousTrack(shuffle:Boolean, loop:Boolean):Object;
		function set trackIndex(value:Number):void;
		function get trackIndex():Number;
		function get selectedTrack():Object;
		function get playListId():String;
		function playTrack(trackIndex:Number):void;
		function selectTrack(trackIndex:Number):void;
		function get length():Number;
		function get datas():ArrayCollection;
	}
}