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
package org.yass.struts.track;

import java.util.Date;

import org.yass.YassConstants;
import org.yass.domain.TrackStat;
import org.yass.domain.User;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	private static final long serialVersionUID = 3411435373847531163L;
	public int id;
	public long lastPlayed;
	public int playCount;
	public int rating;

	@Override
	public String execute() {
		if (LOG.isInfoEnabled())
			LOG.info("Saving TrackStat for Track id:" + id);
		final User user = getUser();
		TrackStat trackStat = user.getTracksStats().get(id);
		if (trackStat == null)
			user.getTracksStats().put(id, trackStat = new TrackStat(user, id));
		trackStat.setRating(rating);
		trackStat.setPlayCount(playCount);
		trackStat.setLastPlayed(new Date(lastPlayed));
		USER_DAO.save(user);
		return NONE;
	}
}
