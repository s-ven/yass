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
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackStat;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class Browse extends YassAction implements YassConstants {

	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		try {
			LOG.info("Getting Library");
			final Library lib = (Library) ActionContext.getContext().getApplication().get(ALL_LIBRARY);
			final Collection<Track> tracks = lib.getTracks();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element libNode = doc.createElement("library");
			doc.appendChild(libNode);
			final Map<Integer, TrackStat> trackStats = (Map<Integer, TrackStat>) ActionContext.getContext().getApplication()
					.get(USER_TRACK_STATS);
			for (final Track track : tracks)
				if (track.getTrackInfo(ARTIST) != null && track.getTrackInfo(ALBUM) != null
						&& track.getTrackInfo(GENRE) != null) {
					final Element trackNode = doc.createElement("track");
					libNode.appendChild(trackNode);
					trackNode.setAttribute("id", track.getId() + "");
					trackNode.setAttribute("trackNr", track.getTrackNr() + "");
					trackNode.setAttribute("title", track.getTitle());
					trackNode.setAttribute("artist", track.getTrackInfo(ARTIST).getId() + "");
					trackNode.setAttribute("album", track.getTrackInfo(ALBUM).getId() + "");
					trackNode.setAttribute("genre", track.getTrackInfo(GENRE).getId() + "");
					if (track.getTrackInfo(YEAR) != null)
						trackNode.setAttribute("year", track.getTrackInfo(YEAR).getValue());
					if (track.getTrackInfo(BITRATE) != null)
						trackNode.setAttribute("bitrate", track.getTrackInfo(BITRATE).getValue());
					trackNode.setAttribute("vbr", track.isVBR() + "");
					trackNode.setAttribute("length", track.getLength() + "");
					trackNode.setAttribute("lastModified", track.getLastModified().getTime() + "");
					final TrackStat stat = trackStats.get(track.getId());
					if (stat != null) {
						trackNode.setAttribute("rating", stat.getRating() + "");
						trackNode.setAttribute("playCount", stat.getPlayCount() + "");
						trackNode.setAttribute("lastPlayed", stat.getLastPlayed() != null ? stat.getLastPlayed().getTime() + ""
								: "0");
					} else {
						trackNode.setAttribute("rating", "0");
						trackNode.setAttribute("playCount", "0");
						trackNode.setAttribute("lastPlayed", "0");
					}
				}
			return outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("Error getting Library", e);
		}
		return ERROR;
	}
}
