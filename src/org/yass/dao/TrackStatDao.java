/*
 Copyright (c) 2008 Sven Duzont sven.duzont@gmail.com> All rights reserved.

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"),
 to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is furnished
 to do so, subject to the following conditions: The above copyright notice
 and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS",
 WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
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

public class TrackStatDao extends AbstractDao<TrackStat> {

	private class TrackStatRowMapper implements ParameterizedRowMapper<TrackStat> {

		public TrackStat mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			return new TrackStat(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getTimestamp(4), rs.getInt(5), rs
					.getTimestamp(6));
		}
	}

	private final static TrackStatDao instance = new TrackStatDao();
	private static final Log LOG = LogFactory.getLog(TrackStatDao.class);

	public final static TrackStatDao getInstance() {
		return instance;
	}

	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into track_stat (user_id, track_id, rating, last_played, play_count, last_selected) values (?, ?, ?, ?, ?, ?)");
	private final TrackStatRowMapper rowMapper = new TrackStatRowMapper();

	private TrackStatDao() {
		pscf.addParameter(new SqlParameter("track_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("user_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("rating", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("last_played", java.sql.Types.TIMESTAMP));
		pscf.addParameter(new SqlParameter("play_count", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("last_selected", java.sql.Types.TIMESTAMP));
	}

	public final Map<Integer, TrackStat> getFromUserId(final int id) {
		LOG.info("Loading Track Stats from User id:" + id);
		final Iterator<TrackStat> it = getJdbcTemplate().query(
				"select user_id, track_id, rating, last_played, play_count, last_selected from track_stat where user_id = ?",
				new Object[] { id }, rowMapper).iterator();
		final Map<Integer, TrackStat> map = new LinkedHashMap<Integer, TrackStat>();
		while (it.hasNext()) {
			final TrackStat tStat = it.next();
			map.put(tStat.getTrackId(), tStat);
		}
		return map;
	}

	public void save(final TrackStat trackStat) {
		getJdbcTemplate().update("delete from track_stat where track_id = ? and user_id = ?",
				new Object[] { trackStat.getTrackId(), trackStat.getUserId() });
		getJdbcTemplate().update(
				pscf.newPreparedStatementCreator(new Object[] { trackStat.getUserId(), trackStat.getTrackId(),
						trackStat.getRating(), trackStat.getLastPlayed(), trackStat.getPlayCount(), trackStat.getLastSelected() }));
	}
}
