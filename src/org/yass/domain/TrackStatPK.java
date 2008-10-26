package org.yass.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TrackStatPK {

	/**
	 * 
	 */
	public TrackStatPK() {
		super();
	}

	/**
	 * @param trackId
	 * @param userId
	 */
	public TrackStatPK(final int trackId, final int userId) {
		super();
		this.trackId = trackId;
		this.userId = userId;
	}

	@Column(name = "TRACK_ID")
	protected int trackId;
	@Column(name = "USER_ID")
	protected int userId;

	@Override
	public boolean equals(final Object obj) {
		return obj != null && obj instanceof TrackStatPK && ((TrackStatPK) obj).trackId == trackId
				&& ((TrackStatPK) obj).userId == userId;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
