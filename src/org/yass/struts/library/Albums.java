package org.yass.struts.library;

import org.yass.YassConstants;
import org.yass.struts.YassAction;

public class Albums extends YassAction implements YassConstants {

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
		if (getSearchQuery().getArtistsFilter() != null || refresh)
			return refreshPlayList();
		return SUCCESS;
	}

	/**
	 * @param artists
	 *          the artists to set
	 */
	public final void setArtists(final String[] artists) {
		getSearchQuery().setArtistsFilter(artists);
		getSearchQuery().setAlbumsFilter(null);
	}
}
