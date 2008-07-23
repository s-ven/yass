package org.yass.domain;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yass.YassConstants;

public final class Track implements YassConstants {

	private final Map<String, TrackInfo> trackInfos = new LinkedHashMap<String, TrackInfo>();
	private String title;
	private int trackNr;
	private String path;
	private long length;
	private int id;
	private Library library;
	private Date lastUpdate = new Date(0);

	public void setTrackInfo(final String type, final TrackInfo trackInfo) {
		trackInfos.put(type, trackInfo);
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
		return trackInfos.get(type);
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
	public final Date getLastUpdate() {
		return lastUpdate;
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

	public void setTrackInfos(final Collection<TrackInfo> infos) {
		for (final TrackInfo trackInfo : infos)
			trackInfos.put(trackInfo.getType(), trackInfo);
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
			final Date lastUpdate, final int typeId) {
		this.id = id;
		this.path = path;
		this.title = title;
		this.trackNr = trackNr;
		this.length = length;
		this.lastUpdate = lastUpdate;
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
	public final void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
