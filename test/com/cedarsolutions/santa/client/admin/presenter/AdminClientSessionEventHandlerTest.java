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
package com.cedarsolutions.santa.client.admin.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IClientSessionRpcAsync;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;

/**
 * Unit tests for AdminClientSessionEventHandler.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminClientSessionEventHandlerTest extends StubbedClientTestCase {

    /** Test getModule(). */
    @Test public void testGetModule() {
        AdminClientSessionEventHandler handler = createEventHandler();
        assertEquals(Module.ADMIN, handler.getModule());
    }

    /** Create a properly-mocked handler, including everything that needs to be injected. */
    private static AdminClientSessionEventHandler createEventHandler() {
        AdminEventBus eventBus = mock(AdminEventBus.class);
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);
        IClientSessionRpcAsync clientSessionRpc = mock(IClientSessionRpcAsync.class);

        AdminClientSessionEventHandler handler = new AdminClientSessionEventHandler();
        handler.setEventBus(eventBus);
        handler.setSystemStateInjector(systemStateInjector);
        handler.setClientSessionRpc(clientSessionRpc);

        assertSame(eventBus, handler.getEventBus());
        assertSame(systemStateInjector, handler.getSystemStateInjector());
        assertSame(session, handler.getSession());
        assertSame(clientSessionRpc, handler.getClientSessionRpc());

        return handler;
    }

}
