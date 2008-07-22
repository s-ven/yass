package org.yass.struts.library;

import org.w3c.dom.Document;
import org.yass.YassConstants;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class GetTree extends YassAction implements YassConstants {

	private String UUID;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		final Document document = (Document) ActionContext.getContext().getApplication().get(LIB_XML_TREE);
		return outputDocument(document);
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
