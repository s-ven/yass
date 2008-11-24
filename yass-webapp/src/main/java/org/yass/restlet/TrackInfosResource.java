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
package org.yass.restlet;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;

/**
 * @author Sven Duzont
 * 
 */
public class TrackInfosResource extends TracksResource {

	/**
	 * @param context
	 * @param request
	 * @param response
	 */
	public TrackInfosResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yass.restlet.BaseResource#createXMLRepresentation(org.w3c.dom.Document)
	 */
	@Override
	protected void createXMLRepresentation(final Document doc) {
		final Element treeNode = doc.createElement("libTree");
		for (final Track track : tracks)
			feedGenre(treeNode, track.getTrackInfo(GENRE), track.getTrackInfo(ARTIST), track.getTrackInfo(ALBUM));
		doc.appendChild(treeNode);
		doc.normalizeDocument();
	}

	/**
	 * @param doc
	 * @param album
	 * @param artistNode
	 * @return
	 */
	private boolean feedAlbum(final TrackInfo album, final Node artistNode) {
		final NodeList albLst = artistNode.getChildNodes();
		for (int k = 0; k < albLst.getLength(); k++)
			if (isNodeValue(album, albLst.item(k)))
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
	private boolean feedArtist(final TrackInfo artist, final TrackInfo album, final Node genreNode) {
		final NodeList artistLst = genreNode.getChildNodes();
		for (int j = 0; j < artistLst.getLength(); j++) {
			final Node artistNode = artistLst.item(j);
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
			if (isNodeValue(genre, genreNode) && feedArtist(artist, album, genreNode))
				return;
		}
		final Document doc = treeNode.getOwnerDocument();
		treeNode.appendChild(genre.toXMLElement(doc)).appendChild(artist.toXMLElement(doc)).appendChild(
				album.toXMLElement(doc));
	}

	/**
	 * @param trackInfo
	 * @param node
	 * @return
	 */
	private boolean isNodeValue(final TrackInfo trackInfo, final Node node) {
		return ((Element) node).getAttribute("value").equals(trackInfo.getValue());
	}
}
