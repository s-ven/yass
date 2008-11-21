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
package org.yass.security;

import java.security.Principal;

/**
 * @author Sven Duzont
 * 
 */
@SuppressWarnings("unchecked")
public abstract class BasePrincipal implements Principal, Comparable {

	private String name;

	public BasePrincipal(final String name) {
		if (name == null)
			throw new NullPointerException("Name may not be null.");
		this.name = name;
	}

	public int compareTo(final Object obj) {
		final BasePrincipal other = (BasePrincipal) obj;
		final int classComp = getClass().getName().compareTo(other.getClass().getName());
		if (classComp == 0)
			return getName().compareTo(other.getName());
		else
			return classComp;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!getClass().equals(obj.getClass()))
			return false;
		final BasePrincipal other = (BasePrincipal) obj;
		if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return getName().hashCode() * 19 + getClass().hashCode() * 19;
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append("(");
		buf.append(getClass().getName());
		buf.append(": name=");
		buf.append(getName());
		buf.append(")");
		return buf.toString();
	}
}