package org.yass.struts.playlist;

import java.util.Date;

import org.yass.YassConstants;
import org.yass.dao.PlayListDao;
import org.yass.domain.PlayList;
import org.yass.domain.SimplePlayList;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	private final PlayListDao playlistDao = new PlayListDao();
	public String id;
	public String name;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	public void setRefresh(final boolean refresh) {
	}

	@Override
	public String execute() {
		PlayList pl = getPlayLists().get(id);
		if (pl == null)
			pl = new SimplePlayList(0, name, new Date());
		playlistDao.savePlaylist(pl);
		getPlayLists().put(pl.id, pl);
		return NONE;
	}
}
