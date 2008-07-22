package org.yass.struts.library;

import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.Track;
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
			final Library lib = (Library) ActionContext.getContext().getApplication().get(YassConstants.ALL_LIBRARY);
			final Iterator<Track> it = lib.getTracks().iterator();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element libNode = doc.createElement("library");
			doc.appendChild(libNode);
			while (it.hasNext()) {
				final Element trackNode = doc.createElement("track");
				libNode.appendChild(trackNode);
				final Track mf = it.next();
				trackNode.setAttribute("id", mf.getId() + "");
				trackNode.setAttribute("trackNr", mf.getTrackNr() + "");
				trackNode.setAttribute("title", StringEscapeUtils.escapeXml(mf.getTitle()));
				trackNode.setAttribute("artist", mf.getTrackInfo(YassConstants.ARTIST).getId() + "");
				trackNode.setAttribute("album", mf.getTrackInfo(YassConstants.ALBUM).getId() + "");
				trackNode.setAttribute("genre", mf.getTrackInfo(YassConstants.GENRE).getId() + "");
				trackNode.setAttribute("length", mf.getLength() + "");
				trackNode.setAttribute("rating", mf.getRating() + "");
				trackNode.setAttribute("playCount", mf.getPlayCount() + "");
			}
			outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("", e);
		}
		return ERROR;
	}
}
