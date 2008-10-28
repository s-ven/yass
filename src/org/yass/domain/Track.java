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

import org.yass.YassConstants;

@Entity
@Table(name = "TRACK")
@NamedQueries( { @NamedQuery(name = "getTrackByPath", query = "SELECT t FROM Track t where t.path = ?1"),
		@NamedQuery(name = "getTrackById", query = "SELECT t FROM Track t where t.id = ?1") })
public class Track implements YassConstants {

	public final void setLibrary(final Library library) {
	}

	public Track() {
		super();
	}

	@ManyToMany(targetEntity = TrackInfo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "TRACK_TRACK_INFO", joinColumns = @JoinColumn(name = "TRACK_ID"), inverseJoinColumns = @JoinColumn(name = "TRACK_INFO_ID"))
	@MapKey(name = "type")
	private Map<String, TrackInfo> trackInfos = new LinkedHashMap<String, TrackInfo>();
	private String title;
	@Column(name = "TRACK_NR")
	private int trackNr;
	private String path;
	private long length;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
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
		return trackInfos.values();
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

	public TrackInfo getTrackInfo(final String trackInfoType) {
		return trackInfos.get(trackInfoType);
	}

	public void setTrackInfo(final TrackInfo trackInfo) {
		trackInfos.put(trackInfo.getType(), trackInfo);
	}
}
