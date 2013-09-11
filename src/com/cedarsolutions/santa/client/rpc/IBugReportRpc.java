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

import org.springframework.security.annotation.Secured;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;

/**
 * Bug report functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RemoteServiceRelativePath("rpc/bugReportRpc.rpc")
@Secured({ "ROLE_USER", "ROLE_ENABLED" })  // only enabled, logged in users can call these methods
@XsrfProtect  // Apply protection to prevent CSRF/XSRF attacks
public interface IBugReportRpc extends RemoteService {

    /**
     * Submit a bug report.
     * @param bugReport  Bug report to submit
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the input data is not valid.
     */
    void submitBugReport(BugReport bugReport) throws ServiceException, InvalidDataException;

}
