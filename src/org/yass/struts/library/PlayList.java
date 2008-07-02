package org.yass.struts.library;

import org.yass.struts.YassAction;

public class PlayList extends YassAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;
	private boolean refresh = false;

	public void setRefresh(final boolean refresh) {
		getSearchQuery().setAlbumsFilter(null);
		this.refresh = refresh;
	}

	@Override
	public String execute() {
		if (getSearchQuery().getAlbumsFilter() != null || refresh)
			refreshPlayList(CURRENT_LIB_PLAYLIST);
		setPlayList(CURRENT_PLAYLIST, getPlayList(CURRENT_LIB_PLAYLIST));
		return SUCCESS;
	}

	/**
	 * @param albums
	 *          the albums to set
	 */
	public final void setAlbums(final String[] albums) {
		getSearchQuery().setAlbumsFilter(albums);
	}
}
