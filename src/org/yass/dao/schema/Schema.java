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
package org.yass.dao.schema;

import org.springframework.jdbc.core.JdbcTemplate;

public abstract class Schema {

	protected boolean columnExists(final JdbcTemplate template, final String table, final String column) {
		try {
			template.execute("select " + column + " from " + table + " where 0 = 1");
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	public abstract void execute(JdbcTemplate template);

	protected boolean indexExists(final JdbcTemplate template, final String indexName) {
		try {
			return template.queryForList("select count(*) from SYS.SYSCONGLOMERATES where 	CONGLOMERATENAME = ?",
					new Object[] { indexName }).size() != 0;
		} catch (final Exception e) {
			return false;
		}
	}

	protected boolean tableExists(final JdbcTemplate template, final String table) {
		try {
			template.execute("select 1 from " + table);
		} catch (final Exception e) {
			return false;
		}
		return true;
	}
}
