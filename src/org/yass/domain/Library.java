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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "LAST_UPDATE")
	private Date lastUpdate = new Date();
	private String path;
	@OneToMany(cascade = CascadeType.ALL, targetEntity = Track.class, fetch = FetchType.EAGER)
	@ElementJoinColumn(name = "LIBRARY_ID", referencedColumnName = "ID")
	private final Collection<Track> tracks = new LinkedHashSet<Track>();
	@OneToOne
	private User user;

	/**
	 * 
	 */
	public Library() {
		super();
	}

	/**
	 * @param tracks
	 *          the tracks to set
	 */
	public final void add(final Track track) {
		tracks.add(track);
		track.setLibrary(this);
	}

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return the lastUpdate
	 */
	public final Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * @return the tracks
	 */
	public final Collection<Track> getTracks() {
		return tracks;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param id
	 *          the id to set
	 */
	public final void setId(final int id) {
		this.id = id;
	}

	/**
	 * @param lastUpdate
	 *          the lastUpdate to set
	 */
	public final void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * @param path
	 *          the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @param user
	 *          the user to set
	 */
	public void setUser(final User user) {
		this.user = user;
	}
}
