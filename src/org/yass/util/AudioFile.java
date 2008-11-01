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
package org.yass.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.file.TAudioFileFormat;
import org.yass.YassConstants;
import org.yass.domain.AlbumCover;
import org.yass.domain.TrackInfo;

/**
 * @author Sven Duzont
 * 
 */
public class AudioFile implements YassConstants {

	private static Map<String, String> pictureMimeTypes = new LinkedHashMap<String, String>();
	private static final String UNKNOWN_ALBUM = "<Unknown Album>";
	private static final String UNKNOWN_ARTIST = "<Unknown Artist>";
	private static final String UNKNOWN_GENRE = "<Unknown Genre>";
	static {
		pictureMimeTypes.put("jpg", "image/jpeg");
		pictureMimeTypes.put("image/jpg", "image/jpeg");
	}
	private final File file;
	@SuppressWarnings("unchecked")
	private final Map props;

	/**
	 * @param file
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
	public AudioFile(final File file) throws UnsupportedAudioFileException, IOException {
		final AudioFileFormat audioFormat = AudioSystem.getAudioFileFormat(file);
		this.file = file;
		if (audioFormat instanceof TAudioFileFormat)
			props = ((TAudioFileFormat) audioFormat).properties();
		else
			throw new UnsupportedAudioFileException();
	}

	/**
	 * @param props
	 * @return
	 */
	public String getAlbum() {
		final String album = (String) props.get("album");
		if (album == null || "".equals(album))
			return UNKNOWN_ALBUM;
		return album;
	}

	/**
	 * @param props
	 * @param albumTrackInfo
	 * @param cover
	 * @return
	 * @throws IOException
	 */
	public AlbumCover getAlbumCover() throws IOException {
		final int albumTrackInfoId = getAlbumTrackInfo().getId();
		if (!ATTACHED_PICTURE_DAO.hasPicture(albumTrackInfoId)) {
			final InputStream id3Frames = (InputStream) props.get("mp3.id3tag.v2");
			if (id3Frames != null) {
				final int tagVersion = Integer.parseInt((String) props.get("mp3.id3tag.v2.version"));
				return getAlbumCover(albumTrackInfoId, tagVersion, id3Frames);
			}
		}
		return null;
	}

	/**
	 * Helper method that will read the id3frames and return a collection of
	 * {@link AlbumCover} objects
	 * 
	 * @param albumId
	 * @param tagVersion
	 * @param id3Frames
	 * @return
	 * @throws IOException
	 */
	private AlbumCover getAlbumCover(final int albumId, final int tagVersion, final InputStream id3Frames)
			throws IOException {
		final int frameIDSize = tagVersion == 2 ? 3 : 4;
		byte[] pictureDatas;
		int loneByte;
		id3Frames.skip(10);
		while (id3Frames.available() > frameIDSize) {
			id3Frames.read(pictureDatas = new byte[frameIDSize]);
			// corrupted frames
			if (pictureDatas[0] == 0)
				break;
			final String frameID = new String(pictureDatas);
			int frameLength = 0;
			if (tagVersion == 4) {
				frameLength += (id3Frames.read() & 0xFF) << 21;
				frameLength += (id3Frames.read() & 0xFF) << 14;
				frameLength += (id3Frames.read() & 0xFF) << 7;
				frameLength += id3Frames.read() & 0xFF;
			} else {
				if (tagVersion == 3)
					frameLength += (id3Frames.read() & 0xFF) << 24;
				frameLength += (id3Frames.read() & 0xFF) << 16;
				frameLength += (id3Frames.read() & 0xFF) << 8;
				frameLength += id3Frames.read() & 0xFF;
			}
			// if corrupted frames
			if (frameLength > id3Frames.available() || frameLength <= 0)
				break;
			// Skips the next two bytes if needed (useless flags and text
			// encoding)
			if (tagVersion != 2)
				id3Frames.skip(1);
			if (frameID.startsWith("APIC") || frameID.startsWith("PIC")) {
				id3Frames.skip(2);
				frameLength -= 2;
				// gets the mime type
				String pictureMimeType;
				if (tagVersion == 2) {
					id3Frames.read(pictureDatas = new byte[3]);
					pictureMimeType = new String(pictureDatas);
					frameLength -= 3;
				} else {
					pictureMimeType = "";
					while ((loneByte = id3Frames.read()) != 0) {
						frameLength--;
						pictureMimeType = pictureMimeType.concat(new String(new byte[] { (byte) loneByte }));
					}
				}
				if (pictureMimeType.length() == 0)
					break;
				// normalize the mimeType
				pictureMimeType = pictureMimeType.toLowerCase();
				pictureMimeType = pictureMimeTypes.get(pictureMimeType) != null ? pictureMimeTypes.get(pictureMimeType)
						: pictureMimeType;
				// Gets the picture type
				final int pictureType = id3Frames.read();
				frameLength--;
				// Gets the description
				String description = "";
				while ((loneByte = id3Frames.read()) != 0) {
					frameLength--;
					description = description.concat(new String(new byte[] { (byte) loneByte }));
				}
				// Gets the actual image
				id3Frames.read(pictureDatas = new byte[frameLength]);
				return new AlbumCover(albumId, description, pictureMimeType, pictureDatas, pictureType);
			} else
				id3Frames.skip(frameLength + 1);
		}
		return null;
	}

