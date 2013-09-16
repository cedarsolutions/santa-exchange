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
import com.cedarsolutions.dao.gae.impl.FilteredResultIterator;
import com.cedarsolutions.dao.gae.impl.GaeDaoTransaction;
import com.cedarsolutions.dao.gae.impl.PaginationUtils;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.googlecode.objectify.Query;

/**
 * DAO to handle audit events.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventDao extends AbstractGaeDao implements IAuditEventDao {

    /**
     * Insert an audit event into the back-end data store.
     * @param auditEvent  Audit event to insert
     * @return Event id for this event, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    @Override
    public Long insertAuditEvent(AuditEvent auditEvent) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            Long result = this.insertAuditEvent(transaction, auditEvent);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Insert an audit event into the back-end data store (transactional).
     * @param transaction Transaction to operate within
     * @param auditEvent  Audit event to insert
     * @return Event id for this event, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    @Override
    public Long insertAuditEvent(IDaoTransaction transaction, AuditEvent auditEvent) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            auditEvent.setEventId(null);    // set to null so it will be auto-generated
            gaeTransaction.getObjectify().put(auditEvent);
            return auditEvent.getEventId(); // Objectify fills the id into the inserted object
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error inserting event.", e);
        }
    }

    /**
     * Retrieve an audit event by event id.
     * @param eventId  Event id, as from insertAuditEvent()
     * @return Audit event retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    @Override
    public AuditEvent retrieveAuditEvent(Long eventId) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            AuditEvent result = this.retrieveAuditEvent(transaction, eventId);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Retrieve an audit event by event id (transactional).
     * @param transaction Transaction to operate within
     * @param eventId     Event id, as from insertAuditEvent()
     * @return Audit event retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    @Override
    public AuditEvent retrieveAuditEvent(IDaoTransaction transaction, Long eventId) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            return gaeTransaction.getObjectify().find(AuditEvent.class, eventId);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving event.", e);
        }
    }

    /**
     * Delete an audit event by event id.
     * @param eventId  Event id, as from insertAuditEvent()
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteAuditEvent(Long eventId) throws DaoException {
        GaeDaoTransaction transaction = getGaeTransaction();
        try {
            this.deleteAuditEvent(transaction, eventId);
            transaction.commit();
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Delete an audit event by event id (transactional).
     * @param transaction  Transaction to operate within
     * @param eventId      Event id, as from insertAuditEvent()
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteAuditEvent(IDaoTransaction transaction, Long eventId) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            AuditEvent auditEvent = gaeTransaction.getObjectify().find(AuditEvent.class, eventId);
            if (auditEvent != null) {
                // Don't bother deleting unless it's still there
                gaeTransaction.getObjectify().delete(auditEvent);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error deleting event.", e);
        }
    }

    /**
     * Delete the passed-in audit event.
     * @param auditEvent  Audit event to delete
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteAuditEvent(AuditEvent auditEvent) throws DaoException {
        this.deleteAuditEvent(auditEvent.getEventId());
    }

    /**
     * Retrieve the audit events that match search criteria.
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
    public Iterator<AuditEvent> retrieveAuditEvents(AuditEventCriteria criteria)
    throws InvalidDataException, DaoException {
        try {
            return buildRetrieveIterator(criteria, null);
        } catch (InvalidDataException e) {
            throw e;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving events.", e);
        }
    }

    /**
     * Retrieve the audit events that match search criteria.
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
    public PaginatedResults<AuditEvent> retrieveAuditEvents(AuditEventCriteria criteria, Pagination pagination)
    throws InvalidDataException, DaoException {
        try {
            FilteredResultIterator<AuditEvent> iterator = buildRetrieveIterator(criteria, pagination);
            return PaginationUtils.createPaginatedResults(pagination, iterator);
        } catch (InvalidDataException e) {
            throw e;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving events.", e);
        }
    }

    /** Build a query to retrieve matching rows, and return the resulting iterator. */
    private FilteredResultIterator<AuditEvent> buildRetrieveIterator(AuditEventCriteria criteria, Pagination pagination) {
        Query<AuditEvent> query = getObjectify().query(AuditEvent.class, pagination);
        query.order("eventTimestamp");
        addDateFilter(query, criteria);
        AuditEventFilterPredicate predicate = new AuditEventFilterPredicate(criteria);
        return new FilteredResultIterator<AuditEvent>(query, predicate);
    }

    /** Add a date filter to a query. */
    private static void addDateFilter(Query<AuditEvent> query, AuditEventCriteria criteria) {
        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            query.filter("eventTimestamp >=", criteria.getStartDate());
            query.filter("eventTimestamp <=", criteria.getEndDate());
        } else if (criteria.getStartDate() != null) {
            query.filter("eventTimestamp >=", criteria.getStartDate());
        } else if (criteria.getEndDate() != null) {
            query.filter("eventTimestamp <=", criteria.getEndDate());
        } else {
            throw new InvalidDataException("Must specify either start date, or end date, or both");
        }
    }

}
