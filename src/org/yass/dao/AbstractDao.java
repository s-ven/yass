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
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.jdbc.core.JdbcTemplate;

public class AbstractDao<T> {

	private final DaoHelper helper = DaoHelper.getInstance();

	protected void beginTransaction() {
		getTransaction().begin();
	}

	protected void commitTransaction() {
		getTransaction().commit();
	}

	/**
	 * @return
	 */
	protected Query createNamedQuery(final String namedQuery) {
		return getEntityManager().createNamedQuery(namedQuery);
	}

	protected EntityManager getEntityManager() {
		return helper.getEntityManager();
	}

	protected JdbcTemplate getJdbcTemplate() {
		return helper.getJdbcTemplate();
	}

	/**
	 * @param setParameter
	 * @return
	 */
	protected T getSingleResult(final Query setParameter) {
		return (T) setParameter.getSingleResult();
	}

	protected EntityTransaction getTransaction() {
		return getEntityManager().getTransaction();
	}

	/**
	 * @param object
	 */
	protected void persist(final Object object) {
		getEntityManager().persist(object);
	}

	/**
	 * @param ctx
	 */
	protected void remove(final Object object) {
		getEntityManager().remove(object);
	}

	protected void rollbackTransaction() {
		getTransaction().rollback();
	}
}
