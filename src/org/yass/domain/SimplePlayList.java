package org.yass.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SIMPLE_PLAYLIST")
@DiscriminatorValue(value = "0")
@AttributeOverride(name = "id", column = @Column(name = "PLAYLIST_ID"))
public class SimplePlayList extends PlayList {

	public SimplePlayList(final int id, final String name, final Date updateDate) {
		this.name = name;
		this.id = id;
		lastUpdate = updateDate;
	}
}
