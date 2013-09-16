/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013 Kenneth J. Pronovici.
 * All rights reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Apache License, Version 2.0.
 * See LICENSE for more information about the licensing terms.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Author   : Kenneth J. Pronovici <pronovic@ieee.org>
 * Language : Java 6
 * Project  : Secret Santa Exchange
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package com.cedarsolutions.santa.server.dao.impl;

import java.util.Iterator;

import com.cedarsolutions.dao.IDaoTransaction;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.dao.gae.impl.AbstractGaeDao;
import com.cedarsolutions.dao.gae.impl.FilteredContainerIterator;
import com.cedarsolutions.dao.gae.impl.GaeDaoTransaction;
import com.cedarsolutions.dao.gae.impl.PaginationUtils;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.server.dao.IExchangeDao;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.googlecode.objectify.Query;

/**
 * DAO to handle exchanges.
 *
 * <p>
 * Practically speaking, it's difficult to directly store large nested object
 * graphs in GAE's datastore.  It's easier to just create a container
 * object and store the real data in a serialized format. The container
 * object can then have all of the indexable fields in one place, and GAE
 * doesn't have to care about the structure of the underlying data.  Since
 * the DAO interface doesn't expose the container, no other code has to
 * know anything about this.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeDao extends AbstractGaeDao implements IExchangeDao {

    /**
     * Insert an exchange into the back-end data store.
     * @param exchange  Exchange to insert
     * @return Id for this exchange, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    @Override
    public Long insertExchange(Exchange exchange) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            Long result = this.insertExchange(transaction, exchange);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Insert an exchange into the back-end data store (transactional).
     * @param transaction Transaction to operate within
     * @param exchange    Exchange to insert
     * @return Id for this exchange, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    @Override
    public Long insertExchange(IDaoTransaction transaction, Exchange exchange) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            ExchangeContainer container = new ExchangeContainer();
            container.fromValue(exchange);
            container.setId(null); // set to null so it will be auto-generated
            gaeTransaction.getObjectify().put(container);
            return container.getId();
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error inserting exchange.", e);
        }
    }

    /**
     * Retrieve an exchange by exchange id.
     * @param exchangeId  Exchange id, as from insertExchange()
     * @return Exchange retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    @Override
    public Exchange retrieveExchange(Long exchangeId) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            Exchange result = this.retrieveExchange(transaction, exchangeId);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Retrieve an exchange by exchange id (transactional).
     * @param transaction Transaction to operate within
     * @param exchangeId  Exchange id, as from insertExchange()
     * @return Exchange retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    @Override
    public Exchange retrieveExchange(IDaoTransaction transaction, Long exchangeId) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            ExchangeContainer container = gaeTransaction.getObjectify().find(ExchangeContainer.class, exchangeId);
            return container == null ? null : container.toValue();
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving exchange.", e);
        }
    }

    /**
     * Delete an exchange by exchange id.
     * @param exchangeId  Exchange id, as from insertExchange()
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteExchange(Long exchangeId) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            this.deleteExchange(transaction, exchangeId);
            transaction.commit();
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Delete an exchange by exchange id (transactional).
     * @param transaction Transaction to operate within
     * @param exchangeId  Exchange id, as from insertExchange()
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteExchange(IDaoTransaction transaction, Long exchangeId) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            ExchangeContainer container = gaeTransaction.getObjectify().find(ExchangeContainer.class, exchangeId);
            if (container != null) {
                // Don't bother deleting unless it's still there
                gaeTransaction.getObjectify().delete(container);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error deleting exchange.", e);
        }
    }

    /**
     * Delete the passed-in exchange.
     * @param exchange  Exchange to delete
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteExchange(Exchange exchange) throws DaoException {
        this.deleteExchange(exchange.getId());
    }

    /**
     * Update the passed-in exchange.
     * @param exchange  Exchange to update
     * @throws DaoException If the operation fails.
     */
    @Override
    public void updateExchange(Exchange exchange) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            this.updateExchange(transaction, exchange);
            transaction.commit();
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Update the passed-in exchange (transactional).
     * @param transaction Transaction to operate within
     * @param exchange    Exchange to update
     * @throws DaoException If the operation fails.
     */
    @Override
    public void updateExchange(IDaoTransaction transaction, Exchange exchange) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            ExchangeContainer container = gaeTransaction.getObjectify().find(ExchangeContainer.class, exchange.getId());
            if (container == null) {
                // Make sure that we don't accidentally insert this user instead of updating it
                throw new DaoException("Failed to update record: does not exist");
            } else {
                container.fromValue(exchange);
                gaeTransaction.getObjectify().put(container);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error updating exchange.", e);
        }
    }

    /**
     * Retrieve the exchanges that match search criteria.
     *
     * <p>
     * See documentation on the criteria class for information on how criteria are applied.
     * There is no transactional version of this method because GAE does not allow queries
     * like this to be run within transactions.
     * </p>
     *
     * @param criteria  Search criteria to apply
     *
     * @return Iterator for the results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    @Override
    public Iterator<Exchange> retrieveExchanges(ExchangeCriteria criteria)
    throws InvalidDataException, DaoException {
        try {
            return buildRetrieveIterator(criteria, null);
        } catch (InvalidDataException e) {
            throw e;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving exchanges.", e);
        }
    }

    /**
     * Retrieve the exchanges that match search criteria.
     *
     * <p>
     * See documentation on the criteria class for information on how criteria are applied.
     * There is no transactional version of this method because GAE does not allow queries
     * like this to be run within transactions.
     * </p>
     *
     * @param criteria  Search criteria to apply
     * @param pagination  Pagination to use
     *
     * @return Paginated results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    @Override
    public PaginatedResults<Exchange> retrieveExchanges(ExchangeCriteria criteria, Pagination pagination)
    throws InvalidDataException, DaoException {
        try {
            FilteredContainerIterator<Exchange> iterator = buildRetrieveIterator(criteria, pagination);
            return PaginationUtils.createPaginatedResults(pagination, iterator);
        } catch (InvalidDataException e) {
            throw e;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving exchanges.", e);
        }
    }

    /** Build a query to retrieve matching rows, and return the resulting iterator. */
    private FilteredContainerIterator<Exchange> buildRetrieveIterator(ExchangeCriteria criteria, Pagination pagination) {
        Query<ExchangeContainer> query = getObjectify().query(ExchangeContainer.class, pagination);
        query.order("id");
        addUserIdFilter(query, criteria);
        ExchangeFilterPredicate predicate = new ExchangeFilterPredicate(criteria);
        return new FilteredContainerIterator<Exchange>(query, predicate);
    }

    /** Add a date filter to a query. */
    private static void addUserIdFilter(Query<ExchangeContainer> query, ExchangeCriteria criteria) {
        if (!GwtStringUtils.isEmpty(criteria.getUserId())) {
            query.filter("userId =", criteria.getUserId());
        } else {
            throw new InvalidDataException("Must specify user id");
        }
    }

}
