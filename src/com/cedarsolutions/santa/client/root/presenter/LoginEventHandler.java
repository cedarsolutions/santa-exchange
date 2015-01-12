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
package com.cedarsolutions.santa.client.root.presenter;

import static com.cedarsolutions.santa.client.SantaExchangeEventTypes.ACCOUNT_LOCKED_PAGE;

import com.cedarsolutions.client.gwt.rpc.IGaeUserRpcAsync;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.root.RootEventBus;
import com.cedarsolutions.santa.client.rpc.util.RpcUtils;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * Event handler to deal with login-related events.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@EventHandler
public class LoginEventHandler extends BaseEventHandler<RootEventBus> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** GAE user service. */
    private IGaeUserRpcAsync gaeUserRpc;

    /**
     * Show the Google Accounts login page for a particular destination token.
     * @param destinationToken Destination token (page) to redirect to after login
     */
    public void onShowGoogleAccountsLoginPageForToken(String destinationToken) {
        String destinationUrl = WidgetUtils.getInstance().getDestinationUrl(destinationToken);
        this.onShowGoogleAccountsLoginPageForUrl(destinationUrl);
    }

    /**
     * Show the Google Accounts login page for a particular destination URL.
     * @param destinationUrl   Destination URL to redirect to after login
     */
    public void onShowGoogleAccountsLoginPageForUrl(String destinationUrl) {
        GetLoginUrlCaller caller = new GetLoginUrlCaller(this.gaeUserRpc, this.eventBus);
        caller.setMethodArguments(destinationUrl);
        caller.invoke();
    }

    /** Log out the current user. */
    public void onLogout() {
        if (this.getSession().getLogoutUrl() != null) {
            // If we have a logout URL, clear the session before redirecting over to it
            String logoutUrl = this.getSession().getLogoutUrl();
            this.eventBus.clearSession();
            WidgetUtils.getInstance().redirect(logoutUrl);
        } else {
            // If we can't do anything else, clear the session and dump them at the landing page
            this.eventBus.clearSession();
            this.eventBus.showLandingPage();
        }
    }

    /** Lock the user out of the application. */
    public void onLockOutUser() {
        String destinationUrl = WidgetUtils.getInstance().getDestinationUrl(ACCOUNT_LOCKED_PAGE);
        GetLogoutUrlCaller caller = new GetLogoutUrlCaller(this.gaeUserRpc, this.eventBus);
        caller.setMethodArguments(destinationUrl);
        caller.invoke();
    }

    /** Get the session from the injector. */
    public ClientSession getSession() {
        return this.systemStateInjector.getSession();
    }

    public SystemStateInjector getSystemStateInjector() {
        return this.systemStateInjector;
    }

    @Inject
    public void setSystemStateInjector(SystemStateInjector systemStateInjector) {
        this.systemStateInjector = systemStateInjector;
    }

    public IGaeUserRpcAsync getGaeUserRpc() {
        return this.gaeUserRpc;
    }

    @Inject
    public void setGaeUserRpc(IGaeUserRpcAsync gaeUserRpc) {
        this.gaeUserRpc = gaeUserRpc;
        RpcUtils.getInstance().applySystemWidePolicies(this.gaeUserRpc);
    }

    /** Caller for IGaeUserRpcAsync.getGoogleAccountsLoginUrl(). */
    protected static class GetLoginUrlCaller extends StandardRpcCaller<IGaeUserRpcAsync, String> {
        protected RootEventBus eventBus;
        protected String destinationUrl;

        public GetLoginUrlCaller(IGaeUserRpcAsync async, RootEventBus eventBus) {
            super(async, "IGaeUserRpc", "getGoogleAccountsLoginUrl");
            this.eventBus = eventBus;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(String destinationUrl) {
            this.destinationUrl = destinationUrl;
        }

        @Override
        public void invokeRpcMethod(IGaeUserRpcAsync async, AsyncCallback<String> callback) {
            async.getGoogleAccountsLoginUrl(this.destinationUrl, callback);
        }

        @Override
        public void onSuccessResult(String result) {
            this.eventBus.clearSession();  // clear it so it gets reloaded later
            WidgetUtils.getInstance().redirect(result);
        }
    }

    /** Caller for IGaeUserRpcAsync.getGoogleAccountsLogoutUrl(). */
    protected static class GetLogoutUrlCaller extends StandardRpcCaller<IGaeUserRpcAsync, String> {
        protected RootEventBus eventBus;
        protected String destinationUrl;

        public GetLogoutUrlCaller(IGaeUserRpcAsync async, RootEventBus eventBus) {
            super(async, "IGaeUserRpc", "getGoogleAccountsLogoutUrl");
            this.eventBus = eventBus;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(String destinationUrl) {
            this.destinationUrl = destinationUrl;
        }

        @Override
        public void invokeRpcMethod(IGaeUserRpcAsync async, AsyncCallback<String> callback) {
            async.getGoogleAccountsLogoutUrl(this.destinationUrl, callback);
        }

        @Override
        public void onSuccessResult(String result) {
            this.eventBus.clearSession();  // clear it so it gets reloaded later
            WidgetUtils.getInstance().redirect(result);
        }
    }
}
