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
				final Track mf = it.next();
				if (mf.getTrackInfo(YassConstants.ARTIST) != null && mf.getTrackInfo(YassConstants.ALBUM) != null
						&& mf.getTrackInfo(YassConstants.GENRE) != null) {
					final Element trackNode = doc.createElement("track");
					libNode.appendChild(trackNode);
					trackNode.setAttribute("id", mf.getId() + "");
					trackNode.setAttribute("trackNr", mf.getTrackNr() + "");
					trackNode.setAttribute("title", mf.getTitle());
					trackNode.setAttribute("artist", mf.getTrackInfo(YassConstants.ARTIST).getId() + "");
					trackNode.setAttribute("album", mf.getTrackInfo(YassConstants.ALBUM).getId() + "");
					trackNode.setAttribute("genre", mf.getTrackInfo(YassConstants.GENRE).getId() + "");
					if (mf.getTrackInfo(YassConstants.YEAR) != null)
						trackNode.setAttribute("year", mf.getTrackInfo(YassConstants.YEAR).getValue());
					if (mf.getTrackInfo(YassConstants.BITRATE) != null)
						trackNode.setAttribute("bitrate", mf.getTrackInfo(YassConstants.BITRATE).getValue());
					trackNode.setAttribute("vbr", mf.isVBR() + "");
					trackNode.setAttribute("length", mf.getLength() + "");
					trackNode.setAttribute("lastModified", mf.getLastModified().getTime() + "");
					final TrackStat stat = trackStats.get(mf.getId());
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
