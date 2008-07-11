package org.yass.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class AbstractDao {

	private final DaoHelper helper = DaoHelper.getInstance();

	protected JdbcTemplate getJdbcTempate() {
		return helper.getJdbcTemplate();
	}
}
