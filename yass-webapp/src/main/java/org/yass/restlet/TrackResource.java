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
import java.util.Date;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;
import org.yass.domain.Track;
import org.yass.domain.TrackStat;

/**
 * @author Sven Duzont
 * 
 */
public class TrackResource extends BaseResource {

	private Track track;
	protected Collection<Track> tracks;

	/**
	 * @param context
	 * @param request
	 * @param response
	 */
	public TrackResource(final Context context, final Request request, final Response response) {
		super(context, request, response);
		if (isAvailable() && (track = TRACK_DAO.findById(getIntAttribute("trackId"))) != null) {
			getVariants().add(new Variant(MediaType.AUDIO_ALL));
			setModifiable(true);
		} else
			setAvailable(false);
	}

	/**
	 * Handle POST requests: create a new item.
	 */
	@Override
	public void acceptRepresentation(final Representation entity) throws ResourceException {
		final Form form = new Form(entity);
		if (LOG.isInfoEnabled())
			LOG.info("Saving TrackStat for Track id:" + track.getId());
		TrackStat trackStat = user.getTracksStats().get(track.getId());
		if (trackStat == null)
			user.getTracksStats().put(track.getId(), trackStat = new TrackStat(user, track.getId()));
		trackStat.setRating(form.getInt("rating"));
		trackStat.setPlayCount(form.getInt("playCount"));
		trackStat.setLastPlayed(new Date(form.getLong("lastPlayed")));
		USER_DAO.save(user);
	}

	/**
	 * Returns a {@link FileRepresentation}
	 */
	@Override
	public Representation represent(final Variant variant) throws ResourceException {
		if (MediaType.AUDIO_ALL.equals(variant.getMediaType()))
			return new FileRepresentation(track.getPath(), MediaType.AUDIO_MPEG);
		return null;
	}
}
