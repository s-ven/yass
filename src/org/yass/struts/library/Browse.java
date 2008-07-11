package org.yass.struts.library;

import java.util.Map;

import org.yass.YassConstants;
import org.yass.lucene.SearchQuery;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class Browse extends YassAction implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;
	private final SearchQuery query = new SearchQuery();

	@Override
	public String execute() {
		final Map session = (Map) ActionContext.getContext().get("session");
		session.put(BROWSING_CONTEXT, query);
		setPlayList(CURRENT_PLAYLIST, getAllLibrary());
		return SUCCESS;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setKeywords(final String keywords) {
		query.setKeywords(keywords);
	}

	/**
	 * @param keywordsFields
	 *          the keywordsFields to set
	 */
	public final void setKeywordsFields(final String keywordsFields) {
		query.setKeywordsFields(keywordsFields);
	}
}
