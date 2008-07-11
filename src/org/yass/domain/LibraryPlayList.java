package org.yass.domain;

import java.util.Date;

import org.yass.dao.TrackDao;

public class LibraryPlayList extends PlayList {

	public String path;
	public Date lastUpdate = new Date();
	private final TrackDao trackDao = new TrackDao();

	/**
	 * @param path
	 * @param lastUpdate
	 */
	public LibraryPlayList(final int id, final String path, final Date lastUpdate) {
		super();
		this.id = id;
		this.path = path;
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	@Override
	public final void add(final Track track) {
		track.setLibrary(this);
		super.add(track);
	}
}
