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
package com.cedarsolutions.santa.client.internal.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.datasource.BackendDataCriteriaResetHandler;
import com.cedarsolutions.client.gwt.datasource.BackendDataInitializeHandler;
import com.cedarsolutions.client.gwt.datasource.BackendDataRefreshHandler;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.datasource.ExchangeDataSource;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeListTabPresenter.CreateExchangeCaller;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeListTabPresenter.CreateHandler;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeListTabPresenter.DeleteExchangesCaller;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeListTabPresenter.DeleteHandler;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeListTabPresenter.EditSelectedRowHandler;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView;
import com.cedarsolutions.santa.client.internal.view.IExchangeListTabView;
import com.cedarsolutions.santa.client.internal.view.InternalConstants;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IExchangeRpcAsync;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for ExchangeListTabPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeListTabPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        ExchangeListTabPresenter presenter = createPresenter();
        presenter.onStart();  // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        ExchangeListTabPresenter presenter = createPresenter();
        presenter.bind();
        verify(presenter.getView()).setInitializationEventHandler(any(BackendDataInitializeHandler.class));
        verify(presenter.getView()).setRefreshEventHandler(any(BackendDataRefreshHandler.class));
        verify(presenter.getView()).setCriteriaResetEventHandler(any(BackendDataCriteriaResetHandler.class));
        verify(presenter.getView()).setSelectedEventHandler(any(BackendDataRefreshHandler.class));
        verify(presenter.getView()).setDeleteHandler(any(DeleteHandler.class));
        verify(presenter.getView()).setCreateHandler(any(CreateHandler.class));
        verify(presenter.getView()).setEditSelectedRowHandler(any(EditSelectedRowHandler.class));
    }

    /** Test onShowExchangeListPage(). */
    @Test public void testOnShowExchangeListPage() {
        ExchangeListTabPresenter presenter = createPresenter();
        presenter.onShowExchangeListPage();
        verify(presenter.getEventBus()).selectExchangeListTab();
    }

    /** Test getDefaultCriteria(). */
    @Test public void testGetDefaultCriteria() {
        ExchangeListTabPresenter presenter = createPresenter();
        when(presenter.getSession().getCurrentUser().getUserId()).thenReturn("user");
        ExchangeCriteria criteria = presenter.getDefaultCriteria();
        assertNotNull(criteria);
        assertEquals("user", criteria.getUserId());
        assertNull(criteria.getExchangeIds());
    }

    /** Test data source methods. */
    @Test public void testDataSourceMethods() {
        ExchangeListTabPresenter presenter = createPresenter();

        assertNull(presenter.getDataSource());

        ExchangeDataSource dataSource = (ExchangeDataSource) presenter.createDataSource();
        assertNotNull(dataSource);
        assertSame(presenter.getRenderer(), dataSource.getRenderer());
        assertSame(presenter.getExchangeRpc(), dataSource.getExchangeRpc());

        presenter.setDataSource(dataSource);
        assertSame(dataSource, presenter.getDataSource());
    }

    /** Test CreateHandler. */
    @SuppressWarnings("unchecked")
    @Test public void testCreateHandler() {
        IExchangeRpcAsync exchangeRpc = mock(IExchangeRpcAsync.class);
        ExchangeListTabView view = mock(ExchangeListTabView.class);
        ExchangeListTabPresenter presenter = mock(ExchangeListTabPresenter.class);
        when(presenter.getView()).thenReturn(view);
        when(presenter.getExchangeRpc()).thenReturn(exchangeRpc);

        CreateHandler handler = new CreateHandler(presenter);
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());

        InternalConstants constants = GWT.create(InternalConstants.class);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        handler.handleEvent(null); // event does not matter
        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        verify(presenter.getExchangeRpc()).createExchange(captor.capture(), any(RpcCallback.class));
        assertEquals(constants.exchangeList_newExchangeName(), captor.getValue());
    }

    /** Test DeleteHandler. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test public void testDeleteHandler() {
        IExchangeRpcAsync exchangeRpc = mock(IExchangeRpcAsync.class);
        ExchangeListTabView view = mock(ExchangeListTabView.class);
        ExchangeListTabPresenter presenter = mock(ExchangeListTabPresenter.class);
        when(presenter.getView()).thenReturn(view);
        when(presenter.getExchangeRpc()).thenReturn(exchangeRpc);

        DeleteHandler handler = new DeleteHandler(presenter);
        assertNotNull(handler);
        assertSame(presenter, handler.getParent());

        List<Exchange> records = new ArrayList<Exchange>();
        when(presenter.getView().getSelectedRecords()).thenReturn(records);

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);

        handler.handleEvent(null); // event does not matter
        verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        verify(presenter.getExchangeRpc()).deleteExchanges(captor.capture(), any(RpcCallback.class));
        assertSame(records, captor.getValue());
    }

    /** Test EditSelectedRowHandler. */
    @SuppressWarnings("unchecked")
    @Test public void testEditSelectedRowHandler() {
        InternalEventBus eventBus = mock(InternalEventBus.class);
        ExchangeListTabPresenter presenter = mock(ExchangeListTabPresenter.class);
        when(presenter.getEventBus()).thenReturn(eventBus);

        EditSelectedRowHandler handler = new EditSelectedRowHandler(presenter);

        UnifiedEventWithContext<Exchange> row = mock(UnifiedEventWithContext.class);

        when(row.getContext()).thenReturn(null);
        handler.handleEvent(row);
        verifyNoMoreInteractions(eventBus);

        Exchange exchange = new Exchange();
        exchange.setId(null);
        handler.handleEvent(row);
        verifyNoMoreInteractions(eventBus);

        exchange.setId(12L);
        when(row.getContext()).thenReturn(exchange);
        handler.handleEvent(row);
        verify(presenter.getEventBus()).showEditExchangePage(new Long(12L));
    }

    /** Test CreateExchangeCallback. */
    @SuppressWarnings("unchecked")
    @Test public void testCreateExchangeCallback() {
        ExchangeListTabPresenter presenter = mock(ExchangeListTabPresenter.class, Mockito.RETURNS_DEEP_STUBS);
        CreateExchangeCaller caller = new CreateExchangeCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("createExchange", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertFalse(caller.isMarkedRetryable());

        caller.setMethodArguments("exchange");
        assertSame("exchange", caller.name);

        AsyncCallback<Exchange> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).createExchange("exchange", callback);

        caller.onSuccessResult(null);
        verify(presenter.getDataSource()).clear();
    }

    /** Test DeleteExchangesCallback. */
    @SuppressWarnings("unchecked")
    @Test public void testDeleteExchangesCallback() {
        ExchangeListTabPresenter presenter = mock(ExchangeListTabPresenter.class, Mockito.RETURNS_DEEP_STUBS);
        DeleteExchangesCaller caller = new DeleteExchangesCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("deleteExchanges", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertFalse(caller.isMarkedRetryable());

        List<Exchange> records = new ArrayList<Exchange>();
        caller.setMethodArguments(records);
        assertSame(records, caller.records);

        AsyncCallback<Void> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).deleteExchanges(records, callback);

        caller.onSuccessResult(null);
        verify(presenter.getDataSource()).clear();
    }

    /** Create a properly-mocked presenter, including everything that needs to be injected. */
    private static ExchangeListTabPresenter createPresenter() {
        InternalEventBus eventBus = mock(InternalEventBus.class);
        IExchangeListTabView view = mock(IExchangeListTabView.class);
        ClientSession session = mock(ClientSession.class, Mockito.RETURNS_DEEP_STUBS);
        SystemStateInjector systemStateInjector = mock(SystemStateInjector.class);
        when(systemStateInjector.getSession()).thenReturn(session);
        IExchangeRpcAsync exchangeRpc = mock(IExchangeRpcAsync.class);

        ViewEventHandler initializationHander = mock(ViewEventHandler.class);
        when(view.getInitializationEventHandler()).thenReturn(initializationHander);

        ViewEventHandler refreshHander = mock(ViewEventHandler.class);
        when(view.getRefreshEventHandler()).thenReturn(refreshHander);

        ExchangeListTabPresenter presenter = new ExchangeListTabPresenter();
        presenter.setEventBus(eventBus);
        presenter.setView(view);
        presenter.setSystemStateInjector(systemStateInjector);
        presenter.setExchangeRpc(exchangeRpc);

        assertSame(eventBus, presenter.getEventBus());
        assertSame(view, presenter.getView());
        assertSame(systemStateInjector, presenter.getSystemStateInjector());
        assertSame(session, presenter.getSession());
        assertSame(exchangeRpc, presenter.getExchangeRpc());

        return presenter;
    }

}
