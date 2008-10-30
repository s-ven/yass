package org.yass.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ALBUM_COVER")
@NamedQuery(name = "getAlbumCoverIdFromAlbumId", query = "select ac.albumId from AlbumCover ac where ac.albumId = ?1")
public class AlbumCover {

	public AlbumCover() {
		super();
	}

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

	/**
	 * @param description
	 * @param mimeType
	 * @param pictureData
	 * @param pictureType
	 */
	public AlbumCover(final int albumId, final String description, final String mimeType, final byte[] pictureData,
			final int pictureType) {
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

	/**
	 * Will translate the ID3 picture type int into a human readable type string
	 * 
	 * @param pictureType
	 * @return
	 */
	private final static String getPictureTypeAsString(final int pictureType) {
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
}
