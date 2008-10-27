package org.yass.dao;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.TrackInfo;

public class TrackInfoDao extends AbstractDao {

	/**
	 * @return the instance
	 */
	public static final TrackInfoDao getInstance() {
		return instance;
	}

	private static final TrackInfoDao instance = new TrackInfoDao();
	private static final Log LOG = LogFactory.getLog(TrackInfoDao.class);

	public TrackInfo getFromValue(final String value, final String type) {
		try {
			final Query q = getEntityManager().createNamedQuery("getFromTypeAndValue").setParameter(1, type).setParameter(2,
					value);
			return (TrackInfo) q.getSingleResult();
		} catch (final Exception e) {
			final TrackInfo trackInfo = new TrackInfo(0, type, value);
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(trackInfo);
			getEntityManager().getTransaction().commit();
			return trackInfo;
		}
	}
}
