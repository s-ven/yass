package org.yass.listener;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.domain.Track;
import org.yass.domain.TrackProperty;
import org.yass.lucene.Constants;
import org.yass.lucene.FilePlayList;
import org.yass.lucene.IndexManager;
import org.yass.lucene.SearchQuery;

/**
 * Servlet implementation class for Servlet: Init
 * 
 */
public class Init implements ServletContextListener, YassConstants, Constants {

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
				try {
					final PlayList plst = mib.search(new SearchQuery());
					servletContext.setAttribute(ALL_LIBRARY, plst);
					servletContext.setAttribute(LIB_XML_TREE, buildXMLDoc(plst));
				} catch (final IOException e) {
					e.printStackTrace();
				}
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

	// TODO :: LA METHODE DE LA MORT, PLUS LONGUE TU MEURS, ABSOLUMENT POURRI, A
	// REFAIRE
	private Document buildXMLDoc(final PlayList pl) {
		Document doc = null;
		try {
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = builder.newDocument();
			final Element treeNode = doc.createElement("libTree");
			TrackProperty artist;
			TrackProperty album;
			TrackProperty genre;
			for (final Track track : pl.getMediaFiles()) {
				boolean exists = false;
				final NodeList genreList = treeNode.getChildNodes();
				Element albNode;
				Element artistNode;
				Element genreNode;
				artist = track.getProperty(ARTIST);
				album = track.getProperty(ALBUM);
				genre = track.getProperty(GENRE);
				for (int i = 0; i < genreList.getLength(); i++) {
					genreNode = (Element) genreList.item(i);
					if (genreNode.getAttribute("value").equals(genre.value)) {
						// If this genre already exists, will populate the corresponding
						// node
						// with
						final NodeList artistLst = genreNode.getChildNodes();
						for (int j = 0; j < artistLst.getLength(); j++) {
							artistNode = (Element) artistLst.item(j);
							if (artistNode.getAttribute("value").equals(artist.value)) {
								// If this artist already exists, will populate the
								// corresponding
								// node with
								final NodeList albLst = artistNode.getChildNodes();
								for (int k = 0; k < albLst.getLength(); k++)
									if (((Element) albLst.item(k)).getAttribute("value").equals(album.value)) {
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
				if (!exists) {
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
			e.printStackTrace(System.err);
		}
		return doc;
	}

	private Element makeNodeFromProp(final Document doc, final TrackProperty album) {
		Element albNode;
		albNode = doc.createElement("node");
		albNode.setAttribute("id", "" + album.id);
		albNode.setAttribute("value", album.value);
		albNode.setAttribute("type", album.type);
		return albNode;
	}

	public void contextDestroyed(final ServletContextEvent event) {
		initThread.interrupt();
	}
}
