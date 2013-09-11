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
package com.cedarsolutions.santa.client.rpc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.cedarsolutions.client.gwt.datasource.BackendDataSource;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for BackendDataRpcCaller.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BackendDataRpcCallerTest extends StubbedClientTestCase {

    /** Test the constructor. */
    @SuppressWarnings("unchecked")
    @Test public void testConstructor() {
        BackendDataSource<String, Integer> dataSource = (BackendDataSource<String, Integer>) mock(BackendDataSource.class);
        Caller caller = new Caller(dataSource, 5);
        assertNotNull(caller);
        assertEquals("rpc", caller.getRpc());
        assertEquals("method", caller.getMethod());
        assertEquals(5, caller.getStart());
        assertSame(dataSource, caller.getDataSource());
        assertTrue(caller.isMarkedRetryable());
    }

    /** Test onSuccess(). */
    @SuppressWarnings("unchecked")
    @Test public void testOnSuccess() {
        PaginatedResults<String> results = new PaginatedResults<String>();
        BackendDataSource<String, Integer> dataSource = (BackendDataSource<String, Integer>) mock(BackendDataSource.class);
        Caller caller = new Caller(dataSource, 5);
        caller.onSuccessResult(results);
        verify(dataSource).applyResults(5, results);
    }

    /** Test onUnhandledError(). */
    @SuppressWarnings("unchecked")
    @Test public void testOnUnhandledError7() {
        BackendDataSource<String, Integer> dataSource = (BackendDataSource<String, Integer>) mock(BackendDataSource.class);
        Throwable exception = new RuntimeException("Whatever");
        Caller caller = new Caller(dataSource, 5);
        caller.onUnhandledError(exception);
        verify(dataSource).markRetrieveComplete();
    }

    /** Dummy asynchronous RPC for testing. */
    private interface IDummyAsync {
    }

    /** Callback for testing. */
    private static class Caller extends BackendDataRpcCaller<IDummyAsync, String, Integer> {
        public Caller(BackendDataSource<String, Integer> dataSource, int start) {
            super(mock(IDummyAsync.class), "rpc", "method", dataSource, start);
        }

        @Override
        public void invokeRpcMethod(IDummyAsync async, AsyncCallback<PaginatedResults<String>> callback) {
        }
    }

}
