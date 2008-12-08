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

import javax.persistence.EntityManager;

/**
 * 
 * @author svenduzont
 */
public class Schema00 extends Schema {

	/**
	 * @param entityManager
	 */
	public Schema00(final EntityManager entityManager) {
		super(entityManager);
	}

	/**
     *
     */
	@Override
	public void execute() {
		LOG.info("Executing Schema check version 0.0.");
		if (!tableExists("schema_version")) {
			LOG.info(" table 'schema_version' not found...");
			executeUpdate("create table schema_version (version int not null)");
			executeUpdate("insert into schema_version values (1)");
			LOG.info(" table 'schema_version' successfully created");
		}
		// Table 'Role'
		if (!tableExists("role")) {
			LOG.info(" table 'role' not found...");
			executeUpdate("create table role (id int not null generated always as identity,name varchar(25) not null,primary key(id))");
			executeUpdate("insert  into role (name) values ('admin')");
			executeUpdate("insert into role (name) values ('user')");
			LOG.info(" table 'role' successfully created");
		}
		// Table 'user'
		if (!tableExists("yass_user")) {
			LOG.info(" table 'user' not found.  Creating it.");
			executeUpdate("create table yass_user (id int not null generated always as identity, user_name varchar(25) not null, password varchar(25) not null,"
					+ " role_id int not null, primary key (id), foreign key (role_id) references role(id))");
			executeUpdate("insert  into yass_user (user_name, password, role_id) values ('admin', 'admin', 1)");
			LOG.info(" table 'user' was created successfully.");
		}
		// Table 'library'
		if (!tableExists("library")) {
			LOG.info(" table 'library' not found.  Creating it.");
			executeUpdate("create table library (id int not null generated always as identity, user_id int not null, path varchar(512) not null, last_update timestamp not null, foreign key(user_id) references yass_user(id), primary key(id))");
			LOG.info(" table 'library' was created successfully.");
		}
		// Table track_type
		if (!tableExists("track_type")) {
			LOG.info(" table 'track_type' not found.  Creating it.");
			executeUpdate("create table track_type (id int not null, content_type varchar(64) not null, label varchar(64) not null, primary key(id))");
			executeUpdate("insert into track_type (id, content_type, label) values (1, 'audio/mp3', 'MP3')");
			executeUpdate("insert into track_type (id, content_type, label) values (2, 'audio/aac', 'AAC')");
			LOG.info(" table 'track_type' was created successfully.");
		}
		// Table 'track'
		if (!tableExists("track")) {
			LOG.info(" table 'track' not found.  Creating it.");
			executeUpdate("create table track (id  int not null generated always as identity, library_id int not null, track_type_id integer not null, "
					+ "path varchar(512) not null, track_nr int, title varchar(256) not null,"
					+ "last_modified timestamp not null, duration int not null,vbr int not null,"
					+ "primary key(id), foreign key(library_id) references library(id), "
					+ "foreign key(track_type_id) references track_type(id))");
			LOG.info(" table 'track' was created successfully.");
		}
		// Index IDX_track_01
		if (!indexExists("IDX_track_01")) {
			LOG.info(" index IDX_track_01 on 'track' not found.  Creating it.");
			executeUpdate("create unique index IDX_track_01 on track (library_id, path, title, track_nr, length, last_update, track_type_id)");
			LOG.info(" index IDX_track_01 on 'track' was created successfully.");
		}
		// Table track_stats
		if (!tableExists("track_stat")) {
			LOG.info(" table 'track_stat' not found.  Creating it.");
			executeUpdate("create table track_stat (user_id int not null, track_id int not null, rating int not null, last_played timestamp, play_count int not null, last_selected timestamp, "
					+ "foreign key(user_id) references yass_user(id), foreign key(track_id) references track(id))");
			LOG.info(" table 'track_stat' was created successfully.");
		}
		// Table track_info
		if (!tableExists("track_info")) {
			LOG.info(" table 'track_info' not found.  Creating it.");
			executeUpdate("create table track_info (id  int not null generated always as identity, type varchar(64) not null, value varchar(128) not null, primary key(id))");
			LOG.info(" table 'track_info' was created successfully.");
		}
		// Index IDX_track_info_01
		if (!indexExists("IDX_track_info_01")) {
			LOG.info(" index IDX_track_info_01 on 'track_track_info' not found.  Creating it.");
			executeUpdate("create unique index IDX_track_info_01 on track_info (type, value, id)");
			LOG.info(" index IDX_track_info_01 on 'track_track_info' was created successfully.");
		}
		// Index IDX_track_info_02
		if (!indexExists("IDX_track_info_02")) {
			LOG.info(" index IDX_track_info_02 on 'track_track_info' not found.  Creating it.");
			executeUpdate("create unique index IDX_track_info_02 on track_info (id,type, value)");
			LOG.info(" index IDX_track_info_02 on 'track_track_info' was created successfully.");
		}
		// Table track_track_info
		if (!tableExists("track_track_info")) {
			LOG.info(" table 'track_track_info' not found.  Creating it.");
			executeUpdate("create table track_track_info (track_id int not null, track_info_id int not null,"
					+ " foreign key(track_id) references track(id),  foreign key(track_info_id) references track_info(id) , primary key(track_id, track_info_id))");
			LOG.info(" table 'track_track_info' was created successfully.");
		}
		// Index IDX_track_track_info_01
		if (!indexExists("IDX_track_track_info_01")) {
			LOG.info(" index IDX_track_track_info_01 on 'track_track_info' not found.  Creating it.");
			executeUpdate("create unique index IDX_track_track_info_01 on track_track_info (track_id, track_info_id)");
			LOG.info(" index IDX_track_track_info_01 on 'track_track_info' was created successfully.");
		}
		// Table playlist_type
		if (!tableExists("playlist_type")) {
			LOG.info(" table 'playlist_type' not found.  Creating it.");
			executeUpdate("create table playlist_type (id int not null, label varchar(16) not null, primary key(id))");
			executeUpdate("insert  into playlist_type (id, label) values (0, 'simple')");
			executeUpdate("insert  into playlist_type (id, label) values (1, 'smart')");
			LOG.info(" table 'playlist_type' was created successfully.");
		}
		// Table playlist
		if (!tableExists("playlist")) {
			LOG.info(" table 'playlist' not found.  Creating it.");
			executeUpdate("create table playlist (id int not null generated always as identity, user_id int not null, type_id int not null, name varchar(128) not null, last_update timestamp"
					+ ", primary key(id), foreign key(user_id) references yass_user(id), foreign key(type_id) references playlist_type(id))");
			executeUpdate("insert into playlist (user_id, type_id, name) values(1, 1, 'Top rated')");
			executeUpdate("insert into playlist (user_id, type_id, name) values(1, 1, 'Most played')");
			LOG.info(" table 'playlist' was created successfully.");
		}
		// Table simple_playlist
		if (!tableExists("simple_playlist")) {
			LOG.info(" table 'simple_playlist' not found.  Creating it.");
			executeUpdate("create table simple_playlist (playlist_id int not null, track_id int not null, track_order int not null"
					+ ", foreign key(playlist_id) references playlist(id), foreign key(track_id) references track(id))");
			LOG.info(" table 'simple_playlist' was created successfully.");
		}
		// Table smart_playlist
		if (!tableExists("smart_playlist")) {
			LOG.info(" table 'smart_playlist' not found.  Creating it.");
			executeUpdate("create table smart_playlist (playlist_id int not null, max_tracks int not null, order_by varchar(50), operator int not null,"
					+ "foreign key(playlist_id) references playlist(id), primary key(playlist_id))");
			executeUpdate("insert into smart_playlist (playlist_id, max_tracks, order_by, operator) values (1,0,'rating desc', 0)");
			executeUpdate("insert into smart_playlist (playlist_id, max_tracks, order_by, operator) values (2,250,'play_count desc', 0)");
			LOG.info(" table 'smart_playlist' was created successfully.");
		}
		// Table smart_playlist_condition
		if (!tableExists("smart_playlist_condition")) {
			LOG.info(" table 'smart_playlist_condition' not found.  Creating it.");
			executeUpdate("create table smart_playlist_condition (id int not null generated always as identity, playlist_id int not null, term varchar(50) not null, operator varchar(12) not null, value varchar(128) not null,"
					+ "foreign key(playlist_id) references smart_playlist(playlist_id))");
			executeUpdate("insert into smart_playlist_condition (playlist_id, term, operator, value) values (1, 'rating', '>', '0')");
			executeUpdate("insert into smart_playlist_condition (playlist_id, term, operator, value) values (2, 'play_count', '>', '0')");
			LOG.info(" table 'smart_playlist_condition' was created successfully.");
		}
		// Table user_setting
		if (!tableExists("user_setting")) {
			LOG.info(" table 'user_setting' not found.  Creating it.");
			executeUpdate("create table user_setting (user_id int not null, loaded_track_id int not null, volume int not null, shuffle smallint not null,"
					+ "repeat smallint not null, show_remaining smallint not null, display_mode smallint not null, stop_fadeout int not null,"
					+ "skip_fadeout int not null, next_fadeout int not null, foreign key(user_id) references yass_user(id), primary key(user_id))");
			LOG.info(" table 'user_setting' was created successfully.");
		}
		// Table user_browsing_context
		if (!tableExists("user_browsing_context")) {
			LOG.info(" table 'user_browsing_context' not found.  Creating it.");
			executeUpdate("create table user_browsing_context (user_id int not null, track_info_id int not null"
					+ ", foreign key(user_id) references yass_user(id)"
					+ ", foreign key(track_info_id) references track_info(id), primary key(user_id, track_info_id))");
			LOG.info(" table 'user_browsing_context' was created successfully.");
		}
		// Table user_browsing_context
		if (!tableExists("album_cover")) {
			LOG.info(" table 'album_cover' not found.  Creating it.");
			executeUpdate("create table album_cover (track_info_id int not null, mime_type varchar(32) not null, description varchar(64) not null, picture_type smallint not null, picture_data blob(8M) not null"
					+ ", foreign key(track_info_id) references track_info(id), primary key(track_info_id))");
			LOG.info(" table 'album_cover' was created successfully.");
		}
	}
}
