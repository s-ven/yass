package org.yass.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yass.YassConstants;

public class PlayList implements YassConstants {

	protected final Map<Integer, Track> tracks = new LinkedHashMap<Integer, Track>();
	public int id;
	protected String name;
	protected String type;

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return the value
	 */
	public final String getName() {
		return name;
	}

	public final Track getMediaFile(final int id) {
		return tracks.get(id);
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public void add(final Track track) {
		tracks.put(track.getId(), track);
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

	public void add(final PlayList pl) {
		for (final Track mf : pl.tracks.values())
			this.add(mf);
	}
}
