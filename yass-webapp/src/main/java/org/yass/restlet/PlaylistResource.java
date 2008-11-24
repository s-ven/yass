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

import org.restlet.Context;
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
public class PlaylistResource extends BaseResource {

	private PlayList playList;

	/**
	 * @param context
	 * @param request
	 * @param response
	 */
	public PlaylistResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
		if (isAvailable() && (playList = user.getPlayLists().get(getIntAttribute("playlistId"))) != null) {
			getVariants().add(new Variant(MediaType.TEXT_XML));
			setModifiable(true);
			if (playList instanceof SmartPlayList)
				PLAYLIST_DAO.reloadSmartPlayLsit((SmartPlayList) playList);
		}
	}

	/**
	 * Handle PUT requests: create a new item.
	 */
	@Override
	public void acceptRepresentation(final Representation entity) throws ResourceException {
		final Form form = new Form(entity);
		if (playList instanceof SimplePlayList) {
			playList.addTracks(form.getInts("trackIds"));
			PLAYLIST_DAO.save(playList);
		}
		getResponse().setStatus(Status.SUCCESS_OK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yass.restlet.BaseResource#createXMLRepresentation(org.w3c.dom.Document)
	 */
	@Override
	protected void createXMLRepresentation(final Document doc) {
		final Node libNode = doc.appendChild(doc.createElement("playlist"));
		for (final Integer trackId : playList.getTrackIds())
			((Element) libNode.appendChild(doc.createElement("track"))).setAttribute("id", trackId.toString());
	}
}
