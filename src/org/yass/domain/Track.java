package org.yass.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.CascadeType.REMOVE;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.yass.YassConstants;

@Entity
@Table(name = "TRACK")
public final class Track implements YassConstants {

	private Map<String, TrackInfo> trackInfosMap = new LinkedHashMap<String, TrackInfo>();
	@ManyToMany(cascade = { ALL, PERSIST, MERGE, REMOVE, REFRESH })
	@JoinTable(name = "TRACK_TRACK_INFO", joinColumns = @JoinColumn(name = "TRACK_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "TRACK_INFO_ID", referencedColumnName = "ID"))
	private Collection<TrackInfo> trackInfos;
	private String title;
	@Column(name = "TRACK_NR")
	private int trackNr;
	private String path;
	private long length;
	@Id
	private int id;
	@ManyToOne(cascade = ALL)
	@JoinColumn(name = "LIBRARY_ID", referencedColumnName = "ID")
	private Library library;
	@Column(name = "LAST_MODIFIED")
	private Date lastModified = new Date(0);
	@Column(name = "TRACK_TYPE_ID")
	private int typeId = 1;
	@Column(nullable = true)
	private boolean VBR = false;

	/**
	 * @param vbr
	 *          the vBR to set
	 */
	public final void setVBR(final boolean vbr) {
		VBR = vbr;
	}

	/**
	 * @return the vBR
	 */
	public final boolean isVBR() {
		return VBR;
	}

	/**
	 * @return the typeId
	 */
	public final int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *          the typeId to set
	 */
	public final void setTypeId(final int typeId) {
		this.typeId = typeId;
	}

	public void setTrackInfo(final String type, final TrackInfo trackInfo) {
		trackInfosMap.put(type, trackInfo);
	}

	/**
	 * @param library
	 *          the library to set
	 */
	public final void setLibrary(final Library library) {
		this.library = library;
	}

	/**
	 * @return the library
	 */
	public final Library getLibrary() {
		return library;
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
	 * @param title
	 *          the title to set
	 */
	public final void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * @return the length
	 */
	public final long getLength() {
		return length;
	}

	public Track(final File file) throws IOException {
	}

	public TrackInfo getTrackInfo(final String type) {
		return trackInfosMap.get(type);
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @return the trackNr
	 */
	public final int getTrackNr() {
		return trackNr;
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

	public Collection<TrackInfo> getTrackInfos() {
		return trackInfos;
	}

	public void setTrackInfos(final Collection<TrackInfo> infos) {
		trackInfos = infos;
		for (final TrackInfo trackInfo : infos)
			trackInfosMap.put(trackInfo.getType(), trackInfo);
	}

	/**
	 * @param id
	 * @param path
	 * @param title
	 * @param trackNr
	 * @param length
	 * @param lastUpdate
	 */
	public Track(final int id, final String path, final String title, final int trackNr, final int length,
			final Date lastUpdate, final int typeId, final boolean vbr) {
		this.id = id;
		this.path = path;
		this.title = title;
		this.trackNr = trackNr;
		this.length = length;
		VBR = vbr;
		lastModified = lastUpdate;
	}

	public Track() {
	}

	/**
	 * @param trackNr
	 *          the trackNr to set
	 */
	public final void setTrackNr(final int trackNr) {
		this.trackNr = trackNr;
	}

	/**
	 * @param path
	 *          the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * @param length
	 *          the length to set
	 */
	public final void setLength(final long length) {
		this.length = length;
	}

	/**
	 * @param lastUpdate
	 *          the lastUpdate to set
	 */
	public final void setLastModified(final Date lastUpdate) {
		lastModified = lastUpdate;
	}
}
