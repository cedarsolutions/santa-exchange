/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2012-2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.cedarsolutions.santa.client.rpc.util.CallerIdManager;
import com.cedarsolutions.santa.shared.domain.ClientSession;

/**
 * Unit tests for SantaExchangeGinModule.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchangeGinModuleTest {

    /** Test getClientSession(). */
    @Test public void testGetClientSession() {
        SantaExchangeGinModule module = new SantaExchangeGinModule();
        ClientSession session1 = module.getClientSession();
        assertNotNull(session1);
        ClientSession session2 = module.getClientSession();
        assertSame(session1, session2);
    }

    /** Test getCallerIdManager(). */
    @Test public void testGetCallerIdManager() {
        SantaExchangeGinModule module = new SantaExchangeGinModule();
        CallerIdManager manager1 = module.getCallerIdManager();
        assertNotNull(manager1);
        CallerIdManager manager2 = module.getCallerIdManager();
        assertSame(manager1, manager2);
    }

}
