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
package org.yass.struts.track;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.yass.YassConstants;
import org.yass.domain.Track;
import org.yass.struts.YassAction;

public class Play extends YassAction implements YassConstants {

	private static final long serialVersionUID = 3411435373847531163L;
	private int id;

	@Override
	public String execute() {
		final Track track = TRACK_DAO.findById(id);
		if (track == null) {
			LOG.error("The Track id:" + id + " cannot be found in persistent store");
			return ERROR;
		}
		final File trackFile = new File(track.getPath());
		OutputStream out = null;
		InputStream fis = null;
		try {
			final HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("audio/mpeg");
			response.setContentLength(new Long(trackFile.length()).intValue());
			if (LOG.isInfoEnabled())
				LOG.info("Streaming Track id:" + id + ", path:" + track.getPath());
			fis = new FileInputStream(trackFile);
			out = response.getOutputStream();
			final byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = fis.read(buf)) != -1)
				out.write(buf, 0, bytesRead);
			response.flushBuffer();
		} catch (final FileNotFoundException e) {
			LOG.error("", e);
			return ERROR;
		} catch (final IOException e) {
			LOG.error("", e);
			return ERROR;
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (final IOException e) {
				}
			if (out != null)
				try {
					out.close();
				} catch (final IOException e) {
				}
		}
		return NONE;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setId(final int id) {
		this.id = id;
	}
}
