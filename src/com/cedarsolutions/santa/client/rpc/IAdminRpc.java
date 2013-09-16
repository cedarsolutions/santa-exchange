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
package com.cedarsolutions.santa.client.rpc;

import java.util.List;

import org.springframework.security.annotation.Secured;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;

/**
 * Functionality used by the adminstrator interface.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RemoteServiceRelativePath("rpc/adminRpc.rpc")
@Secured({ "ROLE_ADMIN", "ROLE_ENABLED" })  // only enabled, logged in administrators can call these methods
@XsrfProtect  // Apply protection to prevent CSRF/XSRF attacks
public interface IAdminRpc extends RemoteService {

    /**
     * Get a set of audit events that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the input criteria are not valid.
     */
    PaginatedResults<AuditEvent> getAuditEvents(AuditEventCriteria criteria, Pagination pagination) throws ServiceException, InvalidDataException;

    /**
     * Get a set of registered users that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the criteria is not valid.
     */
    PaginatedResults<RegisteredUser> getRegisteredUsers(RegisteredUserCriteria criteria, Pagination pagination) throws ServiceException, InvalidDataException;

    /**
     * Delete a set of registered users.
     * @param records  Records to delete
     * @throws ServiceException If there is a problem with the method call.
     */
    void deleteRegisteredUsers(List<RegisteredUser> records) throws ServiceException;

    /**
     * Lock a set of registered users.
     * @param records  Records to lock
     * @throws ServiceException If there is a problem with the method call.
     */
    void lockRegisteredUsers(List<RegisteredUser> records) throws ServiceException;

    /**
     * Unlock a set of registered users.
     * @param records  Records to unlock
     * @throws ServiceException If there is a problem with the method call.
     */
    void unlockRegisteredUsers(List<RegisteredUser> records) throws ServiceException;

}
