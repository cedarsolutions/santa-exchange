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
package com.cedarsolutions.santa.client.admin.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.datasource.BackendDataCriteriaResetHandler;
import com.cedarsolutions.client.gwt.datasource.BackendDataInitializeHandler;
import com.cedarsolutions.client.gwt.datasource.BackendDataRefreshHandler;
import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter.DeleteHandler;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter.DeleteRegisteredUsersCaller;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter.LockHandler;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter.LockRegisteredUsersCaller;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter.UnlockHandler;
import com.cedarsolutions.santa.client.admin.presenter.UserTabPresenter.UnlockRegisteredUsersCaller;
import com.cedarsolutions.santa.client.admin.view.IUserTabView;
import com.cedarsolutions.santa.client.datasource.RegisteredUserDataSource;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IAdminRpcAsync;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.event.BaseEventBus;

/**
 * Unit tests for UserTabPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class UserTabPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        UserTabPresenter presenter = createPresenter();
        presenter.onStart();  // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        UserTabPresenter presenter = createPresenter();
        when(presenter.getEventBus().selectUserTab()).thenReturn("token");
        presenter.bind();
        verify(presenter.getView()).setInitializationEventHandler(any(BackendDataInitializeHandler.class));
        verify(presenter.getView()).setRefreshEventHandler(any(BackendDataRefreshHandler.class));
        verify(presenter.getView()).setCriteriaResetEventHandler(any(BackendDataCriteriaResetHandler.class));
        verify(presenter.getView()).setSelectedEventHandler(any(BackendDataRefreshHandler.class));
        verify(presenter.getView()).setDeleteEventHandler(any(DeleteHandler.class));
        verify(presenter.getView()).setLockEventHandler(any(LockHandler.class));
        verify(presenter.getView()).setUnlockEventHandler(any(UnlockHandler.class));
        verify(presenter.getView()).setHistoryToken("token");
    }

    /** Test getDefaultCriteria(). */
    @Test public void testGetDefaultCriteria() {
        UserTabPresenter presenter = createPresenter();
        RegisteredUserCriteria criteria = presenter.getDefaultCriteria();
        assertNotNull(criteria);
        assertNull(criteria.getUserIds());
        assertNull(criteria.getUserNames());
        assertNull(criteria.getOpenIdProviders());
        assertNull(criteria.getAdmin());
        assertNull(criteria.getLocked());
        assertNull(criteria.getStartDate());
        assertNull(criteria.getEndDate());
        assertEquals(new RegisteredUserCriteria().getDefaultSortOrder(), criteria.getSortOrder());
        assertEquals(new RegisteredUserCriteria().getDefaultSortColumn(), criteria.getSortColumn());
    }

    /** Test data source methods. */
    @Test public void testDataSourceMethods() {
        UserTabPresenter presenter = createPresenter();

        assertNull(presenter.getDataSource());

        RegisteredUserDataSource dataSource = (RegisteredUserDataSource) presenter.createDataSource();
        assertNotNull(dataSource);
        assertSame(presenter.getRenderer(), dataSource.getRenderer());
        assertSame(presenter.getAdminRpc(), dataSource.getAdminRpc());

        presenter.setDataSource(dataSource);
        assertSame(dataSource, presenter.getDataSource());
    }

    /** Test onShowUserSearchById(). */
    @Test public void testOnShowUserSearchById() {
        UserTabPresenter presenter = createPresenter();
        ArgumentCaptor<RegisteredUserCriteria> criteria = ArgumentCaptor.forClass(RegisteredUserCriteria.class);

        presenter.onShowUserSearchById("userId");
        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus());
        order.verify(presenter.getView()).setSearchCriteria(criteria.capture());
        order.verify(presenter.getEventBus()).selectUserTab();

        assertEquals(1, criteria.getValue().getUserIds().size());
        assertEquals("userId", criteria.getValue().getUserIds().get(0));
        assertNull(criteria.getValue().getUserNames());
        assertNull(criteria.getValue().getOpenIdProviders());
        assertNull(criteria.getValue().getAdmin());
        assertNull(criteria.getValue().getLocked());
        assertNull(criteria.getValue().getStartDate());
        assertNull(criteria.getValue().getEndDate());
        assertEquals(new RegisteredUserCriteria().getDefaultSortOrder(), criteria.getValue().getSortOrder());
        assertEquals(new RegisteredUserCriteria().getDefaultSortColumn(), criteria.getValue().getSortColumn());
    }

    /** Test DeleteHandler. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test public void testDeleteHandler() {
        IAdminRpcAsync adminRpcProxy = mock(IAdminRpcAsync.class);
        IUserTabView view = mock(IUserTabView.class);
        UserTabPresenter presenter = mock(UserTabPresenter.class);
        when(presenter.getView()).thenReturn(view);
        when(presenter.getAdminRpc()).thenReturn(adminRpcProxy);

        DeleteHandler handler = new DeleteHandler(presenter);
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        when(presenter.getView().getSelectedRecords()).thenReturn(records);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        handler.handleEvent(null); // event does not matter
        verify(presenter.getAdminRpc()).deleteRegisteredUsers(captor.capture(), any(RpcCallback.class));
        assertSame(records, captor.getValue());
    }

    /** Test LockHandler. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test public void testLockHandler() {
        IAdminRpcAsync adminRpcProxy = mock(IAdminRpcAsync.class);
        IUserTabView view = mock(IUserTabView.class);
        UserTabPresenter presenter = mock(UserTabPresenter.class);
        when(presenter.getView()).thenReturn(view);
        when(presenter.getAdminRpc()).thenReturn(adminRpcProxy);

        LockHandler handler = new LockHandler(presenter);
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        when(presenter.getView().getSelectedRecords()).thenReturn(records);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        handler.handleEvent(null); // event does not matter
        verify(presenter.getAdminRpc()).lockRegisteredUsers(captor.capture(), any(RpcCallback.class));
        assertSame(records, captor.getValue());
    }

    /** Test UnlockHandler. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test public void testUnlockHandler() {
        IAdminRpcAsync adminRpcProxy = mock(IAdminRpcAsync.class);
        IUserTabView view = mock(IUserTabView.class);
        UserTabPresenter presenter = mock(UserTabPresenter.class);
        when(presenter.getView()).thenReturn(view);
        when(presenter.getAdminRpc()).thenReturn(adminRpcProxy);

        UnlockHandler handler = new UnlockHandler(presenter);
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        when(presenter.getView().getSelectedRecords()).thenReturn(records);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        handler.handleEvent(null); // event does not matter
        verify(presenter.getAdminRpc()).unlockRegisteredUsers(captor.capture(), any(RpcCallback.class));
        assertSame(records, captor.getValue());
    }

    /** Test DeleteRegisteredUsersCaller. */
    @SuppressWarnings("unchecked")
    @Test public void testDeleteRegisteredUsersCaller() {
        UserTabPresenter presenter = mock(UserTabPresenter.class, RETURNS_DEEP_STUBS);
        DeleteRegisteredUsersCaller caller = new DeleteRegisteredUsersCaller(presenter);
        assertNotNull(caller);
        assertEquals("IAdminRpc", caller.getRpc());
        assertEquals("deleteRegisteredUsers", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        caller.setMethodArguments(records);
        assertSame(records, caller.records);

        AsyncCallback<Void> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getAdminRpc()).deleteRegisteredUsers(records, callback);

        caller.onSuccessResult(null);
        verify(presenter.getDataSource()).clear();
    }

    /** Test LockRegisteredUsersCaller. */
    @SuppressWarnings("unchecked")
    @Test public void testLockRegisteredUsersCaller() {
        UserTabPresenter presenter = mock(UserTabPresenter.class, RETURNS_DEEP_STUBS);
        LockRegisteredUsersCaller caller = new LockRegisteredUsersCaller(presenter);
        assertNotNull(caller);
        assertEquals("IAdminRpc", caller.getRpc());
        assertEquals("lockRegisteredUsers", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        caller.setMethodArguments(records);
        assertSame(records, caller.records);

        AsyncCallback<Void> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getAdminRpc()).lockRegisteredUsers(records, callback);

        caller.onSuccessResult(null);
        verify(presenter.getDataSource()).clear();
    }

    /** Test UnlockRegisteredUsersCaller. */
    @SuppressWarnings("unchecked")
    @Test public void testUnlockRegisteredUsersCaller() {
        UserTabPresenter presenter = mock(UserTabPresenter.class, RETURNS_DEEP_STUBS);
        UnlockRegisteredUsersCaller caller = new UnlockRegisteredUsersCaller(presenter);
        assertNotNull(caller);
        assertEquals("IAdminRpc", caller.getRpc());
        assertEquals("unlockRegisteredUsers", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        caller.setMethodArguments(records);
        assertSame(records, caller.records);

        AsyncCallback<Void> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getAdminRpc()).unlockRegisteredUsers(records, callback);

        caller.onSuccessResult(null);
        verify(presenter.getDataSource()).clear();
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static UserTabPresenter createPresenter() {
        // Our mock needs to extend BaseEventBus and implement AdminEventBus, otherwise getTokenGenerator() doesn't work
        AdminEventBus eventBus = (AdminEventBus) mock(BaseEventBus.class, withSettings().extraInterfaces(AdminEventBus.class));

        IUserTabView view = mock(IUserTabView.class);
        IAdminRpcAsync adminRpc = mock(IAdminRpcAsync.class);

        UserTabPresenter presenter = new UserTabPresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);
        presenter.setAdminRpc(adminRpc);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());
        assertSame(view, presenter.getRenderer());
        assertSame(adminRpc, presenter.getAdminRpc());

        return presenter;
    }

}
