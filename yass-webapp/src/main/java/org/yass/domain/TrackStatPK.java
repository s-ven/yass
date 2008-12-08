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

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 
 * @author svenduzont
 */
@Embeddable
public class TrackStatPK {

	/**
     *
     */
	@Column(name = "TRACK_ID")
	protected int trackId;
	/**
     *
     */
	@Column(name = "USER_ID")
	protected int userId;

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
