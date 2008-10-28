package org.yass.domain;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.openjpa.persistence.jdbc.ElementJoinColumn;

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

	@OneToMany(cascade = CascadeType.ALL, targetEntity = Track.class, fetch = FetchType.EAGER)
	@ElementJoinColumn(name = "LIBRARY_ID", referencedColumnName = "ID")
	private Collection<Track> tracks = new LinkedHashSet<Track>();
	private String path;
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate = new Date();
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private User user;

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public final void add(final Track track) {
		tracks.add(track);
		track.setLibrary(this);
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

	/**
	 * @param user
	 *          the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}
