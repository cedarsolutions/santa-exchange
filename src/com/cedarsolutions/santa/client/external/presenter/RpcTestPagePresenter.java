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
package com.cedarsolutions.santa.client.external.presenter;

import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.view.ExternalConstants;
import com.cedarsolutions.santa.client.external.view.IRpcTestPageView;
import com.cedarsolutions.santa.client.external.view.RpcTestPageView;
import com.cedarsolutions.santa.client.rpc.ITestRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Show the RPC test page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = RpcTestPageView.class)
public class RpcTestPagePresenter extends ModulePagePresenter<IRpcTestPageView, ExternalEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Test RPC. */
    private ITestRpcAsync testRpc;

    /** Show the RPC test page. */
    public void onShowRpcTestPage() {
        view.setExpectedResults(this.getExpectedResults());
        view.setUnprotectedRpcHandler(new RpcInvocationHandler(this, "unprotectedRpc"));
        view.setUserRpcHandler(new RpcInvocationHandler(this, "userRpc"));
        view.setAdminRpcHandler(new RpcInvocationHandler(this, "adminRpc"));
        view.setEnabledUserRpcHandler(new RpcInvocationHandler(this, "enabledUserRpc"));
        view.setEnabledAdminRpcHandler(new RpcInvocationHandler(this, "enabledAdminRpc"));
        this.replaceModuleBody();
    }

    /** Get the session from the injector. */
    public ClientSession getSession() {
        return this.systemStateInjector.getSession();
    }

    /** System state injector. */
    public SystemStateInjector getSystemStateInjector() {
        return this.systemStateInjector;
    }

    /** System state injector. */
    @Inject
    public void setSystemStateInjector(SystemStateInjector systemStateInjector) {
        this.systemStateInjector = systemStateInjector;
    }

    /** Get the exchangeRpc. */
    public ITestRpcAsync getTestRpc() {
        return this.testRpc;
    }

    /** Set the exchangeRpc. */
    @Inject
    public void setTestRpc(ITestRpcAsync testRpc) {
        this.testRpc = testRpc;
    }

    /** Get expected results for all RPCs, based on known login state. */
    protected Map<String, String> getExpectedResults() {
        Map<String, String> expectedResults = new HashMap<String, String>();

        ClientSession session = this.getSession();
        boolean loggedIn = session.isLoggedIn();
        boolean admin = session.isAdmin();
        boolean enabled = !session.isLocked();

        expectedResults.put("unprotectedRpc", getResult(true));
        expectedResults.put("userRpc", getResult(loggedIn));
        expectedResults.put("adminRpc", getResult(admin));
        expectedResults.put("enabledUserRpc", getResult(loggedIn && enabled));
        expectedResults.put("enabledAdminRpc", getResult(admin && enabled));

        return expectedResults;
    }

    /** Get the correct expected result. */
    private static String getResult(boolean allowed) {
        ExternalConstants constants = GWT.create(ExternalConstants.class);
        return allowed ? constants.rpcTest_allowed() : constants.rpcTest_notAllowed();
    }

    /** RPC invocation handler for all RPCs. */
    protected static class RpcInvocationHandler extends AbstractViewEventHandler<RpcTestPagePresenter> {
        private String method;

        public RpcInvocationHandler(RpcTestPagePresenter parent, String method) {
            super(parent);
            this.method = method;
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            TestRpcCaller caller = new TestRpcCaller(this.getParent(), this.getMethod());
            caller.invoke();
        }

        public String getMethod() {
            return this.method;
        }
    }

    /** Caller for ITestRpc methods, which all have an identical interface. */
    protected static class TestRpcCaller extends StandardRpcCaller<ITestRpcAsync, String> {
        protected RpcTestPagePresenter parent;

        public TestRpcCaller(RpcTestPagePresenter parent, String method) {
            super(parent.getTestRpc(), "ITestRpc", method);
            this.parent = parent;
            this.markNotRetryable();  // we do not want this call retried
        }

        @Override
        public void invokeRpcMethod(ITestRpcAsync async, AsyncCallback<String> callback) {
            if ("unprotectedRpc".equals(this.getMethod())) {
                async.unprotectedRpc("value", callback);
            } else if ("userRpc".equals(this.getMethod())) {
                async.userRpc("value", callback);
            } else if ("adminRpc".equals(this.getMethod())) {
                async.adminRpc("value", callback);
            } else if ("enabledUserRpc".equals(this.getMethod())) {
                async.enabledUserRpc("value", callback);
            } else if ("enabledAdminRpc".equals(this.getMethod())) {
                async.enabledAdminRpc("value", callback);
            }
        }

        @Override
        public void onSuccessResult(String result) {
            this.parent.getView().setActualResult(this.getMethod(), result);
        }
    }

}
