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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author svenduzont
 */
@Entity
@Table(name = "SMART_PLAYLIST_CONDITION")
public class SmartPlayListCondition {

	@Id
	private int id;
	private String operator;
	@ManyToOne(targetEntity = org.yass.domain.SmartPlayList.class)
	@JoinColumn(name = "PLAYLIST_ID", referencedColumnName = "PLAYLIST_ID")
	private SmartPlayList smartPlayList;
	private String term;
	private String value;

	/**
	 * 
	 */
	public SmartPlayListCondition() {
		super();
	}

    /**
     *
     * @param smartPlayList
     * @param term
     * @param operator
     * @param value
     */
    public SmartPlayListCondition(final SmartPlayList smartPlayList, final String term, final String operator,
			final String value) {
		this.smartPlayList = smartPlayList;
		this.term = term;
		this.operator = operator;
		this.value = value;
	}

    /**
     *
     * @return
     */
    public int getId() {
		return id;
	}

	/**
	 * @return the operator
	 */
	public final String getOperator() {
		return operator;
	}

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
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

    /**
     *
     * @param id
     */
    public void setId(final int id) {
		this.id = id;
	}
}