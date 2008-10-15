package org.yass.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.yass.domain.TrackStat;

public class TrackStatDao extends AbstractDao {

	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into track_stat (user_id, track_id, rating, last_played, play_count, last_selected) values (?, ?, ?, ?, ?, ?)");

	public TrackStatDao() {
		pscf.addParameter(new SqlParameter("track_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("user_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("rating", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("last_played", java.sql.Types.TIMESTAMP));
		pscf.addParameter(new SqlParameter("play_count", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("last_selected", java.sql.Types.TIMESTAMP));
	}

	private static final Log LOG = LogFactory.getLog(TrackStatDao.class);
	private final TrackStatRowMapper rowMapper = new TrackStatRowMapper();

	public void save(final TrackStat trackStat) {
		getJdbcTempate().update("delete from track_stat where track_id = ? and user_id = ?",
				new Object[] { trackStat.getTrackId(), trackStat.getUserId() });
		getJdbcTempate().update(
				pscf.newPreparedStatementCreator(new Object[] { trackStat.getUserId(), trackStat.getTrackId(),
						trackStat.getRating(), trackStat.getLastPlayed(), trackStat.getPlayCount(), trackStat.getLastSelected() }));
	}

	public final Map<Integer, TrackStat> getFromUserId(final int usrId) {
		LOG.info("Loading Track Stats from User id:" + usrId);
		final Iterator<TrackStat> it = getJdbcTempate().query(
				"select user_id, track_id, rating, last_played, play_count, last_selected from track_stat where user_id = ?",
				new Object[] { usrId }, rowMapper).iterator();
		final Map<Integer, TrackStat> map = new LinkedHashMap<Integer, TrackStat>();
		while (it.hasNext()) {
			final TrackStat tStat = it.next();
			map.put(tStat.getTrackId(), tStat);
		}
		return map;
	}

	private class TrackStatRowMapper implements ParameterizedRowMapper<TrackStat> {

		public TrackStat mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new TrackStat(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getTimestamp(4), rs.getInt(5), rs
					.getTimestamp(6));
		}
	}
}
