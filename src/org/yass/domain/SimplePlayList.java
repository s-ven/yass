package org.yass.domain;

import java.util.Date;

public class SimplePlayList extends PlayList {

	public SimplePlayList(final int id, final String name, final Date updateDate) {
		this.name = name;
		this.id = id;
		lastUpdate = updateDate;
	}
}
