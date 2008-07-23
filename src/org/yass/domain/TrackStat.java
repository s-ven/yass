package org.yass.domain;

import java.util.Date;

/**
 * @author svenduzont
 * 
 */
public class TrackStat {

	private int rating = 0;
	private int playCount = 0;
	private final int trackId;
	private final int userId;
	private Date lastPlayed;
	private Date lastSelected;

	/**
	 * @param trackTypeId
	 * @param userId
	 */
	public TrackStat(final int userId, final int trackId) {
		super();
		this.trackId = trackId;
		this.userId = userId;
	}

	/**
	 * @param userId
	 * @param trackId
	 * @param rating
	 * @param lastPlayed
	 * @param playCount
	 * @param lastSelected
	 */
	public TrackStat(final int userId, final int trackId, final int rating, final Date lastPlayed, final int playCount,
			final Date lastSelected) {
		super();
		this.userId = userId;
		this.trackId = trackId;
		this.rating = rating;
		this.lastPlayed = lastPlayed;
		this.playCount = playCount;
		this.lastSelected = lastSelected;
	}

	/**
	 * @return the lastPlayed
	 */
	public final Date getLastPlayed() {
		return lastPlayed;
	}

	/**
	 * @param lastPlayed
	 *          the lastPlayed to set
	 */
	public final void setLastPlayed(final Date lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	/**
	 * @return the lastSelected
	 */
	public final Date getLastSelected() {
		return lastSelected;
	}

	/**
	 * @param lastSelected
	 *          the lastSelected to set
	 */
	public final void setLastSelected(final Date lastSelected) {
		this.lastSelected = lastSelected;
	}

	/**
	 * @return the trackTypeId
	 */
	public final int getTrackTypeId() {
		return trackId;
	}

	/**
	 * @return the userId
	 */
	public final int getUserId() {
		return userId;
	}

	/**
	 * @return the rating
	 */
	public final int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *          the rating to set
	 */
	public final void setRating(final int rating) {
		this.rating = rating;
	}

	/**
	 * @return the playCount
	 */
	public final int getPlayCount() {
		return playCount;
	}

	/**
	 * @param playCount
	 *          the playCount to set
	 */
	public final void setPlayCount(final int playCount) {
		this.playCount = playCount;
	}
}
