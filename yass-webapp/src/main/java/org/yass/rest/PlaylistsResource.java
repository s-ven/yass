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

import java.util.Map;

import javax.ws.rs.GET;
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
import org.yass.domain.Library;
import org.yass.domain.PlayList;
import org.yass.domain.SmartPlayList;
import org.yass.domain.User;
import org.yass.util.XMLSerializer;

/**
 * @author Sven Duzont
 * 
 */
@Path("/users/{userId}/playlists")
public class PlaylistsResource implements YassConstants {

    /**
     *
     */
    public static final Log LOG = LogFactory.getLog(PlaylistsResource.class);

    /**
     *
     * @param userId
     * @return
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    @GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getPlaylists(@PathParam("userId") final int userId) throws ParserConfigurationException {
		final User user = USER_DAO.findById(userId);
		if (user == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Library lib = user.getLibrary();
		if (lib == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		LOG.info("Getting Playlists for User id:" + userId);
		final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final Node playListsNode = doc.appendChild(doc.createElement("playlists"));
		final Element libraryNode = (Element) playListsNode.appendChild(doc.createElement("library"));
		libraryNode.setAttribute("name", "LIBRARY");
		libraryNode.setAttribute("type", "void");
		Element plstNode = (Element) libraryNode.appendChild(doc.createElement("playlist"));
		plstNode.setAttribute("name", "Music");
		plstNode.setAttribute("type", "library");
		plstNode.setAttribute("id", user.getLibrary().getId() + "");
		final Element smartPlNode = (Element) playListsNode.appendChild(doc.createElement("smart"));
		smartPlNode.setAttribute("name", "SMART PLAYLISTS");
		smartPlNode.setAttribute("type", "void");
		final Element usrPlNode = (Element) playListsNode.appendChild(doc.createElement("user"));
		usrPlNode.setAttribute("name", "USER PLAYLISTS");
		usrPlNode.setAttribute("type", "void");
		plstNode = (Element) usrPlNode.appendChild(doc.createElement("playlist"));
		plstNode.setAttribute("name", "<New>");
		plstNode.setAttribute("type", "user");
		plstNode.setAttribute("id", "0");
		final Map<Integer, PlayList> playlists = user.getPlayLists();
		for (final PlayList plst : playlists.values()) {
			if (plst instanceof SmartPlayList)
				(plstNode = (Element) smartPlNode.appendChild(doc.createElement("playlist"))).setAttribute("type", "smart");
			else
				(plstNode = (Element) usrPlNode.appendChild(doc.createElement("playlist"))).setAttribute("type", "user");
			plstNode.setAttribute("name", plst.getName());
			plstNode.setAttribute("id", plst.getId() + "");
		}
		return Response.ok(XMLSerializer.serialize(doc)).type(MediaType.APPLICATION_XML).build();
	}
}