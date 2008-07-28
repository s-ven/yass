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
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;

public class TrackDao extends AbstractDao {

	private static final Log LOG = LogFactory.getLog(TrackDao.class);
	private final PreparedStatementCreatorFactory insertTrackPscf = new PreparedStatementCreatorFactory(
			"insert into track (library_id, path, track_nr, title, last_modified, length, track_type_id, vbr) values (?, ?, ?, ?, ?, ?, ?, ?) ");
	private final PreparedStatementCreatorFactory updateTrackPscf = new PreparedStatementCreatorFactory(
			"update track set track_nr = ?, title = ?, last_modified = ?, vbr = ? where id = ? ");
	private final PreparedStatementCreatorFactory insertTrackInfoPscf = new PreparedStatementCreatorFactory(
			"insert into track_info (type, value) values (?, ?) ");
	private final static TrackInfoDao trackInfoDao = new TrackInfoDao();
	private final TrackRowMapper rowMapper = new TrackRowMapper();

	public TrackDao() {
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
		insertTrackPscf.setReturnGeneratedKeys(true);
		insertTrackInfoPscf.addParameter(new SqlParameter("type", java.sql.Types.VARCHAR));
		insertTrackInfoPscf.addParameter(new SqlParameter("value", java.sql.Types.VARCHAR));
		insertTrackInfoPscf.setReturnGeneratedKeys(true);
	}

	public void save(final Track track) {
		if (track.getId() == 0) {
			final PreparedStatementCreator insertPsc = insertTrackPscf.newPreparedStatementCreator(new Object[] {
					track.getLibrary().id, track.getPath(), track.getTrackNr(), track.getTitle(), track.getLastModified(),
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
		getJdbcTempate().execute("delete from track_stat where track_id = " + track.getId());
		getJdbcTempate().execute("delete from simple_playlist where track_id = " + track.getId());
		getJdbcTempate().execute("delete from track_track_info where track_id = " + track.getId());
		getJdbcTempate().execute("delete from track where id = " + track.getId());
	}

	public final void fillLibrary(final Library lib) {
		final Iterator<Track> it = getJdbcTempate().query(
				"select id, path, title, track_nr, length, last_modified, track_type_id, vbr from track where library_id = ?",
				new Object[] { lib.id }, rowMapper).iterator();
		while (it.hasNext()) {
			final Track track = it.next();
			lib.add(track);
			track.setTrackInfos(trackInfoDao.getFromTrackId(track.getId()));
		}
	}

	public final Track getFromPath(final Library lib, final String path) {
		Track track = null;
		final Iterator<Track> it = getJdbcTempate().query(
				"select id, path, title, track_nr, length, "
						+ "last_modified, track_type_id from track where library_id = ? and path = ?",
				new Object[] { lib.id, path }, rowMapper).iterator();
		if (it.hasNext()) {
			track = it.next();
			track.setTrackInfos(trackInfoDao.getFromTrackId(track.getId()));
			track.setLibrary(lib);
		}
		return track;
	}

	private class TrackRowMapper implements ParameterizedRowMapper<Track> {

		public Track mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new Track(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getTimestamp(6),
					rs.getInt(7), rs.getInt(8) == 1);
		}
	}
}
