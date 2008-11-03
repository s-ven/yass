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
package org.yass.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_SETTING")
@SuppressWarnings("all")
public class UserSetting implements Serializable {

	private static final long serialVersionUID = 1L;
	@Column(name = "DISPLAY_MODE")
	private short displayMode;
	@Column(name = "LOADED_TRACK_ID")
	private int loadedTrackId;
	@Column(name = "NEXT_FADEOUT")
	private int nextFadeout;
	private boolean repeat;
	@Column(name = "SHOW_REMAINING")
	private boolean showRemaining;
	private boolean shuffle;
	@Column(name = "SKIP_FADEOUT")
	private int skipFadeout;
	@Column(name = "STOP_FADEOUT")
	private int stopFadeout;
	@OneToOne
	@Id
	@JoinColumn(name = "USER_ID")
	private User user;
	private int volume;

	public UserSetting() {
		super();
	}

	public short getDisplayMode() {
		return displayMode;
	}

	public int getLoadedTrackId() {
		return loadedTrackId;
	}

	public int getNextFadeout() {
		return nextFadeout;
	}

	public int getSkipFadeout() {
		return skipFadeout;
	}

	public int getStopFadeout() {
		return stopFadeout;
	}

	public int getVolume() {
		return volume;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public boolean isShowRemaining() {
		return showRemaining;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public void setDisplayMode(final short displayMode) {
		this.displayMode = displayMode;
	}

	public void setLoadedTrackId(final int loadedTrackId) {
		this.loadedTrackId = loadedTrackId;
	}

	public void setLoop(final boolean repeat) {
		this.repeat = repeat;
	}

	public void setNextFadeout(final int nextFadeout) {
		this.nextFadeout = nextFadeout;
	}

	public void setShowRemaining(final boolean showRemaining) {
		this.showRemaining = showRemaining;
	}

	public void setShuffle(final boolean shuffle) {
		this.shuffle = shuffle;
	}

	public void setSkipFadeout(final int skipFadeout) {
		this.skipFadeout = skipFadeout;
	}

	public void setStopFadeout(final int stopFadeout) {
		this.stopFadeout = stopFadeout;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setVolume(final int volume) {
		this.volume = volume;
	}
}
