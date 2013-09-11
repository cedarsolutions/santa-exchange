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
package com.cedarsolutions.santa.client.rpc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for StandardRpcCaller.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class StandardRpcCallerTest extends StubbedClientTestCase {

    /** Test the constructor. */
    @Test public void testConstructor() {
        Caller caller = new Caller();
        assertNotNull(caller.getAsync());
        assertTrue(caller.getAsync() instanceof IDummyAsync);
        assertEquals("rpc", caller.getRpc());
        assertEquals("method", caller.getMethod());
    }

    /** Test marked retryable behavior. */
    @Test public void testMarkedRetryable() {
        Caller caller = new Caller();
        assertEquals(1, caller.getMaxAttempts());
        assertFalse(caller.isMarkedRetryable());

        caller.markRetryable();
        assertEquals(2, caller.getMaxAttempts());
        assertTrue(caller.isMarkedRetryable());

        caller.markNotRetryable();
        assertEquals(1, caller.getMaxAttempts());
        assertFalse(caller.isMarkedRetryable());
    }

    /** Test applyPolicies(). */
    @Test public void testApplyPolicies() {
        Caller caller = new Caller();
        caller.applyPolicies();
        verify(RpcUtils.getInstance()).applySystemWidePolicies(caller.getAsync());
    }

    /** Test log(). */
    @Test public void testLog() {
        Caller caller = new Caller();
        caller.log("hello");
        verify(RpcUtils.getInstance()).log("hello");
    }

    /** Test getNextCallerId(). */
    @Test public void testGetNextCallerId() {
        Caller caller = new Caller();
        when(RpcUtils.getInstance().getNextCallerId()).thenReturn("ID");
        assertEquals("ID", caller.getNextCallerId());
    }

    /** Test showProgressIndicator(). */
    @Test public void testShowProgressIndicator() {
        Caller caller = new Caller();
        caller.showProgressIndicator();
        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
    }

    /** Test hideProgressIndicator(). */
    @Test public void testHideProgressIndicator() {
        Caller caller = new Caller();
        caller.hideProgressIndicator();
        verify(WidgetUtils.getInstance()).hideProgressIndicator();
    }

    /** Test showError(). */
    @Test public void testShowError() {
        Caller caller = new Caller();
        ErrorDescription error = new ErrorDescription();
        caller.showError(error);
        verify(WidgetUtils.getInstance()).showErrorPopup(error);
    }

    /** Dummy asynchronous RPC for testing. */
    private interface IDummyAsync {
    }

    /** Stubbed caller for testing. */
    private static class Caller extends StandardRpcCaller<IDummyAsync, Object> {
        public Caller() {
            super(mock(IDummyAsync.class), "rpc", "method");
        }

        @Override
        public void invokeRpcMethod(IDummyAsync async, AsyncCallback<Object> callback) {
        }

        @Override
        public void onSuccessResult(Object result) {
        }
    }

}
