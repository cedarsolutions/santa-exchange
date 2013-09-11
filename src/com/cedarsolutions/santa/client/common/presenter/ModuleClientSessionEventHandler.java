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
package com.cedarsolutions.santa.client.common.presenter;

import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.rpc.IClientSessionRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.event.BaseEventHandler;
import com.mvp4g.client.event.EventBusWithLookup;


/**
 * Client session event handler for a module.
 *
 * <p>
 * This class handles events to initialize and clear the session in a standard
 * way.  All modules share the same cross-module client session as a singleton.
 * However, each module needs to handle the client session events independently
 * otherwise bookmarks don't work properly.
 * </p>
 *
 * <p>
 * It would be nice to pull this out of the application up into CedarCommon.
 * However, the implementation is just too tightly coupled to the application
 * (i.e. type of the client session, calls to WidgetUtils to get URLs and
 * display "please wait messages", etc.).  It would be possible to make a generic
 * class, but it wouldn't be that readable.  Better to just copy it into other
 * applications later.
 * </p>
 *
 * @param <E> Type of the event bus used by the event handler
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class ModuleClientSessionEventHandler<E extends EventBusWithLookup> extends BaseEventHandler<E> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Client session service. */
    private IClientSessionRpcAsync clientSessionRpc;

    /** Get the current module. */
    public abstract Module getModule();

    /** Initialize the current session.*/
    public void onInitializeSession(String eventType, Object[] params) {
        String logoutDestinationUrl = WidgetUtils.getInstance().getDestinationUrl(SantaExchangeEventTypes.LANDING_PAGE);
        EstablishClientSessionCaller caller = new EstablishClientSessionCaller(this, eventType, params);
        caller.setMethodArguments(this.getModule(), logoutDestinationUrl);
        caller.invoke();
    }

    /** Clear the current session. */
    public void onClearSession() {
        // Call down to the server and clear the session there.
        InvalidateClientSessionCaller caller = new InvalidateClientSessionCaller(this.clientSessionRpc, this.getSession());
        caller.setMethodArguments(this.getModule());
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

    public IClientSessionRpcAsync getClientSessionRpc() {
        return this.clientSessionRpc;
    }

    @Inject
    public void setClientSessionRpc(IClientSessionRpcAsync clientSessionRpc) {
        this.clientSessionRpc = clientSessionRpc;
    }

    /** Callback for IClientSessionRpc.establishClientSession(). */
    @SuppressWarnings("rawtypes")
    protected static class EstablishClientSessionCaller extends StandardRpcCaller<IClientSessionRpcAsync, ClientSession> {

        protected ModuleClientSessionEventHandler parent;
        protected Module module;
        protected String logoutDestinationUrl;
        protected ClientSession session;
        protected EventBusWithLookup eventBus;
        protected String eventType;
        protected Object[] params;

        public EstablishClientSessionCaller(ModuleClientSessionEventHandler parent, String eventType, Object[] params) {
            super(parent.getClientSessionRpc(), "IClientSessionRpc", "establishClientSession");
            this.parent = parent;
            this.session = this.parent.getSession();
            this.eventBus = (EventBusWithLookup) this.parent.getEventBus();
            this.eventType = eventType;
            this.params = params;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(Module module, String logoutDestinationUrl) {
            this.module = module;
            this.logoutDestinationUrl = logoutDestinationUrl;
        }

        @Override
        public void invokeRpcMethod(IClientSessionRpcAsync async, AsyncCallback<ClientSession> callback) {
            async.establishClientSession(this.module, this.logoutDestinationUrl, callback);
        }

        @Override
        public void onSuccessResult(ClientSession result) {
            this.session.refresh(result);
            if (this.eventType != null && this.params != null) {
                this.eventBus.dispatch(this.eventType, this.params);
            } else if (this.eventType != null) {
                this.eventBus.dispatch(this.eventType);
            }
        }

        @Override
        public boolean isExceptionRetryable(Throwable caught) {
            // This RPC appears to (sometimes?) fail the first time after we clear
            // the session.  So, we mark all RPC failures as retryable.
            return true;
        }
    }

    /** Caller for IClientSessionRpc.invalidateClientSession(). */
    protected static class InvalidateClientSessionCaller extends StandardRpcCaller<IClientSessionRpcAsync, Void> {
        protected ClientSession session;
        protected Module module;

        public InvalidateClientSessionCaller(IClientSessionRpcAsync clientSessionRpc, ClientSession session) {
            super(clientSessionRpc, "IClientSessionRpc", "invalidateClientSession");
            this.session = session;
            this.markNotRetryable();  // it's NOT safe to retry this RPC call
        }

        public void setMethodArguments(Module module) {
            this.module = module;
        }

        @Override
        public void invokeRpcMethod(IClientSessionRpcAsync async, AsyncCallback<Void> callback) {
            async.invalidateClientSession(this.module, callback);
        }

        @Override
        public void onSuccessResult(Void result) {
            this.session.clear();
        }

        @Override
        public void onUnhandledError(Throwable caught) {
            // This is expected to fail, because once we invalidate the
            // session, it's not possible to return a response to the client.
            // That's kind of ugly, but I can't figure out how to work around
            // it.  So, we're just going to ignore all failures.  There's not
            // anything we can do if it fails.
            this.session.clear();
        }
    }
}
