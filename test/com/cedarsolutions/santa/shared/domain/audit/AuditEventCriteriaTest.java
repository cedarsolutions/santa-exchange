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
package com.cedarsolutions.santa.shared.domain.audit;

import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.ADMIN_LOGIN;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.REGISTER_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for AuditEventCriteria.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventCriteriaTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        AuditEventCriteria criteria = new AuditEventCriteria();
        assertNotNull(criteria);
        assertNull(criteria.getStartDate());
        assertNull(criteria.getEndDate());
        assertNull(criteria.getEventTypes());
        assertNull(criteria.getUserIds());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        AuditEventCriteria criteria = new AuditEventCriteria();

        criteria.setStartDate(DateUtils.createDate("2011-06-14"));
        assertEquals(DateUtils.createDate("2011-06-14"), criteria.getStartDate());

        criteria.setEndDate(DateUtils.createDate("2011-06-16"));
        assertEquals(DateUtils.createDate("2011-06-16"), criteria.getEndDate());

        List<AuditEventType> eventTypes = new ArrayList<AuditEventType>();
        criteria.setEventTypes(eventTypes);
        assertSame(eventTypes, criteria.getEventTypes());

        criteria.setEventTypes((List<AuditEventType>) null);
        assertNull(criteria.getEventTypes());

        criteria.setEventTypes(REGISTER_USER, ADMIN_LOGIN);
        assertNotSame(eventTypes, criteria.getEventTypes());
        assertEquals(2, criteria.getEventTypes().size());
        assertEquals(REGISTER_USER, criteria.getEventTypes().get(0));
        assertEquals(ADMIN_LOGIN, criteria.getEventTypes().get(1));

        criteria.setEventTypes((AuditEventType[]) null);
        assertNull(criteria.getEventTypes());

        List<String> userIds = new ArrayList<String>();
        criteria.setUserIds(userIds);
        assertSame(userIds, criteria.getUserIds());

        criteria.setUserIds((List<String>) null);
        assertNull(criteria.getUserIds());

        criteria.setUserIds("user1", "user2");
        assertNotSame(userIds, criteria.getUserIds());
        assertEquals(2, criteria.getUserIds().size());
        assertEquals("user1", criteria.getUserIds().get(0));
        assertEquals("user2", criteria.getUserIds().get(1));

        criteria.setUserIds((String[]) null);
        assertNull(criteria.getUserIds());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        AuditEventCriteria criteria1;
        AuditEventCriteria criteria2;

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        assertTrue(criteria1.equals(criteria2));
        assertTrue(criteria2.equals(criteria1));

        try {
            criteria1 = createCriteria();
            criteria2 = null;
            criteria1.equals(criteria2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            criteria1 = createCriteria();
            criteria2 = null;
            criteria1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.getEventTypes().clear();
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.getUserIds().clear();
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.setStartDate(DateUtils.getCurrentDate());
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.setEndDate(DateUtils.getCurrentDate());
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {

        AuditEventCriteria criteria1 = createCriteria();
        criteria1.getEventTypes().clear();

        AuditEventCriteria criteria2 = createCriteria();
        criteria2.getUserIds().clear();

        AuditEventCriteria criteria3 = createCriteria();
        criteria3.setStartDate(DateUtils.getCurrentDate());

        AuditEventCriteria criteria4 = createCriteria();
        criteria4.setEndDate(DateUtils.getCurrentDate());

        AuditEventCriteria criteria5 = createCriteria();
        criteria5.getEventTypes().clear();  // same as criteria1

        Map<AuditEventCriteria, String> map = new HashMap<AuditEventCriteria, String>();
        map.put(criteria1, "ONE");
        map.put(criteria2, "TWO");
        map.put(criteria3, "THREE");
        map.put(criteria4, "FOUR");

        assertEquals("ONE", map.get(criteria1));
        assertEquals("TWO", map.get(criteria2));
        assertEquals("THREE", map.get(criteria3));
        assertEquals("FOUR", map.get(criteria4));
        assertEquals("ONE", map.get(criteria5));
    }

    /** Create a AuditEventCriteria for testing. */
    private static AuditEventCriteria createCriteria() {
        AuditEventCriteria criteria = new AuditEventCriteria();

        List<AuditEventType> eventTypes = new ArrayList<AuditEventType>();
        eventTypes.add(AuditEventType.ADMIN_LOGIN);

        List<String> userIds = new ArrayList<String>();
        userIds.add("user");

        criteria.setEventTypes(eventTypes);
        criteria.setUserIds(userIds);
        criteria.setStartDate(DateUtils.createDate(2011, 10, 1));
        criteria.setEndDate(DateUtils.createDate(2011, 10, 2));

        return criteria;
    }

}
