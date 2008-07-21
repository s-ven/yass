package org.yass.dao.schema;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class Schema {

	public abstract void execute(JdbcTemplate template);

	protected boolean tableExists(final JdbcTemplate template, final String table) {
		try {
			template.execute("select 1 from " + table);
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	protected boolean columnExists(final JdbcTemplate template, final String table, final String column) {
		try {
			template.execute("select " + column + " from " + table + " where 0 = 1");
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	protected boolean indexExists(final JdbcTemplate template, final String indexName) {
		try {
			return template.queryForList("select count(*) from SYS.SYSCONGLOMERATES where 	CONGLOMERATENAME = ?",
					new Object[] { indexName }).size() != 0;
		} catch (final Exception e) {
			return false;
		}
	}
}
