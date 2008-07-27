package org.yass.struts.playlist;

import java.util.Iterator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.dao.PlayListDao;
import org.yass.domain.PlayList;
import org.yass.domain.SmartPlayList;
import org.yass.struts.YassAction;

public class Show extends YassAction implements YassConstants {

	public int id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	public void setRefresh(final boolean refresh) {
	}

	@Override
	public String execute() {
		try {
			LOG.info("Playlist id:" + id + " requested");
			final PlayList pl = getPlayLists().get(id);
			if (pl instanceof SmartPlayList)
				new PlayListDao().reloadSmartPlayLsit((SmartPlayList) pl);
			final Iterator<Integer> it = pl.trackIds.iterator();
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element libNode = doc.createElement("playlist");
			doc.appendChild(libNode);
			while (it.hasNext()) {
				final Element trackNode = doc.createElement("track");
				libNode.appendChild(trackNode);
				trackNode.setAttribute("id", it.next().toString());
			}
			return outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("", e);
		}
		return NONE;
	}
}
