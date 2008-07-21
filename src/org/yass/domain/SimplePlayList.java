package org.yass.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

public class SimplePlayList extends PlayList {

	public Set<Integer> trackIds = new LinkedHashSet<Integer>();

	public SimplePlayList(final int id, final String name, final Date updateDate) {
		this.name = name;
		this.id = id;
		lastUpdate = updateDate;
	}

	public void add(final Integer[] toAdd) {
		trackIds.addAll(Arrays.asList(toAdd));
	}

	public void add(final Integer toAdd) {
		trackIds.add(toAdd);
	}
}