	/**
	 * @param audioFile
	 * @return
	 */
	public TrackInfo getAlbumTrackInfo() {
		return TRACK_INFO_DAO.getFromValue(getAlbum(), ALBUM);
	}

	/**
	 * @param props
	 * @return
	 */
	public String getArtist() {
		final String artist = (String) props.get("author");
		if (artist == null || "".equals(artist))
			return UNKNOWN_ARTIST;
		return artist;
	}

	public TrackInfo getArtistTrackInfo() {
		return TRACK_INFO_DAO.getFromValue(getArtist(), ARTIST);
	}

	/**
	 * @param props
	 * @return
	 */
	public String getBitRate() {
		final Integer bitRate = (Integer) props.get("mp3.bitrate.nominal.bps");
		if (bitRate != null && !"".equals(bitRate))
			return bitRate / 1000 + "";
		return null;
	}

	/**
	 * @param audioFile
	 * @return
	 */
	public TrackInfo getBitRateTrackInfo() {
		return TRACK_INFO_DAO.getFromValue(getBitRate(), BITRATE);
	}

	/**
	 * @param props
	 * @return
	 */
	public long getDuration() {
		return (Long) props.get("duration") / 1000;
	}

	/**
	 * @param props
	 * @return
	 */
	public String getGenre() {
		final String genre = (String) props.get("mp3.id3tag.genre");
		if (genre == null || "".equals(genre))
			return UNKNOWN_GENRE;
		return GenresValuePair.getInstance().getValue(genre);
	}

	/**
	 * @param audioFile
	 * @return
	 */
	public TrackInfo getGenreTrackInfo() {
		return TRACK_INFO_DAO.getFromValue(getGenre(), GENRE);
	}

	/**
	 * @param file
	 * @param props
	 * @return
	 */
	public String getTitle() {
		String title = (String) props.get("title");
		if (title == null || "".equals(title = title.trim()))
			return file.getName();
		return title;
	}

	/**
	 * @param props
	 * @return
	 */
	public int getTrackNr() {
		String trackNrs = (String) props.get("mp3.id3tag.track");
		if (trackNrs != null) {
			trackNrs = trackNrs.trim();
			int slashIndex;
			if ((slashIndex = trackNrs.indexOf('/')) > 0)
				trackNrs = trackNrs.substring(0, slashIndex);
			return Integer.parseInt(trackNrs);
		}
		return 0;
	}

	/**
	 * @param props
	 * @return
	 */
	public boolean getVBR() {
		final Boolean vbr = (Boolean) props.get("mp3.vbr");
		if (vbr != null)
			return vbr.booleanValue();
		return false;
	}

	/**
	 * @param props
	 * @return
	 */
	public String getYear() {
		return (String) props.get("date");
	}

	/**
	 * @param audioFile
	 * @return
	 */
	public TrackInfo getYearTrackInfo() {
		return TRACK_INFO_DAO.getFromValue(getYear(), YEAR);
	}
}
