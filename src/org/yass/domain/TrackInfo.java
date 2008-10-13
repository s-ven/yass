package org.yass.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.yass.dao.TrackInfoDao;

@Entity
@Table(name = "TRACK_INFO")
public class TrackInfo {

	public static final TrackInfoDao dao = new TrackInfoDao();
	@ManyToMany
	@JoinTable(name = "TRACK_TRACK_INFO", joinColumns = @JoinColumn(name = "TRACK_INFO_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "TRACK_ID", referencedColumnName = "ID"))
	private Collection<Track> tracks;
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

	private String type;

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
