package org.yass.struts.playlist;

import org.yass.YassConstants;
import org.yass.dao.PlayListDao;
import org.yass.domain.PlayList;
import org.yass.domain.SmartPlayList;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class Show extends YassAction implements YassConstants {

	public int id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	public void setRefresh(final boolean refresh) {
	}

	@Override
	public String execute() {
		final PlayList pl = getPlayLists().get(id);
		if (pl instanceof SmartPlayList)
			new PlayListDao().reloadSmartPlayLsit((SmartPlayList) pl);
		ActionContext.getContext().getSession().put(CURRENT_PLAYLIST, pl);
		return SUCCESS;
	}
}
