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
