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
package com.cedarsolutions.santa.shared.domain.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
 * Unit tests for AuditEvent.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@SuppressWarnings("unlikely-arg-type")
public class AuditEventTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        AuditEvent event = new AuditEvent();
        assertNotNull(event);
        assertNull(event.getEventId());
        assertNull(event.getEventType());
        assertNull(event.getEventTimestamp());
        assertNull(event.getUserId());
        assertNull(event.getSessionId());
        assertNull(event.getExtraData());
    }

    /** Test the simple getters and setters. */
    @Test public void testSimpleGettersSetters() {
        AuditEvent event = new AuditEvent();

        event.setEventId(2L);
        assertEquals((Long) 2L, event.getEventId());

        event.setEventType(AuditEventType.LOCK_USER);
        assertEquals(AuditEventType.LOCK_USER, event.getEventType());

        event.setEventTimestamp(DateUtils.createDate("2011-10-09"));
        assertEquals(DateUtils.createDate("2011-10-09"), event.getEventTimestamp());

        event.setUserId("user");
        assertEquals("user", event.getUserId());

        event.setSessionId("1234");
        assertEquals("1234", event.getSessionId());
    }

    /** Test setExtraData(). */
    @Test public void testSetExtraData() {
        AuditEvent event = new AuditEvent();

        ExtraData extra1 = new ExtraData(ExtraDataKey.MODULE, "module");
        ExtraData extra2 = new ExtraData(ExtraDataKey.USER_ID, "user");

        event.setExtraData((ExtraData[]) null);
        assertNull(event.getExtraData());

        event.setExtraData((List<ExtraData>) null);
        assertNull(event.getExtraData());

        event.setExtraData(extra1);
        assertEquals(1, event.getExtraData().size());
        assertSame(extra1, event.getExtraData().get(0));

        event.setExtraData((ExtraData[]) null);
        assertNull(event.getExtraData());

        event.setExtraData(extra1, extra2);
        assertEquals(2, event.getExtraData().size());
        assertSame(extra1, event.getExtraData().get(0));
        assertSame(extra2, event.getExtraData().get(1));

        event.setExtraData((List<ExtraData>) null);
        assertNull(event.getExtraData());

        List<ExtraData> list = new ArrayList<ExtraData>();
        event.setExtraData(list);
        assertSame(list, event.getExtraData());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        AuditEvent event1;
        AuditEvent event2;

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        assertTrue(event1.equals(event2));
        assertTrue(event2.equals(event1));

        try {
            event1 = createAuditEvent();
            event2 = null;
            event1.equals(event2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            event1 = createAuditEvent();
            event2 = null;
            event1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        event2.setEventId(999L);
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        event2.setEventType(AuditEventType.DELETE_USER);
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        event2.setEventTimestamp(DateUtils.getCurrentDate());
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        event2.setUserId("X");
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        event2.setSessionId("X");
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));

        event1 = createAuditEvent();
        event2 = createAuditEvent();
        event2.getExtraData().clear();
        assertFalse(event1.equals(event2));
        assertFalse(event2.equals(event1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        AuditEvent event1 = createAuditEvent();
        event1.setEventId(999L);

        AuditEvent event2 = createAuditEvent();
        event2.setEventType(AuditEventType.DELETE_USER);

        AuditEvent event3 = createAuditEvent();
        event3.setEventTimestamp(DateUtils.getCurrentDate());

        AuditEvent event4 = createAuditEvent();
        event4.setUserId("X");

        AuditEvent event5 = createAuditEvent();
        event5.setSessionId("X");

        AuditEvent event6 = createAuditEvent();
        event6.getExtraData().clear();

        AuditEvent event7 = createAuditEvent();
        event7.setEventId(999L);  // same as event1

        Map<AuditEvent, String> map = new HashMap<AuditEvent, String>();
        map.put(event1, "ONE");
        map.put(event2, "TWO");
        map.put(event3, "THREE");
        map.put(event4, "FOUR");
        map.put(event5, "FIVE");
        map.put(event6, "SIX");

        assertEquals("ONE", map.get(event1));
        assertEquals("TWO", map.get(event2));
        assertEquals("THREE", map.get(event3));
        assertEquals("FOUR", map.get(event4));
        assertEquals("FIVE", map.get(event5));
        assertEquals("SIX", map.get(event6));
        assertEquals("ONE", map.get(event7));
    }

    /** Create a AuditEvent for testing. */
    private static AuditEvent createAuditEvent() {
        AuditEvent event = new AuditEvent();

        List<ExtraData> extraData = new ArrayList<ExtraData>();
        extraData.add(new ExtraData(ExtraDataKey.MODULE, "value"));

        event.setEventId(1L);
        event.setEventType(AuditEventType.ADMIN_LOGIN);
        event.setEventTimestamp(DateUtils.createDate(2011, 12, 14));
        event.setUserId("user");
        event.setSessionId("session");
        event.setExtraData(extraData);

        return event;
    }


}
