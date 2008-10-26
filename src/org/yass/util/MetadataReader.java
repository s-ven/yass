package org.yass.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import org.yass.dao.AttachedPictureDao;
import org.yass.dao.TrackDao;
import org.yass.dao.TrackInfoDao;
import org.yass.domain.AlbumCoverPicture;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;

public class MetadataReader implements org.yass.YassConstants {

	private static final AttachedPictureDao ATTACHED_PICTURE_DAO = AttachedPictureDao.getInstance();
	private static final TrackDao TRACK_DAO = TrackDao.getInstance();
	private static final TrackInfoDao TRACK_INFO_DAO = TrackInfoDao.getInstance();
	private static final Log LOG = LogFactory.getLog(MetadataReader.class);
	private final String ext = "mp3";
	private static Map<String, String> mimeTypes = new LinkedHashMap<String, String>();
	static {
		mimeTypes.put("jpg", "image/jpeg");
		mimeTypes.put("image/jpg", "image/jpeg");
	}

	/**
	 * 
	 * @param library
	 */
	public final void scanLibrary(final Library library) {
		if (LOG.isInfoEnabled())
			LOG.info("Scanning path : " + library.getPath());
		final File root = new File(library.getPath());
		final File[] files = FileUtils.getFiles(root, FileUtils.getExtensionFilter(ext)).toArray(new File[] {});
		FileUtils.sortFiles(files);
		final Collection<Track> toKeep = new ArrayList<Track>();
		int id = 0;
		for (final File file : files) {
			if (LOG.isDebugEnabled())
				LOG.debug("Scanning track : " + file.getPath());
			id += 1;
			Track track = TRACK_DAO.getFromPath(file.getPath());
			if (track == null)
				track = new Track();
			if (file.lastModified() > track.getLastModified().getTime() && parseFile(file, track)) {
				library.add(track);
				if (LOG.isInfoEnabled())
					LOG.info(" New file inserted : " + id + "/" + files.length + " : " + file.getName());
				TRACK_DAO.save(track);
			}
			toKeep.add(track);
		}
		final Collection<Track> toDelete = new ArrayList<Track>(library.getTracks());
		toDelete.removeAll(toKeep);
		for (final Track track : toDelete) {
			library.getTracks().remove(track.getId());
			TRACK_DAO.delete(track);
		}
	}

	private final static boolean parseFile(final File file, final Track track) {
		try {
			final AudioFileFormat audioFormat = AudioSystem.getAudioFileFormat(file);
			if (audioFormat instanceof TAudioFileFormat) {
				final Map props = ((TAudioFileFormat) audioFormat).properties();
				// artist
				String artist = (String) props.get("author");
				if (artist == null || "".equals(artist))
					artist = UNKNOWN_ARTIST;
				track.setTrackInfo(TRACK_INFO_DAO.getFromValue(artist, ARTIST));
				// genre
				String genre = (String) props.get("mp3.id3tag.genre");
				if (genre == null || "".equals(genre))
					genre = UNKNOWN_GENRE;
				track.setTrackInfo(TRACK_INFO_DAO.getFromValue(GenresValuePair.getInstance().getValue(genre), GENRE));
				// album
				String album = (String) props.get("album");
				if (album == null || "".equals(album))
					album = UNKNOWN_ALBUM;
				final TrackInfo albumTrackInfo = TRACK_INFO_DAO.getFromValue(album, ALBUM);
				track.setTrackInfo(albumTrackInfo);
				final InputStream id3Frames = (InputStream) props.get("mp3.id3tag.v2");
				if (id3Frames != null) {
					final int tagVersion = Integer.parseInt((String) props.get("mp3.id3tag.v2.version"));
					final Iterator<AlbumCoverPicture> pictures = getAttachedPictures(albumTrackInfo.getId(), tagVersion,
							id3Frames).iterator();
					if (pictures.hasNext())
						ATTACHED_PICTURE_DAO.save(pictures.next());
				}
				// year
				final String year = (String) props.get("date");
				if (year != null && !"".equals(year))
					track.setTrackInfo(TRACK_INFO_DAO.getFromValue(year, YEAR));
				final Integer bitRate = (Integer) props.get("mp3.bitrate.nominal.bps");
				if (bitRate != null && !"".equals(album))
					track.setTrackInfo(TRACK_INFO_DAO.getFromValue(bitRate / 1000 + "", BITRATE));
				final Boolean vbr = (Boolean) props.get("mp3.vbr");
				if (vbr != null)
					track.setVBR(vbr.booleanValue());
				String title = ((String) props.get("title")).trim();
				if (title == null || "".equals(title))
					title = file.getName();
				track.setTitle(title);
				String trackNr = ((String) props.get("mp3.id3tag.track")).trim();
				int slashIndex;
				if ((slashIndex = trackNr.indexOf('/')) > 0)
					trackNr = trackNr.substring(0, slashIndex);
				track.setTrackNr(Integer.parseInt(trackNr));
				track.setPath(file.getPath());
				track.setLastModified(new Date(file.lastModified()));
				track.setLength((Long) props.get("duration") / 1000);
			}
		} catch (final Exception e) {
			LOG.error(e);
			return false;
		}
		return true;
	}

	/**
	 * Helper method that will read the id3frames and return a collection of
	 * {@link AlbumCoverPicture} objects
	 * 
	 * @param albumId
	 * @param tagVersion
	 * @param id3Frames
	 * @return
	 * @throws IOException
	 */
	public static Collection<AlbumCoverPicture> getAttachedPictures(final int albumId, final int tagVersion,
			final InputStream id3Frames) throws IOException {
		final Collection<AlbumCoverPicture> pics = new ArrayList<AlbumCoverPicture>();
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
				String mimeType;
				if (tagVersion == 2) {
					id3Frames.read(pictureDatas = new byte[3]);
					mimeType = new String(pictureDatas);
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
				mimeType = mimeType.toLowerCase();
				mimeType = mimeTypes.get(mimeType) != null ? mimeTypes.get(mimeType) : mimeType;
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
				pics.add(new AlbumCoverPicture(albumId, description, mimeType, pictureDatas, pictureType));
			} else
				id3Frames.skip(frameLength + 1);
		}
		return pics;
	}
}
