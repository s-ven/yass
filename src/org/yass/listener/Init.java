package org.yass.listener;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.lucene.FilePlayList;
import org.yass.lucene.IndexManager;

/**
 * Servlet implementation class for Servlet: Init
 * 
 */
public class Init implements ServletContextListener, YassConstants {

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
		initThread = new Thread(new Runnable() {

			public void run() {
				final IndexManager mib = new IndexManager(servletContext.getInitParameter("org.yass.mediaFilesRoot"),
						servletContext.getInitParameter("org.yass.metadataIndexRoot"));
				mib.setMediaFilesExtensions(servletContext.getInitParameter("org.yass.mediaFilesExtensions"));
				servletContext.setAttribute(INDEX_MANAGER, mib);
				mib.createIndex();
				// Loads user playlists
				final HashMap<String, PlayList> userPl = getUserPlayLists(mib);
				final File plRoot = new File("PLAYLISTS/user");
				if (!plRoot.exists())
					plRoot.mkdirs();
				for (final File plFile : plRoot.listFiles(new FileFilter() {

					public boolean accept(final File pathname) {
						return pathname.getName().endsWith(".txt");
					}
				})) {
					final FilePlayList fpl = new FilePlayList(plFile, mib);
					userPl.put(fpl.id, fpl);
				}
				servletContext.setAttribute(USER_PLAYLISTS, userPl);
			}

			private HashMap<String, PlayList> getUserPlayLists(final IndexManager mib) {
				final HashMap<String, PlayList> userPl = new LinkedHashMap<String, PlayList>();
				return userPl;
			}
		});
		initThread.setDaemon(true);
		initThread.setPriority(Thread.MIN_PRIORITY);
		initThread.start();
	}

	public void contextDestroyed(final ServletContextEvent event) {
		initThread.interrupt();
	}
}
