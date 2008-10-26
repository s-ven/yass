package org.yass.struts.settings;

import org.yass.YassConstants;
import org.yass.domain.User;
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
			LOG.info("Saving Settings for user id:" + user.getId());
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
		// new SettingsDao().save(settings);
		return NONE;
	}
}
