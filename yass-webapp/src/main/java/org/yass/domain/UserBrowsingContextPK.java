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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author svenduzont
 */
@Embeddable
public class UserBrowsingContextPK implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "TRACK_INFO_ID")
	private int trackInfoId;
	@Column(name = "USER_ID")
	private int userId;

	/**
     *
     */
	public UserBrowsingContextPK() {
		super();
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

	/**
	 * 
	 * @return
	 */
	public int getTrackInfoId() {
		return trackInfoId;
	}

	/**
	 * 
	 * @return
	 */
	public int getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + userId;
		hash = hash * prime + trackInfoId;
		return hash;
	}

	/**
	 * 
	 * @param trackInfoId
	 */
	public void setTrackInfoId(final int trackInfoId) {
		this.trackInfoId = trackInfoId;
	}

	/**
	 * 
	 * @param userId
	 */
	public void setUserId(final int userId) {
		this.userId = userId;
	}
}
