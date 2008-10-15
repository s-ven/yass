package org.yass.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ALBUM_COVER_PICTURE")
public class AlbumCoverPicture {

	@Id
	@Column(name = "TRACK_INFO_ID")
	private int albumId;
	@Column(name = "PICTURE_TYPE")
	private int pictureType;
	@Column(name = "MIME_TYPE")
	private String mimeType;
	private String description;
	@Column(name = "PICTURE_DATA")
	private byte[] pictureData;
	private static Map<String, String> mimeTypes = new LinkedHashMap<String, String>();
	static {
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("image/jpg", "image/jpeg");
	}

	/**
	 * @param description
	 * @param mimeType
	 * @param pictureData
	 * @param pictureType
	 */
	public AlbumCoverPicture(final int albumId, final String description, final String mimeType,
			final byte[] pictureData, final int pictureType) {
		super();
		this.albumId = albumId;
		this.description = description;
		this.mimeType = mimeType;
		this.pictureData = pictureData;
		this.pictureType = pictureType;
	}

	/**
	 * @return the albumId
	 */
	public final int getAlbumId() {
		return albumId;
	}

	/**
	 * @return the pictureType
	 */
	public final int getPictureType() {
		return pictureType;
	}

	/**
	 * @return the mimeType
	 */
	public final String getMimeType() {
		return mimeType;
	}

	/**
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the pictureData
	 */
	public final byte[] getPictureData() {
		return pictureData;
	}

	@Override
	public String toString() {
		return "{mimeType:'" + mimeType + "', description:'" + description + ", pictureType:'"
				+ getPictureTypeAsString(pictureType) + "'}";
	}

	public final static String getPictureTypeAsString(final int pictureType) {
		switch (pictureType & 0xFF) {
		case 0x00:
			return "Other";
		case 0x01:
			return "32x32 pixels file icon";
		case 0x02:
			return "Other file icon";
		case 0x03:
			return "Cover (front)";
		case 0x04:
			return "Cover (back)";
		case 0x05:
			return "Leaflet page";
		case 0x06:
			return "Media (e.g. lable side of CD)";
		case 0x07:
			return "Lead artist/lead performer/soloist";
		case 0x08:
			return "Artist/performer";
		case 0x09:
			return "Conductor";
		case 0x0A:
			return "Band/Orchestra";
		case 0x0B:
			return "Composer";
		case 0x0C:
			return "Lyricist/text writer";
		case 0x0D:
			return "Recording Location";
		case 0x0E:
			return "During recording";
		case 0x0F:
			return "During performance";
		case 0x10:
			return "Movie/video screen capture";
		case 0x11:
			return "A bright coloured fish";
		case 0x12:
			return "Illustration";
		case 0x13:
			return "Band/artist logotype";
		case 0x14:
			return "Publisher/Studio logotype";
		}
		return "Unknown";
	}

	public static Collection<AlbumCoverPicture> getAttachedPictures(final int albumId, final int tagVersion,
			final InputStream id3Frames) throws IOException {
		final Collection<AlbumCoverPicture> pics = new ArrayList<AlbumCoverPicture>();
		final int frameIDSize = tagVersion == 2 ? 3 : 4;
		byte[] b;
		int loneByte;
		id3Frames.skip(10);
		while (id3Frames.available() > frameIDSize) {
			id3Frames.read(b = new byte[frameIDSize]);
			// corrupted frames
			if (b[0] == 0)
				break;
			final String frameID = new String(b);
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
				String mimeType;
				if (tagVersion == 2) {
					id3Frames.read(b = new byte[3]);
					mimeType = new String(b);
					frameLength -= 3;
				} else {
					mimeType = "";
					while ((loneByte = id3Frames.read()) != 0) {
						frameLength--;
						mimeType = mimeType.concat(new String(new byte[] { (byte) loneByte }));
					}
				}
				if (mimeType.length() == 0)
					break;
				// normalize the mimeType
				mimeType = mimeTypes.get(mimeType.toLowerCase()) != null ? mimeTypes.get(mimeType.toLowerCase()) : mimeType
						.toLowerCase();
				// Gets the picture type
				final int picType = id3Frames.read();
				frameLength--;
				// Gets the description
				String description = "";
				while ((loneByte = id3Frames.read()) != 0) {
					frameLength--;
					description = description.concat(new String(new byte[] { (byte) loneByte }));
				}
				// Gets the actual image
				;
				id3Frames.read(b = new byte[frameLength]);
				pics.add(new AlbumCoverPicture(albumId, description, mimeType, b, picType));
			} else
				id3Frames.skip(frameLength + 1);
		}
		return pics;
	}
}
