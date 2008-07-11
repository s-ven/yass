package org.yass.domain;

import org.yass.dao.TrackInfoDao;
import org.yass.lucene.Constants;

public class TrackInfo implements Constants {

	public static final TrackInfoDao dao = new TrackInfoDao();
	public static int UID_COUNTER = 0;
	public String value;
	public int id;
	public String type;

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
		return "{id:" + id + ",type:\"" + type + "\",value:\"" + value + "\"}";
	}
}
