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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Unit tests for RpcUtils.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcUtilsTest extends StubbedClientTestCase {

    /** Test applySystemWidePolicies() for an object of incorrect type. */
    @Test public void testApplySystemWidePoliciesNull() {
        try {
            Object remoteInterface = null;
            RpcUtils rpcUtils = new RpcUtils();
            rpcUtils.applySystemWidePolicies(remoteInterface);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }
    }

    /** Test applySystemWidePolicies() for an object of incorrect type. */
    @Test public void testApplySystemWidePoliciesInvalid() {
        try {
            Object remoteInterface = "whatever";
            RpcUtils rpcUtils = new RpcUtils();
            rpcUtils.applySystemWidePolicies(remoteInterface);
            fail("Expected CedarRuntimeException");
        } catch (CedarRuntimeException e) { }
    }

    /** Test applySystemWidePolicies() for a valid remote interface. */
    @Test public void testApplySystemWidePoliciesValid() {
        ServiceDefTarget remoteInterface = mock(ServiceDefTarget.class);
        RpcUtils rpcUtils = new RpcUtils();
        rpcUtils.applySystemWidePolicies(remoteInterface);
        verify(remoteInterface).setRpcRequestBuilder(any(PolicyRpcRequestBuilder.class));
    }

    /** Test getNextCallerId(). */
    @Test public void testGetNextCallerId() {
        CallerIdManager callerIdManager = mock(CallerIdManager.class);
        when(callerIdManager.getNextCallerId()).thenReturn("hello");
        RpcUtils utils = new RpcUtils();
        when(utils.systemStateInjector.getCallerIdManager()).thenReturn(callerIdManager);
        assertEquals("hello", utils.getNextCallerId());
    }

}
