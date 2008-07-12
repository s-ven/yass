package org.yass.struts.track;

import org.yass.YassConstants;
import org.yass.dao.TrackDao;
import org.yass.domain.Track;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	public int id;
	public int rating;
	private static final TrackDao trackDao = new TrackDao();
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		final Track track = getAllLibraryList().getMediaFile(id);
		track.setRating(rating);
		trackDao.save(track);
		return NONE;
	}
}
