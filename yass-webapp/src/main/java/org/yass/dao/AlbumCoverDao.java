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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.AlbumCover;

public class AlbumCoverDao extends AbstractDao<AlbumCover> {

	private static final AlbumCoverDao instance = new AlbumCoverDao();
	private static final Log LOG = LogFactory.getLog(AlbumCoverDao.class);

	/**
	 * @return the instance
	 */
	public static final AlbumCoverDao getInstance() {
		return instance;
	}

	public AlbumCover get(final int albumId) {
		return null;
	}

	public boolean hasPicture(final int albumId) {
		try {
			return getSingleResult(createNamedQuery("findAlbumCoverIdByAlbumId").setParameter(1, albumId)) != null;
		} catch (final Exception e) {
			return false;
		}
	}

	public AlbumCover save(final AlbumCover cover) {
		if (cover == null)
			return null;
		beginTransaction();
		try {
			if (LOG.isInfoEnabled())
				LOG.info("Save attachedPicture albumId:" + cover.getAlbumId());
			persist(cover);
			commitTransaction();
		} catch (final Exception e) {
			rollbackTransaction();
			LOG.error("Error saving AlbumCover, albumId:" + cover.getAlbumId(), e);
		}
		return cover;
	}
}
