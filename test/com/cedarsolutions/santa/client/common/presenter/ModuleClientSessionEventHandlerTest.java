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
package com.cedarsolutions.santa.client.common.presenter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.santa.client.common.presenter.ModuleClientSessionEventHandler.EstablishClientSessionCaller;
import com.cedarsolutions.santa.client.common.presenter.ModuleClientSessionEventHandler.InvalidateClientSessionCaller;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IClientSessionRpcAsync;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.mvp4g.client.Mvp4gException;
import com.mvp4g.client.event.EventBus;
import com.mvp4g.client.event.EventBusWithLookup;
import com.mvp4g.client.event.EventFilter;
import com.mvp4g.client.event.EventHandlerInterface;
import com.mvp4g.client.history.HistoryProxy;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;


/**
 * Unit tests for ModuleClientSessionEventHandler.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ModuleClientSessionEventHandlerTest extends StubbedClientTestCase {

    /** Test onInitializeSession(). */
    @SuppressWarnings("unchecked")
    @Test public void testOnInitializeSession() {
        String eventType = "event";
        Object[] params = new Object[] { "hello", };

        ArgumentCaptor<Module> module = ArgumentCaptor.forClass(Module.class);
        ArgumentCaptor<String> logoutDestinationUrl = ArgumentCaptor.forClass(String.class);

        ConcreteModuleClientSessionEventHandler handler = createEventHandler();
        handler.onInitializeSession(eventType, params);

        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        verify(handler.getClientSessionRpc()).establishClientSession(module.capture(), logoutDestinationUrl.capture(), any(RpcCallback.class));
        assertEquals(Module.ROOT, module.getValue()); // defined in ConcreteModuleClientSessionEventHandler
    }

    /** Test onClearSession(). */
    @SuppressWarnings("unchecked")
    @Test public void testOnClearSession() {
        ArgumentCaptor<Module> module = ArgumentCaptor.forClass(Module.class);

        ConcreteModuleClientSessionEventHandler handler = createEventHandler();
        handler.onClearSession();

        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        verify(handler.getClientSessionRpc()).invalidateClientSession(module.capture(), any(RpcCallback.class));
        assertSame(handler.getModule(), module.getValue());
    }

    /** Test EstablishClientSessionCaller's basic functionality. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test public void testEstablishClientSessionCallerBasic() {
        ModuleClientSessionEventHandler eventHandler = createEventHandler();
        String eventType = null;
        Object[] params = null;

        EstablishClientSessionCaller caller = new EstablishClientSessionCaller(eventHandler, eventType, params);
        assertSame(eventHandler.getClientSessionRpc(), caller.getAsync());
        assertSame(eventHandler.getSession(), caller.session);
        assertSame(eventHandler.getEventBus(), caller.eventBus);
        assertEquals(eventType, caller.eventType);
        assertArrayEquals(params, caller.params);
        assertTrue(caller.isMarkedRetryable());

        caller.setMethodArguments(Module.ROOT, "destinationUrl");
        assertEquals(Module.ROOT, caller.module);

        caller.invoke();
        verify(caller.getAsync()).establishClientSession(eq(Module.ROOT), eq("destinationUrl"), any(RpcCallback.class));

        assertTrue(caller.isExceptionRetryable(null)); // any exception yields true

        Exception caught = new Exception("Hello");
        caller.onUnhandledError(caught);
        ArgumentCaptor<ErrorDescription> error = ArgumentCaptor.forClass(ErrorDescription.class);
        verify(WidgetUtils.getInstance()).showErrorPopup(error.capture());
        assertSame(caught, error.getValue().getException());
    }

    /** Test EstablishClientSessionCaller.onSuccessResult(), when there's no event to dispatch. */
    @SuppressWarnings("rawtypes")
    @Test public void testEstablishClientSessionCallerSuccess() {
        ModuleClientSessionEventHandler eventHandler = createEventHandler();
        String eventType = null;
        Object[] params = null;
        EstablishClientSessionCaller caller = new EstablishClientSessionCaller(eventHandler, eventType, params);

        ClientSession result = mock(ClientSession.class);
        caller.onSuccessResult(result);

        verify(caller.session).refresh(result);
        verify(caller.eventBus, never()).dispatch(anyString());
        verify(caller.eventBus, never()).dispatch(anyString(), any());
    }

    /** Test EstablishClientSessionCaller.onSuccessResult(), when there's an event type but no params. */
    @SuppressWarnings("rawtypes")
    @Test public void testEstablishClientSessionCallerNoParams() {
        ModuleClientSessionEventHandler eventHandler = createEventHandler();
        String eventType = "eventType";
        Object[] params = null;
        EstablishClientSessionCaller caller = new EstablishClientSessionCaller(eventHandler, eventType, params);

        ClientSession result = mock(ClientSession.class);
        caller.onSuccessResult(result);

        verify(caller.session).refresh(result);
        verify(caller.eventBus).dispatch(eventType);
        verify(caller.eventBus, never()).dispatch(anyString(), any());
    }

    /** Test EstablishClientSessionCaller.onSuccessResult(), when there's an event type and params. */
    @Test public void testEstablishClientSessionCallerWithParams() {
        @SuppressWarnings("rawtypes")
        ModuleClientSessionEventHandler eventHandler = createEventHandler();
        String eventType = "eventType";
        Object[] params = new Object[] { "Hello", };
        EstablishClientSessionCaller caller = new EstablishClientSessionCaller(eventHandler, eventType, params);

        ClientSession result = mock(ClientSession.class);
        caller.onSuccessResult(result);

        verify(caller.session).refresh(result);
        verify(caller.eventBus, never()).dispatch(anyString());
        verify(caller.eventBus).dispatch(eventType, params);
    }

    /** Test InvalidateClientSessionCaller(). */
    @SuppressWarnings("unchecked")
    @Test public void testInvalidateClientSessionCaller() {
        IClientSessionRpcAsync clientSessionRpc = mock(IClientSessionRpcAsync.class);
        ClientSession session = mock(ClientSession.class);
        InvalidateClientSessionCaller caller = new InvalidateClientSessionCaller(clientSessionRpc, session);
        assertSame(clientSessionRpc, caller.getAsync());
        assertSame(session, caller.session);

        caller.setMethodArguments(Module.ROOT);
        caller.invoke();
        verify(clientSessionRpc).invalidateClientSession(eq(Module.ROOT), any(RpcCallback.class));

        caller.onSuccessResult(null);
        verify(session).clear();

        reset(session);  // since both methods call clear()
        caller.onUnhandledError(null);  // passed-in value doesn't matter
        verify(session).clear();
    }

    /** Create a properly-mocked handler, including everything that needs to be injected. */
    private static ConcreteModuleClientSessionEventHandler createEventHandler() {
        StubbedEventBus eventBus = mock(StubbedEventBus.class);
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);
        IClientSessionRpcAsync clientSessionRpc = mock(IClientSessionRpcAsync.class);

        ConcreteModuleClientSessionEventHandler handler = new ConcreteModuleClientSessionEventHandler();
        handler.setEventBus(eventBus);
        handler.setSystemStateInjector(systemStateInjector);
        handler.setClientSessionRpc(clientSessionRpc);

        assertSame(eventBus, handler.getEventBus());
        assertSame(systemStateInjector, handler.getSystemStateInjector());
        assertSame(session, handler.getSession());
        assertSame(clientSessionRpc, handler.getClientSessionRpc());

        return handler;
    }

    /** Concrete class that we can use for testing. */
    private static class ConcreteModuleClientSessionEventHandler extends ModuleClientSessionEventHandler<StubbedEventBus> {
        @Override
        public Module getModule() {
            return Module.ROOT;  // just pick any module for this test
        }
    }

    /** Stubbed event bus that we can use for testing. */
    private static class StubbedEventBus implements EventBusWithLookup {

        @Override
        public void setHistoryStored(boolean historyStored) {
        }

        @Override
        public void setApplicationHistoryStored(boolean historyStored) {
        }

        @Override
        public void setHistoryStoredForNextOne(boolean historyStored) {
        }

        @Override
        public boolean isHistoryStored() {
            return false;
        }

        @Override
        public void setFilteringEnabled(boolean filteringEnabled) {
        }

        @Override
        public void setFilteringEnabledForNextOne(boolean filteringEnabled) {
        }

        @Override
        public boolean isFilteringEnabled() {
            return false;
        }

        @Override
        public <E extends EventBus, T extends EventHandlerInterface<E>> T addHandler(Class<T> handlerClass) throws Mvp4gException {
            return null;
        }

        @Override
        public <E extends EventBus, T extends EventHandlerInterface<E>> T addHandler(Class<T> handlerClass, boolean bind) throws Mvp4gException {
            return null;
        }

        @Override
        public <T extends EventHandlerInterface<?>> void removeHandler(T handler) {
        }

        @Override
        public void addEventFilter(EventFilter<? extends EventBus> filter) {
        }

        @Override
        public void removeEventFilter(EventFilter<? extends EventBus> filter) {
        }

        @Override
        public void setNavigationConfirmation(NavigationConfirmationInterface navigationConfirmation) {
        }

        @Override
        public void confirmNavigation(NavigationEventCommand event) {
        }

        @Override
        public HistoryProxy getHistory() {
            return null;
        }

        @Override
        public void setTokenGenerationModeForNextEvent() {
        }

        @Override
        public void dispatch(String eventName, Object... objects) {
        }

        @Override
        public void dispatch(String eventName) {
        }

        @Override
        public <E extends Enum<E>> void dispatch(Enum<E> enumEventName, Object... objects) {
        }

        @Override
        public <E extends Enum<E>> void dispatch(Enum<E> enumEventName) {
        }
    }

}
