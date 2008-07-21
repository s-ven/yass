package org.yass.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.yass.YassConstants;

public abstract class PlayList implements YassConstants {

	public int id;
	protected String name;
	public int typeId;
	public int userId = 1;
	public Date lastUpdate = new Date();
	public Set<Integer> trackIds = new LinkedHashSet<Integer>();

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

	public void add(final Integer[] toAdd) {
		trackIds.addAll(Arrays.asList(toAdd));
	}

	public void add(final Integer toAdd) {
		trackIds.add(toAdd);
	}
}
