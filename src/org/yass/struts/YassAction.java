package org.yass.struts;

import java.io.IOException;
import java.util.Map;

import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.lucene.IndexManager;
import org.yass.lucene.SearchQuery;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class YassAction extends ActionSupport implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4592155945099982999L;

	protected SearchQuery getSearchQuery() {
		final Map session = (Map) ActionContext.getContext().get("session");
		SearchQuery msr = (SearchQuery) session.get(BROWSING_CONTEXT);
		if (msr == null) {
			msr = new SearchQuery();
			session.put(BROWSING_CONTEXT, msr);
		}
		return msr;
	}

	protected PlayList getPlayList() {
		return (PlayList) ActionContext.getContext().getSession().get(CURRENT_PLAYLIST);
	}

	protected IndexManager getIndexManager() {
		return (IndexManager) ActionContext.getContext().getApplication().get(INDEX_MANAGER);
	}

	protected String refreshPlayList() {
		try {
			ActionContext.getContext().getSession().put(CURRENT_PLAYLIST, getIndexManager().search(getSearchQuery()));
		} catch (final IOException e) {
			return ERROR;
		}
		return SUCCESS;
	}

	protected Map<String, PlayList> getPlayLists() {
		return (Map<String, PlayList>) ActionContext.getContext().getApplication().get(USER_PLAYLISTS);
	}
}
