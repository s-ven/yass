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
import mx.controls.Menu;
import mx.events.MenuEvent;

private var keywordsSearchField:String = "ALL";
/* public function doSearch():void {
	mainPane.libraryBrowser.genre.search(searchTextInput.text, keywordsSearchField);
}
 
private function showSearchMenu(x:int, y:int):void {
	var searchMenu:Menu = Menu.createMenu(null, searchMenuData, true);
	searchMenu.addEventListener(MenuEvent.ITEM_CLICK, searchMenuClick);
    searchMenu.show(x,y);
}

private function searchMenuClick(event:MenuEvent):void{
	var searchSentence:String = "Search";
	switch(event.item.label){
	case "All" :
		keywordsSearchField = "ALL";
		labelKeywordsScope.text = "Search";
	break;
	case "Artist" :
		keywordsSearchField = "ARTIST";
		labelKeywordsScope.text = "Search an artist";
	break;
	case "Album" :
		keywordsSearchField = "ALBUM";
		labelKeywordsScope.text = "Search an album";
	break;
	case "Title" :
		keywordsSearchField = "TITLE";
		labelKeywordsScope.text = "Search a title";
	break;
	}
}
   [Bindable] 
public var searchMenuData:Array = [
    {label: "Search", enabled: "false"},
    {label: "All", type: "radio", groupName: "g1", toggled: true},
    {label: "Artist", type: "radio", groupName: "g1"}, 
    {label: "Album", type: "radio", groupName: "g1"}, 
    {label: "Title", type: "radio", groupName: "g1"} 
    ]; */