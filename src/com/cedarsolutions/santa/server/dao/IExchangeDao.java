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
package com.cedarsolutions.santa.server.dao;

import java.util.Iterator;

import com.cedarsolutions.dao.IDaoTransaction;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;

/**
 * DAO to handle exchanges.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IExchangeDao {

    /**
     * Insert an exchange into the back-end data store.
     * @param exchange  Exchange to insert
     * @return Id for this exchange, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    Long insertExchange(Exchange exchange) throws DaoException;

    /**
     * Insert an exchange into the back-end data store (transactional).
     * @param transaction Transaction to operate within
     * @param exchange    Exchange to insert
     * @return Id for this exchange, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    Long insertExchange(IDaoTransaction transaction, Exchange exchange) throws DaoException;

    /**
     * Retrieve an exchange by exchange id.
     * @param exchangeId  Exchange id, as from insertExchange()
     * @return Exchange retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    Exchange retrieveExchange(Long exchangeId) throws DaoException;

    /**
     * Retrieve an exchange by exchange id (transactional).
     * @param transaction Transaction to operate within
     * @param exchangeId  Exchange id, as from insertExchange()
     * @return Exchange retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    Exchange retrieveExchange(IDaoTransaction transaction, Long exchangeId) throws DaoException;

    /**
     * Delete an exchange by exchange id.
     * @param exchangeId  Exchange id, as from insertExchange()
     * @throws DaoException If the operation fails.
     */
    void deleteExchange(Long exchangeId) throws DaoException;

    /**
     * Delete an exchange by exchange id (transactional).
     * @param transaction Transaction to operate within
     * @param exchangeId  Exchange id, as from insertExchange()
     * @throws DaoException If the operation fails.
     */
    void deleteExchange(IDaoTransaction transaction, Long exchangeId) throws DaoException;

    /**
     * Delete the passed-in exchange.
     * @param exchange  Exchange to delete
     * @throws DaoException If the operation fails.
     */
    void deleteExchange(Exchange exchange) throws DaoException;

    /**
     * Update the passed-in exchange.
     * @param exchange  Exchange to update
     * @throws DaoException If the operation fails.
     */
    void updateExchange(Exchange exchange) throws DaoException;

    /**
     * Update the passed-in exchange (transactional).
     * @param transaction Transaction to operate within
     * @param exchange    Exchange to update
     * @throws DaoException If the operation fails.
     */
    void updateExchange(IDaoTransaction transaction, Exchange exchange) throws DaoException;

    /**
     * Retrieve the exchanges that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria  Search criteria to apply
     * @return Iterator for the results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    Iterator<Exchange> retrieveExchanges(ExchangeCriteria criteria)
    throws InvalidDataException, DaoException;

    /**
     * Retrieve the exchanges that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    PaginatedResults<Exchange> retrieveExchanges(ExchangeCriteria criteria, Pagination pagination)
    throws InvalidDataException, DaoException;

}
