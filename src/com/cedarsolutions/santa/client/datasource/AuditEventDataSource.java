/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2014 Kenneth J. Pronovici.
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
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Backend data source for AuditEvent data.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventDataSource extends BackendDataSource<AuditEvent, AuditEventCriteria> {

    /** Admin RPC. */
    private IAdminRpcAsync adminRpc;

    /** Create a data source. */
    public AuditEventDataSource(IBackendDataRenderer<AuditEvent, AuditEventCriteria> renderer, IAdminRpcAsync adminRpc) {
        super(renderer);
        this.adminRpc = adminRpc;
    }

    /** Get the Admin RPC. */
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
        GetAuditEventsCaller caller = new GetAuditEventsCaller(this, start);
        caller.setMethodArguments(this.getSearchCriteria(), pagination);
        caller.invoke();
    }

    /** Caller for IAdminRpc.getAuditEvents(). */
    protected static class GetAuditEventsCaller extends BackendDataRpcCaller<IAdminRpcAsync, AuditEvent, AuditEventCriteria> {
        private AuditEventCriteria criteria;
        private Pagination pagination;

        public GetAuditEventsCaller(AuditEventDataSource parent, int start) {
            super(parent.getAdminRpc(), "IAdminRpc", "getAuditEvents", parent, start);
        }

        public void setMethodArguments(AuditEventCriteria criteria, Pagination pagination) {
            this.criteria = criteria;
            this.pagination = pagination;
        }

        @Override
        public void invokeRpcMethod(IAdminRpcAsync async, AsyncCallback<PaginatedResults<AuditEvent>> callback) {
            async.getAuditEvents(this.criteria, this.pagination, callback);
        }
    }

}
