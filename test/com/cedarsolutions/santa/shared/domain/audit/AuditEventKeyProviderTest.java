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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * Unit tests for AuditEventKeyProvider.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventKeyProviderTest {

    /** Test AuditEventKeyProvider. */
    @Test public void testAuditEventKeyProvider() {
        AuditEvent item = mock(AuditEvent.class);

        AuditEventKeyProvider provider = new AuditEventKeyProvider();

        assertNull(provider.getKey(null));

        when(item.getEventId()).thenReturn(null);
        assertNull(provider.getKey(item));

        when(item.getEventId()).thenReturn(32L);
        assertEquals(new Long(32), provider.getKey(item));
    }

}
