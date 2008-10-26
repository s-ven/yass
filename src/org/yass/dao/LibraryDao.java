package org.yass.dao;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yass.domain.Library;

public class LibraryDao extends AbstractDao {

	private static final TrackDao trackDao = new TrackDao();
	private static final Log LOG = LogFactory.getLog(LibraryDao.class);
	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into library (path, last_update) values (?, ?) ");

	public LibraryDao() {
		pscf.addParameter(new SqlParameter("path", java.sql.Types.VARCHAR));
		pscf.addParameter(new SqlParameter("last_update", java.sql.Types.TIMESTAMP));
		pscf.setReturnGeneratedKeys(true);
	}

	public void saveLibrary(final Library lib) {
		LOG.info("Saving Library");
		if (lib.getId() == 0) {
			final PreparedStatementCreator pst = pscf.newPreparedStatementCreator(new Object[] { lib.getPath(),
					lib.getLastUpdate() });
			final KeyHolder kh = new GeneratedKeyHolder();
			getJdbcTempate().update(pst, kh);
			lib.setId(kh.getKey().intValue());
			if (LOG.isInfoEnabled())
				LOG.info(" new Library created id:" + lib.getId());
		}
	}

	public Library getFromId(final int id) {
		LOG.info("Loading Library id:" + id);
		final Query q = getEntityManager().createNamedQuery("getLibraryById");
		q.setParameter(1, id);
		return (Library) q.getSingleResult();
	}
}
