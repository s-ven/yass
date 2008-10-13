package org.yass.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.yass.domain.Settings;

public class SettingsDao extends AbstractDao {

	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into user_setting (user_id, loaded_track_id, volume, shuffle, repeat, show_remaining, display_mode, stop_fadeout, skip_fadeout, next_fadeout) values (?,?,?,?,?,?,?,?,?,?)");

	public SettingsDao() {
		pscf.addParameter(new SqlParameter("user_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("loaded_track_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("volume", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("shuffle", java.sql.Types.SMALLINT));
		pscf.addParameter(new SqlParameter("repeat", java.sql.Types.SMALLINT));
		pscf.addParameter(new SqlParameter("show_remaining", java.sql.Types.SMALLINT));
		pscf.addParameter(new SqlParameter("display_mode", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("stop_fadeout", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("skip_fadeout", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("next_fadeout", java.sql.Types.INTEGER));
	}

	private static final Log LOG = LogFactory.getLog(SettingsDao.class);
	private final SettingsRowMapper rowMapper = new SettingsRowMapper();

	public void save(final Settings settings) {
		getJdbcTempate().update("delete from user_setting where user_id = ?", new Object[] { settings.getUserId() });
		getJdbcTempate().update(
				pscf.newPreparedStatementCreator(new Object[] { settings.getUserId(), settings.getLoadedTrackId(),
						settings.getVolume(), settings.isShuffle() ? 1 : 0, settings.isRepeat() ? 1 : 0,
						settings.isShowRemaining() ? 1 : 0, settings.getDisplayMode(), settings.getStopFadeout(),
						settings.getSkipFadeout(), settings.getNextFadeout() }));
		getJdbcTempate().update("delete from user_browsing_context where user_id = ?",
				new Object[] { settings.getUserId() });
		for (final Integer trackInfoId : settings.getTrackInfoIds())
			getJdbcTempate().update("insert into user_browsing_context (user_id, track_info_id) values (?, ?)",
					new Object[] { settings.getUserId(), trackInfoId });
	}

	public final Settings getFromUserId(final int usrId) {
		LOG.info("Loading Settings for user id:" + usrId);
		final Iterator<Settings> it = getJdbcTempate()
				.query(
						"select user_id, loaded_track_id, volume, shuffle, repeat, show_remaining, display_mode, stop_fadeout, skip_fadeout, next_fadeout from user_setting where user_id = ?",
						new Object[] { usrId }, rowMapper).iterator();
		if (it.hasNext())
			return it.next();
		return null;
	}

	private class SettingsRowMapper implements ParameterizedRowMapper<Settings> {

		public Settings mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final ArrayList<Integer> trackInfos = new ArrayList<Integer>();
			final Iterator<Map<String, Integer>> it = DaoHelper.getInstance().getJdbcTemplate().queryForList(
					"select track_info_id from user_browsing_context where user_id = ?", new Object[] { rs.getInt(1) })
					.iterator();
			while (it.hasNext())
				trackInfos.add(it.next().get("TRACK_INFO_ID"));
			return new Settings(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4) == 1, rs.getInt(5) == 1,
					rs.getInt(6) == 1, rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), trackInfos
							.toArray(new Integer[] {}));
		}
	}
}
