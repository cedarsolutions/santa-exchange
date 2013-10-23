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
package com.cedarsolutions.santa.client.admin;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.shared.domain.ErrorDescription;

/**
 * Unit tests for AdminEventFilter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminEventFilterTest extends StubbedClientTestCase {

    /** Test getClientSession(). */
    @Test public void testGetClientSession() {
        AdminEventFilter filter = createFilter();
        assertSame(filter.getSession(), filter.getClientSession());
    }

    /** Test getExcludedEventTypes(). */
    @Test public void getExcludedEventTypes() {
        AdminEventFilter filter = createFilter();
        Set<String> excluded = filter.getExcludedEventTypes();
        assertEquals(3, excluded.size());
        assertTrue(excluded.contains(SantaExchangeEventTypes.START));
        assertTrue(excluded.contains(SantaExchangeEventTypes.REPLACE_ROOT_BODY));
        assertTrue(excluded.contains(SantaExchangeEventTypes.CLEAR_SESSION));
    }

    /** Test filterEventOnceInitialized() for a logged-in user that is an admin user. */
    @Test public void testFilterEventOnceInitializedAdmin() {
        AdminEventFilter filter = createFilter();
        when(filter.getSession().isLocked()).thenReturn(false);
        when(filter.getSession().isLoggedIn()).thenReturn(true);
        when(filter.getSession().isAdmin()).thenReturn(true);
        assertTrue(filter.filterEventOnceInitialized(null, null, null));  // args don't matter for this case
    }

    /** Test filterEventOnceInitialized() for a locked admin user. */
    @Test public void testFilterEventOnceInitializedAdminLocked() {
        AdminEventBus eventBus = mock(AdminEventBus.class);
        AdminEventFilter filter = createFilter();
        when(filter.getSession().isLocked()).thenReturn(true);
        when(filter.getSession().isLoggedIn()).thenReturn(true);
        when(filter.getSession().isAdmin()).thenReturn(true);
        assertFalse(filter.filterEventOnceInitialized(null, null, eventBus));  // some args don't matter
        verify(eventBus).lockOutUser();
    }

    /** Test filterEventOnceInitialized() for a locked non-admin user. */
    @Test public void testFilterEventOnceInitializedNonAdminLocked() {
        AdminEventBus eventBus = mock(AdminEventBus.class);
        AdminEventFilter filter = createFilter();
        when(filter.getSession().isLocked()).thenReturn(true);
        when(filter.getSession().isLoggedIn()).thenReturn(true);
        when(filter.getSession().isAdmin()).thenReturn(false);
        assertFalse(filter.filterEventOnceInitialized(null, null, eventBus));  // some args don't matter
        verify(eventBus).lockOutUser();
    }

    /** Test filterEventOnceInitialized() for a logged-in user that is not an admin user. */
    @Test public void testFilterEventOnceInitializedLoggedIn() {
        String eventType = "event";
        Object[] params = new Object[] { "Hello" };
        AdminEventBus eventBus = mock(AdminEventBus.class);

        AdminEventFilter filter = createFilter();
        when(filter.getSession().isLocked()).thenReturn(false);
        when(filter.getSession().isLoggedIn()).thenReturn(true);
        when(filter.getSession().isAdmin()).thenReturn(false);
        when(WidgetUtils.getInstance().getWndLocationHref()).thenReturn("here");

        ArgumentCaptor<ErrorDescription> error = ArgumentCaptor.forClass(ErrorDescription.class);

        InOrder inOrder = inOrder(eventBus, WidgetUtils.getInstance());
        assertFalse(filter.filterEventOnceInitialized(eventType, params, eventBus));
        inOrder.verify(eventBus).setFilteringEnabled(false);
        inOrder.verify(eventBus).showLandingPage();
        inOrder.verify(eventBus).showErrorPopup(error.capture());
        inOrder.verify(eventBus).setFilteringEnabled(true);
        assertEquals(MESSAGES.filter_adminAccessRequired(), error.getValue().getMessage());
        assertEquals(MESSAGES.filter_youWillBeRedirected(), error.getValue().getSupportingTextItems().get(0));
    }

    /** Test filterEventOnceInitialized() when there is no logged in user. */
    @Test public void testFilterEventOnceInitializedNotLoggedIn() {
        String eventType = "event";
        Object[] params = new Object[] { "Hello" };
        AdminEventBus eventBus = mock(AdminEventBus.class);

        AdminEventFilter filter = createFilter();
        when(filter.getSession().isLoggedIn()).thenReturn(false);
        when(WidgetUtils.getInstance().getWndLocationHref()).thenReturn("here");

        InOrder inOrder = inOrder(eventBus);
        assertFalse(filter.filterEventOnceInitialized(eventType, params, eventBus));
        inOrder.verify(eventBus).setFilteringEnabled(false);
        inOrder.verify(eventBus).showLoginRequiredPage("here");
        inOrder.verify(eventBus).setFilteringEnabled(true);
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static AdminEventFilter createFilter() {
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        AdminEventFilter filter = new AdminEventFilter();
        filter.setSystemStateInjector(systemStateInjector);
        filter.setMessages(MESSAGES);

        assertSame(systemStateInjector, filter.getSystemStateInjector());
        assertSame(session, filter.getSession());
        assertSame(MESSAGES, filter.getMessages());

        return filter;
    }

}
