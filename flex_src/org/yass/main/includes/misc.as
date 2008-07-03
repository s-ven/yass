import mx.formatters.DateFormatter;
    import mx.controls.dataGridClasses.DataGridColumn;
private function formatVolume(val:String):String {
    return "Volume : " + String(val)+"%";
}
private var timeFormatter:DateFormatter = new DateFormatter();
private function formatSeconds(duration:int):String{
	return dateFormatter.format(new Date(duration * 1000));
}
private function formatDurationColumn(item:Object, column:DataGridColumn):String {
	return dateFormatter.format(new Date(0, 0, 0, 0, 0, item[column.dataField], 0));  
}  
[Bindable]
private function formatPos(pos:Number):String {
	return dateFormatter.format(new Date(0, 0, 0, 0, 0, 0, pos));
}            