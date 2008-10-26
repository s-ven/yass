package org.yass;

import org.yass.dao.AttachedPictureDao;
import org.yass.dao.LibraryDao;
import org.yass.dao.PlayListDao;
import org.yass.dao.TrackDao;
import org.yass.dao.TrackInfoDao;
import org.yass.dao.TrackStatDao;

public interface YassConstants {

	public static final String YASS_HOME = "yass";
	public static final String INDEX_MANAGER = "INDEX_MANAGER";
	public static final String BROWSING_CONTEXT = "BROWSING_CONTEXT";
	public static final String CURRENT_PLAYLIST = "CURRENT_PLAYLIST";
	public static final String USER_PLAYLISTS = "USER_PLAYLISTS";
	public static final String SMART_PLAYLISTS = "SMART_PLAYLIST";
	public static final String ALL_LIBRARY = "ALL_LIBRARY";
	public static final String USER_TRACK_STATS = "USER_TRACK_STATS";
	public static final String LIB_XML_TREE = "LIB_XML_TREE";
	public static final String CURRENT_LIB_PLAYLIST = "CURRENT_LIB_PLAYLIST";
	public static final String ARTIST_ID = "ARTIST_ID";
	public static final String ALBUM_ID = "ALBUM_ID";
	public static final String GENRE_ID = "GENRE_ID";
	public static final String ARTIST = "artist";
	public static final String ALBUM = "album";
	public static final String TRACK = "TRACK";
	public static final String TITLE = "TITLE";
	public static final String GENRE = "genre";
	public static final String BITRATE = "bitrate";
	public static final String YEAR = "year";
	public static final String PATH = "PATH";
	public static final String LENGTH = "LENGTH";
	public static final String UUID = "UUID";
	public static final String UNKNOWN_GENRE = "<Unknown Genre>";
	public static final String UNKNOWN_ARTIST = "<Unknown Artist>";
	public static final String UNKNOWN_ALBUM = "<Unknown Album>";
	public static final String UNKNOWN_TITLE = "<Unknown Title>";
	public static final String USER_SETTINGS = "USER_SETTINGS";
	public static final LibraryDao LIBRARY_DAO = LibraryDao.getInstance();
	public static final AttachedPictureDao ATTACHED_PICTURE_DAO = AttachedPictureDao.getInstance();
	public static final TrackStatDao TRACK_STAT_DAO = TrackStatDao.getInstance();
	public static final PlayListDao PLAYLIST_DAO = PlayListDao.getInstance();
	public static final TrackDao TRACK_DAO = TrackDao.getInstance();
	public static final TrackInfoDao TRACK_INFO_DAO = TrackInfoDao.getInstance();
}
