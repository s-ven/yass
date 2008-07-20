package org.yass.domain;

import java.util.Date;

public class SimplePlayList extends PlayList {

	public Integer[] trackIds = {};

	public SimplePlayList(final int id, final String name, final Date updateDate) {
		this.name = name;
		this.id = id;
		lastUpdate = updateDate;
	}

	public void add(final Integer[] toAdd) {
		final Integer[] result = new Integer[trackIds.length + toAdd.length];
		System.arraycopy(trackIds, 0, result, 0, trackIds.length);
		System.arraycopy(toAdd, 0, result, trackIds.length, toAdd.length);
		trackIds = result;
	}
}
