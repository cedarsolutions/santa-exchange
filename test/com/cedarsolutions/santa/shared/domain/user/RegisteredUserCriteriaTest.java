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
package com.cedarsolutions.santa.shared.domain.user;

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

import com.cedarsolutions.dao.domain.SortOrder;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for RegisteredUserCriteria.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserCriteriaTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        assertNotNull(criteria);
        assertNull(criteria.getUserIds());
        assertNull(criteria.getUserNames());
        assertNull(criteria.getOpenIdProviders());
        assertNull(criteria.getAdmin());
        assertNull(criteria.getLocked());
        assertNull(criteria.getStartDate());
        assertNull(criteria.getEndDate());
        assertEquals(SortOrder.ASCENDING, criteria.getDefaultSortOrder());
        assertEquals(RegisteredUserSortColumn.USER_NAME, criteria.getDefaultSortColumn());
        assertEquals(criteria.getDefaultSortOrder(), criteria.getSortOrder());
        assertEquals(criteria.getDefaultSortColumn(), criteria.getSortColumn());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();

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

        List<String> userNames = new ArrayList<String>();
        criteria.setUserNames(userNames);
        assertSame(userNames, criteria.getUserNames());

        criteria.setUserNames((List<String>) null);
        assertNull(criteria.getUserNames());

        criteria.setUserNames("user1", "user2");
        assertNotSame(userNames, criteria.getUserNames());
        assertEquals(2, criteria.getUserNames().size());
        assertEquals("user1", criteria.getUserNames().get(0));
        assertEquals("user2", criteria.getUserNames().get(1));

        criteria.setUserNames((String[]) null);
        assertNull(criteria.getUserNames());

        criteria.setUserIds((String[]) null);
        assertNull(criteria.getUserIds());

        List<OpenIdProvider> openIdProviders = new ArrayList<OpenIdProvider>();
        criteria.setOpenIdProviders(openIdProviders);
        assertSame(openIdProviders, criteria.getOpenIdProviders());

        criteria.setOpenIdProviders((List<OpenIdProvider>) null);
        assertNull(criteria.getOpenIdProviders());

        criteria.setOpenIdProviders(OpenIdProvider.GOOGLE, OpenIdProvider.AOL);
        assertNotSame(openIdProviders, criteria.getOpenIdProviders());
        assertEquals(2, criteria.getOpenIdProviders().size());
        assertEquals(OpenIdProvider.GOOGLE, criteria.getOpenIdProviders().get(0));
        assertEquals(OpenIdProvider.AOL, criteria.getOpenIdProviders().get(1));

        criteria.setOpenIdProviders((OpenIdProvider[]) null);
        assertNull(criteria.getOpenIdProviders());

        criteria.setAdmin(true);
        assertTrue(criteria.getAdmin());

        criteria.setAdmin(false);
        assertFalse(criteria.getAdmin());

        criteria.setAdmin(null);
        assertNull(criteria.getAdmin());

        criteria.setLocked(true);
        assertTrue(criteria.getLocked());

        criteria.setLocked(false);
        assertFalse(criteria.getLocked());

        criteria.setLocked(null);
        assertNull(criteria.getLocked());

        criteria.setStartDate(DateUtils.createDate("2011-06-14"));
        assertEquals(DateUtils.createDate("2011-06-14"), criteria.getStartDate());

        criteria.setEndDate(DateUtils.createDate("2011-06-16"));
        assertEquals(DateUtils.createDate("2011-06-16"), criteria.getEndDate());

        criteria.setSortOrder(SortOrder.DESCENDING);
        assertEquals(SortOrder.DESCENDING, criteria.getSortOrder());

        criteria.setSortColumn(RegisteredUserSortColumn.LOCKED);
        assertEquals(RegisteredUserSortColumn.LOCKED, criteria.getSortColumn());
    }

    /** Test getDefaultSortColumn(). */
    @Test public void testGetDefaultSortColumn() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        assertEquals(RegisteredUserSortColumn.USER_NAME, criteria.getDefaultSortColumn());
    }

    /** Test getSortColumn(). */
    @Test public void testGetSortColumn() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        assertNull(criteria.getSortColumn(null));
        assertNull(criteria.getSortColumn(""));
        assertEquals(RegisteredUserSortColumn.USER_NAME, criteria.getSortColumn("USER_NAME"));
    }

    /** Test equals(). */
    @Test public void testEquals() {
        RegisteredUserCriteria criteria1;
        RegisteredUserCriteria criteria2;

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
        criteria2.getUserIds().clear();
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.getUserNames().clear();
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.getOpenIdProviders().clear();
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.setAdmin(false);
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.setLocked(false);
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

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.setSortOrder(SortOrder.DESCENDING);
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.setSortColumn(RegisteredUserSortColumn.LOCKED);
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {

        RegisteredUserCriteria criteria1 = createCriteria();
        criteria1.getUserIds().clear();

        RegisteredUserCriteria criteria2 = createCriteria();
        criteria2.getUserNames().clear();

        RegisteredUserCriteria criteria3 = createCriteria();
        criteria3.getOpenIdProviders().clear();

        RegisteredUserCriteria criteria4 = createCriteria();
        criteria4.setAdmin(false);

        RegisteredUserCriteria criteria5 = createCriteria();
        criteria5.setLocked(false);

        RegisteredUserCriteria criteria6 = createCriteria();
        criteria6.setStartDate(DateUtils.getCurrentDate());

        RegisteredUserCriteria criteria7 = createCriteria();
        criteria7.setEndDate(DateUtils.getCurrentDate());

        RegisteredUserCriteria criteria8 = createCriteria();
        criteria8.setSortOrder(SortOrder.DESCENDING);

        RegisteredUserCriteria criteria9 = createCriteria();
        criteria9.setSortColumn(RegisteredUserSortColumn.REGISTRATION_DATE);

        RegisteredUserCriteria criteria10 = createCriteria();
        criteria10.getUserIds().clear(); // same as criteria1

        Map<RegisteredUserCriteria, String> map = new HashMap<RegisteredUserCriteria, String>();
        map.put(criteria1, "ONE");
        map.put(criteria2, "TWO");
        map.put(criteria3, "THREE");
        map.put(criteria4, "FOUR");
        map.put(criteria5, "FIVE");
        map.put(criteria6, "SIX");
        map.put(criteria7, "SEVEN");
        map.put(criteria8, "EIGHT");
        map.put(criteria9, "NINE");

        assertEquals("ONE", map.get(criteria1));
        assertEquals("TWO", map.get(criteria2));
        assertEquals("THREE", map.get(criteria3));
        assertEquals("FOUR", map.get(criteria4));
        assertEquals("FIVE", map.get(criteria5));
        assertEquals("SIX", map.get(criteria6));
        assertEquals("SEVEN", map.get(criteria7));
        assertEquals("EIGHT", map.get(criteria8));
        assertEquals("NINE", map.get(criteria9));
        assertEquals("ONE", map.get(criteria10));
    }

    /** Create a RegisteredUserCriteria for testing. */
    private static RegisteredUserCriteria createCriteria() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();

        List<String> userIds = new ArrayList<String>();
        userIds.add("user");

        List<String> userNames = new ArrayList<String>();
        userNames.add("name");

        List<OpenIdProvider> openIdProviders = new ArrayList<OpenIdProvider>();
        openIdProviders.add(OpenIdProvider.GOOGLE);

        criteria.setUserIds(userIds);
        criteria.setUserNames(userNames);
        criteria.setOpenIdProviders(openIdProviders);
        criteria.setAdmin(true);
        criteria.setLocked(true);
        criteria.setStartDate(DateUtils.createDate(2011, 10, 1));
        criteria.setEndDate(DateUtils.createDate(2011, 10, 2));
        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.ADMIN);

        return criteria;
    }
}
