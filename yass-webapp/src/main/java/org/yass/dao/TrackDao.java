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
package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.Track;

public class TrackDao extends AbstractDao<Track> {

	private static final TrackDao instance = new TrackDao();
	private static final Log LOG = LogFactory.getLog(TrackDao.class);

	/**
	 * @return the instance
	 */
	public static final TrackDao getInstance() {
		return instance;
	}

	private TrackDao() {
	}

	public final Track findById(final int id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get Track id:" + id);
			return getSingleResult(createNamedQuery("findTrackById").setParameter(1, id));
		} catch (final Exception e) {
			if (LOG.isDebugEnabled())
				LOG.error("Error getting Track id:" + id, e);
		}
		return null;
	}

	public final Track findByPath(final String path) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get Track path:" + path);
			return getSingleResult(createNamedQuery("findTrackByPath").setParameter(1, path));
		} catch (final Exception e) {
			if (LOG.isDebugEnabled())
				LOG.debug("Error getting Track path:" + path, e);
		}
		return null;
	}

	public Track save(final Track track) {
		beginTransaction();
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Save Track path:" + track.getPath());
			persist(track);
			commitTransaction();
		} catch (final Exception e) {
			rollbackTransaction();
			LOG.fatal("Error saving Track path:" + track.getPath(), e);
		}
		return track;
	}
}
