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
package com.cedarsolutions.santa.shared.domain.exchange;

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

/**
 * Unit tests for ExchangeCriteria.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeCriteriaTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        ExchangeCriteria criteria = new ExchangeCriteria();
        assertNotNull(criteria);
        assertNull(criteria.getUserId());
        assertNull(criteria.getExchangeIds());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        ExchangeCriteria criteria = new ExchangeCriteria();

        criteria.setUserId("id");
        assertEquals("id", criteria.getUserId());

        List<Long> exchangeIds = new ArrayList<Long>();
        criteria.setExchangeIds(exchangeIds);
        assertSame(exchangeIds, criteria.getExchangeIds());

        criteria.setExchangeIds((List<Long>) null);
        assertNull(criteria.getExchangeIds());

        criteria.setExchangeIds(2L, 4L);
        assertNotSame(exchangeIds, criteria.getExchangeIds());
        assertEquals(2, criteria.getExchangeIds().size());
        assertEquals(new Long(2), criteria.getExchangeIds().get(0));
        assertEquals(new Long(4), criteria.getExchangeIds().get(1));

        criteria.setExchangeIds((Long[]) null);
        assertNull(criteria.getExchangeIds());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        ExchangeCriteria criteria1;
        ExchangeCriteria criteria2;

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
        criteria2.setUserId("X");
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));

        criteria1 = createCriteria();
        criteria2 = createCriteria();
        criteria2.getExchangeIds().clear();
        assertFalse(criteria1.equals(criteria2));
        assertFalse(criteria2.equals(criteria1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {

        ExchangeCriteria criteria1 = createCriteria();
        criteria1.setUserId("X");

        ExchangeCriteria criteria2 = createCriteria();
        criteria2.getExchangeIds().clear();

        ExchangeCriteria criteria3 = createCriteria();
        criteria3.setUserId("X");  // same as criteria1

        Map<ExchangeCriteria, String> map = new HashMap<ExchangeCriteria, String>();
        map.put(criteria1, "ONE");
        map.put(criteria2, "TWO");

        assertEquals("ONE", map.get(criteria1));
        assertEquals("TWO", map.get(criteria2));
        assertEquals("ONE", map.get(criteria3));
    }

    /** Create a ExchangeCriteria for testing. */
    private static ExchangeCriteria createCriteria() {
        ExchangeCriteria criteria = new ExchangeCriteria();

        List<Long> exchangeIds = new ArrayList<Long>();
        exchangeIds.add(2L);

        criteria.setUserId("userId");
        criteria.setExchangeIds(exchangeIds);

        return criteria;
    }

}
