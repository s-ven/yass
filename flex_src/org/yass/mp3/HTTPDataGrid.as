package org.yass.mp3
{
	import mx.controls.DataGrid;
	import mx.rpc.http.HTTPService;

	public class HTTPDataGrid extends DataGrid
	{
		public var httpService:HTTPService = new HTTPService();;
		public function HTTPDataGrid()
		{
			super();
		}
		
	}
}