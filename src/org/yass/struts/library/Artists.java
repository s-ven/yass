package org.yass.struts.library;

import org.yass.YassConstants;
import org.yass.struts.YassAction;

public class Artists extends YassAction implements YassConstants {

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
		if (getSearchQuery().getGenresFilter() != null || refresh)
			return refreshPlayList();
		return SUCCESS;
	}

	/**
	 * @param genres
	 *          the genres to set
	 */
	public final void setGenres(final String[] genres) {
		getSearchQuery().setGenresFilter(genres);
		getSearchQuery().setArtistsFilter(null);
		getSearchQuery().setAlbumsFilter(null);
	}
}
