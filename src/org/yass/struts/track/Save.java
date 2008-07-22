package org.yass.struts.track;

import org.yass.YassConstants;
import org.yass.dao.TrackDao;
import org.yass.domain.Track;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	public int id;
	public int rating;
	public int playCount;
	private static final TrackDao trackDao = new TrackDao();
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		LOG.info("Saving track id:" + id);
		final Track track = getLibrary().getMediaFile(id);
		track.setRating(rating);
		track.setPlayCount(playCount);
		trackDao.save(track);
		return NONE;
	}
}
