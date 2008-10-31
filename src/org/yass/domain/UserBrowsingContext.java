package org.yass.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_BROWSING_CONTEXT")
public class UserBrowsingContext implements Serializable {

	@EmbeddedId
	private UserBrowsingContextPK pk;
	@ManyToOne
	private User user;
	private static final long serialVersionUID = 1L;

	public UserBrowsingContext() {
		super();
	}

	public UserBrowsingContext(final User user) {
		super();
		pk = new UserBrowsingContextPK();
		pk.setUserId(user.getId());
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public int getTrackInfoId() {
		return pk.getTrackInfoId();
	}

	public void setTrackInfoId(final Integer trackInfoId) {
		pk.setTrackInfoId(trackInfoId);
	}
}
