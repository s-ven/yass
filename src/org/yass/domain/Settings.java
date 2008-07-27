package org.yass.domain;

public class Settings {

	private final int userId;
	private int loadedTrackId;
	private int volume;
	private boolean shuffle;
	private boolean loop;
	private boolean showRemaining;
	private int displayMode;
	private int stopFadeout;
	private int nextFadeout;
	private int skipFadeout;
	private Integer[] trackInfoIds = new Integer[] {};

	/**
	 * @return the trackInfoIds
	 */
	public final Integer[] getTrackInfoIds() {
		return trackInfoIds;
	}

	/**
	 * @param trackInfoIds
	 *          the trackInfoIds to set
	 */
	public final void setTrackInfoIds(final Integer[] trackInfoIds) {
		this.trackInfoIds = trackInfoIds;
	}

	/**
	 * @param userId
	 * @param shuffle
	 * @param loop
	 * @param volume
	 * @param displayMode
	 * @param loadedTrackId
	 * @param showRemaining
	 * @param nextFadeout
	 * @param skipFadeout
	 * @param stopFadeout
	 */
	public Settings(final int userId, final int loadedTrackId, final int volume, final boolean shuffle,
			final boolean loop, final boolean showRemaining, final int displayMode, final int stopFadeout,
			final int skipFadeout, final int nextFadeout, final Integer[] trackInfoIds) {
		super();
		this.userId = userId;
		this.shuffle = shuffle;
		this.loop = loop;
		this.volume = volume;
		this.displayMode = displayMode;
		this.loadedTrackId = loadedTrackId;
		this.showRemaining = showRemaining;
		this.nextFadeout = nextFadeout;
		this.skipFadeout = skipFadeout;
		this.stopFadeout = stopFadeout;
		this.trackInfoIds = trackInfoIds == null ? new Integer[] {} : trackInfoIds;
	}

	/**
	 * @return the userId
	 */
	public final int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 */
	public Settings(final int userId) {
		super();
		this.userId = userId;
	}

	/**
	 * @return the loadedTrackId
	 */
	public final int getLoadedTrackId() {
		return loadedTrackId;
	}

	/**
	 * @param loadedTrackId
	 *          the loadedTrackId to set
	 */
	public final void setLoadedTrackId(final int loadedTrackId) {
		this.loadedTrackId = loadedTrackId;
	}

	/**
	 * @return the volume
	 */
	public final int getVolume() {
		return volume;
	}

	/**
	 * @param volume
	 *          the volume to set
	 */
	public final void setVolume(final int volume) {
		this.volume = volume;
	}

	/**
	 * @return the shuffle
	 */
	public final boolean isShuffle() {
		return shuffle;
	}

	/**
	 * @param shuffle
	 *          the shuffle to set
	 */
	public final void setShuffle(final boolean shuffle) {
		this.shuffle = shuffle;
	}

	/**
	 * @return the repeat
	 */
	public final boolean isLoop() {
		return loop;
	}

	/**
	 * @param repeat
	 *          the repeat to set
	 */
	public final void setLoop(final boolean repeat) {
		loop = repeat;
	}

	/**
	 * @return the showRemaining
	 */
	public final boolean isShowRemaining() {
		return showRemaining;
	}

	/**
	 * @param showRemaining
	 *          the showRemaining to set
	 */
	public final void setShowRemaining(final boolean showRemaining) {
		this.showRemaining = showRemaining;
	}

	/**
	 * @return the displayMode
	 */
	public final int getDisplayMode() {
		return displayMode;
	}

	/**
	 * @param displayMode
	 *          the displayMode to set
	 */
	public final void setDisplayMode(final int displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @return the stopFadeout
	 */
	public final int getStopFadeout() {
		return stopFadeout;
	}

	/**
	 * @param stopFadeout
	 *          the stopFadeout to set
	 */
	public final void setStopFadeout(final int stopFadeout) {
		this.stopFadeout = stopFadeout;
	}

	/**
	 * @return the nextFadeout
	 */
	public final int getNextFadeout() {
		return nextFadeout;
	}

	/**
	 * @param nextFadeout
	 *          the nextFadeout to set
	 */
	public final void setNextFadeout(final int nextFadeout) {
		this.nextFadeout = nextFadeout;
	}

	/**
	 * @return the skipFadeout
	 */
	public final int getSkipFadeout() {
		return skipFadeout;
	}

	/**
	 * @param skipFadeout
	 *          the skipFadeout to set
	 */
	public final void setSkipFadeout(final int skipFadeout) {
		this.skipFadeout = skipFadeout;
	}
}