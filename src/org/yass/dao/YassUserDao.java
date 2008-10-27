package org.yass.dao;

import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.User;

public class YassUserDao extends AbstractDao {

	private static final YassUserDao instance = new YassUserDao();
	private static final Log LOG = LogFactory.getLog(YassUserDao.class);

	private YassUserDao() {
	}

	public final User getFromId(final int id) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Trying to load yass user " + id + " from database");
			final Query q = getEntityManager().createNamedQuery("getUserById").setParameter(1, id);
			return (User) q.getSingleResult();
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * @return the instance
	 */
	public static final YassUserDao getInstance() {
		return instance;
	}
}