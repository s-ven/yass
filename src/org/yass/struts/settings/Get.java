package org.yass.struts.settings;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.dao.SettingsDao;
import org.yass.domain.Settings;
import org.yass.struts.YassAction;

public class Get extends YassAction implements YassConstants {

	public int userId;
	public int loadedTrackId;
	public int volume;
	public boolean shuffle;
	public boolean repeat;
	public boolean showRemaining;
	public int displayMode;
	public int stopFadeout;
	public int nextFadeout;
	public int skipFadeout;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		try {
			LOG.info("Getting Settings ");
			final Settings settings = new SettingsDao().getFromUserId(1);
			final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			final Element settingsNode = doc.createElement("settings");
			doc.appendChild(settingsNode);
			if (settings != null) {
				settingsNode.setAttribute("loadedTrackId", settings.getLoadedTrackId() + "");
				settingsNode.setAttribute("volume", settings.getVolume() + "");
				settingsNode.setAttribute("shuffle", settings.isShuffle() + "");
				settingsNode.setAttribute("loop", settings.isRepeat() + "");
				settingsNode.setAttribute("showRemaining", settings.isShowRemaining() + "");
				settingsNode.setAttribute("displayMode", settings.getDisplayMode() + "");
				settingsNode.setAttribute("stopFadeout", settings.getStopFadeout() + "");
				settingsNode.setAttribute("skipFadeout", settings.getSkipFadeout() + "");
				settingsNode.setAttribute("nextFadeout", settings.getNextFadeout() + "");
				final Element tiIdsNode = doc.createElement("trackInfoIds");
				settingsNode.appendChild(tiIdsNode);
				for (final Integer id : settings.getTrackInfoIds()) {
					final Element tiIdNode = doc.createElement("trackInfo");
					tiIdNode.setAttribute("id", id.intValue() + "");
					tiIdsNode.appendChild(tiIdNode);
				}
			}
			return outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("", e);
		}
		return NONE;
	}
}
