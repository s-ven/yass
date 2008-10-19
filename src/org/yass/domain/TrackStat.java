package org.yass.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author svenduzont
 * 
 */
@Entity
@Table(name = "TRACK_STAT")
public class TrackStat {

	private int rating = 0;
	@Column(name = "PLAY_COUNT")
	private int playCount = 0;
	@EmbeddedId
	private TrackStatPK trackStatPK;
	@Column(name = "LAST_PLAYED")
	private Date lastPlayed;
	@Column(name = "LAST_SELECTED")
	private Date lastSelected;

	/**
	 * @param trackTypeId
	 * @param userId
	 */
	public TrackStat(final int userId, final int trackId) {
		super();
		trackStatPK = new TrackStatPK(trackId, userId);
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
		this(userId, trackId);
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
	public final int getTrackId() {
		return trackStatPK.trackId;
	}

	/**
	 * @return the userId
	 */
	public final int getUserId() {
		return trackStatPK.userId;
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
