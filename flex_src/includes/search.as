import mx.controls.Menu;
import mx.events.MenuEvent;

private var keywordsSearchField:String = "ALL";
public function doSearch():void {
	genre.search(searchTextInput.text, keywordsSearchField);
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
    ];