package org.yass.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "TRACK_INFO")
@NamedQuery(name = "getFromTypeAndValue", query = "SELECT ti FROM TrackInfo ti WHERE ti.type = ?1 AND ti.value = ?2")
public class TrackInfo {

	private String value;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * @param id
	 *          the id to set
	 */
	public final void setId(final int id) {
		this.id = id;
	}

	@Basic
	private String type;

	/**
	 * 
	 */
	public TrackInfo() {
		super();
	}

	public TrackInfo(final String type, final String value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public final String toString() {
		return "{id:" + getId() + ",type:\"" + getType() + "\",value:\"" + getValue() + "\"}";
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
}
