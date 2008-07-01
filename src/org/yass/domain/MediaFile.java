package org.yass.domain;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.document.Document;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3FileReader;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.id3.valuepair.GenreTypes;
import org.yass.lucene.Constants;

public final class MediaFile implements Constants {

	private final String uuid;
	private String artist;
	private String album;
	private String genre;
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

	public MediaFile(final File file) throws IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		// TODO : Use regexp to parse tags
		final AudioFile audioFile = new MP3FileReader().read(file);
		final Tag mp3Tag = audioFile.getTag();
		length = audioFile.getAudioHeader().getTrackLength();
		artist = mp3Tag.getFirstArtist().trim();
		if ("".equals(artist))
			artist = UNKNOWN_ARTIST;
		album = mp3Tag.getFirstAlbum().trim();
		if ("".equals(album))
			album = UNKNOWN_ALBUM;
		genre = mp3Tag.getFirstGenre().trim();
		if (genre.startsWith("("))
			genre = GenreTypes.getInstanceOf().getValueForId(Integer.parseInt(genre.substring(1, genre.indexOf(')')))).trim();
		if ("".equals(genre))
			genre = UNKNOWN_GENRE;
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

	public MediaFile(final Document doc) {
		artist = doc.getFieldable(ARTIST).stringValue();
		album = doc.getFieldable(ALBUM).stringValue();
		genre = doc.getFieldable(GENRE).stringValue();
		title = doc.getFieldable(TITLE).stringValue();
		track = doc.getFieldable(TRACK).stringValue();
		path = doc.getFieldable(PATH).stringValue();
		length = Integer.parseInt(doc.getFieldable(LENGTH).stringValue());
		uuid = java.util.UUID.nameUUIDFromBytes(path.getBytes()).toString();
	}

	/**
	 * @return the artist
	 */
	public final String getArtist() {
		return artist;
	}

	/**
	 * @return the album
	 */
	public final String getAlbum() {
		return album;
	}

	/**
	 * @return the genre
	 */
	public final String getGenre() {
		return genre;
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
