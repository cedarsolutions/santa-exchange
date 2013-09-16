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
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;

/**
 * DAO to handle audit events.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IAuditEventDao {

    /**
     * Insert an audit event into the back-end data store.
     * @param auditEvent  Audit event to insert
     * @return Event id for this event, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    Long insertAuditEvent(AuditEvent auditEvent) throws DaoException;

    /**
     * Insert an audit event into the back-end data store (transactional).
     * @param transaction Transaction to operate within
     * @param auditEvent  Audit event to insert
     * @return Event id for this event, auto-generated upon insert.
     * @throws DaoException If the operation fails.
     */
    Long insertAuditEvent(IDaoTransaction transaction, AuditEvent auditEvent) throws DaoException;

    /**
     * Retrieve an audit event by event id.
     * @param eventId  Event id, as from insertAuditEvent()
     * @return Audit event retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    AuditEvent retrieveAuditEvent(Long eventId) throws DaoException;

    /**
     * Retrieve an audit event by event id (transactional).
     * @param transaction  Transaction to operate within
     * @param eventId      Event id, as from insertAuditEvent()
     * @return Audit event retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    AuditEvent retrieveAuditEvent(IDaoTransaction transaction, Long eventId) throws DaoException;

    /**
     * Delete an audit event by event id.
     * @param eventId  Event id, as from insertAuditEvent()
     * @throws DaoException If the operation fails.
     */
    void deleteAuditEvent(Long eventId) throws DaoException;

    /**
     * Delete an audit event by event id (transactional).
     * @param transaction  Transaction to operate within
     * @param eventId      Event id, as from insertAuditEvent()
     * @throws DaoException If the operation fails.
     */
    void deleteAuditEvent(IDaoTransaction transaction, Long eventId) throws DaoException;

    /**
     * Delete the passed-in audit event.
     * @param auditEvent  Audit event to delete
     * @throws DaoException If the operation fails.
     */
    void deleteAuditEvent(AuditEvent auditEvent) throws DaoException;

    /**
     * Retrieve the audit events that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria  Search criteria to apply
     * @return Iterator for the results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    Iterator<AuditEvent> retrieveAuditEvents(AuditEventCriteria criteria)
    throws InvalidDataException, DaoException;

    /**
     * Retrieve the audit events that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    PaginatedResults<AuditEvent> retrieveAuditEvents(AuditEventCriteria criteria, Pagination pagination)
    throws InvalidDataException, DaoException;

}
