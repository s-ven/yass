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
package org.yass.dao.schema;

import javax.persistence.EntityManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Schema {

	protected final static Log LOG = LogFactory.getLog(Schema.class);
	private final EntityManager entityManager;

	/**
	 * @param entityManager
	 */
	public Schema(final EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	protected boolean columnExists(final String table, final String column) {
		try {
			entityManager.createNativeQuery("select " + column + " from " + table + " where 0 = 1").getSingleResult();
		} catch (final Exception e) {
			return false;
		}
		return true;
	}

	public abstract void execute();

	protected void executeUpdate(final String query) {
		try {
			entityManager.getTransaction().begin();
			entityManager.createNativeQuery(query).executeUpdate();
			entityManager.getTransaction().commit();
		} catch (final Exception e) {
			LOG.debug("Error executing update " + query, e);
			entityManager.getTransaction().rollback();
		}
	}

	protected boolean indexExists(final String indexName) {
		try {
			return entityManager.createNativeQuery("select count(*) from SYS.SYSCONGLOMERATES where 	CONGLOMERATENAME = ?1")
					.setParameter(1, indexName).getResultList().size() != 0;
		} catch (final Exception e) {
			return false;
		}
	}

	protected boolean tableExists(final String table) {
		try {
			entityManager.createNativeQuery("select 1 from " + table).getResultList();
		} catch (final Exception e) {
			return false;
		}
		return true;
	}
}
