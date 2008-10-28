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

	/**
	 * This method will create an instance of the {@link IndexManager} and set it
	 * in the ServletContext
	 * 
	 * @see javax.servlet.GenericServlet#init()
	 */
	public void contextInitialized(final ServletContextEvent event) {
		final ServletContext servletContext = event.getServletContext();
		final String trackroot = servletContext.getInitParameter("org.yass.mediaFilesRoot");
		try {
			LOG.info("Yass Initalization phase starting...");
			final User user = YASS_USER_DAO.getFromId(1);
			Library library = LIBRARY_DAO.getFromUserId(user.getId());
			if (library == null) {
				LOG.warn("No Library found, creating new one");
				library = new Library();
				library.setPath(trackroot);
				user.setLibrary(library);
				LIBRARY_DAO.save(library);
			}
			servletContext.setAttribute(ALL_LIBRARY, library);
			final LibraryScanner scanner = new LibraryScanner(library);
			(initThread = new Thread(scanner, "Yass-LibraryScanner:" + library.getId())).start();
			servletContext.setAttribute(USER, user);
			servletContext.setAttribute(USER_PLAYLISTS, PLAYLIST_DAO.getFromUserId(user.getId()));
			servletContext.setAttribute(USER_TRACK_STATS, TRACK_STAT_DAO.getFromUserId(user.getId()));
			LOG.info("Initalization phase over");
		} catch (final Exception e) {
			LOG.fatal("Error during Yass initialization", e);
		}
	}

	public void contextDestroyed(final ServletContextEvent event) {
		initThread.interrupt();
	}
}
