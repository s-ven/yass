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

import java.io.File;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.yass.YassConstants;
import org.yass.dao.schema.Schema00;
import org.yass.util.FileUtils;

public class DaoHelper implements YassConstants {

	/**
	 * @return the entity manager
	 */
	public final EntityManager getEntityManager() {
		return entytyManager;
	}

	private final static Log LOG = LogFactory.getLog(DaoHelper.class);
	private final DataSource dataSource;
	private final static DaoHelper instance = new DaoHelper();
	private final EntityManager entytyManager;

	/**
	 * @return the instance
	 */
	public static final DaoHelper getInstance() {
		return instance;
	}

	private DaoHelper() {
		final File home = FileUtils.createFolder(YASS_HOME);
		final DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		ds.setUrl("jdbc:derby:directory:" + home.getPath() + "/db/yass;create=true");
		ds.setUsername("sa");
		ds.setPassword("");
		dataSource = ds;
		checkDataBase();
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("yass");
		entytyManager = emf.createEntityManager();
	}

	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	private void checkDataBase() {
		try {
			new Schema00().execute(getJdbcTemplate());
		} catch (final Exception e) {
			LOG.fatal(e);
		}
	}
}
