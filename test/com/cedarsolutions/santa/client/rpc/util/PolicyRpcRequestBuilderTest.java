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

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;

/**
 * Unit tests for PolicyRpcRequestBuilder.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PolicyRpcRequestBuilderTest extends StubbedClientTestCase {

    /** Test doCreate(). */
    @Test public void testDoCreate() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        PolicyRpcRequestBuilder builder = new PolicyRpcRequestBuilder();
        RequestBuilder result = builder.doCreate("whatever");
        assertEquals("whatever", result.getUrl());
        assertEquals(RequestBuilder.POST.toString(), result.getHTTPMethod());
        assertEquals(config.system_defaultRpcTimeoutMs(), result.getTimeoutMillis());
    }

}
