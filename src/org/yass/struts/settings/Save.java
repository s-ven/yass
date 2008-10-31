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
package org.yass.struts.settings;

import java.util.LinkedHashSet;
import java.util.Set;

import org.yass.YassConstants;
import org.yass.domain.User;
import org.yass.domain.UserBrowsingContext;
import org.yass.domain.UserSetting;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	public int userId;
	public int loadedTrackId;
	public int volume;
	public boolean shuffle;
	public boolean loop;
	public boolean showRemaining;
	public short displayMode;
	public int stopFadeout;
	public int nextFadeout;
	public int skipFadeout;
	public Integer[] trackInfoIds;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;

	@Override
	public String execute() {
		final User user = getUser();
		if (LOG.isInfoEnabled())
			LOG.info("Saving Settings User id:" + user.getId());
		UserSetting settings = user.getUserSetting();
		if (settings == null)
			user.setUserSetting(settings = new UserSetting());
		settings.setDisplayMode(displayMode);
		settings.setLoadedTrackId(loadedTrackId);
		settings.setShuffle(shuffle);
		settings.setRepeat(loop);
		settings.setShowRemaining(showRemaining);
		settings.setNextFadeout(nextFadeout);
		settings.setStopFadeout(stopFadeout);
		settings.setSkipFadeout(skipFadeout);
		settings.setVolume(volume);
		final Set<UserBrowsingContext> browsingContext = new LinkedHashSet<UserBrowsingContext>();
		if (trackInfoIds != null && trackInfoIds.length != 0)
			for (final Integer trackInfoId : trackInfoIds) {
				final UserBrowsingContext context = new UserBrowsingContext(user);
				context.setTrackInfoId(trackInfoId);
				browsingContext.add(context);
			}
		USER_DAO.cleanBrowsingContext(user);
		user.setBrowsingContext(browsingContext);
		USER_DAO.save(user);
		return NONE;
	}
}
