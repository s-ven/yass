package org.yass.dao;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.Track;

public class TrackDao extends AbstractDao {

	private static final TrackDao instance = new TrackDao();
	private static final Log LOG = LogFactory.getLog(TrackDao.class);

	private TrackDao() {
	}

	public void save(final Track track) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Saving track " + track.getPath());
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(track);
			getEntityManager().getTransaction().commit();
		} catch (final Exception e) {
			LOG.error(e);
		}
	}

	public final Track getByPath(final String path) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Trying to load track by path:" + path + " from database");
			final Query q = getEntityManager().createNamedQuery("getTrackByPath").setParameter(1, path);
			return (Track) q.getSingleResult();
		} catch (final Exception e) {
			return null;
		}
	}

	public final Track getById(final int id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Trying to load track by id:" + id + " from database");
			final Query q = getEntityManager().createNamedQuery("getTrackById").setParameter(1, id);
			return (Track) q.getSingleResult();
		} catch (final Exception e) {
			LOG.error("Error while trying to get Track id:" + id, e);
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
