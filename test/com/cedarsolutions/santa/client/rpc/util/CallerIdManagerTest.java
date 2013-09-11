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
package com.cedarsolutions.santa.client.rpc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for CallerIdManager.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CallerIdManagerTest {

    /** Test getNextCallerId(). */
    @Test public void testGetNextCallerId() {
        CallerIdManager manager = new CallerIdManager();
        assertEquals(0, manager.index);
        assertEquals("rpc000001", manager.getNextCallerId());

        manager.index = 0xFFFFFF - 1;  // this test assumes MAX_INDEX is 0xFFFFFF
        assertEquals("rpcffffff", manager.getNextCallerId());
        assertEquals("rpc000001", manager.getNextCallerId());
    }

}
