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

import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.santa.client.SantaExchangeCustomLogger;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.mvp4g.client.event.Mvp4gLogger;

/**
 * Common RPC utilities.
 *
 * <p>
 * Unlike a lot of other utility classes, which typically provide static
 * methods, this class is a singleton.
 * </p>
 *
 * <p>
 * It's difficult to mock static method calls, but I really want that option
 * for some of these methods.  Normally, I would fall back on dependency
 * injection to solve this problem.  However, it's not always possible to
 * inject an instance of this class into all of the places where it needs to be
 * used.  Making the class a singleton works around that problem.
 * </p>
 *
 * <p>
 * For testing, the stubbed client test framework is wired into the
 * GWT.create() call within getInstance().  The framework "automagically"
 * generates a mocked version of this class for use by unit tests.  However,
 * keep in mind that even the mock object is a singleton, so you must remember
 * to reset the mock between test cases.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcUtils {

    /** Singleton instance. */
    private static RpcUtils INSTANCE;

    /** RPC request builder used to enforce system-wide policies. */
    protected RpcRequestBuilder rpcRequestBuilder;

    /** Logger that RPCs can use. */
    protected Mvp4gLogger logger;

    /** System state injector. */
    protected SystemStateInjector systemStateInjector;

    /** Default constructor is protected so class cannot be instantiated. */
    protected RpcUtils() {
        this.rpcRequestBuilder = new PolicyRpcRequestBuilder();
        this.logger = new SantaExchangeCustomLogger();
        this.systemStateInjector = GWT.create(SystemStateInjector.class);
    }

    /** Get an instance of this class to use. */
    public static synchronized RpcUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = GWT.create(RpcUtils.class);
        }

        return INSTANCE;
    }

    /** Log a message. */
    public void log(String message) {
        this.logger.log(message, 0);
    }

    /**
     * Apply system-wide RPC policies to a remote interface.
     * @param remoteInterface  Asynchronous RPC remote interface, usually provided by GIN.
     * @throws CedarRuntimeException If the passed-in object is not actually a remote interface.
     */
    public void applySystemWidePolicies(Object remoteInterface) {
        try {
            // Unfortunately, the stupid GWT interface doesn't let us ask
            // whether an RpcRequestBuilder is already set on the remote
            // interface, so we have no choice but to just set it again.
            ((ServiceDefTarget) remoteInterface).setRpcRequestBuilder(this.rpcRequestBuilder);
        } catch (ClassCastException e) {
            throw new CedarRuntimeException("Passed in object is not a remote RPC interface.", e);
        }
    }

    /** Get the next caller id. */
    public String getNextCallerId() {
        return this.systemStateInjector.getCallerIdManager().getNextCallerId();
    }

}
