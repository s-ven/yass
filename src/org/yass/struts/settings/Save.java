package org.yass.struts.settings;

import org.yass.YassConstants;
import org.yass.dao.SettingsDao;
import org.yass.domain.Settings;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	public int userId;
	public int loadedTrackId;
	public int volume;
	public boolean shuffle;
	public boolean loop;
	public boolean showRemaining;
	public int displayMode;
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
		LOG.info("Saving Settings for user id:");
		final Settings settings = new Settings(1, loadedTrackId, volume, shuffle, loop, showRemaining, displayMode,
				stopFadeout, skipFadeout, nextFadeout, trackInfoIds);
		new SettingsDao().save(settings);
		return NONE;
	}
}
