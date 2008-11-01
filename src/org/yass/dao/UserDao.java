/*
 * 
 * 
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
import org.yass.domain.User;
import org.yass.domain.UserBrowsingContext;

public class UserDao extends AbstractDao<User> {

	private static final UserDao instance = new UserDao();
	private static final Log LOG = LogFactory.getLog(UserDao.class);

	/**
	 * @return the instance
	 */
	public static final UserDao getInstance() {
		return instance;
	}

	private UserDao() {
	}

	public void cleanBrowsingContext(final User user) {
		for (final UserBrowsingContext ctx : user.getBrowsingContext()) {
			beginTransaction();
			try {
				if (LOG.isDebugEnabled())
					LOG.debug("Delete BrowsingContext id:" + user.getId());
				remove(ctx);
				commitTransaction();
			} catch (final Exception e) {
				LOG.error("Error deleting BrowsingContext id:" + user.getId(), e);
				rollbackTransaction();
			}
		}
	}

	public final User getFromId(final int id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Get User id:" + id);
			return getSingleResult(createNamedQuery("getUserById").setParameter(1, id));
		} catch (final Exception e) {
			LOG.error("Error getting User id:" + id, e);
			return null;
		}
	}

	public final User save(final User user) {
		beginTransaction();
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Save User id:" + user.getId());
			persist(user);
			commitTransaction();
		} catch (final Exception e) {
			rollbackTransaction();
			LOG.error("Error saving User id:" + user.getId(), e);
		}
		return user;
	}
}
