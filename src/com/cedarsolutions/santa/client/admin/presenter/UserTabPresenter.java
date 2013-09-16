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

import java.util.List;

import com.cedarsolutions.client.gwt.datasource.BackendDataSource;
import com.cedarsolutions.client.gwt.datasource.BackendDataUtils;
import com.cedarsolutions.client.gwt.datasource.IBackendDataPresenter;
import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.view.IUserTabView;
import com.cedarsolutions.santa.client.admin.view.UserTabView;
import com.cedarsolutions.santa.client.datasource.RegisteredUserDataSource;
import com.cedarsolutions.santa.client.rpc.IAdminRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for user tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = UserTabView.class)
public class UserTabPresenter
extends ModulePagePresenter<IUserTabView, AdminEventBus>
implements IBackendDataPresenter<RegisteredUser, RegisteredUserCriteria> {

    /** Admin RPC. */
    private IAdminRpcAsync adminRpc;

    /** Data source for our data. */
    private BackendDataSource<RegisteredUser, RegisteredUserCriteria> dataSource;

    /** Handle the start event so that the view gets bound. */
    public void onStart() {
    }

    /** Bind the user interface. */
    @Override
    public void bind() {
        BackendDataUtils.setStandardEventHandlers(this);
        this.view.setSelectedEventHandler(this.view.getRefreshEventHandler());
        this.view.setDeleteEventHandler(new DeleteHandler(this));
        this.view.setLockEventHandler(new LockHandler(this));
        this.view.setUnlockEventHandler(new UnlockHandler(this));
        this.view.setHistoryToken(this.getTokenGenerator().selectUserTab());
    }

    /**
     * Show the user search page, with criteria limited by user id.
     * @param userId  Id of the user to search for
     */
    public void onShowUserSearchById(String userId) {
        RegisteredUserCriteria criteria = this.getDefaultCriteria();
        criteria.setUserIds(userId);
        this.getView().setSearchCriteria(criteria);
        this.getView().showSearchCriteria();  // so the user can see that a filter is set
        this.getEventBus().selectUserTab();
    }

    /** Get the renderer view. */
    @Override
    public IBackendDataRenderer<RegisteredUser, RegisteredUserCriteria> getRenderer() {
        return this.getView();
    }

    /** Get default search criteria that view should use. */
    @Override
    public RegisteredUserCriteria getDefaultCriteria() {
        return new RegisteredUserCriteria();  // empty criteria is legal
    }

    /** Create a new instance of the correct data source. */
    @Override
    public BackendDataSource<RegisteredUser, RegisteredUserCriteria> createDataSource() {
        return new RegisteredUserDataSource(this.getView(), this.getAdminRpc());
    }

    /** Get the data source that is in use. */
    @Override
    public BackendDataSource<RegisteredUser, RegisteredUserCriteria> getDataSource() {
        return this.dataSource;
    }

    /** Set the data source that is in use. */
    @Override
    public void setDataSource(BackendDataSource<RegisteredUser, RegisteredUserCriteria> dataSource) {
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

    /** Delete handler. */
    protected static class DeleteHandler extends AbstractViewEventHandler<UserTabPresenter> {
        public DeleteHandler(UserTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            List<RegisteredUser> records = this.getParent().getView().getSelectedRecords();
            DeleteRegisteredUsersCaller caller = new DeleteRegisteredUsersCaller(this.getParent());
            caller.setMethodArguments(records);
            caller.invoke();
        }
    }

    /** Lock handler. */
    protected static class LockHandler extends AbstractViewEventHandler<UserTabPresenter> {
        public LockHandler(UserTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            List<RegisteredUser> records = this.getParent().getView().getSelectedRecords();
            LockRegisteredUsersCaller caller = new LockRegisteredUsersCaller(this.getParent());
            caller.setMethodArguments(records);
            caller.invoke();
        }
    }

    /** Unlock handler. */
    protected static class UnlockHandler extends AbstractViewEventHandler<UserTabPresenter> {
        public UnlockHandler(UserTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            List<RegisteredUser> records = this.getParent().getView().getSelectedRecords();
            UnlockRegisteredUsersCaller caller = new UnlockRegisteredUsersCaller(this.getParent());
            caller.setMethodArguments(records);
            caller.invoke();
        }
    }

    /** Caller for IAdminRpc.deleteRegisteredUsers(). */
    protected static class DeleteRegisteredUsersCaller extends StandardRpcCaller<IAdminRpcAsync, Void> {
        protected UserTabPresenter parent;
        protected List<RegisteredUser> records;

        public DeleteRegisteredUsersCaller(UserTabPresenter parent) {
            super(parent.getAdminRpc(), "IAdminRpc", "deleteRegisteredUsers");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(List<RegisteredUser> records) {
            this.records = records;
        }

        @Override
        public void invokeRpcMethod(IAdminRpcAsync async, AsyncCallback<Void> callback) {
            async.deleteRegisteredUsers(this.records, callback);
        }

        @Override
        public void onSuccessResult(Void result) {
            this.parent.getDataSource().clear();  // to refresh the display
        }
    }

    /** Caller for IAdminRpc.lockRegisteredUsers(). */
    protected static class LockRegisteredUsersCaller extends StandardRpcCaller<IAdminRpcAsync, Void> {
        protected UserTabPresenter parent;
        protected List<RegisteredUser> records;

        public LockRegisteredUsersCaller(UserTabPresenter parent) {
            super(parent.getAdminRpc(), "IAdminRpc", "lockRegisteredUsers");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(List<RegisteredUser> records) {
            this.records = records;
        }

        @Override
        public void invokeRpcMethod(IAdminRpcAsync async, AsyncCallback<Void> callback) {
            async.lockRegisteredUsers(this.records, callback);
        }

        @Override
        public void onSuccessResult(Void result) {
            this.parent.getDataSource().clear();  // to refresh the display
        }
    }

    /** Caller for IAdminRpc.unlockRegisteredUsers(). */
    protected static class UnlockRegisteredUsersCaller extends StandardRpcCaller<IAdminRpcAsync, Void> {
        protected UserTabPresenter parent;
        protected List<RegisteredUser> records;

        public UnlockRegisteredUsersCaller(UserTabPresenter parent) {
            super(parent.getAdminRpc(), "IAdminRpc", "unlockRegisteredUsers");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(List<RegisteredUser> records) {
            this.records = records;
        }

        @Override
        public void invokeRpcMethod(IAdminRpcAsync async, AsyncCallback<Void> callback) {
            async.unlockRegisteredUsers(this.records, callback);
        }

        @Override
        public void onSuccessResult(Void result) {
            this.parent.getDataSource().clear();  // to refresh the display
        }
    }
}
