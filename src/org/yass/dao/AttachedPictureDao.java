package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	public void save(final AlbumCoverPicture attPict) {
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Saving attachedPicture albumId:" + attPict.getAlbumId());
			getEntityManager().getTransaction().begin();
			getEntityManager().persist(attPict);
			getEntityManager().getTransaction().commit();
		} catch (final Exception e) {
			LOG.error("Error while saving AlbumCoverPicture, albumId:" + attPict.getAlbumId());
		}
	}

	public AlbumCoverPicture get(final int albumId) {
		return null;
	}

	public boolean hasPicture(final int albumId) {
		return getJdbcTempate().queryForList("select track_info_id from album_cover_picture where track_info_id = ?",
				new Object[] { albumId }).size() != 0;
	}
}
