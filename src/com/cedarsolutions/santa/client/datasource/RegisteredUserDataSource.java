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
package com.cedarsolutions.santa.client.datasource;

import com.cedarsolutions.client.gwt.datasource.BackendDataSource;
import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.santa.client.rpc.IAdminRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.BackendDataRpcCaller;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Backend data source for RegisteredUser data.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserDataSource extends BackendDataSource<RegisteredUser, RegisteredUserCriteria> {

    /** Admin RPC. */
    private IAdminRpcAsync adminRpc;

    /** Create a data source. */
    public RegisteredUserDataSource(IBackendDataRenderer<RegisteredUser, RegisteredUserCriteria> renderer, IAdminRpcAsync adminRpc) {
        super(renderer);
        this.adminRpc = adminRpc;
    }

    /** Get the admin RPC. */
    public IAdminRpcAsync getAdminRpc() {
        return this.adminRpc;
    }

    /**
     * Asynchronously invoke the back end.
     * @param start       Starting index in the display
     * @param pagination  Pagination to use when fetching
     */
    @Override
    protected void retrieveFromBackEnd(int start, Pagination pagination) {
        GetRegisteredUsersCaller caller = new GetRegisteredUsersCaller(this, start);
        caller.setMethodArguments(this.getSearchCriteria(), pagination);
        caller.invoke();
    }

    /** Caller for IAdminRpc.getRegisteredUsers(). */
    protected static class GetRegisteredUsersCaller extends BackendDataRpcCaller<IAdminRpcAsync, RegisteredUser, RegisteredUserCriteria> {
        private RegisteredUserCriteria criteria;
        private Pagination pagination;

        public GetRegisteredUsersCaller(RegisteredUserDataSource parent, int start) {
            super(parent.getAdminRpc(), "IAdminRpc", "getRegisteredUsers", parent, start);
        }

        public void setMethodArguments(RegisteredUserCriteria criteria, Pagination pagination) {
            this.criteria = criteria;
            this.pagination = pagination;
        }

        @Override
        public void invokeRpcMethod(IAdminRpcAsync async, AsyncCallback<PaginatedResults<RegisteredUser>> callback) {
            async.getRegisteredUsers(this.criteria, pagination, callback);
        }
    }
}
