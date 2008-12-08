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
package org.yass.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Sven Duzont
 * 
 */
public class XMLElementBuilder {

	private Element child;

	/**
	 * @param doc
	 * @param tagName
	 */
	public XMLElementBuilder(final Document doc, final String tagName) {
		child = doc.createElement(tagName);
		doc.appendChild(child);
	}

	/**
	 * 
	 * @param parent
	 * @param tagName
	 */
	public XMLElementBuilder(final Element parent, final String tagName) {
		child = parent.getOwnerDocument().createElement(tagName);
		parent.appendChild(child);
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public XMLElementBuilder append(final String name, final boolean value) {
		child.setAttribute(name, Boolean.toString(value));
		return this;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public XMLElementBuilder append(final String name, final int value) {
		child.setAttribute(name, Integer.toString(value));
		return this;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	public XMLElementBuilder append(final String name, final long value) {
		child.setAttribute(name, Long.toString(value));
		return this;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public XMLElementBuilder append(final String name, final String value) {
		child.setAttribute(name, value);
		return this;
	}

	/**
	 * 
	 * @param tagName
	 * @return
	 */
	public XMLElementBuilder appendChild(final String tagName) {
		return new XMLElementBuilder(child, tagName);
	}
}
