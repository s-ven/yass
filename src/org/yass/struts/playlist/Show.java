package org.yass.struts.playlist;

import java.util.Map;

import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class Show extends YassAction implements YassConstants {

	public String id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;
	private boolean refresh = false;

	public void setRefresh(final boolean refresh) {
		this.refresh = refresh;
	}

	@Override
	public String execute() {
		final PlayList pl = ((Map<String, PlayList>) ActionContext.getContext().getApplication().get(USER_PLAYLISTS))
				.get(id);
		ActionContext.getContext().getSession().put(CURRENT_PLAYLIST, pl);
		return SUCCESS;
	}

	/**
	 * @param albums
	 *          the albums to set
	 */
	public final void setAlbums(final String[] genres) {
		getSearchQuery().setAlbumsFilter(genres);
	}
}
