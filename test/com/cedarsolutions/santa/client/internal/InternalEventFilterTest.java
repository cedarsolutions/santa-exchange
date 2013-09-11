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
package com.cedarsolutions.santa.client.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Test;
import org.mockito.InOrder;

import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;

/**
 * Unit tests for InternalEventFilter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalEventFilterTest extends StubbedClientTestCase {

    /** Test getClientSession(). */
    @Test public void testGetClientSession() {
        InternalEventFilter filter = createFilter();
        assertSame(filter.getSession(), filter.getClientSession());
    }

    /** Test getExcludedEventTypes(). */
    @Test public void getExcludedEventTypes() {
        InternalEventFilter filter = createFilter();
        Set<String> excluded = filter.getExcludedEventTypes();
        assertEquals(3, excluded.size());
        assertTrue(excluded.contains(SantaExchangeEventTypes.START));
        assertTrue(excluded.contains(SantaExchangeEventTypes.REPLACE_ROOT_BODY));
        assertTrue(excluded.contains(SantaExchangeEventTypes.CLEAR_SESSION));
    }

    /** Test filterEventOnceInitialized() for a logged-in user. */
    @Test public void testFilterEventOnceInitializedLoggedIn() {
        InternalEventFilter filter = createFilter();
        when(filter.getSession().isLocked()).thenReturn(false);
        when(filter.getSession().isLoggedIn()).thenReturn(true);
        assertTrue(filter.filterEventOnceInitialized(null, null, null));  // args don't matter for this case
    }

    /** Test filterEventOnceInitialized() for a logged-in user that is locked. */
    @Test public void testFilterEventOnceInitializedLoggedInLocked() {
        InternalEventBus eventBus = mock(InternalEventBus.class);
        InternalEventFilter filter = createFilter();
        when(filter.getSession().isLocked()).thenReturn(true);
        when(filter.getSession().isLoggedIn()).thenReturn(true);
        assertFalse(filter.filterEventOnceInitialized(null, null, eventBus));  // some args don't matter
        verify(eventBus).lockOutUser();
    }

    /** Test filterEventOnceInitialized() when there is no logged in user. */
    @Test public void testFilterEventOnceInitializedNotLoggedIn() {
        String eventType = "event";
        Object[] params = new Object[] { "Hello" };
        InternalEventBus eventBus = mock(InternalEventBus.class);

        InternalEventFilter filter = createFilter();
        when(filter.getSession().isLoggedIn()).thenReturn(false);
        when(WidgetUtils.getInstance().getWndLocationHref()).thenReturn("here");

        InOrder inOrder = inOrder(eventBus);
        assertFalse(filter.filterEventOnceInitialized(eventType, params, eventBus));
        inOrder.verify(eventBus).setFilteringEnabled(false);
        inOrder.verify(eventBus).showLoginRequiredPage("here");
        inOrder.verify(eventBus).setFilteringEnabled(true);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static InternalEventFilter createFilter() {
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        InternalEventFilter filter = new InternalEventFilter();
        filter.setSystemStateInjector(systemStateInjector);

        assertSame(systemStateInjector, filter.getSystemStateInjector());
        assertSame(session, filter.getSession());

        return filter;
    }

}
