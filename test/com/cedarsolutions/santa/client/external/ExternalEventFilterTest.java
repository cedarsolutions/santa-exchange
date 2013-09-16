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
package com.cedarsolutions.santa.client.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Test;

import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;

/**
 * Unit tests for ExternalEventFilter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalEventFilterTest extends StubbedClientTestCase {

    /** Test getClientSession(). */
    @Test public void testGetClientSession() {
        ExternalEventFilter filter = createFilter();
        assertSame(filter.getSession(), filter.getClientSession());
    }

    /** Test getExcludedEventTypes(). */
    @Test public void getExcludedEventTypes() {
        ExternalEventFilter filter = createFilter();
        Set<String> excluded = filter.getExcludedEventTypes();
        assertEquals(3, excluded.size());
        assertTrue(excluded.contains(SantaExchangeEventTypes.START));
        assertTrue(excluded.contains(SantaExchangeEventTypes.REPLACE_ROOT_BODY));
        assertTrue(excluded.contains(SantaExchangeEventTypes.CLEAR_SESSION));
    }

    /** Test filterEventOnceInitialized(). */
    @Test public void testFilterEventOnceInitialized() {
        ExternalEventFilter filter = createFilter();
        assertTrue(filter.filterEventOnceInitialized(null, null, null)); // anything is allowed
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static ExternalEventFilter createFilter() {
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        ExternalEventFilter filter = new ExternalEventFilter();
        filter.setSystemStateInjector(systemStateInjector);

        assertSame(systemStateInjector, filter.getSystemStateInjector());
        assertSame(session, filter.getSession());

        return filter;
    }

}
