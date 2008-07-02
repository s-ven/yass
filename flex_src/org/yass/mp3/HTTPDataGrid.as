package org.yass.mp3
{
	import mx.binding.utils.BindingUtils;
	import mx.controls.DataGrid;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;

	public class HTTPDataGrid extends DataGrid
	{
		public var httpService:HTTPService = new HTTPService();
		public function HTTPDataGrid()
		{
			super();
 			httpService.addEventListener(ResultEvent.RESULT, resultEvent);
		}
	
		protected  function resultEvent (event:ResultEvent):void{
			enabled = true;	
	}
	}
}