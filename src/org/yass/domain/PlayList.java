package org.yass.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yass.YassConstants;

public class PlayList implements YassConstants {

	protected final Map<String, Track> tracks = new LinkedHashMap<String, Track>();
	public String id;
	protected String name;
	protected String type;

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @return the value
	 */
	public final String getName() {
		return name;
	}

	public final Track getMediaFile(final String uuid) {
		return tracks.get(uuid);
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public final void add(final Track track) {
		tracks.put(track.getUuid(), track);
	}

	public final void clean() {
		tracks.clear();
	}

	/**
	 * @return the tracks
	 */
	public final Collection<Track> getMediaFiles() {
		return tracks.values();
	}

	public void add(final PlayList pl) {
		for (final Track mf : pl.tracks.values())
			this.add(mf);
	}
}
