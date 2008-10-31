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
package org.yass.struts.playlist;

import java.util.Date;

import org.yass.YassConstants;
import org.yass.domain.PlayList;
import org.yass.domain.SimplePlayList;
import org.yass.struts.YassAction;

public class Save extends YassAction implements YassConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3411435373847531163L;
	public String id;
	public String name;

	@Override
	public String execute() {
		LOG.info("Saving playlist " + id);
		PlayList pl = getPlayLists().get(id);
		if (pl == null)
			pl = new SimplePlayList(0, name, new Date());
		PLAYLIST_DAO.save(pl);
		getPlayLists().put(pl.getId(), pl);
		return NONE;
	}

	public void setRefresh(final boolean refresh) {
	}
}
