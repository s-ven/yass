package org.yass.struts.track;

import org.yass.YassConstants;
import org.yass.dao.TrackStatDao;
import org.yass.domain.TrackStat;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	public int id;
	public int rating;
	public int playCount;
	private static final TrackStatDao trackStatDao = new TrackStatDao();
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		LOG.info("Saving track id:" + id);
		TrackStat trackStat = getTrackStats().get(id);
		if (trackStat == null)
			getTrackStats().put(id, trackStat = new TrackStat(1, id));
		trackStat.setRating(rating);
		trackStat.setPlayCount(playCount);
		trackStatDao.save(trackStat);
		return NONE;
	}
}
