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
package org.yass.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.restlet.Context;
import org.restlet.ext.jaxrs.JaxRsApplication;

/**
 * @author Sven Duzont
 * 
 */
public class YassApplication extends JaxRsApplication {

	/**
	 * @param context
	 */
	public YassApplication() {
		this.initApplication();
	}

	/**
	 * @param context
	 */
	public YassApplication(final Context context) {
		super(context);
		this.initApplication();
	}

	/**
	 * 
	 */
	private void initApplication() {
		this.add(new Application() {

			@Override
			public Set<Class<?>> getClasses() {
				final Set<Class<?>> classes = new HashSet<Class<?>>();
				classes.add(SettingsResource.class);
				classes.add(TracksResource.class);
				classes.add(TrackResource.class);
				classes.add(PlaylistsResource.class);
				classes.add(PlaylistResource.class);
				classes.add(TrackInfosResource.class);
				return classes;
			}
		});
	}
}
