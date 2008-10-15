package org.yass.domain;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SMART_PLAYLIST")
@DiscriminatorValue(value = "1")
@AttributeOverride(name = "id", column = @Column(name = "PLAYLIST_ID"))
public class SmartPlayList extends PlayList {

	@Column(name = "MAX_TRACKS")
	private int maxTracks = 0;
	@Column(name = "ORDER_BY")
	private String orderBy;
	private int operator;

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

	@OneToMany
	private Set<SmartPlayListCondition> conditions = new LinkedHashSet<SmartPlayListCondition>();

	/**
	 * @return the maxTracks
	 */
	public final int getMaxTracks() {
		return maxTracks;
	}

	/**
	 * @param maxTracks
	 *          the maxTracks to set
	 */
	public final void setMaxTracks(final int maxTracks) {
		this.maxTracks = maxTracks;
	}

	/**
	 * @return the orderBy
	 */
	public final String getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *          the orderBy to set
	 */
	public final void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * @return the operator
	 */
	public final int getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *          the operator to set
	 */
	public final void setOperator(final int operator) {
		this.operator = operator;
	}

	/**
	 * @return the conditions
	 */
	public final Set<SmartPlayListCondition> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *          the conditions to set
	 */
	public final void setConditions(final Set<SmartPlayListCondition> conditions) {
		this.conditions = conditions;
	}

	@Embeddable
	@Table(name = "SMART_PLAYLIST_CONDITION")
	public static class SmartPlayListCondition {

		@ManyToOne
		private SmartPlayList smartPlayList;
		private String term;
		private String operator;
		private String value;

		public SmartPlayListCondition(final SmartPlayList smartPlayList, final String term, final String operator,
				final String value) {
			this.smartPlayList = smartPlayList;
			this.term = term;
			this.operator = operator;
			this.value = value;
		}
	}
}
