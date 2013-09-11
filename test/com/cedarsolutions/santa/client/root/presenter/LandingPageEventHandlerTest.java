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
package com.cedarsolutions.santa.client.root.presenter;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.root.RootEventBus;
import com.cedarsolutions.santa.shared.domain.ClientSession;

/**
 * Unit tests for LandingPageEventHandler.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class LandingPageEventHandlerTest extends StubbedClientTestCase {

    /** Test onShowLandingPage() when the user is not logged in. */
    @Test public void testOnShowLandingPageNotLoggedIn() {
        LandingPageEventHandler handler = createEventHandler();
        when(handler.getSession().isLoggedIn()).thenReturn(false);
        handler.onShowLandingPage();
        verify(handler.getEventBus()).showExternalLandingPage();
    }

    /** Test onShowLandingPage() when the user is logged in but not admin. */
    @Test public void testOnShowLandingPageNormalUser() {
        LandingPageEventHandler handler = createEventHandler();
        when(handler.getSession().isLoggedIn()).thenReturn(true);
        when(handler.getSession().isAdmin()).thenReturn(false);
        handler.onShowLandingPage();
        verify(handler.getEventBus()).showInternalLandingPage();
    }

    /** Test onShowLandingPage() when the user is logged in as an admin user. */
    @Test public void testOnShowLandingPageAdminUser() {
        LandingPageEventHandler handler = createEventHandler();
        when(handler.getSession().isLoggedIn()).thenReturn(true);
        when(handler.getSession().isAdmin()).thenReturn(true);
        handler.onShowLandingPage();
        verify(handler.getEventBus()).showAdminLandingPage();
    }

    /** Create a properly-mocked handler, including everything that needs to be injected. */
    private static LandingPageEventHandler createEventHandler() {
        RootEventBus eventBus = mock(RootEventBus.class);
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        LandingPageEventHandler handler = new LandingPageEventHandler();
        handler.setEventBus(eventBus);
        handler.setSystemStateInjector(systemStateInjector);

        assertSame(eventBus, handler.getEventBus());
        assertSame(systemStateInjector, handler.getSystemStateInjector());
        assertSame(session, handler.getSession());

        return handler;
    }

}
