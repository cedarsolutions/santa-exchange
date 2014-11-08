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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;

/**
 * Client-side unit tests for AuditEventTypeList.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventTypeListClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        AuditEventTypeList list = new AuditEventTypeList();
        assertNotNull(list);
        assertEquals(1, list.getVisibleItemCount());
        assertEquals(AuditEventType.values().length, list.getItemCount());

        list = new AuditEventTypeList(true);
        assertNotNull(list);
        assertEquals(1, list.getVisibleItemCount());
        assertEquals(AuditEventType.values().length + 1, list.getItemCount());
    }

    /** Check that every value can be selected. */
    public void testsetSelectedObjectValue() {
        AuditEventTypeList list = new AuditEventTypeList(true);

        list.setSelectedObjectValue(null);
        assertEquals(null, list.getSelectedObjectValue());

        for (AuditEventType eventType : AuditEventType.values()) {
            list.setSelectedObjectValue(eventType);
            assertEquals(eventType, list.getSelectedObjectValue());
        }
    }

}
