package org.yass.dao;

import javax.persistence.EntityTransaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.AlbumCover;

public class AlbumCoverDao extends AbstractDao {

	/**
	 * @return the instance
	 */
	public static final AlbumCoverDao getInstance() {
		return instance;
	}

	private static final AlbumCoverDao instance = new AlbumCoverDao();
	private static final Log LOG = LogFactory.getLog(AlbumCoverDao.class);

	public AlbumCover save(final AlbumCover albumPict) {
		EntityTransaction transaction = null;
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Save attachedPicture albumId:" + albumPict.getAlbumId());
			transaction = getEntityManager().getTransaction();
			transaction.begin();
			getEntityManager().persist(albumPict);
			transaction.commit();
		} catch (final Exception e) {
			LOG.error("Error saving AlbumCover, albumId:" + albumPict.getAlbumId(), e);
			if (transaction != null)
				transaction.rollback();
		}
		return albumPict;
	}

	public AlbumCover get(final int albumId) {
		return null;
	}

	public boolean hasPicture(final int albumId) {
		try {
			return getEntityManager().createNamedQuery("getAlbumCoverIdFromAlbumId").setParameter(1, albumId)
					.getSingleResult() != null;
		} catch (final Exception e) {
			return false;
		}
	}
}
