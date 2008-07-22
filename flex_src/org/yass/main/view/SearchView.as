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
package org.yass.main.view
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.utils.setTimeout;
	
	import mx.controls.Menu;
	import mx.events.CollectionEvent;
	import mx.events.MenuEvent;
	
	import org.yass.Yass;
	import org.yass.main.model.TextFilterScope;
	import org.yass.main.view.component.SearchField;
	
	public class SearchView extends EventDispatcher{
		private var component:SearchField;
		
		private var _keyBuff:Boolean=false;
		private var _tmpString:String;
		private var _timeOut:int=100
		private var _scopeLabel:String = "Search"
		private var searchMenuData:Array = [
		    {label: "Search", enabled: "false"},
		    {label: TextFilterScope.ALL, type: "radio", groupName: "g1", toggled: true},
		    {label: TextFilterScope.ARTISTS, type: "radio", groupName: "g1"}, 
		    {label: TextFilterScope.ALBUMS, type: "radio", groupName: "g1"}, 
		    {label: TextFilterScope.TITLE, type: "radio", groupName: "g1"} 
		    ]; 

		public function SearchView(_component:SearchField):void{
			this.component = _component;
			component.dropDownMenu.addEventListener(MouseEvent.CLICK, onDropDownRollover);
			component.searchInput.addEventListener(Event.CHANGE, onChange);
			component.resetSearch.addEventListener(MouseEvent.CLICK, onClickReset);
		}
		private function onClickReset(evt:Event):void{
			component.searchInput.text = "";
			onChange(evt);
		}
		private function onDropDownRollover(evt:MouseEvent):void {
			var searchMenu:Menu = Menu.createMenu(null, searchMenuData, true);
			searchMenu.addEventListener(MenuEvent.ITEM_CLICK, searchMenuClick);
		    searchMenu.show(evt.stageX - evt.localX,evt.stageY - evt.localY);
		}

		private function searchMenuClick(event:MenuEvent):void{
			var searchSentence:String = "Search";
			switch(event.item.label){
			case TextFilterScope.ALL :
				_scopeLabel = "Search";
				break;
			case TextFilterScope.ARTISTS :
				_scopeLabel = "Search an artist";
				break;
			case TextFilterScope.ALBUMS :
				_scopeLabel = "Search an album";
				break;
			case TextFilterScope.TITLE :
				_scopeLabel = "Search a title";
				break;
			}
			Yass.library.textFilterScope = event.item.label;
			computeScopeLabel();
		}
		private function onChange(evt:Event):void{
			if(!_keyBuff){
				_tmpString = component.searchInput.text
				_keyBuff = true
				setTimeout(timeoutFunction , _timeOut)
			}
		}
		private function timeoutFunction():void{
			if(component.searchInput.text != _tmpString){
				_tmpString = component.searchInput.text
				setTimeout(timeoutFunction , _timeOut)
			}
			else{
				Yass.library.filteredText = component.searchInput.text;
				component.resetSearch.visible = component.searchInput.text != "";
				_keyBuff = false
				computeScopeLabel();
			}
		}
		private function computeScopeLabel():void{
			if(component.searchInput.text.length !=0 && Yass.library.source.length > Yass.library.length){
				onCollectionUpdate(null);
				Yass.library.addEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionUpdate)
			}
			else{
				Yass.library.removeEventListener(CollectionEvent.COLLECTION_CHANGE, onCollectionUpdate)
				component.labelKeywordsScope.text=_scopeLabel
			}
		}
		private function onCollectionUpdate(evt:Event):void{
			component.labelKeywordsScope.text= "Found " + Yass.library.length + " tracks on " +Yass.library.source.length + " in library"
		}
	}
}