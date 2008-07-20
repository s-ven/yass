package org.yass.struts;

import java.util.Map;

import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.PlayList;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class YassAction extends ActionSupport implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4592155945099982999L;

	protected PlayList getPlayList(final String plId) {
		return (PlayList) ActionContext.getContext().getSession().get(plId);
	}

	protected Library getLibrary() {
		return (Library) ActionContext.getContext().getApplication().get(ALL_LIBRARY);
	}

	protected void setPlayList(final String playListId, final PlayList search) {
		ActionContext.getContext().getSession().put(playListId, search);
	}

	protected Map<Integer, PlayList> getPlayLists() {
		return (Map<Integer, PlayList>) ActionContext.getContext().getApplication().get(USER_PLAYLISTS);
	}
}
