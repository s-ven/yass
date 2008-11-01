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
package org.yass.struts;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.PlayList;
import org.yass.domain.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("all")
public class YassAction extends ActionSupport implements YassConstants {

	public static final Log LOG = LogFactory.getLog(YassAction.class);
	private static final long serialVersionUID = -4592155945099982999L;

	protected Library getLibrary() {
		return (Library) ActionContext.getContext().getApplication().get(ALL_LIBRARY);
	}

	protected PlayList getPlayList(final String plId) {
		return (PlayList) ActionContext.getContext().getSession().get(plId);
	}

	protected Map<Integer, PlayList> getPlayLists() {
		return (Map<Integer, PlayList>) ActionContext.getContext().getApplication().get(USER_PLAYLISTS);
	}

	protected User getUser() {
		return (User) ActionContext.getContext().getApplication().get(USER);
	}

	/**
	 * @param document
	 * @throws TransformerFactoryConfigurationError
	 * @throws IllegalArgumentException
	 */
	public String outputDocument(final Document document) {
		try {
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			ServletActionContext.getResponse().setContentType("text/xml");
			ServletActionContext.getResponse().setHeader("Content-Encoding", "gzip");
			// Zip the output for saving bandwidth
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final GZIPOutputStream gzoz = new GZIPOutputStream(bout);
			transformer.transform(new DOMSource(document), new StreamResult(gzoz));
			gzoz.finish();
			ServletActionContext.getResponse().setContentLength(bout.size());
			bout.writeTo(ServletActionContext.getResponse().getOutputStream());
		} catch (final FileNotFoundException e) {
			LOG.error("", e);
			return ERROR;
		} catch (final IOException e) {
			LOG.error("", e);
			return ERROR;
		} catch (final TransformerException e) {
			LOG.error("", e);
			return ERROR;
		}
		return NONE;
	}

	protected void setPlayList(final String playListId, final PlayList search) {
		ActionContext.getContext().getSession().put(playListId, search);
	}
}
