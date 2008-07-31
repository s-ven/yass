package org.yass.listener;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.yass.YassConstants;
import org.yass.dao.LibraryDao;
import org.yass.dao.PlayListDao;
import org.yass.dao.TrackStatDao;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;
import org.yass.id3.MetadataReader;

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
			LOG.info("Loading Library from DB");
			final LibraryDao libDao = new LibraryDao();
			Library lib = libDao.getFromId(1);
			if (lib == null) {
				LOG.info("No Library found, creating new one");
				libDao.saveLibrary(lib = new Library(0, trackroot, new Date()));
			}
			servletContext.setAttribute(ALL_LIBRARY, lib);
			final Runnable runnable = new Runnable() {

				public void run() {
					LOG.info("Scanning library path to add new media");
					new MetadataReader().scanLibrary((Library) servletContext.getAttribute(ALL_LIBRARY));
				}
			};
			(initThread = new Thread(runnable)).start();
			servletContext.setAttribute(USER_PLAYLISTS, new PlayListDao().getFromUserId(1));
			servletContext.setAttribute(USER_TRACK_STATS, new TrackStatDao().getFromUserId(1));
		} catch (final Exception e) {
			LOG.fatal("Error in Yass init", e);
		}
	}

	// TODO :: LA METHODE DE LA MORT, PLUS LONGUE TU MEURS, ABSOLUMENT POURRI, A
	// REFAIRE
	public final static Document buildXMLDoc(final Library pl) {
		Document doc = null;
		try {
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.newDocument();
			final Element treeNode = doc.createElement("libTree");
			TrackInfo artist;
			TrackInfo album;
			TrackInfo genre;
			for (final Track track : pl.getTracks()) {
				boolean exists = false;
				final NodeList genreList = treeNode.getChildNodes();
				Element albNode;
				Element artistNode;
				Element genreNode;
				artist = track.getTrackInfo(ARTIST);
				album = track.getTrackInfo(ALBUM);
				genre = track.getTrackInfo(GENRE);
				for (int i = 0; i < genreList.getLength(); i++) {
					genreNode = (Element) genreList.item(i);
					if (genre != null && genreNode.getAttribute("value").equals(genre.getValue())) {
						// If this genre already exists, will populate the corresponding
						// node
						// with
						final NodeList artistLst = genreNode.getChildNodes();
						for (int j = 0; j < artistLst.getLength(); j++) {
							artistNode = (Element) artistLst.item(j);
							if (artistNode.getAttribute("value").equals(artist.getValue())) {
								// If this artist already exists, will populate the
								// corresponding
								// node with
								final NodeList albLst = artistNode.getChildNodes();
								for (int k = 0; k < albLst.getLength(); k++)
									if (((Element) albLst.item(k)).getAttribute("value").equals(album.getValue())) {
										exists = true;
										break;
									}
								if (!exists) {
									albNode = makeNodeFromProp(doc, album);
									artistNode.appendChild(albNode);
									exists = true;
								}
							}
							if (exists)
								break;
						}
						if (!exists) {
							artistNode = makeNodeFromProp(doc, artist);
							albNode = makeNodeFromProp(doc, album);
							artistNode.appendChild(albNode);
							genreNode.appendChild(artistNode);
							exists = true;
						}
					}
					if (exists)
						break;
				}
				if (genre != null && !exists) {
					artistNode = makeNodeFromProp(doc, artist);
					albNode = makeNodeFromProp(doc, album);
					genreNode = makeNodeFromProp(doc, genre);
					artistNode.appendChild(albNode);
					genreNode.appendChild(artistNode);
					treeNode.appendChild(genreNode);
				}
			}
			doc.appendChild(treeNode);
		} catch (final Exception e) {
			LOG.trace("Error in Library TrackInfo XML creation", e);
		}
		return doc;
	}

	private final static Element makeNodeFromProp(final Document doc, final TrackInfo album) {
		Element albNode;
		albNode = doc.createElement("node");
		albNode.setAttribute("id", "" + album.getId());
		albNode.setAttribute("value", album.getValue());
		albNode.setAttribute("type", album.getType());
		return albNode;
	}

	public void contextDestroyed(final ServletContextEvent event) {
		initThread.interrupt();
	}
}
