/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of IAdminRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IAdminRpcAsync {

    /**
     * Get a set of audit events that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @param callback    Callback to be invoked after method call completes
     */
    void getAuditEvents(AuditEventCriteria criteria, Pagination pagination, AsyncCallback<PaginatedResults<AuditEvent>> callback);

    /**
     * Get a set of registered users that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @param callback    Callback to be invoked after method call completes
     */
    void getRegisteredUsers(RegisteredUserCriteria criteria, Pagination pagination, AsyncCallback<PaginatedResults<RegisteredUser>> callback);

    /**
     * Delete a set of registered users.
     * @param records  Records to delete
     * @param callback Callback to be invoked after method call completes
     */
    void deleteRegisteredUsers(List<RegisteredUser> records, AsyncCallback<Void> callback);

    /**
     * Lock a set of registered users.
     * @param records   Records to lock
     * @param callback  Callback to be invoked after method call completes
     */
    void lockRegisteredUsers(List<RegisteredUser> records, AsyncCallback<Void> callback);

    /**
     * Unlock a set of registered users.
     * @param records   Records to unlock
     * @param callback  Callback to be invoked after method call completes
     */
    void unlockRegisteredUsers(List<RegisteredUser> records, AsyncCallback<Void> callback);

}
