package org.yass.domain;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.yass.lucene.Constants;

public final class Track implements Constants {

	private final Map<String, TrackInfo> trackInfos = new LinkedHashMap<String, TrackInfo>();
	private String title;
	private int trackNr;
	private String path;
	private long length;
	private int id;
	private LibraryPlayList library;
	private Date lastUpdate = new Date(0);
	private int rating = 0;
	private int playCount = 0;

	public void setTrackInfo(final String type, final TrackInfo trackInfo) {
		trackInfos.put(type, trackInfo);
	}

	/**
	 * @param library
	 *          the library to set
	 */
	public final void setLibrary(final LibraryPlayList library) {
		this.library = library;
	}

	/**
	 * @return the library
	 */
	public final LibraryPlayList getLibrary() {
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

	public Track(final Document doc) {
		trackInfos.put(ARTIST, TrackInfo.getFromValue(doc.getFieldable(ARTIST).stringValue(), ARTIST));
		trackInfos.put(GENRE, TrackInfo.getFromValue(doc.getFieldable(GENRE).stringValue(), GENRE));
		trackInfos.put(ALBUM, TrackInfo.getFromValue(doc.getFieldable(ALBUM).stringValue(), ALBUM));
		title = doc.getFieldable(TITLE).stringValue();
		final String trackNr = doc.getFieldable(TRACK).stringValue();
		if (!"".equals(trackNr))
			this.trackNr = Integer.parseInt(trackNr);
		else
			this.trackNr = -1;
		path = doc.getFieldable(PATH).stringValue();
		length = Integer.parseInt(doc.getFieldable(LENGTH).stringValue());
		lastUpdate = new Date();
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
			trackInfos.put(trackInfo.type, trackInfo);
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
			final Date lastUpdate, final int playCount, final int rating) {
		this.id = id;
		this.path = path;
		this.title = title;
		this.trackNr = trackNr;
		this.length = length;
		this.lastUpdate = lastUpdate;
		this.playCount = playCount;
		this.rating = rating;
	}

	public Track() {
	}

	/**
	 * @return the rating
	 */
	public final int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *          the rating to set
	 */
	public final void setRating(final int rating) {
		this.rating = rating;
	}

	/**
	 * @return the playCount
	 */
	public final int getPlayCount() {
		return playCount;
	}

	/**
	 * @param playCount
	 *          the playCount to set
	 */
	public final void setPlayCount(final int playCount) {
		this.playCount = playCount;
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
