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
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author svenduzont
 */
@Entity
@Table(name = "TRACK")
@NamedQueries( { @NamedQuery(name = "findTrackByPath", query = "select t from Track t where t.path = ?1"),
		@NamedQuery(name = "findTrackById", query = "select t from Track t where t.id = ?1") })
public class Track {

	private long duration;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "LAST_MODIFIED")
	private Date lastModified = new Date(0);
	private String path;
	private String title;
	@ManyToMany(targetEntity = TrackInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "TRACK_TRACK_INFO", joinColumns = @JoinColumn(name = "TRACK_ID"), inverseJoinColumns = @JoinColumn(name = "TRACK_INFO_ID"))
	@MapKey(name = "type")
	private Map<String, TrackInfo> trackInfos = new LinkedHashMap<String, TrackInfo>();
	@Column(name = "TRACK_NR")
	private int trackNr;
	@Column(name = "TRACK_TYPE_ID")
	private int typeId = 1;
	@Column(nullable = true)
	private boolean VBR = false;

    /**
     *
     */
    public Track() {
		super();
	}

	/**
	 * @return the duration
	 */
	public final long getDuration() {
		return duration;
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
	public final Date getLastModified() {
		return lastModified;
	}

	/**
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

    /**
     *
     * @param trackInfoType
     * @return
     */
    public TrackInfo getTrackInfo(final String trackInfoType) {
		return trackInfos.get(trackInfoType);
	}

    /**
     *
     * @return
     */
    public Collection<TrackInfo> getTrackInfos() {
		return trackInfos.values();
	}

	/**
	 * @return the trackNr
	 */
	public final int getTrackNr() {
		return trackNr;
	}

	/**
	 * @return the typeId
	 */
	public final int getTypeId() {
		return typeId;
	}

	/**
	 * @return the vBR
	 */
	public final boolean isVBR() {
		return VBR;
	}

	/**
     * @param length
	 */
	public final void setDuration(final long length) {
		duration = length;
	}

	/**
	 * @param lastUpdate
	 *          the lastUpdate to set
	 */
	public final void setLastModified(final Date lastUpdate) {
		lastModified = lastUpdate;
	}

	/**
	 * @param path
	 *          the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @param title
	 *          the title to set
	 */
	public final void setTitle(final String title) {
		this.title = title;
	}

    /**
     *
     * @param trackInfo
     */
    public void setTrackInfo(final TrackInfo trackInfo) {
		if (trackInfo != null)
			trackInfos.put(trackInfo.getType(), trackInfo);
	}

	/**
	 * @param trackNr
	 *          the trackNr to set
	 */
	public final void setTrackNr(final int trackNr) {
		this.trackNr = trackNr;
	}

	/**
	 * @param typeId
	 *          the typeId to set
	 */
	public final void setTypeId(final int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @param vbr
	 *          the vBR to set
	 */
	public final void setVBR(final boolean vbr) {
		VBR = vbr;
	}
}
