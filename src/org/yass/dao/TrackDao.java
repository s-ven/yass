package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.Track;

public class TrackDao extends AbstractDao {

	private static final TrackDao instance = new TrackDao();
	private static final Log LOG = LogFactory.getLog(TrackDao.class);

	private TrackDao() {
	}

	public Track save(final Track track) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Save Track path:" + track.getPath());
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(track);
			getEntityManager().getTransaction().commit();
		} catch (final Exception e) {
			LOG.fatal("Error saving Track path:" + track.getPath(), e);
		}
		return track;
	}

	public final Track getByPath(final String path) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get Track path:" + path);
			return (Track) getEntityManager().createNamedQuery("getTrackByPath").setParameter(1, path).getSingleResult();
		} catch (final Exception e) {
			LOG.warn("Error getting Track path:" + path, e);
			return null;
		}
	}

	public final Track getById(final int id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get Track id:" + id);
			return (Track) getEntityManager().createNamedQuery("getTrackById").setParameter(1, id).getSingleResult();
		} catch (final Exception e) {
			LOG.error("Error getting Track id:" + id, e);
			return null;
		}
	}

	/**
	 * @return the instance
	 */
	public static final TrackDao getInstance() {
		return instance;
	}
}
