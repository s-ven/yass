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