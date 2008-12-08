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
package org.yass.rest;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackStat;
import org.yass.domain.User;
import org.yass.util.XMLElementBuilder;
import org.yass.util.XMLSerializer;

/**
 * @author Sven Duzont
 * 
 */
@Path("/users/{userId}/libraries/{libraryId}/tracks")
public class TracksResource implements YassConstants {

	/**
     *
     */
	public static final Log LOG = LogFactory.getLog(TracksResource.class);

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws javax.xml.parsers.ParserConfigurationException
	 */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Response getTracks(@PathParam("userId") final int userId, @PathParam("libraryId") final int libraryId)
			throws ParserConfigurationException {
		final User user = USER_DAO.findById(userId);
		if (user == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Library lib = user.getLibrary(libraryId);
		if (lib == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Collection<Track> tracks = lib.getTracks();
		LOG.info("Getting Tracks for User id:" + userId);
		final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
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
		return Response.ok(XMLSerializer.serialize(doc)).type(MediaType.APPLICATION_XML).build();
	}
}