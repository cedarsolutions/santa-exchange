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

import java.util.ArrayList;
import java.util.List;

import com.cedarsolutions.client.gwt.rpc.util.AbstractRpcCaller;
import com.cedarsolutions.exception.RpcSecurityException;
import com.cedarsolutions.santa.client.SantaExchangeMessages;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.web.metadata.HttpStatusCode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RpcTokenException;

/**
 * Standard RPC caller class.
 *
 * <p>
 * The goal here is to apply behavioral policies to RPCs on a system-wide
 * basis, rather than pushing those decisions out to all of the individual RPC
 * invocations.  For instance, we can apply a system-wide RPC timeout policy,
 * and we have a place to handle specific errors, retry failed requests, and
 * log information in a consistent way across all RPCs.
 * </p>
 *
 * <p>
 * The resulting RPC invocation idiom looks a little unsual compared to
 * "typical" example GWT code.  Client code never directly invokes RPCs.
 * However, since I prefer to avoid anonymous class implementations anyway for
 * testing purposes, this code already looked fairly unusual.  It's not a
 * perfect system, but it gets me enough functional benefits that I'm happy
 * with it.
 * <p>
 *
 * @param <A> Type of the asynchronous RPC
 * @param <T> Return type of the RPC method
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class StandardRpcCaller<A, T> extends AbstractRpcCaller<A, T> {

    /** Localized messages. */
    private SantaExchangeMessages messages;

    /**
     * Create an RPC caller, not retryable by default.
     * @param async     The asynchronous RPC
     * @param rpc       Name of the RPC that is being invoked, like "IClientSessionRpc"
     * @param method    Name of the method that is being invoked, like "establishClientSession"
     */
    protected StandardRpcCaller(A async, String rpc, String method) {
        super(async, rpc, method);
        this.messages = GWT.create(SantaExchangeMessages.class);
        this.markNotRetryable();
    }

    /** Whether this RPC is marked as retryable. */
    public boolean isMarkedRetryable() {
        return this.getMaxAttempts() > 1;
    }

    /** Mark that the RPC is retryable. */
    public void markRetryable() {
        this.setMaxAttempts(2);
    }

    /** Mark that the RPC is not retryable. */
    public void markNotRetryable() {
        this.setMaxAttempts(1);
    }

    /** Apply policies to the RPC. */
    @Override
    public void applyPolicies() {
        RpcUtils.getInstance().applySystemWidePolicies(this.getAsync());
    }

    /** Log a message to an appropriate logger mechanism, like GWT.log(). */
    @Override
    public void log(String message) {
        RpcUtils.getInstance().log(message);
    }

    /** Get the next caller id from some application-wide id manager, or null. */
    @Override
    public String getNextCallerId() {
        return RpcUtils.getInstance().getNextCallerId();
    }

    /** Show the front-end progress indicator. */
    @Override
    public void showProgressIndicator() {
        WidgetUtils.getInstance().showPleaseWaitProgressIndicator();
    }

    /** Hide the front-end progress indicator. */
    @Override
    public void hideProgressIndicator() {
        WidgetUtils.getInstance().hideProgressIndicator();
    }

    /** Show an error to the user. */
    @Override
    public void showError(ErrorDescription error) {
        WidgetUtils.getInstance().showErrorPopup(error);
    }

    /** Generate an error due to an authorization problem like HTTP FORBIDDEN. */
    @Override
    public ErrorDescription generateNotAuthorizedError(HttpStatusCode statusCode) {
        String rpcMethod = this.getRpcMethod();
        String message = this.messages.abstractRpcCallback_notAuthorizedRpcErrorMessage();

        List<String> supporting = new ArrayList<String>();
        supporting.add(this.messages.abstractRpcCallback_rpcName(rpcMethod));
        if (statusCode != null) {
            supporting.add(this.messages.abstractRpcCallback_httpStatusCode(statusCode, statusCode.getValue()));
        }

        // Note: for a security error, we don't show the exception stack trace.
        return new ErrorDescription(message, supporting);
    }

    /** Generate an error due to an RpcSecurityException. */
    @Override
    public ErrorDescription generateRpcSecurityExceptionError(RpcSecurityException exception) {
        String rpcMethod = this.getRpcMethod();
        String message = this.messages.abstractRpcCallback_notAuthorizedRpcErrorMessage();

        List<String> supporting = new ArrayList<String>();
        supporting.add(this.messages.abstractRpcCallback_rpcName(rpcMethod));
        supporting.add(this.messages.abstractRpcCallback_securityException());
        supporting.add(this.messages.abstractRpcCallback_logOutAndTryAgain());
        supporting.add(this.messages.abstractRpcCallback_message(exception.getMessage()));

        // Note: for security-type errors, we don't show the exception stack trace.
        return new ErrorDescription(message, supporting);
    }

    /** Generate an error due to an RpcTokenException. */
    @Override
    public ErrorDescription generateRpcTokenExceptionError(RpcTokenException exception) {
        String rpcMethod = this.getRpcMethod();
        String message = this.messages.abstractRpcCallback_requestBlockedMessage();

        List<String> supporting = new ArrayList<String>();
        supporting.add(this.messages.abstractRpcCallback_rpcName(rpcMethod));
        supporting.add(this.messages.abstractRpcCallback_securityException());
        supporting.add(this.messages.abstractRpcCallback_logOutAndTryAgain());
        supporting.add(this.messages.abstractRpcCallback_message(exception.getMessage()));

        // Note: for security-type errors, we don't show the exception stack trace.
        return new ErrorDescription(message, supporting);
    }

    /** Generate an error due to a RequestTimeoutException. */
    @Override
    public ErrorDescription generateRequestTimeoutExceptionError(RequestTimeoutException exception) {
        String rpcMethod = this.getRpcMethod();
        String message = this.messages.abstractRpcCallback_requestTimedOut();

        List<String> supporting = new ArrayList<String>();
        supporting.add(this.messages.abstractRpcCallback_rpcName(rpcMethod));
        supporting.add(this.messages.abstractRpcCallback_message(exception.getMessage()));
        supporting.add(this.messages.abstractRpcCallback_timeoutExplanation());

        // Note: for a timeout, there's no reason to show a stack trace
        return new ErrorDescription(message, supporting);
    }

    /** Generate an error due to a IncompatibleRemoteServiceException. */
    @Override
    public ErrorDescription generateIncompatibleRemoteServiceExceptionError(IncompatibleRemoteServiceException exception) {
        String rpcMethod = this.getRpcMethod();
        String message = this.messages.abstractRpcCallback_backendUpgraded();

        List<String> supporting = new ArrayList<String>();
        supporting.add(this.messages.abstractRpcCallback_rpcName(rpcMethod));
        supporting.add(this.messages.abstractRpcCallback_message(exception.getMessage()));
        supporting.add(this.messages.abstractRpcCallback_closeBrowserDueToUpgrade());

        // Note: for this sort of error, we don't show the exception stack trace.
        return new ErrorDescription(message, supporting);
    }

    /** Generate an error due to a general exception. */
    @Override
    public ErrorDescription generateGeneralRpcError(Throwable exception) {
        return generateGeneralRpcError(exception, null);
    }

    /** Generate an error due to a general exception that resulted in an HTTP error. */
    @Override
    public ErrorDescription generateGeneralRpcError(Throwable exception, HttpStatusCode statusCode) {
        String rpcMethod = this.getRpcMethod();
        String message = this.messages.abstractRpcCallback_generalRpcErrorMessage();

        List<String> supporting = new ArrayList<String>();
        supporting.add(this.messages.abstractRpcCallback_rpcName(rpcMethod));
        if (statusCode != null && statusCode != HttpStatusCode.UNKNOWN) {
            supporting.add(this.messages.abstractRpcCallback_httpStatusCode(statusCode, statusCode.getValue()));
        }

        return new ErrorDescription(message, exception, supporting);
    }

}
