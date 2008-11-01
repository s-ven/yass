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
import org.yass.domain.Library;

public class LibraryDao extends AbstractDao<Library> {

	private static final LibraryDao instance = new LibraryDao();
	private static final Log LOG = LogFactory.getLog(LibraryDao.class);

	/**
	 * @return the instance
	 */
	public static final LibraryDao getInstance() {
		return instance;
	}

	private LibraryDao() {
	}

	public Library getFromId(final int id) {
		try {
			if (LOG.isInfoEnabled())
				LOG.debug("Loading Library id:" + id);
			return getSingleResult(createNamedQuery("getLibraryById").setParameter(1, id));
		} catch (final Exception e) {
			LOG.debug("Error while getting library", e);
		}
		return null;
	}

	public Library getFromUserId(final int id) {
		try {
			if (LOG.isInfoEnabled())
				LOG.debug("Loading Library user_id:" + id);
			return getSingleResult(createNamedQuery("getLibraryByUserId").setParameter(1, id));
		} catch (final Exception e) {
			LOG.debug("Error while getting library", e);
		}
		return null;
	}

	public Library save(final Library library) {
		LOG.info("Saving Library...");
		beginTransaction();
		try {
			persist(library);
			commitTransaction();
			if (LOG.isInfoEnabled())
				LOG.info("Library saved id:" + library.getId());
		} catch (final Exception e) {
			rollbackTransaction();
			LOG.error("Error while persisting library", e);
		}
		return library;
	}
}
