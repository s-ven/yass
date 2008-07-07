package org.yass.struts.library;

import java.util.Map;

import org.yass.YassConstants;
import org.yass.lucene.SearchQuery;
import org.yass.struts.YassAction;

import com.opensymphony.xwork2.ActionContext;

public class Browse extends YassAction implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;
	private final SearchQuery query = new SearchQuery();

	@Override
	public String execute() {
		final Map session = (Map) ActionContext.getContext().get("session");
		session.put(BROWSING_CONTEXT, query);
		refreshPlayList(CURRENT_LIB_PLAYLIST);
		setPlayList(CURRENT_PLAYLIST, getPlayList(CURRENT_LIB_PLAYLIST));
		return SUCCESS;
	}

	/**
	 * @param keywords
	 *          the keywords to set
	 */
	public final void setKeywords(final String keywords) {
		query.setKeywords(keywords);
	}

	/**
	 * @param keywordsFields
	 *          the keywordsFields to set
	 */
	public final void setKeywordsFields(final String keywordsFields) {
		query.setKeywordsFields(keywordsFields);
	}

	/**
	 * @param genres
	 *          the genres to set
	 */
	public final void setGenres(final String[] genres) {
		query.setGenresFilter(genres);
	}

	/**
	 * @param artists
	 *          the artists to set
	 */
	public final void setArtists(final String[] artists) {
		query.setArtistsFilter(artists);
	}

	/**
	 * @param artists
	 *          the artists to set
	 */
	public final void setAlbums(final String[] artists) {
		query.setAlbumsFilter(artists);
	}
}
