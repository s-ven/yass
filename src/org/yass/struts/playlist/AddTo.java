package org.yass.struts.playlist;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.lucene.FilePlayList;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class AddTo extends YassAction implements YassConstants {

	public String id;
	public String[] UUIDs;
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
		try {
			pl.add(super.getIndexManager().searchFromUIDS(Arrays.asList(UUIDs)));
			((FilePlayList) pl).save();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
