package org.yass.domain;

import org.yass.dao.TrackInfoDao;

public class TrackInfo {

	public static final TrackInfoDao dao = new TrackInfoDao();
	private final String value;
	private int id;

	/**
	 * @param id
	 *          the id to set
	 */
	public final void setId(final int id) {
		this.id = id;
	}

	private final String type;

	public TrackInfo(final int id, final String type, final String value) {
		this.type = type;
		this.value = value;
		this.id = id;
	}

	public static TrackInfo getFromValue(final String value, final String type) {
		return dao.getFromValue(value.trim(), type);
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
