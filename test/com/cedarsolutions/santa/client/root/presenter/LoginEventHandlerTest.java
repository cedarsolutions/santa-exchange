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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import com.cedarsolutions.client.gwt.rpc.IGaeUserRpcAsync;
import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.root.RootEventBus;
import com.cedarsolutions.santa.client.root.presenter.LoginEventHandler.GetLoginUrlCaller;
import com.cedarsolutions.santa.client.root.presenter.LoginEventHandler.GetLogoutUrlCaller;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for LoginEventHandler.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@SuppressWarnings("unchecked")
public class LoginEventHandlerTest extends StubbedClientTestCase {

    /** Test onShowLoginPageForToken(). */
    @Test public void testOnShowLoginPageForToken() {
        ArgumentCaptor<OpenIdProvider> openIdProvider = ArgumentCaptor.forClass(OpenIdProvider.class);
        ArgumentCaptor<String> destinationUrl = ArgumentCaptor.forClass(String.class);
        when(WidgetUtils.getInstance().getDestinationUrl("whatever")).thenReturn("http://whatever");

        LoginEventHandler handler = createEventHandler();
        handler.onShowLoginPageForToken(OpenIdProvider.GOOGLE, "whatever");

        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        verify(handler.getGaeUserRpc()).getLoginUrl(openIdProvider.capture(), destinationUrl.capture(), isA(RpcCallback.class));
        assertEquals(OpenIdProvider.GOOGLE, openIdProvider.getValue());
        assertEquals("http://whatever", destinationUrl.getValue());
    }

    /** Test onShowLoginPageForUrl(). */
    @Test public void testOnShowLoginPageForUrl() {
        ArgumentCaptor<OpenIdProvider> openIdProvider = ArgumentCaptor.forClass(OpenIdProvider.class);
        ArgumentCaptor<String> destinationUrl = ArgumentCaptor.forClass(String.class);

        LoginEventHandler handler = createEventHandler();
        handler.onShowLoginPageForUrl(OpenIdProvider.GOOGLE, "whatever");

        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        verify(handler.getGaeUserRpc()).getLoginUrl(openIdProvider.capture(), destinationUrl.capture(), isA(RpcCallback.class));
        assertEquals(OpenIdProvider.GOOGLE, openIdProvider.getValue());
        assertEquals("whatever", destinationUrl.getValue());
    }

    /** Test onLogout() when there is a logout URL. */
    @Test public void testOnLogoutWithLogoutUrl() {
        LoginEventHandler handler = createEventHandler();
        when(handler.getSession().getLogoutUrl()).thenReturn(null);
        handler.onLogout();
        verify(handler.getEventBus()).clearSession();
        verify(handler.getEventBus()).showLandingPage();
    }

    /** Test onLogout() when there is a logout URL. */
    @Test public void testOnLogoutWithoutLogoutUrl() {
        LoginEventHandler handler = createEventHandler();
        when(handler.getSession().getLogoutUrl()).thenReturn("http://whatever");
        handler.onLogout();
        verify(handler.getEventBus()).clearSession();
        verify(WidgetUtils.getInstance()).redirect("http://whatever");
    }

    /** Test onLockOutUser(). */
    @Test public void testOnLockOutUser() {
        ArgumentCaptor<String> destination = ArgumentCaptor.forClass(String.class);
        when(WidgetUtils.getInstance().getDestinationUrl(ACCOUNT_LOCKED_PAGE)).thenReturn("whatever");
        LoginEventHandler handler = createEventHandler();
        handler.onLockOutUser();
        verify(handler.getGaeUserRpc()).getLogoutUrl(destination.capture(), isA(RpcCallback.class));
        assertEquals("whatever", destination.getValue());
    }

    /** Test GetLoginUrlCallback. */
    @Test public void testGetLoginUrlCallback() {
        IGaeUserRpcAsync gaeUserRpcAsync = mock(IGaeUserRpcAsync.class);
        RootEventBus eventBus = mock(RootEventBus.class);
        GetLoginUrlCaller caller = new GetLoginUrlCaller(gaeUserRpcAsync, eventBus);
        assertSame(eventBus, caller.eventBus);
        assertTrue(caller.isMarkedRetryable());

        caller.setMethodArguments(OpenIdProvider.GOOGLE, "destinationUrl");
        assertEquals(OpenIdProvider.GOOGLE, caller.openIdProvider);
        assertEquals("destinationUrl", caller.destinationUrl);

        AsyncCallback<String> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(gaeUserRpcAsync).getLoginUrl(OpenIdProvider.GOOGLE, "destinationUrl", callback);

        caller.onSuccessResult("whatever");
        InOrder inOrder = inOrder(eventBus, WidgetUtils.getInstance());
        inOrder.verify(eventBus).clearSession();
        inOrder.verify(WidgetUtils.getInstance()).redirect("whatever");
    }

    /** Test GetLogoutUrlCallback. */
    @Test public void testGetLogoutUrlCallback() {
        IGaeUserRpcAsync gaeUserRpcAsync = mock(IGaeUserRpcAsync.class);
        RootEventBus eventBus = mock(RootEventBus.class);
        GetLogoutUrlCaller caller = new GetLogoutUrlCaller(gaeUserRpcAsync, eventBus);
        assertSame(eventBus, caller.eventBus);
        assertTrue(caller.isMarkedRetryable());

        caller.setMethodArguments("destinationUrl");
        assertEquals("destinationUrl", caller.destinationUrl);

        AsyncCallback<String> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(gaeUserRpcAsync).getLogoutUrl("destinationUrl", callback);

        caller.onSuccessResult("whatever");
        InOrder inOrder = inOrder(eventBus, WidgetUtils.getInstance());
        inOrder.verify(eventBus).clearSession();
        inOrder.verify(WidgetUtils.getInstance()).redirect("whatever");
    }

    /** Create a properly-mocked handler, including everything that needs to be injected. */
    private static LoginEventHandler createEventHandler() {
        RootEventBus eventBus = mock(RootEventBus.class);
        ClientSession session = mock(ClientSession.class);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);
        IGaeUserRpcAsync gaeUserRpc = mock(IGaeUserRpcAsync.class);

        LoginEventHandler handler = new LoginEventHandler();
        handler.setEventBus(eventBus);
        handler.setSystemStateInjector(systemStateInjector);
        handler.setGaeUserRpc(gaeUserRpc);

        assertSame(eventBus, handler.getEventBus());
        assertSame(systemStateInjector, handler.getSystemStateInjector());
        assertSame(session, handler.getSession());
        assertSame(gaeUserRpc, handler.getGaeUserRpc());

        return handler;
    }

}
