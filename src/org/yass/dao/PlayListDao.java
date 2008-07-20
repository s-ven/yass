package org.yass.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
		if (plst.id == 0) {
			final PreparedStatementCreator pst = pscf.newPreparedStatementCreator(new Object[] { plst.typeId, plst.userId,
					plst.getName(), plst.getLastUpdate() });
			final KeyHolder kh = new GeneratedKeyHolder();
			this.getJdbcTempate().update(pst, kh);
			plst.id = kh.getKey().intValue();
			LOG.info(" new PlayList created id:" + plst.getId());
		} else {
			getJdbcTempate().update("update playlist set name = ?, last_update = ?",
					new Object[] { plst.getName(), new Date() });
			if (plst instanceof SimplePlayList) {
				getJdbcTempate().execute("delete from simple_playlist where playlist_id = " + plst.getId());
				int trackOrder = 0;
				for (final int trackId : ((SimplePlayList) plst).trackIds)
					getJdbcTempate().update("insert into simple_playlist (playlist_id, track_id, track_order) values (?, ?, ?)",
							new Object[] { plst.id, trackId, trackOrder++ });
			}
		}
	}

	public Map<Integer, PlayList> getFromUserId(final int userId) {
		LOG.info("Loading Playlist from user_id:" + userId);
		final Map<Integer, PlayList> plsts = new LinkedHashMap<Integer, PlayList>();
		final Iterator<PlayList> it = getJdbcTempate().query(
				"select id, type_id, name, last_update from playlist where user_id = ?", new Object[] { userId }, rowMapper)
				.iterator();
		if (it.hasNext()) {
			final PlayList lib = it.next();
			plsts.put(lib.getId(), lib);
		}
		LOG.info("Playlists succefuly loaded");
		return plsts;
	}

	private static class PlayListRowMapper implements ParameterizedRowMapper<SimplePlayList> {

		public SimplePlayList mapRow(final ResultSet rs, final int rowNum) throws SQLException {
			final SimplePlayList list = new SimplePlayList(rs.getInt(1), rs.getString(3), rs.getDate(4));
			list.trackIds = (Integer[]) DaoHelper.getInstance().getJdbcTemplate().queryForList(
					"select track_id from simple_playlist where playlist_id = ?", new Object[] { list.id }).toArray(
					new Integer[] {});
			return list;
		}
	}
}
