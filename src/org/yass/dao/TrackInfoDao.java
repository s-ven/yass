package org.yass.dao;

import org.yass.domain.TrackInfo;

public class TrackInfoDao extends AbstractDao {

	/**
	 * @return the instance
	 */
	public static final TrackInfoDao getInstance() {
		return instance;
	}

	private static final TrackInfoDao instance = new TrackInfoDao();

	public TrackInfo getFromValue(final String value, final String type) {
		try {
			return (TrackInfo) getEntityManager().createNamedQuery("getFromTypeAndValue").setParameter(1, type).setParameter(
					2, value).getSingleResult();
		} catch (final Exception e) {
			final TrackInfo trackInfo = new TrackInfo(type, value);
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(trackInfo);
			getEntityManager().getTransaction().commit();
			return trackInfo;
		}
	}
}
