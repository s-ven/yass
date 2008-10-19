package org.yass.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Library {

	private Map<Integer, Track> tracksMap = new LinkedHashMap<Integer, Track>();
	private String path;
	private Map<String, Track> paths = new LinkedHashMap<String, Track>();
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate = new Date();
	@Id
	private int id;
	@OneToMany(mappedBy = "library", fetch = EAGER, cascade = ALL)
	private Collection<Track> tracks;

	/**
	 * @param path
	 * @param lastUpdate
	 */
	public Library(final int id, final String path, final Date lastUpdate, final Collection<Track> tracks) {
		super();
		this.id = id;
		this.path = path;
		this.lastUpdate = lastUpdate;
		this.setTracks(tracks);
	}

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
	public final void setTracks(final Collection<Track> tracks) {
		this.tracks = tracks;
		clean();
		for (final Track track : tracks)
			add(track);
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public final void add(final Track track) {
		track.setLibrary(this);
		paths.put(track.getPath(), track);
		tracksMap.put(track.getId(), track);
	}

	public final Track getFromPath(final String filePath) {
		return paths.get(filePath);
	}

	public final Track getMediaFile(final int id) {
		return tracksMap.get(id);
	}

	public final void clean() {
		tracksMap.clear();
		paths.clear();
	}

	/**
	 * @return the tracks
	 */
	public final Collection<Track> getTracks() {
		return tracks;
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public final void setId(final int id) {
		this.id = id;
	}

	/**
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * @param path
	 *          the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @return the lastUpdate
	 */
	public final Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate
	 *          the lastUpdate to set
	 */
	public final void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
