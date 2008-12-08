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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.yass.YassConstants;

/**
 * 
 * @author svenduzont
 */
@Entity
@Table(name = "TRACK_INFO")
@NamedQuery(name = "findTrackInfoByValue", query = "SELECT ti FROM TrackInfo ti WHERE ti.type = ?1 AND ti.value = ?2")
public class TrackInfo implements YassConstants {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String type;
	private String value;

	/**
	 * 
	 */
	public TrackInfo() {
		super();
	}

	/**
	 * 
	 * @param type
	 * @param value
	 */
	public TrackInfo(final String type, final String value) {
		this.type = type;
		this.value = value;
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

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	@Override
	public final String toString() {
		return "{id:" + getId() + ",type:\"" + getType() + "\",value:\"" + getValue() + "\"}";
	}

	/**
	 * 
	 * @param doc
	 * @return
	 */
	public final Element toXMLElement(final Document doc) {
		final Element node = doc.createElement(getType());
		node.setAttribute("id", "" + getId());
		node.setAttribute("value", getValue());
		// If the trackIngo is an album, will try to check if it have an attached
		// picture in the database
		if (getType().equals(ALBUM))
			node.setAttribute("hasPicture", ATTACHED_PICTURE_DAO.hasPicture(getId()) + "");
		return node;
	}
}
