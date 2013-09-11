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

import java.util.List;

import com.cedarsolutions.client.gwt.datasource.BackendDataSource;
import com.cedarsolutions.client.gwt.datasource.BackendDataUtils;
import com.cedarsolutions.client.gwt.datasource.IBackendDataPresenter;
import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.common.presenter.SystemStateInjector;
import com.cedarsolutions.santa.client.datasource.ExchangeDataSource;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView;
import com.cedarsolutions.santa.client.internal.view.IExchangeListTabView;
import com.cedarsolutions.santa.client.internal.view.InternalConstants;
import com.cedarsolutions.santa.client.rpc.IExchangeRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for exchange list tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = ExchangeListTabView.class)
public class ExchangeListTabPresenter
extends ModulePagePresenter<IExchangeListTabView, InternalEventBus>
implements IBackendDataPresenter<Exchange, ExchangeCriteria> {

    /** Injector for the shared client session singleton. */
    private SystemStateInjector systemStateInjector;

    /** Exchange RPC. */
    private IExchangeRpcAsync exchangeRpc;

    /** Data provider for our data. */
    private BackendDataSource<Exchange, ExchangeCriteria> dataSource;

    /** Handle the start event so that the view gets bound. */
    public void onStart() {
    }

    /** Bind the user interface. */
    @Override
    public void bind() {
        BackendDataUtils.setStandardEventHandlers(this);
        this.view.setSelectedEventHandler(this.view.getRefreshEventHandler());
        this.view.setDeleteHandler(new DeleteHandler(this));
        this.view.setCreateHandler(new CreateHandler(this));
        this.view.setEditSelectedRowHandler(new EditSelectedRowHandler(this));
    }

    /** Show the exchange list page. */
    public void onShowExchangeListPage() {
        // select it immediately, since the table shows its own loading indicator
        this.eventBus.selectExchangeListTab();
    }

    /** Get the renderer view. */
    @Override
    public IBackendDataRenderer<Exchange, ExchangeCriteria> getRenderer() {
        return this.getView();
    }

    /** Get default search criteria that view should use. */
    @Override
    public ExchangeCriteria getDefaultCriteria() {
        ExchangeCriteria criteria = new ExchangeCriteria();
        criteria.setUserId(this.getSession().getCurrentUser().getUserId());
        return criteria;
    }

    /** Create a new instance of the correct data source. */
    @Override
    public BackendDataSource<Exchange, ExchangeCriteria> createDataSource() {
        return new ExchangeDataSource(this.getView(), this.getExchangeRpc());
    }

    /** Get the data source that is in use. */
    @Override
    public BackendDataSource<Exchange, ExchangeCriteria> getDataSource() {
        return this.dataSource;
    }

    /** Set the data source that is in use. */
    @Override
    public void setDataSource(BackendDataSource<Exchange, ExchangeCriteria> dataSource) {
        this.dataSource = dataSource;
    }

    /** Get the session from the injector. */
    public ClientSession getSession() {
        return this.systemStateInjector.getSession();
    }

    /** Create handler. */
    protected static class CreateHandler extends AbstractViewEventHandler<ExchangeListTabPresenter> {
        public CreateHandler(ExchangeListTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            InternalConstants constants = GWT.create(InternalConstants.class);
            String name = constants.exchangeList_newExchangeName();
            CreateExchangeCaller caller = new CreateExchangeCaller(this.getParent());
            caller.setMethodArguments(name);
            caller.invoke();
        }
    }

    /** Delete handler. */
    protected static class DeleteHandler extends AbstractViewEventHandler<ExchangeListTabPresenter> {
        public DeleteHandler(ExchangeListTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            List<Exchange> records = this.getParent().getView().getSelectedRecords();
            DeleteExchangesCaller caller = new DeleteExchangesCaller(this.getParent());
            caller.setMethodArguments(records);
            caller.invoke();
        }
    }

    /** Edit selected row handler. */
    protected static class EditSelectedRowHandler extends AbstractViewEventHandlerWithContext<ExchangeListTabPresenter, Exchange> {
        public EditSelectedRowHandler(ExchangeListTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEventWithContext<Exchange> event) {
            Exchange row = event.getContext();
            if (row != null && row.getId() != null) {
                this.getParent().getEventBus().showEditExchangePage(row.getId());
            }
        }
    }

    /** Caller for IExchangeRpc.createExchange(). */
    protected static class CreateExchangeCaller extends StandardRpcCaller<IExchangeRpcAsync, Exchange> {
        protected ExchangeListTabPresenter parent;
        protected String name;

        public CreateExchangeCaller(ExchangeListTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "createExchange");
            this.parent = parent;
            this.markNotRetryable();  // it's NOT safe to retry this RPC call
        }

        public void setMethodArguments(String name) {
            this.name = name;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Exchange> callback) {
            async.createExchange(this.name, callback);
        }

        @Override
        public void onSuccessResult(Exchange result) {
            this.parent.getDataSource().clear();  // to refresh the display
        }
    }

    /** Caller for IExchangeRpc.deleteExchanges(). */
    protected static class DeleteExchangesCaller extends StandardRpcCaller<IExchangeRpcAsync, Void> {
        protected ExchangeListTabPresenter parent;
        protected List<Exchange> records;

        public DeleteExchangesCaller(ExchangeListTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "deleteExchanges");
            this.parent = parent;
            this.markNotRetryable();  // it's NOT safe to retry this RPC call
        }

        public void setMethodArguments(List<Exchange> records) {
            this.records = records;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Void> callback) {
            async.deleteExchanges(records, callback);
        }

        @Override
        public void onSuccessResult(Void result) {
            this.parent.getDataSource().clear();  // to refresh the display
        }
    }

    public SystemStateInjector getSystemStateInjector() {
        return this.systemStateInjector;
    }

    @Inject
    public void setSystemStateInjector(SystemStateInjector systemStateInjector) {
        this.systemStateInjector = systemStateInjector;
    }

    public IExchangeRpcAsync getExchangeRpc() {
        return this.exchangeRpc;
    }

    @Inject
    public void setExchangeRpc(IExchangeRpcAsync exchangeRpc) {
        this.exchangeRpc = exchangeRpc;
    }

}
