package org.yass.domain;

import java.util.Date;

import org.yass.YassConstants;

public abstract class PlayList implements YassConstants {

	public int id;
	protected String name;
	public int typeId;
	public int userId = 1;
	private final Date lastUpdate = new Date();

	/**
	 * @return the id
	 */
	public final int getId() {
		return id;
	}

	/**
	 * @return the lastUpdate
	 */
	public final Date getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @return the value
	 */
	public final String getName() {
		return name;
	}
}
