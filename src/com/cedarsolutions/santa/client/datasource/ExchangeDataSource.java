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
import com.cedarsolutions.santa.client.rpc.IExchangeRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.BackendDataRpcCaller;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Backend data source for Exchange data.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeDataSource extends BackendDataSource<Exchange, ExchangeCriteria> {

    /** Exchange RPC. */
    private IExchangeRpcAsync exchangeRpc;

    /** Create a data source. */
    public ExchangeDataSource(IBackendDataRenderer<Exchange, ExchangeCriteria> renderer, IExchangeRpcAsync exchangeRpc) {
        super(renderer);
        this.exchangeRpc = exchangeRpc;
    }

    /** Get the exchange RPC. */
    public IExchangeRpcAsync getExchangeRpc() {
        return this.exchangeRpc;
    }

    /**
     * Asynchronously invoke the back end.
     * @param start       Starting index in the display
     * @param pagination  Pagination to use when fetching
     */
    @Override
    protected void retrieveFromBackEnd(int start, Pagination pagination) {
        GetExchangesCaller caller = new GetExchangesCaller(this, start);
        caller.setMethodArguments(this.getSearchCriteria(), pagination);
        caller.invoke();
    }

    /** Caller for IExchangeRpc.getExchanges(). */
    protected static class GetExchangesCaller extends BackendDataRpcCaller<IExchangeRpcAsync, Exchange, ExchangeCriteria> {
        private ExchangeCriteria criteria;
        private Pagination pagination;

        public GetExchangesCaller(ExchangeDataSource parent, int start) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "getExchanges", parent, start);
        }

        public void setMethodArguments(ExchangeCriteria criteria, Pagination pagination) {
            this.criteria = criteria;
            this.pagination = pagination;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<PaginatedResults<Exchange>> callback) {
            async.getExchanges(this.criteria, this.pagination, callback);
        }
    }
}
