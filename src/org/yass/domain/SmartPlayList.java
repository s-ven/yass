package org.yass.domain;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class SmartPlayList extends PlayList {

	public int maxTracks = 0;
	public String orderBy;
	public int operator;

	/**
	 * @param id
	 * @param maxTracks
	 * @param operator
	 * @param orderBy
	 */
	public SmartPlayList(final int id, final String name, final int maxTracks, final int operator, final String orderBy) {
		super();
		typeId = 1;
		this.name = name;
		this.id = id;
		this.maxTracks = maxTracks;
		this.operator = operator;
		this.orderBy = orderBy;
	}

	public String getSqlStatement() {
		final Iterator<SmartPlayListCondition> it = conditions.iterator();
		final StringBuffer buff = new StringBuffer("select track_id from track_stat where ");
		while (it.hasNext()) {
			final SmartPlayListCondition condition = it.next();
			buff.append(condition.term).append(condition.operator).append(condition.value);
			if (it.hasNext())
				buff.append(operator == 0 ? " and " : " or ");
		}
		if (orderBy != null)
			buff.append(" order by ").append(orderBy);
		return buff.toString();
	}

	public Set<SmartPlayListCondition> conditions = new LinkedHashSet<SmartPlayListCondition>();
}
