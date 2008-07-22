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
		// Table 'Role'
		if (!tableExists(template, "role")) {
			LOG.info(" table 'role' not found...");
			template
					.execute("create table role (id int not null generated always as identity,name varchar(25) not null,primary key(id))");
			template.execute("insert  into role (name) values ('admin')");
			template.execute("insert into role (name) values ('playlist')");
			LOG.info(" table 'role' successfully created");
		}
		// Table 'yass_user'
		if (!tableExists(template, "yass_user")) {
			LOG.info(" table 'yass_user' not found.  Creating it.");
			template
					.execute("create table yass_user (id int not null generated always as identity, user_name varchar(25) not null, password varchar(25) not null,"
							+ " role_id int not null, primary key (id), foreign key (role_id) references role(id))");
			template.execute("insert  into yass_user (user_name, password, role_id) values ('admin', 'admin', 1)");
			LOG.info(" table 'yass_user' was created successfully.");
		}
		// Table 'library'
		if (!tableExists(template, "library")) {
			LOG.info(" table 'library' not found.  Creating it.");
			template
					.execute("create table library (id int not null generated always as identity, path varchar(512) not null, last_update date not null, primary key(id))");
			LOG.info(" table 'library' was created successfully.");
		}
		// Table 'track'
		if (!tableExists(template, "track")) {
			LOG.info(" table 'track' not found.  Creating it.");
			template
					.execute("create table track (id  int not null generated always as identity, library_id int not null, path varchar(512) not null, track_nr int, title varchar(256) not null,"
							+ " last_update date not null, play_count int default 0, last_play date,"
							+ " rating int default 0, length int not null, primary key(id), foreign key(library_id) references library(id))");
			LOG.info(" table 'track' was created successfully.");
		}
		// Table track_stats
		if (!tableExists(template, "track_stats")) {
			LOG.info(" table 'track_stats' not found.  Creating it.");
			template
					.execute("create table track_stats (user_id int not null, track_id int not null, rating int not null, last_played date, playcount int not null, last_selected date, "
							+ "foreign key(user_id) references yass_user(id), foreign key(track_id) references track(id))");
			LOG.info(" table 'track_stats' was created successfully.");
		}
		// Table track_info
		if (!tableExists(template, "track_info")) {
			LOG.info(" table 'track_info' not found.  Creating it.");
			template
					.execute("create table track_info (id  int not null generated always as identity, type varchar(64) not null, value varchar(128) not null, primary key(id))");
			LOG.info(" table 'track_info' was created successfully.");
		}
		if (!indexExists(template, "IDX_track_info_01")) {
			LOG.info(" index IDX_track_info_01 on 'track_track_info' not found.  Creating it.");
			template.execute("create unique index IDX_track_info_01 on track_info (type, value, id)");
			LOG.info(" index IDX_track_info_01 on 'track_track_info' was created successfully.");
		}
		if (!indexExists(template, "IDX_track_info_02")) {
			LOG.info(" index IDX_track_info_02 on 'track_track_info' not found.  Creating it.");
			template.execute("create unique index IDX_track_info_02 on track_info (id,type, value)");
			LOG.info(" index IDX_track_info_02 on 'track_track_info' was created successfully.");
		}
		if (!tableExists(template, "track_track_info")) {
			LOG.info(" table 'track_track_info' not found.  Creating it.");
			template.execute("create table track_track_info (track_id int not null, track_info_id int not null,"
					+ " foreign key(track_id) references track(id), " + " foreign key(track_info_id) references track_info(id))");
			LOG.info(" table 'track_track_info' was created successfully.");
		}
		if (!indexExists(template, "IDX_track_track_info_01")) {
			LOG.info(" index IDX_track_track_info_01 on 'track_track_info' not found.  Creating it.");
			template.execute("create unique index IDX_track_track_info_01 on track_track_info (track_id, track_info_id)");
			LOG.info(" index IDX_track_track_info_01 on 'track_track_info' was created successfully.");
		}
		if (!tableExists(template, "playlist_type")) {
			LOG.info(" table 'playlist_type' not found.  Creating it.");
			template.execute("create table playlist_type (id int not null, label varchar(16) not null, primary key(id))");
			template.execute("insert  into playlist_type (id, label) values (0, 'simple')");
			template.execute("insert  into playlist_type (id, label) values (1, 'smart')");
			LOG.info(" table 'playlist_type' was created successfully.");
		}
		template.execute("drop table smart_playlist_condition");
		template.execute("drop table smart_playlist");
		template.execute("drop table simple_playlist");
		template.execute("drop table playlist");
		if (!tableExists(template, "playlist")) {
			LOG.info(" table 'playlist' not found.  Creating it.");
			template
					.execute("create table playlist (id int not null generated always as identity, user_id int not null, type_id int not null, name varchar(128) not null, last_update date"
							+ ", primary key(id), foreign key(user_id) references yass_user(id), foreign key(type_id) references playlist_type(id))");
			template.execute("insert into playlist (user_id, type_id, name) values(1, 1, 'Top rated')");
			template.execute("insert into playlist (user_id, type_id, name) values(1, 1, 'Most played')");
			LOG.info(" table 'playlist' was created successfully.");
		}
		if (!tableExists(template, "simple_playlist")) {
			LOG.info(" table 'simple_playlist' not found.  Creating it.");
			template
					.execute("create table simple_playlist (playlist_id int not null, track_id int not null, track_order int not null"
							+ ", foreign key(playlist_id) references playlist(id), foreign key(track_id) references track(id))");
			LOG.info(" table 'simple_playlist' was created successfully.");
		}
		if (!tableExists(template, "smart_playlist")) {
			LOG.info(" table 'smart_playlist' not found.  Creating it.");
			template
					.execute("create table smart_playlist (playlist_id int not null, max_tracks int not null, order_by varchar(50), operator int not null,"
							+ "foreign key(playlist_id) references playlist(id), primary key(playlist_id))");
			template
					.execute("insert into smart_playlist (playlist_id, max_tracks, order_by, operator) values (1,0,'rating desc', 0)");
			template
					.execute("insert into smart_playlist (playlist_id, max_tracks, order_by, operator) values (2,250,'play_count desc', 0)");
			LOG.info(" table 'smart_playlist' was created successfully.");
		}
		if (!tableExists(template, "smart_playlist_condition")) {
			LOG.info(" table 'smart_playlist_condition' not found.  Creating it.");
			template
					.execute("create table smart_playlist_condition (playlist_id int not null, term varchar(50) not null, operator varchar(12) not null, value varchar(128) not null,"
							+ "foreign key(playlist_id) references smart_playlist(playlist_id))");
			template
					.execute("insert into smart_playlist_condition (playlist_id, term, operator, value) values (1, 'rating', '>', '0')");
			template
					.execute("insert into smart_playlist_condition (playlist_id, term, operator, value) values (2, 'play_count', '>', '0')");
			LOG.info(" table 'smart_playlist_condition' was created successfully.");
		}
	}
}
