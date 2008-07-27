package org.yass.main.loaders
{
	import flash.net.URLLoader;
	import flash.net.URLRequest;

	public class LibraryLoader extends URLLoader	{
		private var urlReq:URLRequest = new URLRequest("/yass/library_browse.do");
		public function LibraryLoader() :void		{
			super();
		}
		public function loadData(){
			load(urlReq);
		}
		
	}
}