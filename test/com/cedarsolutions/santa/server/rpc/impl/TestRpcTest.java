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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for TestRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class TestRpcTest {

    /** Quickly test all of the methods. */
    @Test public void testMethods() {
        TestRpc rpc = new TestRpc();
        assertEquals("unprotectedRpc/value", rpc.unprotectedRpc("value"));
        assertEquals("userRpc/value", rpc.userRpc("value"));
        assertEquals("adminRpc/value", rpc.adminRpc("value"));
        assertEquals("enabledUserRpc/value", rpc.enabledUserRpc("value"));
        assertEquals("enabledAdminRpc/value", rpc.enabledAdminRpc("value"));
    }

}
