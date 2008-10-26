package org.yass.domain;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.yass.dao.TrackInfoDao;

@Entity
@Table(name = "TRACK_INFO")
public class TrackInfo {

	public static TrackInfoDao dao = null;
	private String value;
	@Id
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

	public TrackInfo(final int id, final String type, final String value) {
		this.type = type;
		this.value = value;
		this.id = id;
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
