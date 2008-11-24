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
package org.yass.restlet;

import java.util.ArrayList;

import org.restlet.resource.Representation;

/**
 * @author Sven Duzont
 * 
 */
public class Form extends org.restlet.data.Form {

	/**
	 * @param entity
	 */
	public Form(final Representation entity) {
		super(entity);
	}

	/**
	 * @param string
	 * @return
	 */
	public boolean getBoolean(final String name) {
		return Boolean.parseBoolean(getFirstValue(name));
	}

	public Integer getInt(final String name) {
		return Integer.parseInt(getFirstValue(name));
	}

	public Integer[] getInts(final String name) {
		final String[] strArray = getValuesArray(name);
		if (strArray != null && strArray.length != 0) {
			final ArrayList<Integer> intArrayLst = new ArrayList<Integer>(strArray.length);
			for (final String str : strArray)
				intArrayLst.add(Integer.parseInt(str));
			return intArrayLst.toArray(new Integer[] {});
		}
		return null;
	}

	/**
	 * @param string
	 * @return
	 */
	public short getShort(final String name) {
		return Short.parseShort(getFirstValue(name));
	}
}
