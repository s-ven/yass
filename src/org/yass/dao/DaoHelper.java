package org.yass.dao;

import java.io.File;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.yass.YassConstants;
import org.yass.dao.schema.Schema00;
import org.yass.util.FileUtils;

public class DaoHelper implements YassConstants {

	private final static Log LOG = LogFactory.getLog(DaoHelper.class);
	private final DataSource dataSource;
	private final static DaoHelper instance = new DaoHelper();

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
