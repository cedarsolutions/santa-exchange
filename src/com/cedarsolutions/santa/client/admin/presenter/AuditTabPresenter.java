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
package com.cedarsolutions.santa.client.admin.presenter;

import com.cedarsolutions.client.gwt.datasource.BackendDataSource;
import com.cedarsolutions.client.gwt.datasource.BackendDataUtils;
import com.cedarsolutions.client.gwt.datasource.IBackendDataPresenter;
import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.view.AuditTabView;
import com.cedarsolutions.santa.client.admin.view.IAuditTabView;
import com.cedarsolutions.santa.client.datasource.AuditEventDataSource;
import com.cedarsolutions.santa.client.rpc.IAdminRpcAsync;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for audit tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = AuditTabView.class)
public class AuditTabPresenter extends ModulePagePresenter<IAuditTabView, AdminEventBus>
implements IBackendDataPresenter<AuditEvent, AuditEventCriteria> {

    /** Admin RPC. */
    private IAdminRpcAsync adminRpc;

    /** Data source for our data. */
    private BackendDataSource<AuditEvent, AuditEventCriteria> dataSource;

    /** Handle the start event so that the view gets bound. */
    public void onStart() {
    }

    /** Bind the user interface. */
    @Override
    public void bind() {
        BackendDataUtils.setStandardEventHandlers(this);
        this.view.setSelectedEventHandler(this.view.getRefreshEventHandler());
        this.view.setUserIdSelectedHandler(new UserIdSelectedHandler(this));
        this.view.setHistoryToken(this.getTokenGenerator().selectAuditTab());
    }

    /** Get the renderer view. */
    @Override
    public IBackendDataRenderer<AuditEvent, AuditEventCriteria> getRenderer() {
        return this.getView();
    }

    /** Get default search criteria that view should use. */
    @Override
    public AuditEventCriteria getDefaultCriteria() {
        AuditEventCriteria criteria = new AuditEventCriteria();

        // The back-end requires us to set at least one of the dates.
        // By default, run the query for the current day, and let the
        // user change it if they want to.
        criteria.setStartDate(GwtDateUtils.getCurrentDateAtTime("00:00"));
        criteria.setEndDate(GwtDateUtils.getCurrentDateAtTime("23:59"));

        return criteria;
    }

    /** Create a new instance of the correct data source. */
    @Override
    public BackendDataSource<AuditEvent, AuditEventCriteria> createDataSource() {
        return new AuditEventDataSource(this.getView(), this.getAdminRpc());
    }

    /** Get the data source that is in use. */
    @Override
    public BackendDataSource<AuditEvent, AuditEventCriteria> getDataSource() {
        return this.dataSource;
    }

    /** Set the data source that is in use. */
    @Override
    public void setDataSource(BackendDataSource<AuditEvent, AuditEventCriteria> dataSource) {
        this.dataSource = dataSource;
    }

    /** Get adminRpc. */
    public IAdminRpcAsync getAdminRpc() {
        return this.adminRpc;
    }

    /** Set adminRpc. */
    @Inject
    public void setAdminRpc(IAdminRpcAsync adminRpc) {
        this.adminRpc = adminRpc;
    }

    /** User id selected handler. */
    protected static class UserIdSelectedHandler extends AbstractViewEventHandlerWithContext<AuditTabPresenter, String> {
        public UserIdSelectedHandler(AuditTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEventWithContext<String> event) {
            this.getParent().getEventBus().showUserSearchById(event.getContext());
        }
    }

}
