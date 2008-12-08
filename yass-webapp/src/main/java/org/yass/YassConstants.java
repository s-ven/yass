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
package org.yass;

import org.yass.dao.AlbumCoverDao;
import org.yass.dao.LibraryDao;
import org.yass.dao.PlayListDao;
import org.yass.dao.TrackDao;
import org.yass.dao.TrackInfoDao;
import org.yass.dao.UserDao;

/**
 * 
 * @author svenduzont
 */
public interface YassConstants {

	/**
     *
     */
	public static final String ALBUM = "album";
	/**
     *
     */
	public static final String ARTIST = "artist";
	/**
     *
     */
	public static final AlbumCoverDao ATTACHED_PICTURE_DAO = AlbumCoverDao.getInstance();
	/**
     *
     */
	public static final String BITRATE = "bitrate";
	/**
     *
     */
	public static final String GENRE = "genre";
	/**
     *
     */
	public static final String LENGTH = "LENGTH";
	/**
     *
     */
	public static final LibraryDao LIBRARY_DAO = LibraryDao.getInstance();
	/**
     *
     */
	public static final PlayListDao PLAYLIST_DAO = PlayListDao.getInstance();
	/**
     *
     */
	public static final String SMART_PLAYLISTS = "SMART_PLAYLIST";
	/**
     *
     */
	public static final TrackDao TRACK_DAO = TrackDao.getInstance();
	/**
     *
     */
	public static final TrackInfoDao TRACK_INFO_DAO = TrackInfoDao.getInstance();
	/**
     *
     */
	public static final String USER = "USER";
	/**
     *
     */
	public static final UserDao USER_DAO = UserDao.getInstance();
	/**
     *
     */
	public static final String YASS_HOME = "yass";
	/**
     *
     */
	public static final String YEAR = "year";
}
