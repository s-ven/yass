package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.yass.domain.AlbumCoverPicture;

public class AttachedPictureDao extends AbstractDao {

	/**
	 * @return the instance
	 */
	public static final AttachedPictureDao getInstance() {
		return instance;
	}

	private static final AttachedPictureDao instance = new AttachedPictureDao();
	private static final Log LOG = LogFactory.getLog(AttachedPictureDao.class);
	private final PreparedStatementCreatorFactory insertTrackPscf = new PreparedStatementCreatorFactory(
			"insert into album_cover_picture (track_info_id, mime_type, description, picture_type, picture_data) values (?, ?, ?, ?, ?)");

	private AttachedPictureDao() {
		insertTrackPscf.addParameter(new SqlParameter("track_info_id", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("mime_type", java.sql.Types.VARCHAR));
		insertTrackPscf.addParameter(new SqlParameter("description", java.sql.Types.VARCHAR));
		insertTrackPscf.addParameter(new SqlParameter("picture_type", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("picture_data", java.sql.Types.BLOB));
	}

	public void save(final AlbumCoverPicture attPict) {
		LOG.info("Saving attachedPicture albumId:" + attPict.getAlbumId());
		if (getJdbcTempate().queryForList("select track_info_id from album_cover_picture where 	track_info_id = ?",
				new Object[] { attPict.getAlbumId() }).size() == 0)
			getJdbcTempate().update(
					insertTrackPscf.newPreparedStatementCreator(new Object[] { attPict.getAlbumId(), attPict.getMimeType(),
							attPict.getDescription(), attPict.getPictureType(), attPict.getPictureData() }));
	}

	public AlbumCoverPicture get(final int albumId) {
		return null;
	}

	public boolean hasPicture(final int albumId) {
		return getJdbcTempate().queryForList("select track_info_id from album_cover_picture where track_info_id = ?",
				new Object[] { albumId }).size() != 0;
	}
}
