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
package com.cedarsolutions.santa.client.rpc.util;

import com.cedarsolutions.client.gwt.datasource.BackendDataSource;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.exception.InvalidDataException;

/**
 * Standard RPC caller for use with BackendDataSource.
 * @param <A> Type of the asynchronous RPC
 * @param <T> Type of the backend data
 * @param <C> Type of the search criteria
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class BackendDataRpcCaller<A, T, C> extends StandardRpcCaller<A, PaginatedResults<T>> {

    /** Data source associated with this callback. */
    private BackendDataSource<T, C> dataSource;

    /** Starting index in the display. */
    private int start;

    /**
     * Create an RPC caller, not retryable by default.
     * @param async      The asynchronous RPC
     * @param rpc        Name of the RPC that is being invoked, like "IClientSessionRpc"
     * @param method     Name of the method that is being invoked, like "establishClientSession"
     * @param dataSource Backend data source
     * @param start      Starting index on the display, where results should be placed
     */
    protected BackendDataRpcCaller(A async, String rpc, String method, BackendDataSource<T, C> dataSource, int start) {
        super(async, rpc, method);
        this.dataSource = dataSource;
        this.start = start;
        this.markRetryable();  // it's safe to retry this sort of RPC call, since it's just loading data
    }

    /** Invoked when an asynchronous call completes successfully. */
    @Override
    public void onSuccessResult(PaginatedResults<T> results) {
        this.dataSource.markRetrieveComplete();  // VERY important to make sure this is called
        this.dataSource.applyResults(this.start, results);
    }

    /** Invoked when an asynchronous call results in an unhandled error. */
    @Override
    public void onUnhandledError(Throwable caught) {
        this.dataSource.markRetrieveComplete();  // VERY important to make sure this is called
        super.onUnhandledError(caught);
    }

    /** Invoked when an asynchronous call results in a validation error. */
    @Override
    public boolean onValidationError(InvalidDataException caught) {
        this.dataSource.markRetrieveComplete();  // VERY important to make sure this is called
        this.getDataSource().getRenderer().showValidationError(caught);
        return true;  // indicate that we've handled the validation error
    }

    /** Get the back-end data source. */
    public BackendDataSource<T, C> getDataSource() {
        return this.dataSource;
    }

    /** Get the configured starting index. */
    public int getStart() {
        return this.start;
    }

}
