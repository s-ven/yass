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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * @author svenduzont
 */
@Entity
@Table(name = "ALBUM_COVER")
@NamedQuery(name = "findAlbumCoverIdByAlbumId", query = "select ac.albumId from AlbumCover ac where ac.albumId = ?1")
public class AlbumCover {

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

	@Id
	@Column(name = "TRACK_INFO_ID")
	private int albumId;
	private String description;
	@Column(name = "MIME_TYPE")
	private String mimeType;
	@Column(name = "PICTURE_DATA")
	private byte[] pictureData;
	@Column(name = "PICTURE_TYPE")
	private int pictureType;

	/**
     *
     */
	public AlbumCover() {
		super();
	}

	/**
	 * @param albumId
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
	 * @return the description
	 */
	public final String getDescription() {
		return description;
	}

	/**
	 * @return the mimeType
	 */
	public final String getMimeType() {
		return mimeType;
	}

	/**
	 * @return the pictureData
	 */
	public final byte[] getPictureData() {
		return pictureData;
	}

	/**
	 * @return the pictureType
	 */
	public final int getPictureType() {
		return pictureType;
	}

	@Override
	public String toString() {
		return "{mimeType:'" + mimeType + "', description:'" + description + ", pictureType:'"
				+ getPictureTypeAsString(pictureType) + "'}";
	}
}
