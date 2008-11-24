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

import java.util.Date;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.yass.domain.PlayList;
import org.yass.domain.SimplePlayList;
import org.yass.domain.SmartPlayList;

/**
 * @author Sven Duzont
 * 
 */
public class PlaylistsResource extends BaseResource {

	private Map<Integer, PlayList> playlists;

	/**
	 * @param context
	 * @param request
	 * @param response
	 */
	public PlaylistsResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
		if (isAvailable()) {
			playlists = user.getPlayLists();
			getVariants().add(new Variant(MediaType.TEXT_XML));
		}
	}

	/**
	 * Handle POST requests: create a new item.
	 */
	@Override
	public void acceptRepresentation(final Representation entity) throws ResourceException {
		final Form form = new Form(entity);
		final String name = form.getFirstValue("name");
		final PlayList pl = new SimplePlayList(name, new Date());
		playlists.put(pl.getId(), pl);
		PLAYLIST_DAO.save(pl);
		getResponse().setStatus(Status.SUCCESS_CREATED);
		getResponse().setEntity(
				new PlaylistResource(getContext(), getRequest(), getResponse()).represent(new Variant(MediaType.TEXT_XML)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yass.restlet.BaseResource#createXMLRepresentation(org.w3c.dom.Document
	 * )
	 */
	@Override
	protected void createXMLRepresentation(final Document doc) {
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
		for (final PlayList plst : playlists.values()) {
			if (plst instanceof SmartPlayList)
				(plstNode = (Element) smartPlNode.appendChild(doc.createElement("playlist"))).setAttribute("type", "smart");
			else
				(plstNode = (Element) usrPlNode.appendChild(doc.createElement("playlist"))).setAttribute("type", "user");
			plstNode.setAttribute("name", plst.getName());
			plstNode.setAttribute("id", plst.getId() + "");
		}
	}
}
