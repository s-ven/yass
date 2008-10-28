package org.yass.struts.library;

import java.util.Collection;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.yass.YassConstants;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;
import org.yass.struts.YassAction;

public class GetTree extends YassAction implements YassConstants {

	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		LOG.info("Library trackInfos requested");
		try {
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element treeNode = doc.createElement("libTree");
			final Collection<Track> tracks = getLibrary().getTracks();
			for (final Track track : tracks) {
				boolean exists = false;
				final NodeList genreList = treeNode.getChildNodes();
				final TrackInfo genre = track.getTrackInfo(GENRE);
				final TrackInfo artist = track.getTrackInfo(ARTIST);
				final TrackInfo album = track.getTrackInfo(ALBUM);
				for (int i = 0; i < genreList.getLength(); i++) {
					final Element genreNode = (Element) genreList.item(i);
					if (genre != null && genreNode.getAttribute("value").equals(genre.getValue())) {
						// If this genre already exists, will populate the corresponding
						// node with
						final NodeList artistLst = genreNode.getChildNodes();
						for (int j = 0; j < artistLst.getLength(); j++) {
							final Element artistNode = (Element) artistLst.item(j);
							if (artistNode.getAttribute("value").equals(artist.getValue())) {
								// If this artist already exists, will populate the
								// corresponding node with
								final NodeList albLst = artistNode.getChildNodes();
								for (int k = 0; k < albLst.getLength(); k++)
									if (((Element) albLst.item(k)).getAttribute("value").equals(album.getValue())) {
										exists = true;
										break;
									}
								if (!exists) {
									artistNode.appendChild(makeNodeFromProp(doc, album));
									exists = true;
								}
							}
							if (exists)
								break;
						}
						if (!exists) {
							genreNode.appendChild(makeNodeFromProp(doc, artist)).appendChild(makeNodeFromProp(doc, album));
							exists = true;
						}
					}
					if (exists)
						break;
				}
				if (genre != null && !exists)
					treeNode.appendChild(makeNodeFromProp(doc, genre)).appendChild(makeNodeFromProp(doc, artist)).appendChild(
							makeNodeFromProp(doc, album));
			}
			doc.appendChild(treeNode);
			return outputDocument(doc);
		} catch (final Exception e) {
			LOG.fatal("Error during Library TrackInfo XML creation", e);
		}
		return NONE;
	}

	private final static Element makeNodeFromProp(final Document doc, final TrackInfo trackInfo) {
		final Element node = doc.createElement(trackInfo.getType());
		node.setAttribute("id", "" + trackInfo.getId());
		node.setAttribute("value", trackInfo.getValue());
		// If the trackIngo is an album, will try to check if it have an attached
		// picture in the database
		if (trackInfo.getType().equals(ALBUM))
			node.setAttribute("hasPicture", ATTACHED_PICTURE_DAO.hasPicture(trackInfo.getId()) + "");
		return node;
	}
}
