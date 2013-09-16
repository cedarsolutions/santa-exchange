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
package com.cedarsolutions.santa.server.rpc.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;

/**
 * Unit tests for ClientSessionRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientSessionRpcTest {

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        ClientSessionRpc rpc = new ClientSessionRpc();
        assertNull(rpc.getClientSessionService());

        IClientSessionService clientSessionService = mock(IClientSessionService.class);
        rpc.setClientSessionService(clientSessionService);
        assertSame(clientSessionService, rpc.getClientSessionService());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        ClientSessionRpc rpc = new ClientSessionRpc();
        IClientSessionService clientSessionService = mock(IClientSessionService.class);

        rpc.setClientSessionService(clientSessionService);
        rpc.afterPropertiesSet();

        try {
            rpc.setClientSessionService(null);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test establishClientSession(). */
    @Test public void testEstablishClientSession() {
        ClientSessionRpc rpc = createRpc();
        ClientSession session = mock(ClientSession.class);
        when(rpc.getClientSessionService().establishClientSession(Module.ROOT, "dest")).thenReturn(session);
        assertSame(session, rpc.establishClientSession(Module.ROOT, "dest"));
    }

    /** Test establishClientSession(), when an exception is thrown. */
    @Test public void testEstablishClientSessionException() {
        ClientSessionRpc rpc = createRpc();
        RuntimeException exception = new RuntimeException("Hello");
        when(rpc.getClientSessionService().establishClientSession(Module.ROOT, "dest")).thenThrow(exception);

        try {
            rpc.establishClientSession(Module.ROOT, "dest");
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test invalidateClientSession(). */
    @Test public void testInvalidateClientSession() {
        ClientSessionRpc rpc = createRpc();
        rpc.invalidateClientSession(Module.ROOT);
        verify(rpc.getClientSessionService()).invalidateClientSession(Module.ROOT);
    }

    /** Test invalidateClientSession() when an exception is thrown. */
    @Test public void testInvalidateClientSessionException() {
        ClientSessionRpc rpc = createRpc();
        RuntimeException exception = new RuntimeException("Hello");
        doThrow(exception).when(rpc.getClientSessionService()).invalidateClientSession(Module.ROOT);

        try {
            rpc.invalidateClientSession(Module.ROOT);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Create a mocked RPC. */
    private static ClientSessionRpc createRpc() {
        IClientSessionService clientSessionService = mock(IClientSessionService.class);

        ClientSessionRpc rpc = new ClientSessionRpc();
        rpc.setClientSessionService(clientSessionService);
        rpc.afterPropertiesSet();

        return rpc;
    }

}
