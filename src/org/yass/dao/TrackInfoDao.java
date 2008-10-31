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
import org.yass.domain.TrackInfo;

public class TrackInfoDao extends AbstractDao {

	private static final Log LOG = LogFactory.getLog(TrackInfoDao.class);

	/**
	 * @return the instance
	 */
	public static final TrackInfoDao getInstance() {
		return instance;
	}

	private static final TrackInfoDao instance = new TrackInfoDao();

	public TrackInfo getFromValue(final String value, final String type) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get TrackInfo type:" + type + ", value:" + value);
			return (TrackInfo) getEntityManager().createNamedQuery("getFromTypeAndValue").setParameter(1, type).setParameter(
					2, value).getSingleResult();
		} catch (final Exception e) {
			if (LOG.isDebugEnabled())
				LOG.debug("TrackInfo type:" + type + ", value:" + value + " doesn't exist, persisting it...");
			return save(new TrackInfo(type, value));
		}
	}

	private TrackInfo save(final TrackInfo trackInfo) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Save TrackInfo type:" + trackInfo.getType() + ", value:" + trackInfo.getValue());
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(trackInfo);
			getEntityManager().getTransaction().commit();
		} catch (final Exception e) {
			if (LOG.isDebugEnabled())
				LOG.debug("Error saving TrackInfo type:" + trackInfo.getType() + ", value:" + trackInfo.getValue());
		}
		return trackInfo;
	}

	public TrackInfo getFromId(final Integer id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get TrackInfo id:" + id);
			return (TrackInfo) getEntityManager().createNamedQuery("getFromId").setParameter(1, id);
		} catch (final Exception e) {
			if (LOG.isDebugEnabled())
				LOG.debug("TrackInfo id:" + id);
		}
		return null;
	}
}
