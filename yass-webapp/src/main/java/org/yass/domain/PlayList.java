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

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.yass.YassConstants;

@Entity
@Table(name = "PLAYLIST", schema = "SA")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER, name = "TYPE_ID")
@NamedQuery(name = "findPlayListByUserId", query = "select plsts from PlayList plst where plst.userId = ?1")
public abstract class PlayList implements YassConstants {

	@Id
	protected int id;
	@Column(name = "LAST_UPDATE")
	protected Date lastUpdate = new Date();
	protected String name;
	private Set<Integer> trackIds = new LinkedHashSet<Integer>();
	@Column(name = "TYPE_ID")
	protected int typeId;
	@ManyToOne
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	protected User user;

	/**
	 * Empty constructor
	 */
	public PlayList() {
		super();
	}

	/**
	 * 
	 * @param track
	 */
	public void addTrack(final Integer track) {
		trackIds.add(track);
	}

	public void addTracks(final Integer[] tracks) {
		trackIds.addAll(Arrays.asList(tracks));
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
	 * @return the value
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return the trackIds
	 */
	public final Set<Integer> getTrackIds() {
		return trackIds;
	}

	/**
	 * @return the typeId
	 */
	public final int getTypeId() {
		return typeId;
	}

	/**
	 * @param trackIds
	 *          the trackIds to set
	 */
	public final void setTrackIds(final Set<Integer> trackIds) {
		this.trackIds = trackIds;
	}
}
