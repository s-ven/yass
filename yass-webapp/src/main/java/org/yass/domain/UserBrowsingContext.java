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

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author svenduzont
 */
@Entity
@Table(name = "USER_BROWSING_CONTEXT")
public class UserBrowsingContext implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private UserBrowsingContextPK pk;
	@ManyToOne
	private User user;

	/**
     *
     */
	public UserBrowsingContext() {
		super();
	}

	/**
	 * 
	 * @param user
	 */
	public UserBrowsingContext(final User user) {
		super();
		pk = new UserBrowsingContextPK();
		pk.setUserId(user.getId());
	}

	/**
	 * 
	 * @param user
	 * @param trackInfoId
	 */
	public UserBrowsingContext(final User user, final int trackInfoId) {
		super();
		pk = new UserBrowsingContextPK();
		pk.setUserId(user.getId());
		pk.setTrackInfoId(trackInfoId);
	}

	/**
	 * 
	 * @return
	 */
	public int getTrackInfoId() {
		return pk.getTrackInfoId();
	}

	/**
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 
	 * @param trackInfoId
	 */
	public void setTrackInfoId(final Integer trackInfoId) {
		pk.setTrackInfoId(trackInfoId);
	}

	/**
	 * 
	 * @param user
	 */
	public void setUser(final User user) {
		this.user = user;
	}
}
