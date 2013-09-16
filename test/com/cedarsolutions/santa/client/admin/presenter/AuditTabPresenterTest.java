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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;

import com.cedarsolutions.client.gwt.datasource.BackendDataCriteriaResetHandler;
import com.cedarsolutions.client.gwt.datasource.BackendDataInitializeHandler;
import com.cedarsolutions.client.gwt.datasource.BackendDataRefreshHandler;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.presenter.AuditTabPresenter.UserIdSelectedHandler;
import com.cedarsolutions.santa.client.admin.view.IAuditTabView;
import com.cedarsolutions.santa.client.datasource.AuditEventDataSource;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IAdminRpcAsync;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.util.DateUtils;
import com.mvp4g.client.event.BaseEventBus;

/**
 * Unit tests for AuditTabPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditTabPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        AuditTabPresenter presenter = createPresenter();
        presenter.onStart();  // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        AuditTabPresenter presenter = createPresenter();
        when(presenter.getEventBus().selectAuditTab()).thenReturn("token");
        presenter.bind();
        verify(presenter.getView()).setInitializationEventHandler(any(BackendDataInitializeHandler.class));
        verify(presenter.getView()).setRefreshEventHandler(any(BackendDataRefreshHandler.class));
        verify(presenter.getView()).setCriteriaResetEventHandler(any(BackendDataCriteriaResetHandler.class));
        verify(presenter.getView()).setSelectedEventHandler(any(BackendDataRefreshHandler.class));
        verify(presenter.getView()).setUserIdSelectedHandler(any(UserIdSelectedHandler.class));
        verify(presenter.getView()).setHistoryToken("token");
    }

    /** Test getDefaultCriteria(). */
    @Test public void testGetDefaultCriteria() {
        AuditTabPresenter presenter = createPresenter();
        AuditEventCriteria criteria = presenter.getDefaultCriteria();
        assertNotNull(criteria);
        assertNull(criteria.getEventTypes());
        assertNull(criteria.getUserIds());
        assertEquals(DateUtils.getCurrentDateAtTime("00:00"), criteria.getStartDate());
        assertEquals(DateUtils.getCurrentDateAtTime("23:59"), criteria.getEndDate());
    }

    /** Test data source methods. */
    @Test public void testDataSourceMethods() {
        AuditTabPresenter presenter = createPresenter();

        assertNull(presenter.getDataSource());

        AuditEventDataSource dataSource = (AuditEventDataSource) presenter.createDataSource();
        assertNotNull(dataSource);
        assertSame(presenter.getRenderer(), dataSource.getRenderer());
        assertSame(presenter.getAdminRpc(), dataSource.getAdminRpc());

        presenter.setDataSource(dataSource);
        assertSame(dataSource, presenter.getDataSource());
    }

    /** Test UserIdSelectedHandler. */
    @Test public void testUserIdSelectedHandler() {
        AdminEventBus eventBus = mock(AdminEventBus.class);
        IAuditTabView view = mock(IAuditTabView.class);
        AuditTabPresenter presenter = mock(AuditTabPresenter.class);
        when(presenter.getView()).thenReturn(view);
        when(presenter.getEventBus()).thenReturn(eventBus);

        UserIdSelectedHandler handler = new UserIdSelectedHandler(presenter);
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());

        UnifiedEventWithContext<String> event = new UnifiedEventWithContext<String>(UnifiedEventType.CLICK_EVENT, "userId");
        handler.handleEvent(event); // event does not matter
        verify(presenter.getEventBus()).showUserSearchById("userId");
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static AuditTabPresenter createPresenter() {
        // Our mock needs to extend BaseEventBus and implement AdminEventBus, otherwise getTokenGenerator() doesn't work
        AdminEventBus eventBus = (AdminEventBus) mock(BaseEventBus.class, withSettings().extraInterfaces(AdminEventBus.class));

        IAuditTabView view = mock(IAuditTabView.class);
        IAdminRpcAsync adminRpc = mock(IAdminRpcAsync.class);

        AuditTabPresenter presenter = new AuditTabPresenter();
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
