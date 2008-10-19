package org.yass.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.yass.domain.PlayList;
import org.yass.domain.SimplePlayList;
import org.yass.domain.SmartPlayList;
import org.yass.domain.SmartPlayListCondition;

public class PlayListDao extends AbstractDao {

	private static final Log LOG = LogFactory.getLog(PlayListDao.class);
	private final PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
			"insert into playlist (type_id, user_id, name, last_update) values (?, ?, ?, ?) ");
	private final PlayListRowMapper rowMapper = new PlayListRowMapper();

	public PlayListDao() {
		pscf.addParameter(new SqlParameter("type_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("user_id", java.sql.Types.INTEGER));
		pscf.addParameter(new SqlParameter("name", java.sql.Types.VARCHAR));
		pscf.addParameter(new SqlParameter("last_update", java.sql.Types.TIMESTAMP));
		pscf.setReturnGeneratedKeys(true);
	}

	public void savePlaylist(final PlayList plst) {
		LOG.info("Saving PlayList");
		if (plst.getId() == 0) {
			final PreparedStatementCreator pst = pscf.newPreparedStatementCreator(new Object[] { plst.getTypeId(),
					plst.getUserId(), plst.getName(), plst.getLastUpdate() });
			final KeyHolder kh = new GeneratedKeyHolder();
			this.getJdbcTempate().update(pst, kh);
			plst.setId(kh.getKey().intValue());
			LOG.info(" new PlayList created id:" + plst.getId());
		} else {
			getJdbcTempate().update("update playlist set name = ?, last_update = ?",
					new Object[] { plst.getName(), new Date() });
			if (plst instanceof SimplePlayList) {
				getJdbcTempate().execute("delete from simple_playlist where playlist_id = " + plst.getId());
				int trackOrder = 0;
				for (final int trackId : plst.getTrackIds())
					getJdbcTempate().update("insert into simple_playlist (playlist_id, track_id, track_order) values (?, ?, ?)",
							new Object[] { plst.getId(), trackId, trackOrder++ });
			}
		}
	}

	public Map<Integer, PlayList> getFromUserId(final int userId) {
		LOG.info("Loading Playlist from user_id:" + userId);
		final Map<Integer, PlayList> plsts = new LinkedHashMap<Integer, PlayList>();
		final Iterator<PlayList> it = getJdbcTempate().query(
				"select id, type_id, name, last_update from playlist where user_id = ?", new Object[] { userId }, rowMapper)
				.iterator();
		while (it.hasNext()) {
			final PlayList plst = it.next();
			plsts.put(plst.getId(), plst);
		}
		LOG.info("Playlists succefuly loaded " + plsts.size());
		return plsts;
	}

	private static class PlayListRowMapper implements ParameterizedRowMapper<PlayList> {

		public PlayList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final int id = rs.getInt(1);
			final int typeId = rs.getInt(2);
			final String name = rs.getString(3);
			final Date lastUpdate = rs.getDate(4);
			LOG.info("Loading playlist id:" + id + " type:" + typeId);
			if (typeId == 0) {
				final PlayList pLst = new SimplePlayList(id, name, lastUpdate);
				final List<Map> lst = DaoHelper.getInstance().getJdbcTemplate().queryForList(
						"select track_id from simple_playlist where playlist_id = ?", new Object[] { pLst.getId() });
				for (final Map<String, Integer> map : lst)
					pLst.add(map.get("TRACK_ID"));
				return pLst;
			} else {
				final Map map = DaoHelper.getInstance().getJdbcTemplate().queryForMap(
						"select max_tracks, order_by, operator from smart_playlist where playlist_id = ?", new Object[] { id });
				final SmartPlayList pLst = new SmartPlayList(id, name, ((Integer) map.get("MAX_TRACKS")).intValue(),
						((Integer) map.get("OPERATOR")).intValue(), ((String) map.get("ORDER_BY")));
				final List<Map> lst = DaoHelper.getInstance().getJdbcTemplate().queryForList(
						"select term, operator, value from smart_playlist_condition where playlist_id= ?", new Object[] { id });
				for (final Map<String, String> map1 : lst)
					pLst.getConditions().add(
							new SmartPlayListCondition(pLst, map1.get("TERM"), map1.get("OPERATOR"), map1.get("VALUE")));
				new PlayListDao().reloadSmartPlayLsit(pLst);
				return pLst;
			}
		}
	}

	public void reloadSmartPlayLsit(final SmartPlayList pLst) {
		final List<Map> lst = getJdbcTempate().queryForList(pLst.getSqlStatement());
		pLst.setTrackIds(new LinkedHashSet<Integer>());
		for (final Map<String, Integer> map1 : lst)
			pLst.add(map1.get("TRACK_ID"));
	}
}
