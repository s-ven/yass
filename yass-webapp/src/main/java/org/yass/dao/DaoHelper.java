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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yass.dao.schema.Schema00;

/**
 *
 * @author svenduzont
 */
public class DaoHelper {

	private final static DaoHelper instance = new DaoHelper();
	private final static Log LOG = LogFactory.getLog(DaoHelper.class);

	/**
	 * @return the instance
	 */
	public static final DaoHelper getInstance() {
		return instance;
	}

	private final EntityManager entityManager;

	private DaoHelper() {
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("yass");
		entityManager = emf.createEntityManager();
		checkDataBase();
	}

	private void checkDataBase() {
		try {
			new Schema00(entityManager).execute();
		} catch (final Exception e) {
			LOG.fatal(e);
		}
	}

	/**
	 * @return the entity manager
	 */
	public final EntityManager getEntityManager() {
		return entityManager;
	}
}
