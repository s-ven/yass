package org.yass.struts.library;

import org.yass.YassConstants;
import org.yass.struts.YassAction;

public class Genres extends YassAction implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		return refreshPlayList();
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setKeywords(final String keywords) {
		getSearchQuery().setKeywords(keywords);
	}

	/**
	 * @param keywordsFields
	 *          the keywordsFields to set
	 */
	public final void setKeywordsFields(final String keywordsFields) {
		getSearchQuery().setKeywordsFields(keywordsFields);
	}
}
