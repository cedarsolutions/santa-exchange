/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2015 Kenneth J. Pronovici.
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

import org.junit.Test;

import com.cedarsolutions.junit.gwt.GwtAssertions;

/**
 * Check that all of the RPC interfaces are configured properly.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcInterfaceTest {

    /** The name of the RPC interface package. */
    private static final String RPC_PACKAGE = "com.cedarsolutions.santa.client.rpc";

    /** Check all RPC interfaces for the proper RemoteService interface. */
    @Test public void checkRpcInterfaces() throws Exception {
        GwtAssertions.assertRpcsImplementRemoteService(RPC_PACKAGE);
    }

}
