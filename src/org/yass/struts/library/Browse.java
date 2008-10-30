package org.yass.struts.library;

import java.util.Iterator;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		try {
			LOG.info("Library requested");
			final Library lib = (Library) ActionContext.getContext().getApplication().get(YassConstants.ALL_LIBRARY);
			final Iterator<Track> it = lib.getTracks().iterator();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element libNode = doc.createElement("library");
			doc.appendChild(libNode);
			final Map<Integer, TrackStat> trackStats = (Map<Integer, TrackStat>) ActionContext.getContext().getApplication()
					.get(USER_TRACK_STATS);
			while (it.hasNext()) {
				final Track track = it.next();
				if (track.getTrackInfo(YassConstants.ARTIST) != null && track.getTrackInfo(YassConstants.ALBUM) != null
						&& track.getTrackInfo(YassConstants.GENRE) != null) {
					final Element trackNode = doc.createElement("track");
					libNode.appendChild(trackNode);
					trackNode.setAttribute("id", track.getId() + "");
					trackNode.setAttribute("trackNr", track.getTrackNr() + "");
					trackNode.setAttribute("title", track.getTitle());
					trackNode.setAttribute("artist", track.getTrackInfo(YassConstants.ARTIST).getId() + "");
					trackNode.setAttribute("album", track.getTrackInfo(YassConstants.ALBUM).getId() + "");
					trackNode.setAttribute("genre", track.getTrackInfo(YassConstants.GENRE).getId() + "");
					if (track.getTrackInfo(YassConstants.YEAR) != null)
						trackNode.setAttribute("year", track.getTrackInfo(YassConstants.YEAR).getValue());
					if (track.getTrackInfo(YassConstants.BITRATE) != null)
						trackNode.setAttribute("bitrate", track.getTrackInfo(YassConstants.BITRATE).getValue());
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
			}
			return outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("", e);
		}
		return ERROR;
	}
}
