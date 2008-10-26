package org.yass.domain;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries( { @NamedQuery(name = "getLibraryById", query = "SELECT l FROM Library l where l.id = ?1"),
		@NamedQuery(name = "getLibraryByUserId", query = "SELECT l FROM Library l where l.user.id = ?1") })
public class Library {

	/**
	 * 
	 */
	public Library() {
		super();
	}

	@OneToMany(mappedBy = "library", fetch = FetchType.EAGER)
	@MapKey(name = "id")
	private Map<Integer, Track> tracksMap = new LinkedHashMap<Integer, Track>();
	private String path;
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate = new Date();
	@Id
	private int id;

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
		clean();
		for (final Track track : tracks)
			add(track);
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public final void add(final Track track) {
		tracksMap.put(track.getId(), track);
	}

	public final Track getTrack(final int id) {
		return tracksMap.get(id);
	}

	public final void clean() {
		tracksMap.clear();
	}

	/**
	 * @return the tracks
	 */
	public final Collection<Track> getTracks() {
		return tracksMap.values();
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
