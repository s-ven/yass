package org.yass.struts.playlist;

import org.yass.YassConstants;
import org.yass.dao.PlayListDao;
import org.yass.domain.PlayList;
import org.yass.domain.SimplePlayList;
import org.yass.struts.YassAction;

public class AddTo extends YassAction implements YassConstants {

	private final PlayListDao playlistDao = new PlayListDao();
	public int id;
	public Integer[] trackIds;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	public void setRefresh(final boolean refresh) {
	}

	@Override
	public String execute() {
		final PlayList pl = getPlayLists().get(id);
		if (pl instanceof SimplePlayList) {
			((SimplePlayList) pl).add(trackIds);
			playlistDao.savePlaylist(pl);
		}
		return NONE;
	}
}
