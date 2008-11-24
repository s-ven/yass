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

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;
import org.yass.domain.User;

/**
 * @author Sven Duzont
 */
public abstract class BaseResource extends Resource implements YassConstants {

	public static final Log LOG = LogFactory.getLog(SettingsResource.class);
	/**
	 * 
	 */
	protected User user;

	public BaseResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
		try {
			final int userId = getIntAttribute("userId");
			if (LOG.isInfoEnabled())
				LOG.info("Getting User userId:" + userId);
			if ((user = USER_DAO.findById(userId)) == null)
				setAvailable(false);
		} catch (final NullPointerException e) {
			setAvailable(false);
		}
	}

	/**
	 * @param doc
	 */
	protected abstract void createXMLRepresentation(Document doc);

	/**
	 * Generate an XML representation of an error response.
	 * 
	 * @param errorMessage
	 *          the error message.
	 * @param errorCode
	 *          the error code.
	 */
	protected void generateErrorRepresentation(final String errorMessage, final String errorCode, final Response response) {
		// This is an error
		response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
		// Generate the output representation
		try {
			final DomRepresentation representation = new DomRepresentation(MediaType.TEXT_XML);
			// Generate a DOM document representing the list of
			// items.
			final Document d = representation.getDocument();
			final Element eltError = d.createElement("error");
			final Element eltCode = d.createElement("code");
			eltCode.appendChild(d.createTextNode(errorCode));
			eltError.appendChild(eltCode);
			final Element eltMessage = d.createElement("message");
			eltMessage.appendChild(d.createTextNode(errorMessage));
			eltError.appendChild(eltMessage);
			response.setEntity(representation);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	protected int getIntAttribute(final String name) {
		return Integer.parseInt((String) getRequest().getAttributes().get(name));
	}

	/**
	 * Returns a listing of all registered items.
	 */
	@Override
	public Representation represent(final Variant variant) throws ResourceException {
		// Generate the right representation according to its media type.
		if (MediaType.TEXT_XML.equals(variant.getMediaType()))
			try {
				final DomRepresentation representation = new DomRepresentation(MediaType.TEXT_XML);
				// Creation of the XML Document
				final Document doc = representation.getDocument();
				createXMLRepresentation(doc);
				return representation;
			} catch (final IOException e) {
				LOG.error("Error while creating representation", e);
			}
		return null;
	}
}