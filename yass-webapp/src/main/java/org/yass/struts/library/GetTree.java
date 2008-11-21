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
	public int userId;

	@Override
	public String execute() {
		LOG.info("Getting Library TrackInfos");
		try {
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element treeNode = doc.createElement("libTree");
			final Collection<Track> tracks = USER_DAO.findById(userId).getLibrary().getTracks();
			for (final Track track : tracks)
				feedGenre(treeNode, track.getTrackInfo(GENRE), track.getTrackInfo(ARTIST), track.getTrackInfo(ALBUM));
			doc.appendChild(treeNode);
			return outputDocument(doc);
		} catch (final Exception e) {
			LOG.fatal("Error during Library TrackInfo XML creation", e);
		}
		return NONE;
	}

	/**
	 * @param doc
	 * @param album
	 * @param artistNode
	 * @return
	 */
	private boolean feedAlbum(final TrackInfo album, final Element artistNode) {
		final NodeList albLst = artistNode.getChildNodes();
		for (int k = 0; k < albLst.getLength(); k++)
			if (isNodeValue(album, (Element) albLst.item(k)))
				return true;
		artistNode.appendChild(album.toXMLElement(artistNode.getOwnerDocument()));
		return true;
	}

	/**
	 * @param doc
	 * @param exists
	 * @param artist
	 * @param album
	 * @param genreNode
	 * @return
	 */
	private boolean feedArtist(final TrackInfo artist, final TrackInfo album, final Element genreNode) {
		final NodeList artistLst = genreNode.getChildNodes();
		for (int j = 0; j < artistLst.getLength(); j++) {
			final Element artistNode = (Element) artistLst.item(j);
			if (isNodeValue(artist, artistNode) && feedAlbum(album, artistNode))
				return true;
		}
		final Document doc = genreNode.getOwnerDocument();
		genreNode.appendChild(artist.toXMLElement(doc)).appendChild(album.toXMLElement(doc));
		return true;
	}

	/**
	 * @param doc
	 * @param treeNode
	 * @param exists
	 * @param genre
	 * @param artist
	 * @param album
	 */
	private void feedGenre(final Element treeNode, final TrackInfo genre, final TrackInfo artist, final TrackInfo album) {
		final NodeList genreList = treeNode.getChildNodes();
		for (int i = 0; i < genreList.getLength(); i++) {
			final Element genreNode = (Element) genreList.item(i);
			if (genre != null && isNodeValue(genre, genreNode) && feedArtist(artist, album, genreNode))
				return;
		}
		if (genre != null) {
			final Document doc = treeNode.getOwnerDocument();
			treeNode.appendChild(genre.toXMLElement(doc)).appendChild(artist.toXMLElement(doc)).appendChild(
					album.toXMLElement(doc));
		}
	}

	/**
	 * @param artist
	 * @param artistNode
	 * @return
	 */
	private boolean isNodeValue(final TrackInfo artist, final Element artistNode) {
		return artistNode.getAttribute("value").equals(artist.getValue());
	}
}