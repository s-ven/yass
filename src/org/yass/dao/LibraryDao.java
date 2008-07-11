package org.yass.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yass.domain.LibraryPlayList;

public class LibraryDao extends AbstractDao {

	private static final TrackDao trackDao = new TrackDao();
	private static final Log LOG = LogFactory.getLog(LibraryDao.class);
	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into library (path, last_update) values (?, ?) ");
	private final LibraryRowMapper rowMapper = new LibraryRowMapper();

	public LibraryDao() {
		pscf.addParameter(new SqlParameter("path", java.sql.Types.VARCHAR));
		pscf.addParameter(new SqlParameter("last_update", java.sql.Types.TIMESTAMP));
		pscf.setReturnGeneratedKeys(true);
	}

	public void saveLibrary(final LibraryPlayList lib) {
		LOG.info("Saving Library");
		if (lib.id == 0) {
			final PreparedStatementCreator pst = pscf.newPreparedStatementCreator(new Object[] { lib.path, lib.lastUpdate });
			final KeyHolder kh = new GeneratedKeyHolder();
			this.getJdbcTempate().update(pst, kh);
			lib.id = kh.getKey().intValue();
			LOG.info(" new Library created id:" + lib.id);
		}
	}

	public LibraryPlayList getFromId(final int id) {
		LOG.info("Loading Library id:" + id);
		LibraryPlayList lib = null;
		final Iterator<LibraryPlayList> it = getJdbcTempate().query(
				"select id, path, last_update from library where id = ?", new Object[] { id }, rowMapper).iterator();
		if (it.hasNext()) {
			lib = it.next();
			trackDao.fillLibrary(lib);
			LOG.info(" Library succefuly loaded");
		}
		return lib;
	}

	private static class LibraryRowMapper implements ParameterizedRowMapper<LibraryPlayList> {

		public LibraryPlayList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new LibraryPlayList(rs.getInt(1), rs.getString(2), rs.getDate(3));
		}
	}
}
