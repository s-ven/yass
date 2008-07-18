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
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;

public class TrackDao extends AbstractDao {

	private static final Log LOG = LogFactory.getLog(TrackDao.class);
	private final PreparedStatementCreatorFactory trackPscf = new PreparedStatementCreatorFactory(
			"insert into track (library_id, path, track_nr, title, last_update, length) values (?, ?, ?, ?, ?, ?) ");
	private final PreparedStatementCreatorFactory trackInfoPscf = new PreparedStatementCreatorFactory(
			"insert into track_info (type, value) values (?, ?) ");
	private final static TrackInfoDao trackInfoDao = new TrackInfoDao();
	private final TrackRowMapper rowMapper = new TrackRowMapper();

	public TrackDao() {
		trackPscf.addParameter(new SqlParameter("library_id", java.sql.Types.INTEGER));
		trackPscf.addParameter(new SqlParameter("path", java.sql.Types.VARCHAR));
		trackPscf.addParameter(new SqlParameter("track_nr", java.sql.Types.INTEGER));
		trackPscf.addParameter(new SqlParameter("title", java.sql.Types.VARCHAR));
		trackPscf.addParameter(new SqlParameter("last_update", java.sql.Types.DATE));
		trackPscf.addParameter(new SqlParameter("length", java.sql.Types.INTEGER));
		trackPscf.setReturnGeneratedKeys(true);
		trackInfoPscf.addParameter(new SqlParameter("type", java.sql.Types.VARCHAR));
		trackInfoPscf.addParameter(new SqlParameter("value", java.sql.Types.VARCHAR));
		trackInfoPscf.setReturnGeneratedKeys(true);
	}

	public void save(final Track track) {
		if (track.getId() == 0) {
			final PreparedStatementCreator psc = trackPscf.newPreparedStatementCreator(new Object[] {
					track.getLibrary().getId(), track.getPath(), track.getTrackNr(), track.getTitle(), track.getLastUpdate(),
					track.getLength() });
			final KeyHolder kh = new GeneratedKeyHolder();
			getJdbcTempate().update(psc, kh);
			track.setId(kh.getKey().intValue());
		} else
			getJdbcTempate().update("update track set rating = ? where id = ?",
					new Object[] { track.getRating(), track.getId() });
		getJdbcTempate().execute("delete from track_track_info where track_id = " + track.getId());
		for (final TrackInfo trackInfo : track.getTrackInfos())
			getJdbcTempate().update("insert into track_track_info (track_id, track_info_id) values(?, ?)",
					new Object[] { track.getId(), trackInfo.getId() });
	}

	public final void fillLibrary(final LibraryPlayList lib) {
		final Iterator<Track> it = getJdbcTempate().query(
				"select id, path, title, track_nr, length, "
						+ "last_update, play_count, rating from track where library_id = ?", new Object[] { lib.id }, rowMapper)
				.iterator();
		while (it.hasNext()) {
			final Track track = it.next();
			lib.add(track);
			track.setTrackInfos(trackInfoDao.getFromTrackId(track.getId()));
		}
	}

	public final Track getFromPath(final LibraryPlayList lib, final String path) {
		Track track = null;
		final Iterator<Track> it = getJdbcTempate().query(
				"select id, path, title, track_nr, length, "
						+ "last_update, play_count, rating from track where library_id = ? and path = ?",
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
					rs.getInt(7), rs.getInt(8));
		}
	}
}
