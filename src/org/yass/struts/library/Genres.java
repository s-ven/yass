package org.yass.struts.library;

import org.yass.YassConstants;
import org.yass.domain.LibraryPlayList;
import org.yass.struts.YassAction;

public class Genres extends YassAction implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		if (getSearchQuery().getKeywords() != null && !getSearchQuery().getKeywords().equals("")) {
			refreshPlayList(CURRENT_LIB_PLAYLIST);
			setPlayList(CURRENT_PLAYLIST, getPlayList(CURRENT_LIB_PLAYLIST));
			return SUCCESS;
		}
		if (getPlayList(CURRENT_LIB_PLAYLIST) == null || getSearchQuery().getKeywords() != null
				&& getSearchQuery().getKeywords().equals(""))
			setPlayList(CURRENT_LIB_PLAYLIST, getAllLibraryList());
		((LibraryPlayList) getPlayList(CURRENT_LIB_PLAYLIST)).setGenres(getAllLibraryList().getGenres());
		setPlayList(CURRENT_PLAYLIST, getPlayList(CURRENT_LIB_PLAYLIST));
		return SUCCESS;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setKeywords(final String keywords) {
		getSearchQuery().setKeywords(keywords);
	}

	/**
	 * @param keywordsFields
	 *          the keywordsFields to set
	 */
	public final void setKeywordsFields(final String keywordsFields) {
		getSearchQuery().setKeywordsFields(keywordsFields);
	}
}
