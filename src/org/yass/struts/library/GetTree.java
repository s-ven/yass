package org.yass.struts.library;

import org.yass.YassConstants;
import org.yass.listener.Init;
import org.yass.struts.YassAction;

public class GetTree extends YassAction implements YassConstants {

	private String UUID;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		LOG.info("Library trackInfos requested");
		return outputDocument(Init.buildXMLDoc(getLibrary()));
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
