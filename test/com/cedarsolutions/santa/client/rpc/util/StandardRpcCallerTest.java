/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2017 Kenneth J. Pronovici.
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

import static com.cedarsolutions.web.metadata.HttpStatusCode.CONFLICT;
import static com.cedarsolutions.web.metadata.HttpStatusCode.FORBIDDEN;
import static com.cedarsolutions.web.metadata.HttpStatusCode.UNKNOWN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.cedarsolutions.exception.RpcSecurityException;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RpcTokenException;

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

    /** Test getXsrfRpcProxyTimeoutMs(). */
    @Test public void testGetXsrfRpcProxyTimeoutMs() {
        Caller caller = new Caller();
        assertEquals(CONFIG.system_xsrfRpcTimeoutMs(), caller.getXsrfRpcProxyTimeoutMs());
    }

    /** Test generateNotAuthorizedError(), no status code. */
    @Test public void testGenerateNotAuthorizedErrorNoStatusCode() {
        Caller caller = new Caller();
        ErrorDescription error = caller.generateNotAuthorizedError(null);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_notAuthorizedRpcErrorMessage(), error.getMessage());
        assertEquals(1, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
    }

    /** Test generateNotAuthorizedError(), with statusCode. */
    @Test public void testGenerateNotAuthorizedErrorWithStatusCode() {
        // A good way to generate this error condition in the real application
        // is to annotate @Secured("ROLE_USER") on a method you know will be
        // called before the user is logged in.  For instance, put it onto the
        // ClientSessionRpc.establishClientSession() method.
        Caller caller = new Caller();
        ErrorDescription error = caller.generateNotAuthorizedError(FORBIDDEN);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_notAuthorizedRpcErrorMessage(), error.getMessage());
        assertEquals(2, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_httpStatusCode(FORBIDDEN, FORBIDDEN.getValue()), error.getSupportingTextItems().get(1));
    }

    /** Test generateRpcSecurityExceptionError(). */
    @Test public void testGenerateRpcSecurityExceptionError() {
        // A good way to generate this error condition is to edit an exchange,
        // and then modify the URL to include an exchange id not owned by the
        // current user.  ExchangeRpc disallows this, so you'll get an exception.
        Caller caller = new Caller();
        RpcSecurityException exception = new RpcSecurityException("Hello");
        ErrorDescription error = caller.generateRpcSecurityExceptionError(exception);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_notAuthorizedRpcErrorMessage(), error.getMessage());
        assertEquals(4, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_securityException(), error.getSupportingTextItems().get(1));
        assertEquals(MESSAGES.abstractRpcCallback_logOutAndTryAgain(), error.getSupportingTextItems().get(2));
        assertEquals(MESSAGES.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(3));
    }

    /** Test generateRpcTokenExceptionError(). */
    @Test public void testGenerateRpcTokenExceptionError() {
        // A good way to generate this error condition is modify the proxy
        // caller to insert a bogus token into the RPC request.
        Caller caller = new Caller();
        RpcTokenException exception = new RpcTokenException("Hello");
        ErrorDescription error = caller.generateRpcTokenExceptionError(exception);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_requestBlockedMessage(), error.getMessage());
        assertEquals(4, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_securityException(), error.getSupportingTextItems().get(1));
        assertEquals(MESSAGES.abstractRpcCallback_logOutAndTryAgain(), error.getSupportingTextItems().get(2));
        assertEquals(MESSAGES.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(3));
    }

    /** Test generateRequestTimeoutExceptionError(). */
    @Test public void testGenerateRequestTimeoutExceptionError() {
        // A good way to generate this error condition is put a long sleep()
        // call into the RPC method to list exchanges.  Then, you'll get a
        // timeout after logging in.
        Caller caller = new Caller();
        RequestTimeoutException exception = new RequestTimeoutException(null, 333);
        ErrorDescription error = caller.generateRequestTimeoutExceptionError(exception);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_requestTimedOut(), error.getMessage());
        assertEquals(4, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(1));
        assertEquals(MESSAGES.abstractRpcCallback_timeoutMs(exception.getTimeoutMillis()), error.getSupportingTextItems().get(2));
        assertEquals(MESSAGES.abstractRpcCallback_timeoutExplanation(), error.getSupportingTextItems().get(3));
    }

    /** Test generateNoResponseReceivedError(). */
    @Test public void testGenerateNoResponseReceivedError() {
        Caller caller = new Caller();
        Exception exception = new Exception("Hello");
        ErrorDescription error = caller.generateNoResponseReceivedError(exception);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_noResponse(), error.getMessage());
        assertEquals(2, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_timeoutExplanation(), error.getSupportingTextItems().get(1));
    }

    /** Test generateIncompatibleRemoteServiceExceptionError(). */
    @Test public void testGenerateIncompatibleRemoteServiceExceptionError() {
        // A good way to generate this error condition is put a long sleep()
        // call into the RPC method to list exchanges.  Then, you'll get a
        // timeout after logging in.
        Caller caller = new Caller();
        IncompatibleRemoteServiceException exception = new IncompatibleRemoteServiceException("Hello");
        ErrorDescription error = caller.generateIncompatibleRemoteServiceExceptionError(exception);
        assertNull(error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_backendUpgraded(), error.getMessage());
        assertEquals(3, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(1));
        assertEquals(MESSAGES.abstractRpcCallback_closeBrowserDueToUpgrade(), error.getSupportingTextItems().get(2));
    }

    /** Test generateGeneralRpcError(), no status code. */
    @Test public void testGenerateGeneralRpcErrorNoStatusCode() {
        Caller caller = new Caller();
        Exception exception = new Exception("Hello");
        ErrorDescription error = caller.generateGeneralRpcError(exception);
        assertEquals(exception, error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_generalRpcErrorMessage(), error.getMessage());
        assertEquals(1, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
    }

    /** Test generateGeneralRpcError(), with known status code. */
    @Test public void testGenerateGeneralRpcErrorWithKnownStatusCode() {
        // A good way to generate this error condition in the real application
        // is to throw a RuntimeException from some server-side service method
        // that you know will be called.
        Caller caller = new Caller();
        Exception exception = new Exception("Hello");
        ErrorDescription error = caller.generateGeneralRpcError(exception, CONFLICT);
        assertEquals(exception, error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_generalRpcErrorMessage(), error.getMessage());
        assertEquals(2, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
        assertEquals(MESSAGES.abstractRpcCallback_httpStatusCode(CONFLICT, CONFLICT.getValue()), error.getSupportingTextItems().get(1));
    }

    /** Test generateGeneralRpcError(), with unknown status code. */
    @Test public void testGenerateGeneralRpcErrorWithUnknownStatusCode() {
        // There's not a great way to generate this error condition in the real application.
        // It tends to happen upon logout (i.e. the RPC call to logout actually fails after
        // initiating the logout), but the session-handling code already deals with that.
        Caller caller = new Caller();
        Exception exception = new Exception("Hello");
        ErrorDescription error = caller.generateGeneralRpcError(exception, UNKNOWN);
        assertEquals(exception, error.getException());
        assertEquals(MESSAGES.abstractRpcCallback_generalRpcErrorMessage(), error.getMessage());
        assertEquals(1, error.getSupportingTextItems().size());
        assertEquals(MESSAGES.abstractRpcCallback_rpcName(caller.getRpcMethod()), error.getSupportingTextItems().get(0));
    }

    /** Dummy asynchronous RPC for testing. */
    private interface IDummyAsync {
    }

    /** Stubbed caller for testing. */
    private static class Caller extends StandardRpcCaller<IDummyAsync, Object> {
        Caller() {
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
