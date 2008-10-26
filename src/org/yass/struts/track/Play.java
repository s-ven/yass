package org.yass.struts.track;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
		LOG.info("Playing track id:" + id);
		final Track track = getLibrary().getTrack(id);
		final File toPlayr = new File(track.getPath());
		OutputStream out = null;
		InputStream fis = null;
		try {
			ServletActionContext.getResponse().setContentType("audio/mpeg");
			ServletActionContext.getResponse().setContentLength(new Long(toPlayr.length()).intValue());
			fis = new FileInputStream(toPlayr);
			out = ServletActionContext.getResponse().getOutputStream();
			final byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = fis.read(buf)) != -1)
				out.write(buf, 0, bytesRead);
			ServletActionContext.getResponse().flushBuffer();
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
