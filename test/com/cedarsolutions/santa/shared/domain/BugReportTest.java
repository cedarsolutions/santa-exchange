/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2017 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.shared.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for BugReport.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@SuppressWarnings("unlikely-arg-type")
public class BugReportTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        BugReport bugReport = new BugReport();
        assertNotNull(bugReport);
        assertNull(bugReport.getReportDate());
        assertNull(bugReport.getApplicationVersion());
        assertNull(bugReport.getSubmittingUser());
        assertNull(bugReport.getEmailAddress());
        assertNull(bugReport.getProblemSummary());
        assertNull(bugReport.getDetailedDescription());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        BugReport bugReport = new BugReport();

        bugReport.setReportDate(DateUtils.createDate(2011, 12, 14));
        assertEquals(DateUtils.createDate(2011, 12, 14), bugReport.getReportDate());

        bugReport.setApplicationVersion("version");
        assertEquals("version", bugReport.getApplicationVersion());

        FederatedUser submittingUser = new FederatedUser();
        bugReport.setSubmittingUser(submittingUser);
        assertSame(submittingUser, bugReport.getSubmittingUser());

        bugReport.setEmailAddress("email");
        assertEquals("email", bugReport.getEmailAddress());

        bugReport.setProblemSummary("problem");
        assertEquals("problem", bugReport.getProblemSummary());

        bugReport.setDetailedDescription("detailed");
        assertEquals("detailed", bugReport.getDetailedDescription());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        BugReport bugReport1;
        BugReport bugReport2;

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        assertTrue(bugReport1.equals(bugReport2));
        assertTrue(bugReport2.equals(bugReport1));

        try {
            bugReport1 = createBugReport();
            bugReport2 = null;
            bugReport1.equals(bugReport2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            bugReport1 = createBugReport();
            bugReport2 = null;
            bugReport1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        bugReport2.setReportDate(DateUtils.getCurrentDate());
        assertFalse(bugReport1.equals(bugReport2));
        assertFalse(bugReport2.equals(bugReport1));

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        bugReport2.setApplicationVersion("X");
        assertFalse(bugReport1.equals(bugReport2));
        assertFalse(bugReport2.equals(bugReport1));

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        bugReport2.getSubmittingUser().setUserId("X");
        assertFalse(bugReport1.equals(bugReport2));
        assertFalse(bugReport2.equals(bugReport1));

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        bugReport2.setEmailAddress("X");
        assertFalse(bugReport1.equals(bugReport2));
        assertFalse(bugReport2.equals(bugReport1));

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        bugReport2.setProblemSummary("X");
        assertFalse(bugReport1.equals(bugReport2));
        assertFalse(bugReport2.equals(bugReport1));

        bugReport1 = createBugReport();
        bugReport2 = createBugReport();
        bugReport2.setDetailedDescription("X");
        assertFalse(bugReport1.equals(bugReport2));
        assertFalse(bugReport2.equals(bugReport1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        BugReport bugReport1 = createBugReport();
        bugReport1.setReportDate(DateUtils.createDate(2011, 10, 1));

        BugReport bugReport2 = createBugReport();
        bugReport2.setApplicationVersion("X");

        BugReport bugReport3 = createBugReport();
        bugReport3.getSubmittingUser().setUserId("X");

        BugReport bugReport4 = createBugReport();
        bugReport4.setEmailAddress("X");

        BugReport bugReport5 = createBugReport();
        bugReport5.setProblemSummary("X");

        BugReport bugReport6 = createBugReport();
        bugReport6.setDetailedDescription("X");

        BugReport bugReport7 = createBugReport();
        bugReport7.setReportDate(DateUtils.createDate(2011, 10, 1));  // same as bugReport1

        Map<BugReport, String> map = new HashMap<BugReport, String>();
        map.put(bugReport1, "ONE");
        map.put(bugReport2, "TWO");
        map.put(bugReport3, "THREE");
        map.put(bugReport4, "FOUR");
        map.put(bugReport5, "FIVE");
        map.put(bugReport6, "SIX");

        assertEquals("ONE", map.get(bugReport1));
        assertEquals("TWO", map.get(bugReport2));
        assertEquals("THREE", map.get(bugReport3));
        assertEquals("FOUR", map.get(bugReport4));
        assertEquals("FIVE", map.get(bugReport5));
        assertEquals("SIX", map.get(bugReport6));
        assertEquals("ONE", map.get(bugReport7));
    }

    /** Create a BugReport for testing. */
    private static BugReport createBugReport() {
        BugReport bugReport = new BugReport();

        FederatedUser user = new FederatedUser();
        user.setUserId("12");

        bugReport.setReportDate(DateUtils.createDate(2011, 12, 14));
        bugReport.setApplicationVersion("version");
        bugReport.setSubmittingUser(user);
        bugReport.setEmailAddress("email");
        bugReport.setProblemSummary("summary");
        bugReport.setDetailedDescription("description");

        return bugReport;
    }

}
