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

import java.util.HashMap;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

/**
 * @author Sven Duzont
 * 
 */
public class SecurityConfiguration extends Configuration {

    /**
     *
     */
    static public void init() {
		Configuration.setConfiguration(new SecurityConfiguration());
	}

	@SuppressWarnings("unchecked")
	private final AppConfigurationEntry[] appConfigurationEntry = { new AppConfigurationEntry(
			"org.yass.security.YassLoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, new HashMap()) };

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.security.auth.login.Configuration#getAppConfigurationEntry(java.lang
	 * .String)
	 */
	@Override
	public AppConfigurationEntry[] getAppConfigurationEntry(final String appName) {
		return appConfigurationEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.login.Configuration#refresh()
	 */
	@Override
	public void refresh() {
	}
}
