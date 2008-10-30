package org.yass.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.User;

public class UserDao extends AbstractDao {

	private static final UserDao instance = new UserDao();
	private static final Log LOG = LogFactory.getLog(UserDao.class);

	private UserDao() {
	}

	public final User getFromId(final int id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Loading User id:" + id + " from persistent store");
			return (User) getEntityManager().createNamedQuery("getUserById").setParameter(1, id).getSingleResult();
		} catch (final Exception e) {
			LOG.error("Error while loading User id:" + id + " from persistent store", e);
			return null;
		}
	}

	/**
	 * @return the instance
	 */
	public static final UserDao getInstance() {
		return instance;
	}
}
