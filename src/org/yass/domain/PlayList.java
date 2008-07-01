package org.yass.domain;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yass.YassConstants;

public class PlayList implements YassConstants {

	protected final Map<String, MediaFile> mediaFiles = new LinkedHashMap<String, MediaFile>();
	public String id;
	public String name;
	public String type;

	public final MediaFile getMediaFile(final String uuid) {
		return mediaFiles.get(uuid);
	}

	/**
	 * @param mediaFiles
	 *          the mediaFiles to set
	 */
	public final void add(final MediaFile mediaFile) {
		mediaFiles.put(mediaFile.getUuid(), mediaFile);
	}

	public final void clean() {
		mediaFiles.clear();
	}

	/**
	 * @return the mediaFiles
	 */
	public final Collection<MediaFile> getMediaFiles() {
		return mediaFiles.values();
	}

	public void add(final PlayList pl) {
		for (final MediaFile mf : pl.mediaFiles.values())
			this.add(mf);
	}
}
