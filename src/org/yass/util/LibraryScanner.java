package org.yass.util;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.Track;

public class LibraryScanner implements YassConstants, Runnable {

	private static final Log LOG = LogFactory.getLog(LibraryScanner.class);
	private final String extensions = "mp3";
	private final Library library;

	/**
	 * @param library
	 */
	public LibraryScanner(final Library library) {
		super();
		this.library = library;
	}

	private boolean parseFile(final File file, final Track track) {
		try {
			if (LOG.isDebugEnabled())
				LOG.debug("Parsing File path:" + file.getPath());
			track.setPath(file.getPath());
			track.setLastModified(new Date(file.lastModified()));
			final AudioFile audioFile = new AudioFile(file);
			track.setVBR(audioFile.getVBR());
			track.setTitle(audioFile.getTitle());
			track.setTrackNr(audioFile.getTrackNr());
			track.setDuration(audioFile.getDuration());
			track.setTrackInfo(audioFile.getArtistTrackInfo());
			track.setTrackInfo(audioFile.getGenreTrackInfo());
			track.setTrackInfo(audioFile.getYearTrackInfo());
			track.setTrackInfo(audioFile.getBitRateTrackInfo());
			track.setTrackInfo(audioFile.getAlbumTrackInfo());
			ATTACHED_PICTURE_DAO.save(audioFile.getAlbumCover());
		} catch (final Exception e) {
			if (LOG.isWarnEnabled())
				LOG.warn("Error parsing File path:" + file.getPath() + " : " + e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param library
	 */
	public final void run() {
		if (LOG.isInfoEnabled())
			LOG.info("Scanning Library path:" + library.getPath());
		// Recursively get all files matching the extensions contained into the
		// extensions String
		final File[] files = FileUtils.getFiles(new File(library.getPath()), FileUtils.getExtensionFilter(extensions))
				.toArray(new File[] {});
		FileUtils.sortFiles(files);
		// Will serve to remove the tracks that doesn't exist anymore into the
		// persistent store
		final Collection<Track> toKeep = new HashSet<Track>(files.length);
		int fileIndex = 0;
		for (final File file : files) {
			fileIndex += 1;
			Track track = TRACK_DAO.getByPath(file.getPath());
			// If the track doesn't already exists into the persistent store or have
			// been modified, will parse it and store it
			if (track == null && parseFile(file, track = new Track())
					|| file.lastModified() > track.getLastModified().getTime() && parseFile(file, track)) {
				library.addTrack(track);
				toKeep.add(TRACK_DAO.save(track));
				if (LOG.isInfoEnabled())
					LOG.info("Added new Track : " + fileIndex + "/" + files.length + " path:" + track.getPath());
			} else if (track != null)
				toKeep.add(track);
		}
		// Keeps only the tracks that are on disk
		library.getTracks().retainAll(toKeep);
		LIBRARY_DAO.save(library);
		LOG.info("Scanning Library path over");
	}
}
