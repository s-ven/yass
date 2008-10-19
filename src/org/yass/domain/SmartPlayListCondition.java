package org.yass.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SMART_PLAYLIST_CONDITION")
public class SmartPlayListCondition {

	@Id
	private int id;
	@ManyToOne(targetEntity = org.yass.domain.SmartPlayList.class)
	@JoinColumn(name = "PLAYLIST_ID", referencedColumnName = "PLAYLIST_ID")
	private SmartPlayList smartPlayList;
	private String term;

	/**
	 * @return the smartPlayList
	 */
	public final SmartPlayList getSmartPlayList() {
		return smartPlayList;
	}

	/**
	 * @return the term
	 */
	public final String getTerm() {
		return term;
	}

	/**
	 * @return the operator
	 */
	public final String getOperator() {
		return operator;
	}

	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

	private String operator;
	private String value;

	public SmartPlayListCondition(final SmartPlayList smartPlayList, final String term, final String operator,
			final String value) {
		this.smartPlayList = smartPlayList;
		this.term = term;
		this.operator = operator;
		this.value = value;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}