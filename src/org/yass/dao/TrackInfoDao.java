package org.yass.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yass.domain.TrackInfo;

public class TrackInfoDao extends AbstractDao {

	private static final Log LOG = LogFactory.getLog(TrackInfoDao.class);
	private final TrackInfoRowMapper rowMapper = new TrackInfoRowMapper();
	private final PreparedStatementCreatorFactory trackInfoPscf = new PreparedStatementCreatorFactory(
			"insert into track_info (type, value) values (?, ?) ");

	public TrackInfoDao() {
		trackInfoPscf.addParameter(new SqlParameter("type", java.sql.Types.VARCHAR));
		trackInfoPscf.addParameter(new SqlParameter("value", java.sql.Types.VARCHAR));
		trackInfoPscf.setReturnGeneratedKeys(true);
	}

	public void save(final TrackInfo trackInfo) {
		if (trackInfo.id == 0) {
			final PreparedStatementCreator psc = trackInfoPscf.newPreparedStatementCreator(new Object[] { trackInfo.type,
					trackInfo.value });
			final KeyHolder kh = new GeneratedKeyHolder();
			getJdbcTempate().update(psc, kh);
			trackInfo.id = kh.getKey().intValue();
		}
	}

	public TrackInfo getFromValue(final String value, final String type) {
		final Iterator<TrackInfo> it = getJdbcTempate().query(
				"select id, type, value from track_info where value = ? and type = ?", new Object[] { value, type }, rowMapper)
				.iterator();
		TrackInfo trackInfo = null;
		if (it.hasNext())
			trackInfo = it.next();
		else {
			trackInfo = new TrackInfo(0, type, value);
			this.save(trackInfo);
		}
		return trackInfo;
	}

	public TrackInfo getFromId(final int id) {
		final Iterator<TrackInfo> it = getJdbcTempate().query("select id, type, value from track_info where id = ?",
				new Object[] { id }, rowMapper).iterator();
		TrackInfo trackInfo = null;
		if (it.hasNext())
			trackInfo = it.next();
		return trackInfo;
	}

	public Collection<TrackInfo> getFromTrackId(final int trackId) {
		return getJdbcTempate().query(
				"select id, type, value from track_info, track_track_info where track_id = ? and track_info_id = id",
				new Object[] { trackId }, rowMapper);
	}

	private static class TrackInfoRowMapper implements ParameterizedRowMapper<TrackInfo> {

		public TrackInfo mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new TrackInfo(rs.getInt(1), rs.getString(2), rs.getString(3));
		}
	}
}
