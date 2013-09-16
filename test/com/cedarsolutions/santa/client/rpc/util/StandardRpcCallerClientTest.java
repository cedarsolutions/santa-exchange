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

import com.cedarsolutions.exception.RpcSecurityException;
import com.cedarsolutions.santa.client.SantaExchangeMessages;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.web.metadata.HttpStatusCode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.StatusCodeException;

/**
 * Client-side unit tests for StandardRpcCaller.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class StandardRpcCallerClientTest extends ClientTestCase {

    /** Test generateError() for a generic exception, no status code. */
    public void testGenerateErrorGeneric() {
        // I'm not sure exactly how to generate this error condition in the real application.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        Exception exception = new Exception("Hello, world");
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertSame(exception, error.getException());
        assertEquals(messages.abstractRpcCallback_generalRpcErrorMessage(), error.getMessage());
        assertEquals(1, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
    }

    /** Test generateError() for an unknown HTTP exception. */
    public void testGenerateErrorUnknownHttp() {
        // There's not a great way to generate this error condition in the real application.
        // It tends to happen upon logout (i.e. the RPC call to logout actually fails after
        // initiating the logout), but the session-handling code already deals with that.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        StatusCodeException exception = new StatusCodeException(0, "whatever");  // pick some arbitrary unknown code
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertSame(exception, error.getException());
        assertEquals(messages.abstractRpcCallback_generalRpcErrorMessage(), error.getMessage());
        assertEquals(1, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
    }

    /** Test generateError() for a general HTTP exception with a known status code. */
    public void testGenerateErrorGeneralHttp() {
        // A good way to generate this error condition in the real application
        // is to throw a RuntimeException from some server-side service method
        // that you know will be called.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        StatusCodeException exception = new StatusCodeException(HttpStatusCode.CONFLICT.getValue(), "whatever");
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertSame(exception, error.getException());
        assertEquals(messages.abstractRpcCallback_generalRpcErrorMessage(), error.getMessage());
        assertEquals(2, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
        assertEquals(messages.abstractRpcCallback_httpStatusCode(HttpStatusCode.CONFLICT, HttpStatusCode.CONFLICT.getValue()),
                     error.getSupportingTextItems().get(1));
    }

    /** Test generateError() for a FORBIDDEN HTTP exception. */
    public void testGenerateErrorForbiddenHttp() {
        // A good way to generate this error condition in the real application
        // is to annotate @Secured("ROLE_USER") on a method you know will be
        // called before the user is logged in.  For instance, put it onto the
        // ClientSessionRpc.establishClientSession() method.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        StatusCodeException exception = new StatusCodeException(HttpStatusCode.FORBIDDEN.getValue(), "whatever");
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertNull(error.getException());  // don't show any exception for security errors
        assertEquals(messages.abstractRpcCallback_notAuthorizedRpcErrorMessage(), error.getMessage());
        assertEquals(2, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
        assertEquals(messages.abstractRpcCallback_httpStatusCode(HttpStatusCode.FORBIDDEN, HttpStatusCode.FORBIDDEN.getValue()),
                     error.getSupportingTextItems().get(1));
    }

    /** Test generateError() for a security exception. */
    public void testGenerateErrorSecurityException() {
        // A good way to generate this error condition is to edit an exchange,
        // and then modify the URL to include an exchange id not owned by the
        // current user.  ExchangeRpc disallows this, so you'll get an exception.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        RpcSecurityException exception = new RpcSecurityException("whatever");
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertNull(error.getException());  // don't show any exception for security errors
        assertEquals(messages.abstractRpcCallback_notAuthorizedRpcErrorMessage(), error.getMessage());
        assertEquals(4, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
        assertEquals(messages.abstractRpcCallback_securityException(), error.getSupportingTextItems().get(1));
        assertEquals(messages.abstractRpcCallback_logOutAndTryAgain(), error.getSupportingTextItems().get(2));
        assertEquals(messages.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(3));
    }

    /** Test generateError() for an RPC timeout exception. */
    public void testGenerateRequestTimeoutExceptionError() {
        // A good way to generate this error condition is put a long sleep()
        // call into the RPC method to list exchanges.  Then, you'll get a
        // timeout after logging in.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        RequestTimeoutException exception = new RequestTimeoutException(null, 0);
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertNull(error.getException());  // there's no reason to show a stack trace for a timeout
        assertEquals(messages.abstractRpcCallback_requestTimedOut(), error.getMessage());
        assertEquals(3, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
        assertEquals(messages.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(1));
        assertEquals(messages.abstractRpcCallback_timeoutExplanation(), error.getSupportingTextItems().get(2));
    }

    /** Test generateError() for an IncompatibleRemoteServiceException. */
    public void testGenerateIncompatibleRemoteServiceException() {
        // A good way to generate this error condition is put a long sleep()
        // call into the RPC method to list exchanges.  Then, you'll get a
        // timeout after logging in.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        IncompatibleRemoteServiceException exception = new IncompatibleRemoteServiceException("Hello");
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertNull(error.getException());  // there's no reason to show a stack trace for this error
        assertEquals(messages.abstractRpcCallback_backendUpgraded(), error.getMessage());
        assertEquals(3, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
        assertEquals(messages.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(1));
        assertEquals(messages.abstractRpcCallback_closeBrowserDueToUpgrade(), error.getSupportingTextItems().get(2));
    }

    /** Test generateError() for a CSRF/XSRF attack error. */
    public void testGenerateErrorXsrfAttack() {
        // A good way to generate this error condition is modify the proxy
        // caller to insert a bogus token into the RPC request.

        SantaExchangeMessages messages = GWT.create(SantaExchangeMessages.class);

        RpcTokenException exception = new RpcTokenException("whatever");
        Caller caller = new Caller();
        ErrorDescription error = caller.generateError(exception);
        assertNotNull(error);

        assertNull(error.getException());  // don't show any exception for security errors
        assertEquals(messages.abstractRpcCallback_requestBlockedMessage(), error.getMessage());
        assertEquals(4, error.getSupportingTextItems().size());
        assertEquals(messages.abstractRpcCallback_rpcName("rpc.method()"), error.getSupportingTextItems().get(0));
        assertEquals(messages.abstractRpcCallback_securityException(), error.getSupportingTextItems().get(1));
        assertEquals(messages.abstractRpcCallback_logOutAndTryAgain(), error.getSupportingTextItems().get(2));
        assertEquals(messages.abstractRpcCallback_message(exception.getMessage()), error.getSupportingTextItems().get(3));
    }

    /** Stubbed caller for testing. */
    // Of course, it's completely bogus to have String as the async type, but it happens to work for this case. ;)
    private static class Caller extends StandardRpcCaller<String, Object> {
        public Caller() {
            super("Hello", "rpc", "method");
        }

        @Override
        public void invokeRpcMethod(String async, AsyncCallback<Object> caller) {
        }

        @Override
        public void onSuccessResult(Object result) {
        }
    }
}
