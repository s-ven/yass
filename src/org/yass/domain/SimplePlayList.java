package org.yass.domain;

public class SimplePlayList extends PlayList {

	public int[] trackIds = {};

	public SimplePlayList(final String name) {
		this.name = name;
	}

	public void add(final int[] toAdd) {
		final int[] result = new int[trackIds.length + toAdd.length];
		System.arraycopy(trackIds, 0, result, 0, trackIds.length);
		System.arraycopy(toAdd, 0, result, trackIds.length, toAdd.length);
		trackIds = result;
	}
}
