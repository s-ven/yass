/*
 Copyright (c) 2008 Sven Duzont sven.duzont@gmail.com> All rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"),
 to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished
 to do so, subject to the following conditions: The above copyright notice
 and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS",
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.yass.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author svenduzont
 * 
 */
@Entity
@Table(name = "TRACK_STAT")
@NamedQuery(name = "getTrackStatByUserId", query = "SELECT ts FROM TrackStat ts where ts.userId = ?1")
public class TrackStat {

	@Column(name = "LAST_PLAYED")
	private Date lastPlayed;
	@Column(name = "LAST_SELECTED")
	private Date lastSelected;
	@Column(name = "PLAY_COUNT")
	private int playCount = 0;
	private int rating = 0;
	@Column(name = "TRACK_ID")
	private int trackId;
	@EmbeddedId
	private TrackStatPK trackStatPK;
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	private User user;

	/**
	 * 
	 */
	public TrackStat() {
		super();
	}

	/**
	 * @param trackTypeId
	 * @param userId
	 */
	public TrackStat(final User user, final int trackId) {
		super();
		this.user = user;
		this.trackId = trackId;
		trackStatPK = new TrackStatPK(trackId, user.getId());
	}

	/**
	 * @return the lastPlayed
	 */
	public final Date getLastPlayed() {
		return lastPlayed;
	}

	/**
	 * @return the lastSelected
	 */
	public final Date getLastSelected() {
		return lastSelected;
	}

	/**
	 * @return the playCount
	 */
	public final int getPlayCount() {
		return playCount;
	}

	/**
	 * @return the rating
	 */
	public final int getRating() {
		return rating;
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

	@Override
	public int hashCode() {
		return trackStatPK.hashCode();
	}

	/**
	 * @param lastPlayed
	 *          the lastPlayed to set
	 */
	public final void setLastPlayed(final Date lastPlayed) {
		this.lastPlayed = lastPlayed;
	}

	/**
	 * @param lastSelected
	 *          the lastSelected to set
	 */
	public final void setLastSelected(final Date lastSelected) {
		this.lastSelected = lastSelected;
	}

	/**
	 * @param playCount
	 *          the playCount to set
	 */
	public final void setPlayCount(final int playCount) {
		this.playCount = playCount;
	}

	/**
	 * @param rating
	 *          the rating to set
	 */
	public final void setRating(final int rating) {
		this.rating = rating;
	}
}
