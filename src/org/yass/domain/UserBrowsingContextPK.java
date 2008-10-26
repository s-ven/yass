package org.yass.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserBrowsingContextPK implements Serializable {
	@Column(name="USER_ID", insertable=false, updatable=false)
	private int userId;

	@Column(name="TRACK_INFO_ID")
	private int trackInfoId;

	private static final long serialVersionUID = 1L;

	public UserBrowsingContextPK() {
		super();
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTrackInfoId() {
		return this.trackInfoId;
	}

	public void setTrackInfoId(int trackInfoId) {
		this.trackInfoId = trackInfoId;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof UserBrowsingContextPK)) {
			return false;
		}
		UserBrowsingContextPK other = (UserBrowsingContextPK) o;
		return (this.userId == other.userId)
			&& (this.trackInfoId == other.trackInfoId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.trackInfoId;
		return hash;
	}

}
