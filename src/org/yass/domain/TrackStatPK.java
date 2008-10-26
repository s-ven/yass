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
		if (this == obj)
			return true;
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		return ((TrackStatPK) obj).trackId == trackId && ((TrackStatPK) obj).userId == userId;
	}

	@Override
	public int hashCode() {
		return (32 + userId) * 31 + trackId;
	}
}
