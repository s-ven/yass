package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.yass.id3.AttachedPicture;

public class AttachedPictureDao extends AbstractDao {

	private static final Log LOG = LogFactory.getLog(AttachedPictureDao.class);
	private final PreparedStatementCreatorFactory insertTrackPscf = new PreparedStatementCreatorFactory(
			"insert into album_cover_picture (track_info_id, mime_type, description, picture_type, picture_data) values (?, ?, ?, ?, ?)");

	public AttachedPictureDao() {
		insertTrackPscf.addParameter(new SqlParameter("track_info_id", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("mime_type", java.sql.Types.VARCHAR));
		insertTrackPscf.addParameter(new SqlParameter("description", java.sql.Types.VARCHAR));
		insertTrackPscf.addParameter(new SqlParameter("picture_type", java.sql.Types.INTEGER));
		insertTrackPscf.addParameter(new SqlParameter("picture_data", java.sql.Types.BLOB));
	}

	public void save(final AttachedPicture attPict) {
		LOG.info("Saving attachedPicture albumId:" + attPict.getAlbumId());
		if (getJdbcTempate().queryForList("select count(*) from album_cover_picture where 	track_info_id = ?",
				new Object[] { attPict.getAlbumId() }).size() == 0)
			getJdbcTempate().update(
					insertTrackPscf.newPreparedStatementCreator(new Object[] { attPict.getAlbumId(), attPict.getMimeType(),
							attPict.getDescription(), attPict.getPictureType(), attPict.getPictureData() }));
	}

	public AttachedPicture get(final int albumId) {
		return null;
	}
}
