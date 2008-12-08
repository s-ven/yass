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
package org.yass.rest;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.yass.YassConstants;
import org.yass.domain.User;
import org.yass.domain.UserBrowsingContext;
import org.yass.domain.UserSetting;
import org.yass.util.XMLSerializer;

/**
 * @author Sven Duzont
 * 
 */
@Path("/users/{userId}/settings")
public class SettingsResource implements YassConstants {

	/**
     *
     */
	public static final Log LOG = LogFactory.getLog(SettingsResource.class);

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws javax.xml.parsers.ParserConfigurationException
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getSettings(@PathParam("userId") final int userId) throws ParserConfigurationException {
		final User user = USER_DAO.findById(userId);
		if (user == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final UserSetting settings = user.getUserSetting();
		if (settings == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final Element settingsNode = (Element) doc.appendChild(doc.createElement("settings"));
		settingsNode.setAttribute("loadedTrackId", settings.getLoadedTrackId() + "");
		settingsNode.setAttribute("volume", settings.getVolume() + "");
		settingsNode.setAttribute("shuffle", settings.isShuffle() + "");
		settingsNode.setAttribute("loop", settings.isRepeat() + "");
		settingsNode.setAttribute("showRemaining", settings.isShowRemaining() + "");
		settingsNode.setAttribute("displayMode", settings.getDisplayMode() + "");
		settingsNode.setAttribute("stopFadeout", settings.getStopFadeout() + "");
		settingsNode.setAttribute("skipFadeout", settings.getSkipFadeout() + "");
		settingsNode.setAttribute("nextFadeout", settings.getNextFadeout() + "");
		final Node tiIdsNode = settingsNode.appendChild(doc.createElement("trackInfoIds"));
		for (final UserBrowsingContext id : user.getBrowsingContext())
			((Element) tiIdsNode.appendChild(doc.createElement("trackInfo"))).setAttribute("id", id.getTrackInfoId() + "");
		doc.normalizeDocument();
		return Response.ok(XMLSerializer.serialize(doc)).type(MediaType.APPLICATION_XML).build();
	}

	/**
	 * 
	 * @param userId
	 * @param displayMode
	 * @param loadedTrackId
	 * @param shuffle
	 * @param loop
	 * @param showRemaining
	 * @param nextFadeout
	 * @param stopFadeout
	 * @param skipFadeout
	 * @param volume
	 * @param trackInfoIds
	 * @return
	 */
	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response saveSettings(@PathParam("userId") final int userId,
			@FormParam("displayMode") final short displayMode, @FormParam("loadedTrackId") final int loadedTrackId,
			@FormParam("shuffle") final boolean shuffle, @FormParam("loop") final boolean loop,
			@FormParam("showRemaining") final boolean showRemaining, @FormParam("nextFadeout") final int nextFadeout,
			@FormParam("stopFadeout") final int stopFadeout, @FormParam("skipFadeout") final int skipFadeout,
			@FormParam("volume") final int volume, @FormParam("trackInfoIds") final Set<Integer> trackInfoIds) {
		final User user = USER_DAO.findById(userId);
		if (user == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final UserSetting settings = user.getUserSetting();
		if (settings == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		if (LOG.isInfoEnabled())
			LOG.info("Saving Settings   User id:" + user.getId());
		settings.setDisplayMode(displayMode);
		settings.setLoadedTrackId(loadedTrackId);
		settings.setShuffle(shuffle);
		settings.setLoop(loop);
		settings.setShowRemaining(showRemaining);
		settings.setNextFadeout(nextFadeout);
		settings.setStopFadeout(stopFadeout);
		settings.setSkipFadeout(skipFadeout);
		settings.setVolume(volume);
		final Set<UserBrowsingContext> browsingContext = new LinkedHashSet<UserBrowsingContext>();
		if (trackInfoIds != null)
			for (final Integer trackInfoId : trackInfoIds)
				browsingContext.add(new UserBrowsingContext(user, trackInfoId));
		USER_DAO.cleanBrowsingContext(user);
		user.setBrowsingContext(browsingContext);
		USER_DAO.save(user);
		return Response.ok().build();
	}
}