package org.yass.dao;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.yass.domain.Library;

public class LibraryDao extends AbstractDao {

	/**
	 * @return the instance
	 */
	public static final LibraryDao getInstance() {
		return instance;
	}

	private static final LibraryDao instance = new LibraryDao();
	private static final Log LOG = LogFactory.getLog(LibraryDao.class);
	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into library (path, last_update) values (?, ?) ");

	private LibraryDao() {
		pscf.addParameter(new SqlParameter("path", java.sql.Types.VARCHAR));
		pscf.addParameter(new SqlParameter("last_update", java.sql.Types.TIMESTAMP));
		pscf.setReturnGeneratedKeys(true);
	}

	public void saveLibrary(final Library lib) {
		LOG.info("Saving Library");
		try {
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(lib);
			getEntityManager().getTransaction().commit();
			if (LOG.isInfoEnabled())
				LOG.info(" Library saved id:" + lib.getId());
		} catch (final Exception e) {
			LOG.error("Error while persisting library", e);
		} finally {
		}
	}

	public Library getFromId(final int id) {
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Loading Library id:" + id);
			final Query q = getEntityManager().createNamedQuery("getLibraryById").setParameter(1, id);
			return (Library) q.getSingleResult();
		} catch (final Exception e) {
			return null;
		}
	}

	public Library getFromUserId(final int id) {
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Loading Library user_id:" + id);
			final Query q = getEntityManager().createNamedQuery("getLibraryByUserId").setParameter(1, id);
			return (Library) q.getSingleResult();
		} catch (final Exception e) {
			return null;
		}
	}
}
