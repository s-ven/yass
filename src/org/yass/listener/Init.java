package org.yass.listener;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.valuepair.GenreTypes;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.lucene.Constants;
import org.yass.lucene.FilePlayList;
import org.yass.lucene.IndexManager;
import org.yass.lucene.SearchQuery;
import org.yass.util.FileUtils;

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
					servletContext.setAttribute(ALL_LIBRARY, mib.search(new SearchQuery()));
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
	public static void main(final String[] args) throws Exception {
		final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		final Document doc = builder.newDocument();
		final Element treeNode = doc.createElement("libTree");
		final Collection<File> mp3Files = FileUtils.getFiles(new File("/Volumes/Arc - 1/Music"), FileUtils
				.getExtensionFilter("mp3"));
		final List<String> genres = new ArrayList<String>();
		final List<String> artists = new ArrayList<String>();
		final List<String> albums = new ArrayList<String>();
		for (final File file : mp3Files)
			try {
				final AudioFile audioFile = new MP3FileReader().read(file);
				final Tag mp3Tag = audioFile.getTag();
				String artist = mp3Tag.getFirstArtist().trim();
				if ("".equals(artist))
					artist = UNKNOWN_ARTIST;
				String album = mp3Tag.getFirstAlbum().trim();
				if ("".equals(album))
					album = UNKNOWN_ALBUM;
				String genre = mp3Tag.getFirstGenre().trim();
				if (genre.startsWith("("))
					genre = GenreTypes.getInstanceOf().getValueForId(Integer.parseInt(genre.substring(1, genre.indexOf(')'))))
							.trim();
				if ("".equals(genre))
					genre = UNKNOWN_GENRE;
				boolean exists = false;
				final NodeList genreList = treeNode.getChildNodes();
				genres.add(genre);
				artists.add(artist);
				albums.add(album);
				Element albNode;
				Element artistNode;
				Element genreNode;
				for (int i = 0; i < genreList.getLength(); i++) {
					genreNode = (Element) genreList.item(i);
					if (genreNode.getAttribute("name").equals(genre)) {
						// If this genre already exists, will populate the corresponding
						// node
						// with
						final NodeList artistLst = genreNode.getChildNodes();
						for (int j = 0; j < artistLst.getLength(); j++) {
							artistNode = (Element) artistLst.item(j);
							if (artistNode.getAttribute("name").equals(artist)) {
								// If this artist already exists, will populate the
								// corresponding
								// node with
								final NodeList albLst = artistNode.getChildNodes();
								for (int k = 0; k < albLst.getLength(); k++)
									if (((Element) albLst.item(k)).getAttribute("name").equals(album)) {
										exists = true;
										break;
									}
								if (!exists) {
									albNode = doc.createElement("node");
									albNode.setAttribute("id", "" + albums.indexOf(album));
									albNode.setAttribute("name", album);
									albNode.setAttribute("type", "album");
									artistNode.appendChild(albNode);
									System.out.println(artistNode.getChildNodes().getLength());
									exists = true;
								}
							}
							if (exists)
								break;
						}
						if (!exists) {
							artistNode = doc.createElement("node");
							artistNode.setAttribute("id", "" + artists.indexOf(artist));
							artistNode.setAttribute("name", artist);
							artistNode.setAttribute("type", "artist");
							albNode = doc.createElement("node");
							albNode.setAttribute("id", "" + albums.indexOf(album));
							albNode.setAttribute("name", album);
							albNode.setAttribute("type", "album");
							artistNode.appendChild(albNode);
							genreNode.appendChild(artistNode);
							exists = true;
						}
					}
					if (exists)
						break;
				}
				if (!exists) {
					artistNode = doc.createElement("node");
					artistNode.setAttribute("id", "" + artists.indexOf(artist));
					artistNode.setAttribute("name", artist);
					artistNode.setAttribute("type", "artist");
					albNode = doc.createElement("node");
					albNode.setAttribute("id", "" + albums.indexOf(album));
					albNode.setAttribute("name", album);
					albNode.setAttribute("type", "album");
					genreNode = doc.createElement("node");
					genreNode.setAttribute("id", "" + genres.indexOf(genre));
					genreNode.setAttribute("name", genre);
					genreNode.setAttribute("type", "genre");
					artistNode.appendChild(albNode);
					genreNode.appendChild(artistNode);
					treeNode.appendChild(genreNode);
				}
			} catch (final Exception e) {
			}
		System.out.println(treeNode.getChildNodes().getLength());
		doc.appendChild(treeNode);
		System.out.println(doc.getTextContent());
		final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
		final DOMImplementation domImpl = registry.getDOMImplementation("LS 3.0");
		final DOMImplementationLS implLS = (DOMImplementationLS) domImpl;
		final LSSerializer dom3Writer = implLS.createLSSerializer();
		final LSOutput output = implLS.createLSOutput();
		final OutputStream outputStream = new FileOutputStream(new File("output.xml"));
		output.setByteStream(outputStream);
		output.setEncoding("UTF-8");
		dom3Writer.getDomConfig().setParameter("xml-declaration", false);
		dom3Writer.write(doc, output);
	}

	public void contextDestroyed(final ServletContextEvent event) {
		initThread.interrupt();
	}
}
