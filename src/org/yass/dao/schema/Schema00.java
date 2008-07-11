package org.yass.dao.schema;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class Schema00 extends Schema {

	private final static Log LOG = LogFactory.getLog(Schema00.class);

	@Override
	public void execute(final JdbcTemplate template) {
		LOG.info("Executing Schema check version 0.0.");
		if (!tableExists(template, "schema_version")) {
			LOG.info(" table 'schema_version' not found...");
			template.execute("create table schema_version (version int not null)");
			template.execute("insert into schema_version values (1)");
			LOG.info(" table 'schema_version' successfully created");
		}
		if (!tableExists(template, "role")) {
			LOG.info(" table 'role' not found...");
			template
					.execute("create table role (id int not null generated always as identity,name varchar(25) not null,primary key(id))");
			template.execute("insert  into role (name) values ('admin')");
			template.execute("insert into role (name) values ('playlist')");
			LOG.info(" table 'role' successfully created");
		}
		if (!tableExists(template, "yass_user")) {
			LOG.info(" table 'yass_user' not found.  Creating it.");
			template
					.execute("create table yass_user (id int not null generated always as identity, user_name varchar(25) not null, password varchar(25) not null,"
							+ " role_id int not null, primary key (id), foreign key (role_id) references role(id))");
			template.execute("insert  into yass_user (user_name, password, role_id) values ('admin', 'admin', 1)");
			LOG.info(" table 'yass_user' was created successfully.");
		}
		if (!tableExists(template, "library")) {
			LOG.info(" table 'library' not found.  Creating it.");
			template
					.execute("create table library (id int not null generated always as identity, path varchar(512) not null, last_update date not null, primary key(id))");
			LOG.info(" table 'library' was created successfully.");
		}
		if (!tableExists(template, "track")) {
			LOG.info(" table 'track' not found.  Creating it.");
			template
					.execute("create table track (id  int not null generated always as identity, library_id int not null, path varchar(512) not null, track_nr int, title varchar(256) not null,"
							+ " last_update date not null, play_count int default 0, last_play date,"
							+ " rating int default 0, length int not null, primary key(id), foreign key(library_id) references library(id))");
			LOG.info(" table 'track' was created successfully.");
		}
		if (!tableExists(template, "track_info")) {
			LOG.info(" table 'track_info' not found.  Creating it.");
			template
					.execute("create table track_info (id  int not null generated always as identity, type varchar(64) not null, value varchar(128) not null, primary key(id))");
			LOG.info(" table 'track_info' was created successfully.");
		}
		if (!tableExists(template, "track_track_info")) {
			LOG.info(" table 'track_track_info' not found.  Creating it.");
			template.execute("create table track_track_info (track_id int not null, track_info_id int not null,"
					+ " foreign key(track_id) references track(id), " + " foreign key(track_info_id) references track_info(id))");
			LOG.info(" table 'track_track_info' was created successfully.");
		}
	}
}
