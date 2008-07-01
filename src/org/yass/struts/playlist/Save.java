package org.yass.struts.playlist;

import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.lucene.FilePlayList;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	public String id;
	public String name;
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
		PlayList pl = getPlayLists().get(id);
		if (pl == null)
			pl = new FilePlayList(name);
		((FilePlayList) pl).save();
		getPlayLists().put(pl.id, pl);
		return NONE;
	}

	/**
	 * @param albums
	 *          the albums to set
	 */
	public final void setAlbums(final String[] genres) {
		getSearchQuery().setAlbumsFilter(genres);
	}
}
