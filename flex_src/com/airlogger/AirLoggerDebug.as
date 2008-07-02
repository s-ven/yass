/*

 * Copyright the original author or authors.

 * 

 * Licensed under the MOZILLA PUBLIC LICENSE, Version 1.1 (the "License");

 * you may not use this file except in compliance with the License.

 * You may obtain a copy of the License at

 * 

 *      http://www.mozilla.org/MPL/MPL-1.1.html

 * 

 * Unless required by applicable law or agreed to in writing, software

 * distributed under the License is distributed on an "AS IS" BASIS,

 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

 * See the License for the specific language governing permissions and

 * limitations under the License.

 */



package com.airlogger

{

	import flash.events.SecurityErrorEvent;

	import flash.events.StatusEvent;

	import flash.net.LocalConnection;

	import flash.utils.setInterval;

	import flash.utils.clearInterval;

	import flash.utils.getQualifiedClassName;



	public class AirLoggerDebug

	{

		static public const _DEBUG : Number = 10000;

		static public const _INFO : Number = 20000;

		static public const _WARN : Number = 30000;

		static public const _ERROR : Number = 40000;

		static public const _FATAL : Number = 50000;

		

		static protected const LOCALCONNECTION_ID : String = "_AIRLOGGER_CONSOLE";

		static protected const OUT_SUFFIX : String = "_IN";

		static protected const IN_SUFFIX : String = "_OUT";

		

		static protected var ALTERNATE_ID_IN : String = "";

		

		static private var _oI : AirLoggerDebug = null;

		

		static public function getInstance () : AirLoggerDebug

		{

			if( _oI == null )

				_oI = new AirLoggerDebug ( new PrivateConstructorAccess() );

				

			return _oI;

		}

		

		static public function release () : void

		{

			_oI.close();

			_oI = null;

		}

		

		static public function debug( o : * ) : void

		{

			getInstance().log( o, _DEBUG );

		}

		static public function info( o : * ) : void

		{

			getInstance().log( o, _INFO );

		}

		static public function warn( o : * ) : void

		{

			getInstance().log( o, _WARN );

		}

		static public function error( o : * ) : void

		{

			getInstance().log( o, _ERROR );

		}

		static public function fatal( o : * ) : void

		{

			getInstance().log( o, _FATAL );

		}

		

		static public function DEBUG( o : * ) : void

		{

			getInstance().log( o, _DEBUG );

		}

		static public function INFO( o : * ) : void

		{

			getInstance().log( o, _INFO );

		}

		static public function WARN( o : * ) : void

		{

			getInstance().log( o, _WARN );

		}

		static public function ERROR( o : * ) : void

		{

			getInstance().log( o, _ERROR );

		}

		static public function FATAL( o : * ) : void

		{

			getInstance().log( o, _FATAL );

		}

		/*---------------------------------------------------------------

				INSTANCE MEMBERS

		----------------------------------------------------------------*/

		

		protected var _lcOut : LocalConnection;

		protected var _lcIn : LocalConnection;

		protected var _sID : String;

		

		protected var _bIdentified : Boolean;

		protected var _bRequesting : Boolean;

		

		protected var _aLogStack : Array;

		protected var _nPingRequest : Number;

		

		protected var _sName : String;

		

		public function AirLoggerDebug ( access : PrivateConstructorAccess )

		{

			_lcOut = new LocalConnection();

			_lcOut.addEventListener( StatusEvent.STATUS, onStatus, false, 0, true);

            _lcOut.addEventListener( SecurityErrorEvent.SECURITY_ERROR, onSecurityError, false, 0, true);

            

            _lcIn = new LocalConnection();

            _lcIn.client = this;

            _lcIn.allowDomain( "*" );

            			

			connect();

			

			_aLogStack = new Array();

	            

            _bIdentified = false;

			_bRequesting = false;

		}

		

		protected function connect () : void

		{

			var b : Boolean = true;

			

			while( b )

			{

				try

				{

		           _lcIn.connect( _getInConnectionName( ALTERNATE_ID_IN ) );

		           

		           b = false;

		           break;

				}

				catch ( e : Error )

				{

					_lcOut.send( _getOutConnectionName(), "mainConnectionAlreadyUsed", ALTERNATE_ID_IN );

					

					ALTERNATE_ID_IN += "_";

				}

			}

		}

		public function setName ( s : String ) : void

		{

			_sName = s;

			

			if( _bIdentified )

			{

				_lcOut.send( _getOutConnectionName( _sID ), "setTabName", _sName  );

			}

		}



		public function close() : void

		{

			_lcIn.close();

		}

		

		public function focus() : void

		{

			_send ( new AirLoggerEvent ( "focus" ) );

		}

		

		public function clear() : void

		{

			_send ( new AirLoggerEvent ( "clear" ) );

		}

		

		public function setID ( id : String ) : void

		{

			try

			{

				clearInterval( _nPingRequest );

				_sID = id;

				

				_lcIn.close();

				_lcIn.connect( _getInConnectionName( _sID ) );

				

				_lcOut.send( _getOutConnectionName() , "confirmID", id, _sName  );

				

				_bIdentified = true;

				_bRequesting = false;

				

				var l : Number = _aLogStack.length;

				if( l != 0 )

				{

					for(var i : Number = 0; i < l; i++ )

					{

						_send( _aLogStack.shift() as AirLoggerEvent );

					}

				}

			}

			catch ( e : Error )

			{

				_lcIn.connect( _getInConnectionName( ALTERNATE_ID_IN ) );

				

				_lcOut.send( _getOutConnectionName() , "idAlreadyUsed", id );

			}

		}

		

		public function pingRequest () : void

		{

			_lcOut.send( _getOutConnectionName() , "requestID", ALTERNATE_ID_IN  );

		}

		

		public function isRequesting () : Boolean

		{

			return _bRequesting;

		}

		

		public function isIdentified () : Boolean

		{

			return _bIdentified;

		}

		

		protected function _send ( evt : AirLoggerEvent ) : void

		{

			if( _bIdentified )

			{

				_lcOut.send( _getOutConnectionName( _sID ), evt.type, evt );

			}

			else

			{

				_aLogStack.push( evt );

				

				if( !_bRequesting )

				{					

					pingRequest();

					_nPingRequest = setInterval( pingRequest, 1000 );

					_bRequesting = true;

				}

			}

		}		

		protected function _getInConnectionName ( id : String = "" ) : String

		{

			return LOCALCONNECTION_ID + id + IN_SUFFIX;

		}

		protected function _getOutConnectionName ( id : String = "" ) : String

		{

			return LOCALCONNECTION_ID + id + OUT_SUFFIX;

		}

		

		/*---------------------------------------------------------------

				EVENT HANDLING

		----------------------------------------------------------------*/

		

		public function log( o : *, level : Number = _DEBUG ) : void

		{

			var evt : AirLoggerEvent = new AirLoggerEvent ( "log", 

															o,

															level,

															new Date(),

															getQualifiedClassName( o ) ); 

			

			_send( evt );

		}

		

		private function onStatus( event : StatusEvent ) : void 

		{

			trace("onStatus( " + event + ")");

        }



        private function onSecurityError( event : SecurityErrorEvent ) : void 

        {

			trace("onSecurityError(" + event + ")" );

        }

        

        public function toString () : String

        {

        	return "[AirLoggerDebug]";

        }

	}

}



internal class AirLoggerEvent

{

	public var type : String;

	public var message : *;

	public var level : uint;

	public var date : Date;

	public var messageType : String;	

	

	public function AirLoggerEvent( sType : String,

									message : * = null, 

									level : uint = 0, 

									date : Date = null, 

									messageType : String = null ) 

	{

		this.type = sType;

		this.message = message;

		this.level = level;

		this.date = date;

		this.messageType = messageType;

	}

}



internal class PrivateConstructorAccess {}