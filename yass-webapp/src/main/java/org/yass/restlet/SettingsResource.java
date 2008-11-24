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

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.domain.UserBrowsingContext;
import org.yass.domain.UserSetting;

/**
 * @author Sven Duzont
 * 
 */
public class SettingsResource extends BaseResource implements YassConstants {

	public static final Log LOG = LogFactory.getLog(SettingsResource.class);
	private UserSetting settings;

	/**
	 * @param context
	 * @param request
	 * @param response
	 */
	public SettingsResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
		if (isAvailable() && (settings = user.getUserSetting()) == null)
			user.setUserSetting(settings = new UserSetting());
		getVariants().add(new Variant(MediaType.TEXT_XML));
	}

	/**
	 * Handle POST requests: create a new item.
	 */
	@Override
	public void acceptRepresentation(final Representation entity) throws ResourceException {
		final Form form = new Form(entity);
		if (LOG.isInfoEnabled())
			LOG.info("Saving Settings User id:" + user.getId());
		settings.setDisplayMode(form.getShort("displayMode"));
		settings.setLoadedTrackId(form.getInt("loadedTrackId"));
		settings.setShuffle(form.getBoolean("shuffle"));
		settings.setLoop(form.getBoolean("loop"));
		settings.setShowRemaining(form.getBoolean("showRemaining"));
		settings.setNextFadeout(form.getInt("nextFadeout"));
		settings.setStopFadeout(form.getInt("stopFadeout"));
		settings.setSkipFadeout(form.getInt("skipFadeout"));
		settings.setVolume(form.getInt("volume"));
		final Set<UserBrowsingContext> browsingContext = new LinkedHashSet<UserBrowsingContext>();
		final Integer[] trackInfoIds = form.getInts("trackInfoIds");
		if (trackInfoIds != null && trackInfoIds.length != 0)
			for (final Integer trackInfoId : trackInfoIds)
				browsingContext.add(new UserBrowsingContext(user, trackInfoId));
		USER_DAO.cleanBrowsingContext(user);
		user.setBrowsingContext(browsingContext);
		USER_DAO.save(user);
	}

	/**
	 * @param doc
	 */
	@Override
	public void createXMLRepresentation(final Document doc) {
		final UserSetting settings = user.getUserSetting();
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
		doc.normalizeDocument();
	}
}
