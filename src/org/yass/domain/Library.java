package org.yass.domain;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Library {

	private Map<Integer, Track> tracks = new LinkedHashMap<Integer, Track>();
	private String path;
	private Map<String, Track> paths = new LinkedHashMap<String, Track>();
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate = new Date();
	@Id
	private int id;

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
