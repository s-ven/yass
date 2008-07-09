package org.yass.domain;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.yass.lucene.Constants;

public final class Track implements Constants {

	private final String uuid;
	private final Map<String, TrackInfo> properties = new LinkedHashMap<String, TrackInfo>();
	private String title;
	private String track;
	private final String path;
	private final int length;

	/**
	 * @return the length
	 */
	public final int getLength() {
		return length;
	}

	public Track(final File file) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		// TODO : Use regexp to parse tags
		final AudioFile audioFile = new MP3FileReader().read(file);
		final Tag mp3Tag = audioFile.getTag();
		length = audioFile.getAudioHeader().getTrackLength();
		properties.put(ARTIST, new TrackInfo(0, mp3Tag.getFirstArtist().trim(), ARTIST));
		properties.put(GENRE, new TrackInfo(0, mp3Tag.getFirstGenre().trim(), GENRE));
		properties.put(ALBUM, new TrackInfo(0, mp3Tag.getFirstAlbum().trim(), ALBUM));
		title = mp3Tag.getFirstTitle().trim();
		if ("".equals(title))
			title = UNKNOWN_TITLE;
		track = mp3Tag.getFirstTrack().trim();
		int slashIndex;
		if ((slashIndex = track.indexOf('/')) > 0)
			track = track.substring(0, slashIndex);
		path = file.getPath();
		uuid = java.util.UUID.nameUUIDFromBytes(path.getBytes()).toString();
	}

	public TrackInfo getProperty(final String type) {
		return properties.get(type);
	}

	public Track(final Document doc) {
		properties.put(ARTIST, TrackInfo.get(doc.getFieldable(ARTIST).stringValue(), ARTIST));
		properties.put(GENRE, TrackInfo.get(doc.getFieldable(GENRE).stringValue(), GENRE));
		properties.put(ALBUM, TrackInfo.get(doc.getFieldable(ALBUM).stringValue(), ALBUM));
		title = doc.getFieldable(TITLE).stringValue();
		track = doc.getFieldable(TRACK).stringValue();
		path = doc.getFieldable(PATH).stringValue();
		length = Integer.parseInt(doc.getFieldable(LENGTH).stringValue());
		uuid = java.util.UUID.nameUUIDFromBytes(path.getBytes()).toString();
	}

	/**
	 * @return the title
	 */
	public final String getTitle() {
		return title;
	}

	/**
	 * @return the track
	 */
	public final String getTrack() {
		return track;
	}

	/**
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * @return the uuid
	 */
	public final String getUuid() {
		return uuid;
	}
}
