package org.yass.domain;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Library {

	protected final Map<Integer, Track> tracks = new LinkedHashMap<Integer, Track>();
	public String path;
	private final Map<String, Track> paths = new LinkedHashMap<String, Track>();
	public Date lastUpdate = new Date();
	public int id;

	/**
	 * @param path
	 * @param lastUpdate
	 */
	public Library(final int id, final String path, final Date lastUpdate) {
		super();
		this.id = id;
		this.path = path;
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public final void add(final Track track) {
		track.setLibrary(this);
		paths.put(track.getPath(), track);
		tracks.put(track.getId(), track);
	}

	public final Track getFromPath(final String filePath) {
		return paths.get(filePath);
	}

	public final Track getMediaFile(final int id) {
		return tracks.get(id);
	}

	public final void clean() {
		tracks.clear();
	}

	/**
	 * @return the tracks
	 */
	public final Collection<Track> getTracks() {
		return tracks.values();
	}
}
