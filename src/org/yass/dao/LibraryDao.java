package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.Library;

public class LibraryDao extends AbstractDao {

	private static final LibraryDao instance = new LibraryDao();
	private static final Log LOG = LogFactory.getLog(LibraryDao.class);

	/**
	 * @return the instance
	 */
	public static final LibraryDao getInstance() {
		return instance;
	}

	private LibraryDao() {
	}

	public void save(final Library lib) {
		LOG.info("Saving Library...");
		try {
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(lib);
			getEntityManager().getTransaction().commit();
			if (LOG.isInfoEnabled())
				LOG.info("Library saved id:" + lib.getId());
		} catch (final Exception e) {
			LOG.error("Error while persisting library", e);
		} finally {
		}
	}

	public Library getFromId(final int id) {
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Loading Library id:" + id);
			return (Library) getEntityManager().createNamedQuery("getLibraryById").setParameter(1, id).getSingleResult();
		} catch (final Exception e) {
			LOG.error("Error while getting library", e);
			return null;
		}
	}

	public Library getFromUserId(final int id) {
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Loading Library user_id:" + id);
			return (Library) getEntityManager().createNamedQuery("getLibraryByUserId").setParameter(1, id).getSingleResult();
		} catch (final Exception e) {
			LOG.error("Error while getting library", e);
			return null;
		}
	}
}
