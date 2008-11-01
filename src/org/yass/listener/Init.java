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
package org.yass.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.User;
import org.yass.util.LibraryScanner;

/**
 * Servlet implementation class for Servlet: Init
 * 
 */
public class Init implements ServletContextListener, YassConstants {

	private static Log LOG = LogFactory.getLog(Init.class);
	static final long serialVersionUID = 1L;
	private Thread initThread;

	/*
	 * (non-Java-doc)
	 * 
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public Init() {
		super();
	}

	public void contextDestroyed(final ServletContextEvent event) {
		initThread.interrupt();
	}

	/**
	 * This method will create an instance of the {@link IndexManager} and set it
	 * in the ServletContext
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void contextInitialized(final ServletContextEvent event) {
		final ServletContext servletContext = event.getServletContext();
		final String trackroot = servletContext.getInitParameter("org.yass.mediaFilesRoot");
		boolean rescanOnStartup = servletContext.getInitParameter("org.yass.rescanOnStartup") != null
				&& servletContext.getInitParameter("org.yass.rescanOnStartup").equals("true");
		try {
			LOG.info("Yass Initalization phase starting...");
			final User user = USER_DAO.getFromId(1);
			Library library = LIBRARY_DAO.getFromUserId(user.getId());
			if (library == null) {
				LOG.warn("No Library found, creating new one");
				library = new Library();
				library.setPath(trackroot);
				user.setLibrary(library);
				LIBRARY_DAO.save(library);
				rescanOnStartup = true;
			}
			servletContext.setAttribute(ALL_LIBRARY, library);
			if (rescanOnStartup) {
				final LibraryScanner scanner = new LibraryScanner(library);
				(initThread = new Thread(scanner, "Yass-LibraryScanner:" + library.getId())).start();
			}
			servletContext.setAttribute(USER, user);
			LOG.info("Initalization phase over");
		} catch (final Exception e) {
			LOG.fatal("Error during Yass initialization", e);
		}
	}
}
