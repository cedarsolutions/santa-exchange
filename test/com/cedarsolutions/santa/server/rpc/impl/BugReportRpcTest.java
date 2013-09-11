/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
' *              C E D A R
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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.server.service.IBugReportService;
import com.cedarsolutions.santa.shared.domain.BugReport;

/**
 * Unit tests for BugReportRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportRpcTest {

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        BugReportRpc rpc = new BugReportRpc();
        assertNull(rpc.getBugReportService());

        IBugReportService bugReportService = mock(IBugReportService.class);
        rpc.setBugReportService(bugReportService);
        assertSame(bugReportService, rpc.getBugReportService());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        IBugReportService bugReportService = mock(IBugReportService.class);
        BugReportRpc rpc = new BugReportRpc();

        rpc.setBugReportService(bugReportService);
        rpc.afterPropertiesSet();

        try {
            rpc.setBugReportService(null);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test submitBugReport(). */
    @Test public void testSubmitBugReport() throws Exception {
        IBugReportService bugReportService = mock(IBugReportService.class);
        BugReportRpc rpc = new BugReportRpc();
        rpc.setBugReportService(bugReportService);

        BugReport bugReport = new BugReport();
        rpc.submitBugReport(bugReport);
        verify(bugReportService).submitBugReport(bugReport);
    }

    /** Test submitBugReport() when an exception is thrown. */
    @Test public void testSubmitBugReportException() throws Exception {
        IBugReportService bugReportService = mock(IBugReportService.class);
        BugReportRpc rpc = new BugReportRpc();
        rpc.setBugReportService(bugReportService);

        BugReport bugReport = new BugReport();
        RuntimeException exception = new RuntimeException("Hello");
        doThrow(exception).when(bugReportService).submitBugReport(bugReport);

        try {
            rpc.submitBugReport(bugReport);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test submitBugReport() when an InvalidDataException is thrown. */
    @Test public void testSubmitBugReportInvalidDataException() throws Exception {
        IBugReportService bugReportService = mock(IBugReportService.class);
        BugReportRpc rpc = new BugReportRpc();
        rpc.setBugReportService(bugReportService);

        BugReport bugReport = new BugReport();
        InvalidDataException exception = new InvalidDataException("Hello");
        doThrow(exception).when(bugReportService).submitBugReport(bugReport);

        try {
            rpc.submitBugReport(bugReport);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

}
