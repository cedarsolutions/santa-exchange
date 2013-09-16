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
package com.cedarsolutions.santa.server.rpc.impl;

import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.NULL;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import java.util.List;

import org.apache.log4j.Logger;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.client.rpc.IAdminRpc;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.server.dao.IRegisteredUserDao;
import com.cedarsolutions.santa.server.service.IAuditEventService;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.shared.domain.ValidationErrors;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Functionality used by the adminstrator interface.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminRpc extends AbstractService implements IAdminRpc {

    /** Logger instance. */
    private static Logger LOGGER = LoggingUtils.getLogger(AdminRpc.class);

    /** Audit event service. */
    private IAuditEventService auditEventService;

    /** Audit event DAO. */
    private IAuditEventDao auditEventDao;

    /** Registered user DAO. */
    private IRegisteredUserDao registeredUserDao;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.auditEventService == null ||
                this.auditEventDao == null ||
                this.registeredUserDao == null) {
            throw new NotConfiguredException("AdminRpc is not properly configured.");
        }
    }

    /**
     * Get a set of audit events that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the criteria is not valid.
     */
    @Override
    public PaginatedResults<AuditEvent> getAuditEvents(AuditEventCriteria criteria, Pagination pagination) throws ServiceException, InvalidDataException {
        try {
            validateCriteria(criteria);
            return this.auditEventDao.retrieveAuditEvents(criteria, pagination);
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error retrieving audit events: " + e.getMessage(), e);
            throw new ServiceException("Error retrieving audit events: " + e.getMessage(), e);
        }
    }

    /**
     * Get a set of registered users that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the criteria is not valid.
     */
    @Override
    public PaginatedResults<RegisteredUser> getRegisteredUsers(RegisteredUserCriteria criteria, Pagination pagination) throws ServiceException, InvalidDataException {
        try {
            return this.registeredUserDao.retrieveRegisteredUsers(criteria, pagination);
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error retrieving registered users: " + e.getMessage(), e);
            throw new ServiceException("Error retrieving registered users: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a set of registered users.
     * @param records  Records to delete
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public void deleteRegisteredUsers(List<RegisteredUser> records) throws ServiceException {
        try {
            for (RegisteredUser record : records) {
                this.registeredUserDao.deleteRegisteredUser(record);
                AuditEvent auditEvent = this.auditEventService.buildDeleteUserEvent(record.getUserId(), record.getEmailAddress());
                this.auditEventService.logAuditEvent(auditEvent);
            }
        } catch (Exception e) {
            LOGGER.error("Error deleting registered users: " + e.getMessage(), e);
            throw new ServiceException("Error deleting registered users: " + e.getMessage(), e);
        }
    }

    /**
     * Lock a set of registered users.
     * @param records  Records to lock
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public void lockRegisteredUsers(List<RegisteredUser> records) throws ServiceException {
        try {
            for (RegisteredUser record : records) {
                this.registeredUserDao.lockRegisteredUser(record);
                AuditEvent auditEvent = this.auditEventService.buildLockUserEvent(record.getUserId(), record.getEmailAddress());
                this.auditEventService.logAuditEvent(auditEvent);
            }
        } catch (Exception e) {
            LOGGER.error("Error locking registered users: " + e.getMessage(), e);
            throw new ServiceException("Error locking registered users: " + e.getMessage(), e);
        }
    }

    /**
     * Unlock a set of registered users.
     * @param records  Records to unlock
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public void unlockRegisteredUsers(List<RegisteredUser> records) throws ServiceException {
        try {
            for (RegisteredUser record : records) {
                this.registeredUserDao.unlockRegisteredUser(record);
                AuditEvent auditEvent = this.auditEventService.buildUnlockUserEvent(record.getUserId(), record.getEmailAddress());
                this.auditEventService.logAuditEvent(auditEvent);
            }
        } catch (Exception e) {
            LOGGER.error("Error unlocking registered users: " + e.getMessage(), e);
            throw new ServiceException("Error unlocking registered users: " + e.getMessage(), e);
        }
    }

    /** Validate audit event search criteria. */
    protected static void validateCriteria(AuditEventCriteria criteria) throws InvalidDataException {
        ValidationErrors details = new ValidationErrors(INVALID, "Search criteria are invalid");
        InvalidDataException exception = new InvalidDataException("Search criteria are invalid", details);

        if (criteria == null) {
            details.addMessage(NULL, "Criteria are null");
        } else {
            if (criteria.getStartDate() == null && criteria.getEndDate() == null) {
                details.addMessage(REQUIRED, "startDate", "Provide either start date or end date");
            }
        }

        if (!details.getMessages().isEmpty()) {
            throw exception;
        }
    }

    public IAuditEventService getAuditEventService() {
        return this.auditEventService;
    }

    public void setAuditEventService(IAuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    public IAuditEventDao getAuditEventDao() {
        return this.auditEventDao;
    }

    public void setAuditEventDao(IAuditEventDao auditEventDao) {
        this.auditEventDao = auditEventDao;
    }

    public IRegisteredUserDao getRegisteredUserDao() {
        return this.registeredUserDao;
    }

    public void setRegisteredUserDao(IRegisteredUserDao registeredUserDao) {
        this.registeredUserDao = registeredUserDao;
    }

}
