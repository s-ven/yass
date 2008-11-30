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

import java.io.File;
import java.util.Date;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.YassConstants;
import org.yass.domain.Library;
import org.yass.domain.Track;
import org.yass.domain.TrackStat;
import org.yass.domain.User;

/**
 * @author Sven Duzont
 * 
 */
@Path("/users/{userId}/libraries/{libraryId}/tracks/{trackId}")
public class TrackResource implements YassConstants {

	public static final Log LOG = LogFactory.getLog(TrackResource.class);

	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getTrack(@PathParam("userId") final int userId, @PathParam("trackId") final int trackId) {
		final User user = USER_DAO.findById(userId);
		if (user == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Library lib = user.getLibrary();
		if (lib == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Track track = TRACK_DAO.findById(trackId);
		if (track == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		return Response.ok(new File(track.getPath()), MediaType.APPLICATION_OCTET_STREAM).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_XML)
	public Response saveTrack(@PathParam("userId") final int userId, @PathParam("trackId") final int trackId,
			@FormParam("rating") final int rating, @FormParam("playCount") final int playCount,
			@FormParam("lastPlayed") final Date lastPlayed) {
		final User user = USER_DAO.findById(userId);
		if (user == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Library lib = user.getLibrary();
		if (lib == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		final Track track = TRACK_DAO.findById(trackId);
		if (track == null)
			return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_XML).build();
		if (LOG.isInfoEnabled())
			LOG.info("Saving TrackStat for Track id:" + track.getId());
		TrackStat trackStat = user.getTracksStats().get(track.getId());
		if (trackStat == null)
			user.getTracksStats().put(track.getId(), trackStat = new TrackStat(user, track.getId()));
		trackStat.setRating(rating);
		trackStat.setPlayCount(playCount);
		trackStat.setLastPlayed(lastPlayed);
		USER_DAO.save(user);
		return Response.ok().build();
	}
}