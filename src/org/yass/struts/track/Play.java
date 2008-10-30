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

	private int id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		final Track track = TRACK_DAO.getById(id);
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
				LOG.info("Straming Track id:" + id + ", path:" + track.getPath());
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
