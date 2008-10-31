package org.yass.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserBrowsingContextPK implements Serializable {

	@Column(name = "USER_ID")
	private int userId;
	@Column(name = "TRACK_INFO_ID")
	private int trackInfoId;
	private static final long serialVersionUID = 1L;

	public UserBrowsingContextPK() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(final int userId) {
		this.userId = userId;
	}

	public int getTrackInfoId() {
		return trackInfoId;
	}

	public void setTrackInfoId(final int trackInfoId) {
		this.trackInfoId = trackInfoId;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof UserBrowsingContextPK))
			return false;
		final UserBrowsingContextPK other = (UserBrowsingContextPK) o;
		return userId == other.userId && trackInfoId == other.trackInfoId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + userId;
		hash = hash * prime + trackInfoId;
		return hash;
	}
}
