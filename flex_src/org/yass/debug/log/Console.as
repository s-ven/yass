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
package org.yass.debug.log {

	import flash.external.ExternalInterface;
	/**
	 * Firebug adds a global variable named "console" to all web pages loaded in Firefox.
	 * This object contains many methods that allow you to write to the Firebug console to expose information that is flowing through your scripts.
	 * */

	public class Console {
		/**
		 * Writes a message to the console.
		 * You may pass as many arguments as you'd like, and they will be
		 * joined together in a space-delimited line.
		 * */
		public static function log(...rest):void{
			send("log",rest);
		}
		/**
		 * Writes a message to the console, including a hyperlink to the
		 * line where it was called.
		 * */
		public static function debug(...rest):void{
			send("debug",rest);
		}
		/**
		 * Writes a message to the console with the visual "warning" icon and
		 * color coding and a hyperlink to the line where it was called.
		 * */
		public static function warn(...rest):void{
			send("warn",rest);
		}
		/**
		 * Writes a message to the console with the visual "info" icon and
		 * color coding and a hyperlink to the line where it was called.
		 * */
		public static function info(...rest):void{
			send("info",rest);
		}
		/**
		 * Writes a message to the console with the visual "error" icon and
		 * color coding and a hyperlink to the line where it was called.
		 * */
		public static function error(...rest):void{
			send("error",rest);
		}
		/**
		 * Tests that an expression is true.
		 * If not, it will write a message to the console and throw an exception.
		 * */
		public static function assert(...rest):void{
			send("assert",rest);
		}
		/**
		 * 	Prints an interactive listing of all properties of the object.
		 * 	This looks identical to the view that you would see in the DOM tab.
		 * */
		public static function dir(object:Object):void{
		  send("dir", [object]);
		}
		/**
		 * 	Prints the XML source tree of an HTML or XML element.
		 * 	This looks identical to the view that you would see in the HTML tab.
		 * 	You can click on any node to inspect it in the HTML tab.
		 * */
		public static function dirxml(object:XML):void{
		  send("dirxml", [object]);
		}
		/**
		 * Prints an interactive stack trace of JavaScript execution at the
		 * point where it is called.
		 * The stack trace details the functions on the stack, as well as the values
		 * that were passed as arguments to each function. You can click each function
		 * to take you to its source in the Script tab, and click each argument value
		 * to inspect it in the DOM or HTML tabs.
		 * */
		public static function printStack():void{
		  send("trace");
		}
		/**
		* Writes a message to the console and opens a nested block to indent all future messages sent to the console.
		* Call console.groupEnd() to close the block.
		* */
		public static function group(...rest):void{
		  send("group", rest);
		}
		/**
		 * Closes the most recently opened block created by a call to console.group.
		 * */
		public static function groupEnd():void{
		  send("groupEnd", []);
		}
		/**
		 * Creates a new timer under the given name.
		 * Call console.timeEnd(name) with the same name to stop the timer and print the time elapsed..
		 * */
		public static function time(name:String):void{
		  send("time", [name]);
		}
		/**
		 * Stops a timer created by a call to console.time(name) and writes the time elapsed.
		 * */
		public static function timeEnd(name:String):void{
		  send("timeEnd", [name]);
		}
		/**
		 * Turns on the JavaScript profiler.
		 * The optional argument title would contain the text to be printed in the header of the profile report.
		 * */
		public static function profile(title:String):void{
		  send("profile", [title]);
		}
		/**
		 * Turns off the JavaScript profiler and prints its report.
		 * */
		public static function profileEnd():void{
		  send("profileEnd", []);
		}
		/**
		 * Writes the number of times that the line of code where count was called was executed.
		 * The optional argument title will print a message in addition to the number of the count.
		 * */
		public static function count(name:String=null):void{
		  send("count", name ? [name] : []);
		}
		/**
		* makes the call to console
		* */
		private static function send(level:String,...rest):void{
			try{
				ExternalInterface.call("console."+level,rest[0]);

			}catch(e:Error){
			   trace();

			}
		}
	}
}