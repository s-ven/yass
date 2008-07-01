package org.yass.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.struts2.ServletActionContext;
import org.yass.YassConstants;
import org.yass.domain.MediaFile;
import org.yass.domain.PlayList;

public class Play extends YassAction implements YassConstants {

	private String UUID;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		final PlayList msr = getPlayList();
		final MediaFile mf = msr.getMediaFile(UUID);
		final File toPlayr = new File(mf.getPath());
		OutputStream out = null;
		InputStream fis = null;
		try {
			fis = new FileInputStream(toPlayr);
			out = ServletActionContext.getResponse().getOutputStream();
			final byte[] buf = new byte[4 * 1024]; // 4K buffer
			int bytesRead;
			while ((bytesRead = fis.read(buf)) != -1)
				out.write(buf, 0, bytesRead);
			ServletActionContext.getResponse().flushBuffer();
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			return ERROR;
		} catch (final IOException e) {
			e.printStackTrace();
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
	 * @return the keywords
	 */
	public final String getUUID() {
		return UUID;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setUUID(final String UUID) {
		this.UUID = UUID;
	}
}
