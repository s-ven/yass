package org.yass.id3;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import org.yass.dao.AttachedPictureDao;
import org.yass.dao.TrackDao;
import org.yass.domain.GenresValuePair;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackInfo;
import org.yass.util.FileUtils;

public class MetadataReader implements org.yass.YassConstants {

	private static final AttachedPictureDao ATTACHED_PICTURE_DAO = new AttachedPictureDao();
	private static final TrackDao TRACK_DAO = new TrackDao();
	private final static Log LOG = LogFactory.getLog(MetadataReader.class);
	private final String ext = "mp3";

	public final void scanLibrary(final Library lib) {
		LOG.info("Scanning path : " + lib.path);
		final File root = new File(lib.path);
		final File[] files = FileUtils.getFiles(root, FileUtils.getExtensionFilter(ext)).toArray(new File[] {});
		FileUtils.sortFiles(files);
		final Collection<Track> toKeep = new ArrayList<Track>();
		int id = 0;
		for (final File file : files) {
			id += 1;
			Track track = lib.getFromPath(file.getPath());
			if (track == null)
				track = new Track();
			if (file.lastModified() > track.getLastModified().getTime() && parseFile(file, track)) {
				lib.add(track);
				LOG.info(" file : " + id + "/" + files.length + " : " + file.getName());
				TRACK_DAO.save(track);
			}
			toKeep.add(track);
		}
		final Collection<Track> toDelete = new ArrayList<Track>(lib.getTracks());
		toDelete.removeAll(toKeep);
		for (final Track track : toDelete) {
			lib.tracks.remove(track.getId());
			TRACK_DAO.delete(track);
		}
	}

	public final static boolean parseFile(final File file, final Track track) {
		try {
			final AudioFileFormat audioFormat = AudioSystem.getAudioFileFormat(file);
			if (audioFormat instanceof TAudioFileFormat) {
				final Map props = ((TAudioFileFormat) audioFormat).properties();
				// artist
				String artist = (String) props.get("author");
				if (artist == null || "".equals(artist))
					artist = UNKNOWN_ARTIST;
				track.setTrackInfo(ARTIST, TrackInfo.getFromValue(artist, ARTIST));
				// genre
				String genre = (String) props.get("mp3.id3tag.genre");
				if (genre == null || "".equals(genre))
					genre = UNKNOWN_GENRE;
				track.setTrackInfo(GENRE, TrackInfo.getFromValue(GenresValuePair.getInstance().getValue(genre), GENRE));
				// album
				String album = (String) props.get("album");
				if (album == null || "".equals(album))
					album = UNKNOWN_ALBUM;
				final TrackInfo albumTrackInfo = TrackInfo.getFromValue(album, ALBUM);
				track.setTrackInfo(ALBUM, albumTrackInfo);
				final InputStream id3Frames = (InputStream) props.get("mp3.id3tag.v2");
				if (id3Frames != null) {
					final int tagVersion = Integer.parseInt((String) props.get("mp3.id3tag.v2.version"));
					final Iterator<AttachedPicture> pictures = AttachedPicture.getAttachedPictures(albumTrackInfo.getId(),
							tagVersion, id3Frames).iterator();
					if (pictures.hasNext())
						ATTACHED_PICTURE_DAO.save(pictures.next());
				}
				// year
				final String year = (String) props.get("date");
				if (year != null && !"".equals(year))
					track.setTrackInfo(YEAR, TrackInfo.getFromValue(year, YEAR));
				final Integer bitRate = (Integer) props.get("mp3.bitrate.nominal.bps");
				if (bitRate != null && !"".equals(album))
					track.setTrackInfo(BITRATE, TrackInfo.getFromValue(bitRate / 1000 + "", BITRATE));
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
}
