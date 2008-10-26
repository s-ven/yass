package org.yass.dao;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;

public class TrackDao extends AbstractDao {

	private static final TrackDao instance = new TrackDao();
	private static final Log LOG = LogFactory.getLog(TrackDao.class);
	private final PreparedStatementCreatorFactory insertTrackPscf = new PreparedStatementCreatorFactory(
			"insert into track (library_id, path, track_nr, title, last_modified, length, track_type_id, vbr) values (?, ?, ?, ?, ?, ?, ?, ?) ");
	private final PreparedStatementCreatorFactory updateTrackPscf = new PreparedStatementCreatorFactory(
			"update track set track_nr = ?, title = ?, last_modified = ?, vbr = ? where id = ? ");

	private TrackDao() {
		insertTrackPscf.addParameter(new SqlParameter("library_id", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("path", java.sql.Types.VARCHAR));
		insertTrackPscf.addParameter(new SqlParameter("track_nr", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("title", java.sql.Types.VARCHAR));
		insertTrackPscf.addParameter(new SqlParameter("last_modified", java.sql.Types.TIMESTAMP));
		insertTrackPscf.addParameter(new SqlParameter("length", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("track_type_id", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("vbr", java.sql.Types.INTEGER));
		insertTrackPscf.setReturnGeneratedKeys(true);
		updateTrackPscf.addParameter(new SqlParameter("track_nr", java.sql.Types.INTEGER));
		updateTrackPscf.addParameter(new SqlParameter("title", java.sql.Types.VARCHAR));
		updateTrackPscf.addParameter(new SqlParameter("last_modified", java.sql.Types.TIMESTAMP));
		updateTrackPscf.addParameter(new SqlParameter("vbr", java.sql.Types.INTEGER));
		updateTrackPscf.addParameter(new SqlParameter("id", java.sql.Types.INTEGER));
	}

	public void save(final Track track) {
		if (track.getId() == 0) {
			final PreparedStatementCreator insertPsc = insertTrackPscf.newPreparedStatementCreator(new Object[] {
					track.getLibrary().getId(), track.getPath(), track.getTrackNr(), track.getTitle(), track.getLastModified(),
					track.getLength(), track.getTypeId(), track.isVBR() ? 1 : 0 });
			final KeyHolder kh = new GeneratedKeyHolder();
			getJdbcTempate().update(insertPsc, kh);
			track.setId(kh.getKey().intValue());
		} else
			getJdbcTempate().update(
					updateTrackPscf.newPreparedStatementCreator(new Object[] { track.getTrackNr(), track.getTitle(),
							track.getLastModified(), track.isVBR() ? 1 : 0, track.getId() }));
		getJdbcTempate().execute("delete from track_track_info where track_id = " + track.getId());
		for (final TrackInfo trackInfo : track.getTrackInfos())
			getJdbcTempate().update("insert into track_track_info (track_id, track_info_id) values(?, ?)",
					new Object[] { track.getId(), trackInfo.getId() });
	}

	public void delete(final Track track) {
		if (LOG.isDebugEnabled())
			LOG.debug("Deleting track " + track + " from database");
		getJdbcTempate().execute("delete from track_stat where track_id = " + track.getId());
		getJdbcTempate().execute("delete from simple_playlist where track_id = " + track.getId());
		getJdbcTempate().execute("delete from track_track_info where track_id = " + track.getId());
		getJdbcTempate().execute("delete from track where id = " + track.getId());
	}

	public final Track getFromPath(final String path) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Trying to load track path " + path + " from database");
			final Query q = getEntityManager().createNamedQuery("getTrackByPath");
			q.setParameter(1, path);
			return (Track) q.getSingleResult();
		} catch (final Exception e) {
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
