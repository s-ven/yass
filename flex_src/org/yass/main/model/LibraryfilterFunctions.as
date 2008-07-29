package org.yass.main.model{
	import org.yass.Yass;

	public class LibraryfilterFunctions{
		public function getFilterFunction(genres:Array, artists:Array, albums:Array, subFunction:Function=null, txtArray:Array=null):Function{
			if(subFunction != null){
				if(genres.length !=0 && albums.length ==0 && artists.length ==0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) && genres.lastIndexOf(row.genre) != -1

					};
				else if(genres.length !=0 && albums.length !=0 && artists.length ==0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) && genres.lastIndexOf(row.genre) != -1 &&
									albums.lastIndexOf(row.album) != -1
					};
				else if(genres.length !=0 && albums.length !=0 && artists.length !=0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) && genres.lastIndexOf(row.genre) != -1 &&
								albums.lastIndexOf(row.album) != -1 &&
								artists.lastIndexOf(row.artist) != -1
					};
				else if(genres.length ==0 && albums.length !=0 && artists.length !=0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) && albums.lastIndexOf(row.album) != -1 &&
								artists.lastIndexOf(row.artist) != -1
					};
				else if(genres.length ==0 && albums.length ==0 && artists.length !=0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) && artists.lastIndexOf(row.artist) != -1
					};
				else if(genres.length ==0 && albums.length !=0 && artists.length ==0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) && albums.lastIndexOf(row.album) != -1
					};
				else if(genres.length !=0 && albums.length ==0
				&& artists.length !=0)
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row) &&  genres.lastIndexOf(row.genre) != -1 &&
								artists.lastIndexOf(row.artist) != -1
					};
				else
					return function(row:Object):Boolean{
						return txtArray.every(subFunction, row)
					};
			}
			else{
				if(genres.length !=0 && albums.length ==0 && artists.length ==0)
					return function(row:Object):Boolean{
						return genres.lastIndexOf(row.genre) != -1
					};
				else if(genres.length !=0 && albums.length !=0 && artists.length ==0)
					return function(row:Object):Boolean{
						return genres.lastIndexOf(row.genre) != -1 &&
								albums.lastIndexOf(row.album) != -1
					};
				else if(genres.length !=0 && albums.length !=0 &&
				artists.length !=0)
					return function(row:Object):Boolean{
						return genres.lastIndexOf(row.genre) != -1 &&
								albums.lastIndexOf(row.album) != -1 &&
								artists.lastIndexOf(row.artist) != -1
					};
				else if(genres.length ==0 && albums.length !=0 && artists.length !=0)
					return function(row:Object):Boolean{
						return albums.lastIndexOf(row.album) != -1 &&
										artists.lastIndexOf(row.artist) != -1
					};
				else if(genres.length ==0 && albums.length ==0 && artists.length !=0)
					return function(row:Object):Boolean{
						return artists.lastIndexOf(row.artist) != -1
					};
				else if(genres.length ==0 && albums.length !=0 && artists.length ==0)
					return function(row:Object):Boolean{
						return albums.lastIndexOf(row.album) != -1
					};
				else if(genres.length !=0 && albums.length ==0 && artists.length !=0)
					return function(row:Object):Boolean{
						return genres.lastIndexOf(row.genre) != -1 &&
						 	artists.lastIndexOf(row.artist) != -1
					};
				else
					return null;
			}
		}
	}
}