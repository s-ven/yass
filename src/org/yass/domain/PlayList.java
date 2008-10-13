package org.yass.domain;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.yass.YassConstants;

@Entity
@Table(name = "PLAYLIST")
@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER, name = "TYPE_ID")
public abstract class PlayList implements YassConstants {

	@Id
	protected int id;
	protected String name;

	/**
	 * @param id
	 *          the id to set
	 */
	public final void setId(final int id) {
		this.id = id;
	}

	@Column(name = "TYPE_ID")
	protected int typeId;

	/**
	 * @return the typeId
	 */
	public final int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *          the typeId to set
	 */
	public final void setTypeId(final int typeId) {
		this.typeId = typeId;
	}

	@Column(name = "USER_ID")
	protected int userId = 1;

	/**
	 * @return the userId
	 */
	public final int getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *          the userId to set
	 */
	public final void setUserId(final int userId) {
		this.userId = userId;
	}

	@Column(name = "LAST_UPDATE")
	protected Date lastUpdate = new Date();
	private Set<Integer> trackIds = new LinkedHashSet<Integer>();

	/**
	 * @return the trackIds
	 */
	public final Set<Integer> getTrackIds() {
		return trackIds;
	}

	/**
	 * @param trackIds
	 *          the trackIds to set
	 */
	public final void setTrackIds(final Set<Integer> trackIds) {
		this.trackIds = trackIds;
	}

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
