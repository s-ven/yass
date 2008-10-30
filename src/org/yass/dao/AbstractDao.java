package org.yass.dao;

import javax.persistence.EntityManager;

import org.springframework.jdbc.core.JdbcTemplate;

public class AbstractDao {

	private final DaoHelper helper = DaoHelper.getInstance();

	protected JdbcTemplate getJdbcTemplate() {
		return helper.getJdbcTemplate();
	}

	protected EntityManager getEntityManager() {
		return helper.getEntityManager();
	}
}
