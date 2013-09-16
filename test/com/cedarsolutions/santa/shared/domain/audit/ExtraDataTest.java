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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for ExtraData.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExtraDataTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        ExtraData extraData = new ExtraData();
        assertNotNull(extraData);
        assertNull(extraData.getKey());
        assertNull(extraData.getValue());

        extraData = new ExtraData(ExtraDataKey.MODULE, "module");
        assertNotNull(extraData);
        assertEquals(ExtraDataKey.MODULE, extraData.getKey());
        assertEquals("module", extraData.getValue());

        extraData = new ExtraData(ExtraDataKey.EXCHANGE_ID, new Long(45));
        assertNotNull(extraData);
        assertEquals(ExtraDataKey.EXCHANGE_ID, extraData.getKey());
        assertEquals("45", extraData.getValue());

        extraData = new ExtraData(ExtraDataKey.RECIPIENTS, 12);
        assertNotNull(extraData);
        assertEquals(ExtraDataKey.RECIPIENTS, extraData.getKey());
        assertEquals("12", extraData.getValue());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        ExtraData extraData = new ExtraData();

        extraData.setKey(ExtraDataKey.USER_ID);
        assertEquals(ExtraDataKey.USER_ID, extraData.getKey());

        extraData.setValue("user");
        assertEquals("user", extraData.getValue());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        ExtraData data1;
        ExtraData data2;

        data1 = createExtraData();
        data2 = createExtraData();
        assertTrue(data1.equals(data2));
        assertTrue(data2.equals(data1));

        try {
            data1 = createExtraData();
            data2 = null;
            data1.equals(data2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            data1 = createExtraData();
            data2 = null;
            data1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        data1 = createExtraData();
        data2 = createExtraData();
        data2.setKey(ExtraDataKey.USER_ID);
        assertFalse(data1.equals(data2));
        assertFalse(data2.equals(data1));

        data1 = createExtraData();
        data2 = createExtraData();
        data2.setValue("X");
        assertFalse(data1.equals(data2));
        assertFalse(data2.equals(data1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        ExtraData data1 = createExtraData();
        data1.setKey(ExtraDataKey.USER_ID);

        ExtraData data2 = createExtraData();
        data2.setValue("X");

        ExtraData data3 = createExtraData();
        data3.setKey(ExtraDataKey.USER_ID);  // same as data1

        Map<ExtraData, String> map = new HashMap<ExtraData, String>();
        map.put(data1, "ONE");
        map.put(data2, "TWO");

        assertEquals("ONE", map.get(data1));
        assertEquals("TWO", map.get(data2));
        assertEquals("ONE", map.get(data3));
    }

    /** Create a ExtraData for testing. */
    private static ExtraData createExtraData() {
        ExtraData data = new ExtraData();

        data.setKey(ExtraDataKey.MODULE);
        data.setValue("value");

        return data;
    }
}
