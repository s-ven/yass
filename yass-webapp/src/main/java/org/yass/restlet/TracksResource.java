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
package org.yass.restlet;

import java.util.Collection;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackStat;
import org.yass.util.XMLElementBuilder;

/**
 * @author Sven Duzont
 * 
 */
public class TracksResource extends BaseResource {

	protected Collection<Track> tracks;

	/**
	 * @param context
	 * @param request
	 * @param response
	 */
	public TracksResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
		if (isAvailable()) {
			final Library lib = user.getLibrary();
			if (lib != null) {
				tracks = lib.getTracks();
				getVariants().add(new Variant(MediaType.TEXT_XML));
			} else
				setAvailable(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.yass.restlet.BaseResource#createXMLRepresentation(org.w3c.dom.Document)
	 */
	@Override
	protected void createXMLRepresentation(final Document doc) {
		LOG.info("Getting Tracks");
		final XMLElementBuilder libApp = new XMLElementBuilder(doc, "tracks");
		final Map<Integer, TrackStat> trackStats = user.getTracksStats();
		for (final Track track : tracks)
			if (track.getTrackInfo(ARTIST) != null && track.getTrackInfo(ALBUM) != null && track.getTrackInfo(GENRE) != null) {
				final XMLElementBuilder trackApp = libApp.appendChild("track");
				trackApp.append("id", track.getId()).append("trackNr", track.getTrackNr()).append("title", track.getTitle())
						.append("artist", track.getTrackInfo(ARTIST).getId()).append("album", track.getTrackInfo(ALBUM).getId())
						.append("genre", track.getTrackInfo(GENRE).getId());
				if (track.getTrackInfo(YEAR) != null)
					trackApp.append("year", track.getTrackInfo(YEAR).getValue());
				if (track.getTrackInfo(BITRATE) != null)
					trackApp.append("bitrate", track.getTrackInfo(BITRATE).getValue());
				trackApp.append("vbr", track.isVBR()).append("length", track.getDuration()).append("lastModified",
						track.getLastModified().getTime());
				final TrackStat stat = trackStats.get(track.getId());
				if (stat != null)
					trackApp.append("rating", stat.getRating()).append("playCount", stat.getPlayCount()).append("lastPlayed",
							stat.getLastPlayed() != null ? stat.getLastPlayed().getTime() : 0);
				else
					trackApp.append("rating", 0).append("playCount", 0).append("lastPlayed", 0);
			}
	}
}
