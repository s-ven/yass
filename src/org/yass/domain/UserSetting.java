package org.yass.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_SETTING")
public class UserSetting implements Serializable {

	@Id
	@Column(name = "USER_ID", insertable = false, updatable = false)
	private int userId;
	@Column(name = "LOADED_TRACK_ID")
	private int loadedTrackId;
	private int volume;
	private boolean shuffle;
	private boolean repeat;
	@Column(name = "SHOW_REMAINING")
	private boolean showRemaining;
	@Column(name = "DISPLAY_MODE")
	private short displayMode;
	@Column(name = "STOP_FADEOUT")
	private int stopFadeout;
	@Column(name = "SKIP_FADEOUT")
	private int skipFadeout;
	@Column(name = "NEXT_FADEOUT")
	private int nextFadeout;
	@OneToOne
	private User user;
	private static final long serialVersionUID = 1L;

	public UserSetting() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(final int userId) {
		this.userId = userId;
	}

	public int getLoadedTrackId() {
		return loadedTrackId;
	}

	public void setLoadedTrackId(final int loadedTrackId) {
		this.loadedTrackId = loadedTrackId;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(final int volume) {
		this.volume = volume;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public void setShuffle(final boolean shuffle) {
		this.shuffle = shuffle;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(final boolean repeat) {
		this.repeat = repeat;
	}

	public boolean isShowRemaining() {
		return showRemaining;
	}

	public void setShowRemaining(final boolean showRemaining) {
		this.showRemaining = showRemaining;
	}

	public short getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(final short displayMode) {
		this.displayMode = displayMode;
	}

	public int getStopFadeout() {
		return stopFadeout;
	}

	public void setStopFadeout(final int stopFadeout) {
		this.stopFadeout = stopFadeout;
	}

	public int getSkipFadeout() {
		return skipFadeout;
	}

	public void setSkipFadeout(final int skipFadeout) {
		this.skipFadeout = skipFadeout;
	}

	public int getNextFadeout() {
		return nextFadeout;
	}

	public void setNextFadeout(final int nextFadeout) {
		this.nextFadeout = nextFadeout;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}
}
