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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SMART_PLAYLIST")
@DiscriminatorValue(value = "1")
@AttributeOverride(name = "id", column = @Column(name = "PLAYLIST_ID"))
public class SmartPlayList extends PlayList {

	@OneToMany(mappedBy = "smartPlayList")
	private Collection<SmartPlayListCondition> conditions = new LinkedHashSet<SmartPlayListCondition>();
	@Column(name = "MAX_TRACKS")
	private int maxTracks = 0;
	private int operator;
	@Column(name = "ORDER_BY")
	private String orderBy;

	/**
	 * 
	 */
	public SmartPlayList() {
		super();
	}

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

	/**
	 * @return the conditions
	 */
	public final Collection<SmartPlayListCondition> getConditions() {
		return conditions;
	}

	/**
	 * @return the maxTracks
	 */
	public final int getMaxTracks() {
		return maxTracks;
	}

	/**
	 * @return the operator
	 */
	public final int getOperator() {
		return operator;
	}

	/**
	 * @return the orderBy
	 */
	public final String getOrderBy() {
		return orderBy;
	}

	public String getSqlStatement() {
		final Iterator<SmartPlayListCondition> it = conditions.iterator();
		final StringBuilder sBuilder = new StringBuilder("select track_id from track_stat where ");
		while (it.hasNext()) {
			final SmartPlayListCondition condition = it.next();
			sBuilder.append(condition.getTerm()).append(condition.getOperator()).append(condition.getValue());
			if (it.hasNext())
				sBuilder.append(operator == 0 ? " and " : " or ");
		}
		if (orderBy != null)
			sBuilder.append(" order by ").append(orderBy);
		return sBuilder.toString();
	}

	/**
	 * @param conditions
	 *          the conditions to set
	 */
	public final void setConditions(final Set<SmartPlayListCondition> conditions) {
		this.conditions = conditions;
	}

	/**
	 * @param maxTracks
	 *          the maxTracks to set
	 */
	public final void setMaxTracks(final int maxTracks) {
		this.maxTracks = maxTracks;
	}

	/**
	 * @param operator
	 *          the operator to set
	 */
	public final void setOperator(final int operator) {
		this.operator = operator;
	}

	/**
	 * @param orderBy
	 *          the orderBy to set
	 */
	public final void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}
}
