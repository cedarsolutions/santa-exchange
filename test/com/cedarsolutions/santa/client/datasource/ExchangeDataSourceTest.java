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
package com.cedarsolutions.santa.client.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.santa.client.datasource.ExchangeDataSource.GetExchangesCaller;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IExchangeRpcAsync;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;

/**
 * Unit tests for ExchangeDataSource.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeDataSourceTest extends StubbedClientTestCase {

    /** Test the constructor. */
    @SuppressWarnings("unchecked")
    @Test public void testConstructor() {
        IBackendDataRenderer<Exchange, ExchangeCriteria> renderer = mock(IBackendDataRenderer.class);
        IExchangeRpcAsync exchangeRpc = mock(IExchangeRpcAsync.class);
        ExchangeDataSource dataSource = new ExchangeDataSource(renderer, exchangeRpc);
        assertNotNull(dataSource);
        assertSame(renderer, dataSource.getRenderer());
        assertSame(exchangeRpc, dataSource.getExchangeRpc());
    }

    /** Test retrieveFromBackEnd(). */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveFromBackEnd() {
        IBackendDataRenderer<Exchange, ExchangeCriteria> renderer = mock(IBackendDataRenderer.class);
        IExchangeRpcAsync exchangeRpc = mock(IExchangeRpcAsync.class);
        ExchangeDataSource dataSource = new ExchangeDataSource(renderer, exchangeRpc);

        ArgumentCaptor<ExchangeCriteria> criteriaCaptor = ArgumentCaptor.forClass(ExchangeCriteria.class);
        ArgumentCaptor<Pagination> paginationCaptor = ArgumentCaptor.forClass(Pagination.class);

        ExchangeCriteria criteria = new ExchangeCriteria();
        Pagination pagination = mock(Pagination.class);
        when(renderer.getSearchCriteria()).thenReturn(criteria);
        dataSource.retrieveFromBackEnd(23, pagination);
        verify(exchangeRpc).getExchanges(criteriaCaptor.capture(), paginationCaptor.capture(), isA(RpcCallback.class));
        assertSame(criteria, criteriaCaptor.getValue());
        assertSame(pagination, paginationCaptor.getValue());
    }

    /** Test GetExchangesCallback(). */
    @Test public void testGetExchangesCallback() {
        ExchangeDataSource dataSource = mock(ExchangeDataSource.class, Mockito.RETURNS_DEEP_STUBS);
        GetExchangesCaller caller = new GetExchangesCaller(dataSource, 23);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("getExchanges", caller.getMethod());
        assertSame(dataSource, caller.getDataSource());
        assertEquals(23, caller.getStart());
    }

}
