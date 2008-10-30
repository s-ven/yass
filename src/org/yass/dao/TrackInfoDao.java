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
}
