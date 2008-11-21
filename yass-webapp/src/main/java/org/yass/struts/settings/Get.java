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
package org.yass.struts.settings;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.domain.User;
import org.yass.domain.UserBrowsingContext;
import org.yass.domain.UserSetting;
import org.yass.struts.YassAction;

public class Get extends YassAction implements YassConstants {

	private static final long serialVersionUID = 3411435373847531163L;
	public int userId;

	@Override
	public String execute() {
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Getting Settings userId:" + userId);
			final User user = USER_DAO.findById(userId);
			final UserSetting settings = user.getUserSetting();
			// Creation of the XML Document
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
				for (final UserBrowsingContext id : user.getBrowsingContext()) {
					final Element tiIdNode = doc.createElement("trackInfo");
					tiIdNode.setAttribute("id", id.getTrackInfoId() + "");
					tiIdsNode.appendChild(tiIdNode);
				}
			}
			return outputDocument(doc);
		} catch (final ParserConfigurationException e) {
			LOG.error("Error getting Settings userId:" + userId, e);
		}
		return NONE;
	}
}
