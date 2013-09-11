/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
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

import org.apache.log4j.Logger;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.client.rpc.IBugReportRpc;
import com.cedarsolutions.santa.server.service.IBugReportService;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Bug report functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportRpc extends AbstractService implements IBugReportRpc {

    /** Logger instance. */
    private static Logger LOGGER = LoggingUtils.getLogger(BugReportRpc.class);

    /** Bug report service. */
    private IBugReportService bugReportService;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.bugReportService == null) {
            throw new NotConfiguredException("BugReportRpc is not properly configured.");
        }
    }

    /**
     * Submit a bug report.
     * @param bugReport  Bug report to submit
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the input data is not valid.
     */
    @Override
    public void submitBugReport(BugReport bugReport) throws ServiceException, InvalidDataException {
        try  {
            this.bugReportService.submitBugReport(bugReport);
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error submitting bug report: " + e.getMessage(), e);
            throw new ServiceException("Error submitting bug report: " + e.getMessage(), e);
        }
    }

    public IBugReportService getBugReportService() {
        return this.bugReportService;
    }

    public void setBugReportService(IBugReportService bugReportService) {
        this.bugReportService = bugReportService;
    }

}
