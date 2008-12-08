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
package org.yass.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.domain.PlayList;
import org.yass.domain.SmartPlayList;

/**
 * 
 * @author svenduzont
 */
public class PlayListDao extends AbstractDao<PlayList> {

	private static final PlayListDao instance = new PlayListDao();
	private static final Log LOG = LogFactory.getLog(PlayListDao.class);

	/**
	 * @return the instance
	 */
	public static final PlayListDao getInstance() {
		return PlayListDao.instance;
	}

	private PlayListDao() {
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public Map<Integer, PlayList> findByUserId(final int userId) {
		PlayListDao.LOG.info("Loading Playlist from user_id:" + userId);
		final Collection<PlayList> plstLst = getResultList(createNamedQuery("findPlayListByUserId").setParameter(1, userId));
		final Map<Integer, PlayList> plsts = new LinkedHashMap<Integer, PlayList>();
		final Iterator<PlayList> it = plstLst.iterator();
		while (it.hasNext()) {
			final PlayList plst = it.next();
			plsts.put(plst.getId(), plst);
		}
		PlayListDao.LOG.info("Playlists succefuly loaded " + plsts.size());
		return plsts;
	}

	/**
	 * 
	 * @param pLst
	 */
	@SuppressWarnings("unchecked")
	public void reloadSmartPlayLsit(final SmartPlayList pLst) {
		final List<Integer> lst = getEntityManager().createNativeQuery(pLst.getSqlStatement()).getResultList();
		pLst.setTrackIds(new LinkedHashSet<Integer>());
		for (final Integer map1 : lst)
			pLst.addTrack(map1);
	}

	/**
	 * 
	 * @param plst
	 */
	public void save(final PlayList plst) {
	}
}
