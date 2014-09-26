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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.presenter.RpcTestPagePresenter.RpcInvocationHandler;
import com.cedarsolutions.santa.client.external.presenter.RpcTestPagePresenter.TestRpcCaller;
import com.cedarsolutions.santa.client.external.view.IRpcTestPageView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.ITestRpcAsync;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for RpcTestPagePresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RpcTestPagePresenterTest extends StubbedClientTestCase {

    /** Test onShowRpcTestPage(). */
    @SuppressWarnings("unchecked")
    @Test public void testOnShowRpcTestPage() {
        ArgumentCaptor<RpcInvocationHandler> unprotectedRpc = ArgumentCaptor.forClass(RpcInvocationHandler.class);
        ArgumentCaptor<RpcInvocationHandler> userRpc = ArgumentCaptor.forClass(RpcInvocationHandler.class);
        ArgumentCaptor<RpcInvocationHandler> adminRpc = ArgumentCaptor.forClass(RpcInvocationHandler.class);
        ArgumentCaptor<RpcInvocationHandler> enabledUserRpc = ArgumentCaptor.forClass(RpcInvocationHandler.class);
        ArgumentCaptor<RpcInvocationHandler> enabledAdminRpc = ArgumentCaptor.forClass(RpcInvocationHandler.class);

        RpcTestPagePresenter presenter = createPresenter();
        presenter.onShowRpcTestPage();
        verify(presenter.getView()).setExpectedResults(isA(Map.class));
        verify(presenter.getView()).setUnprotectedRpcHandler(unprotectedRpc.capture());
        verify(presenter.getView()).setUserRpcHandler(userRpc.capture());
        verify(presenter.getView()).setAdminRpcHandler(adminRpc.capture());
        verify(presenter.getView()).setEnabledUserRpcHandler(enabledUserRpc.capture());
        verify(presenter.getView()).setEnabledAdminRpcHandler(enabledAdminRpc.capture());

        assertSame(presenter, unprotectedRpc.getValue().getParent());
        assertEquals("unprotectedRpc", unprotectedRpc.getValue().getMethod());

        assertSame(presenter, userRpc.getValue().getParent());
        assertEquals("userRpc", userRpc.getValue().getMethod());

        assertSame(presenter, adminRpc.getValue().getParent());
        assertEquals("adminRpc", adminRpc.getValue().getMethod());

        assertSame(presenter, enabledUserRpc.getValue().getParent());
        assertEquals("enabledUserRpc", enabledUserRpc.getValue().getMethod());

        assertSame(presenter, enabledAdminRpc.getValue().getParent());
        assertEquals("enabledAdminRpc", enabledAdminRpc.getValue().getMethod());
    }

    /** Test getExpectedResults(), not logged in. */
    @Test public void testGetExpectedResultsNotLoggedIn() {
        RpcTestPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(false);  // logged in is ROLE_USER
        Map<String, String> expectedResults = presenter.getExpectedResults();
        assertEquals("Allowed", expectedResults.get("unprotectedRpc"));
        assertEquals("Not Allowed", expectedResults.get("userRpc"));
        assertEquals("Not Allowed", expectedResults.get("adminRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledUserRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledAdminRpc"));
    }

    /** Test getExpectedResults(), logged in as locked normal user. */
    @Test public void testGetExpectedResultsLockedUser() {
        RpcTestPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(true);  // logged in is ROLE_USER
        when(presenter.getSession().isAdmin()).thenReturn(false);
        when(presenter.getSession().isLocked()).thenReturn(true);    // !locked is ROLE_ENABLED
        Map<String, String> expectedResults = presenter.getExpectedResults();
        assertEquals("Allowed", expectedResults.get("unprotectedRpc"));
        assertEquals("Allowed", expectedResults.get("userRpc"));
        assertEquals("Not Allowed", expectedResults.get("adminRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledUserRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledAdminRpc"));
    }

    /** Test getExpectedResults(), logged in as normal user. */
    @Test public void testGetExpectedResultsUser() {
        RpcTestPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(true);  // logged in is ROLE_USER
        when(presenter.getSession().isAdmin()).thenReturn(false);    // admin is ROLE_ADMIN
        when(presenter.getSession().isLocked()).thenReturn(false);   // !locked is ROLE_ENABLED
        Map<String, String> expectedResults = presenter.getExpectedResults();
        assertEquals("Allowed", expectedResults.get("unprotectedRpc"));
        assertEquals("Allowed", expectedResults.get("userRpc"));
        assertEquals("Not Allowed", expectedResults.get("adminRpc"));
        assertEquals("Allowed", expectedResults.get("enabledUserRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledAdminRpc"));
    }

    /** Test getExpectedResults(), logged in as locked admin user. */
    @Test public void testGetExpectedResultsLockedAdmin() {
        RpcTestPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(false);  // logged in is ROLE_USER
        when(presenter.getSession().isAdmin()).thenReturn(true);      // admin is ROLE_ADMIN
        when(presenter.getSession().isLocked()).thenReturn(true);     // !locked is ROLE_ENABLED
        Map<String, String> expectedResults = presenter.getExpectedResults();
        assertEquals("Allowed", expectedResults.get("unprotectedRpc"));
        assertEquals("Not Allowed", expectedResults.get("userRpc"));
        assertEquals("Allowed", expectedResults.get("adminRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledUserRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledAdminRpc"));
    }

    /** Test getExpectedResults(), logged in as admin user. */
    @Test public void testGetExpectedResultsAdmin() {
        RpcTestPagePresenter presenter = createPresenter();
        when(presenter.getSession().isLoggedIn()).thenReturn(false);  // logged in is ROLE_USER
        when(presenter.getSession().isAdmin()).thenReturn(true);      // admin is ROLE_ADMIN
        when(presenter.getSession().isLocked()).thenReturn(false);    // !locked is ROLE_ENABLED
        Map<String, String> expectedResults = presenter.getExpectedResults();
        assertEquals("Allowed", expectedResults.get("unprotectedRpc"));
        assertEquals("Not Allowed", expectedResults.get("userRpc"));
        assertEquals("Allowed", expectedResults.get("adminRpc"));
        assertEquals("Not Allowed", expectedResults.get("enabledUserRpc"));
        assertEquals("Allowed", expectedResults.get("enabledAdminRpc"));
    }

    /** Test RpcInvocationHandler.*/
    @SuppressWarnings("unchecked")
    @Test public void testRpcInvocationHandler() {
        RpcTestPagePresenter presenter = createPresenter();

        RpcInvocationHandler handler = new RpcInvocationHandler(presenter, "unprotectedRpc");
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());
        assertEquals("unprotectedRpc", handler.getMethod());

        handler.handleEvent(null); // event doesn't matter
        verify(presenter.getTestRpc()).unprotectedRpc(eq("value"), isA(RpcCallback.class)); // spot-check
    }

    /** Test TestRpcCaller. */
    @SuppressWarnings("unchecked")
    @Test public void testTestRpcCaller() {
        RpcTestPagePresenter presenter = null;
        TestRpcCaller caller = null;
        AsyncCallback<String> callback = null;

        presenter = createPresenter();
        caller = new TestRpcCaller(presenter, "unprotectedRpc");
        callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getTestRpc()).unprotectedRpc("value", callback);
        caller.onSuccessResult("hello");
        verify(presenter.getView()).setActualResult("unprotectedRpc", "hello");

        presenter = createPresenter();
        caller = new TestRpcCaller(presenter, "userRpc");
        callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getTestRpc()).userRpc("value", callback);
        caller.onSuccessResult("hello");
        verify(presenter.getView()).setActualResult("userRpc", "hello");

        presenter = createPresenter();
        caller = new TestRpcCaller(presenter, "adminRpc");
        callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getTestRpc()).adminRpc("value", callback);
        caller.onSuccessResult("hello");
        verify(presenter.getView()).setActualResult("adminRpc", "hello");

        presenter = createPresenter();
        caller = new TestRpcCaller(presenter, "enabledUserRpc");
        callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getTestRpc()).enabledUserRpc("value", callback);
        caller.onSuccessResult("hello");
        verify(presenter.getView()).setActualResult("enabledUserRpc", "hello");

        presenter = createPresenter();
        caller = new TestRpcCaller(presenter, "enabledAdminRpc");
        callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getTestRpc()).enabledAdminRpc("value", callback);
        caller.onSuccessResult("hello");
        verify(presenter.getView()).setActualResult("enabledAdminRpc", "hello");
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static RpcTestPagePresenter createPresenter() {
        ITestRpcAsync testRpc = mock(ITestRpcAsync.class);
        ExternalEventBus eventBus = mock(ExternalEventBus.class);
        IRpcTestPageView view = mock(IRpcTestPageView.class);
        ClientSession session = mock(ClientSession.class, RETURNS_DEEP_STUBS);  // so call1().call2().call3() works
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);

        RpcTestPagePresenter presenter = new RpcTestPagePresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);
        presenter.setSystemStateInjector(systemStateInjector);
        presenter.setTestRpc(testRpc);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());
        assertSame(systemStateInjector, presenter.getSystemStateInjector());
        assertSame(session, presenter.getSession());
        assertSame(testRpc, presenter.getTestRpc());

        return presenter;
    }

}
