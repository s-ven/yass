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
package org.yass.security;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.YassConstants;
import org.yass.domain.User;

/**
 * @author Sven Duzont
 * 
 */
public class YaasLoginModule implements LoginModule, YassConstants {

	private static Log LOG = LogFactory.getLog(YaasLoginModule.class);
	private CallbackHandler callbackHandler;
	private Subject subject;
	private User user;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		user = null;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		switch (user.getRoleId()) {
		case 0:
			subject.getPrincipals().add(new AdminPrincipal());
			subject.getPrincipals().add(new UserPrincipal());
			break;
		default:
			subject.getPrincipals().add(new UserPrincipal());
			break;
		}
		// TODO fill in roles from database
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject,
	 * javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	public void initialize(final Subject subject, final CallbackHandler callbackHandler,
			final Map<String, ?> sharedState, final Map<String, ?> options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		final NameCallback nameCB = new NameCallback("Username");
		final PasswordCallback passwordCB = new PasswordCallback("Password", false);
		// Delegate to the provided CallbackHandler to gather the
		// username and password.
		try {
			callbackHandler.handle(new Callback[] { nameCB, passwordCB });
		} catch (final IOException e) {
			LOG.error("Error while login", e);
			throw (LoginException) new LoginException("IOException caught while logging in.").initCause(e);
		} catch (final UnsupportedCallbackException e) {
			throw (LoginException) new LoginException(e.getCallback().getClass().getName() + " is not a supported Callback.")
					.initCause(e);
		}
		return (user = USER_DAO.findByNamePassword(nameCB.getName(), String.valueOf(passwordCB.getPassword()))) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		user = null;
		return true;
	}
}
