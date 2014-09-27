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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.santa.client.datasource.RegisteredUserDataSource.GetRegisteredUsersCaller;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IAdminRpcAsync;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;

/**
 * Unit tests for RegisteredUserDataSource.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserDataSourceTest extends StubbedClientTestCase {

    /** Test the constructor. */
    @SuppressWarnings("unchecked")
    @Test public void testConstructor() {
        IBackendDataRenderer<RegisteredUser, RegisteredUserCriteria> renderer = mock(IBackendDataRenderer.class);
        IAdminRpcAsync adminRpc = mock(IAdminRpcAsync.class);
        RegisteredUserDataSource dataSource = new RegisteredUserDataSource(renderer, adminRpc);
        assertNotNull(dataSource);
        assertSame(renderer, dataSource.getRenderer());
        assertSame(adminRpc, dataSource.getAdminRpc());
    }

    /** Test retrieveFromBackEnd(). */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveFromBackEnd() {
        IBackendDataRenderer<RegisteredUser, RegisteredUserCriteria> renderer = mock(IBackendDataRenderer.class);
        IAdminRpcAsync adminRpc = mock(IAdminRpcAsync.class);
        RegisteredUserDataSource dataSource = new RegisteredUserDataSource(renderer, adminRpc);

        ArgumentCaptor<RegisteredUserCriteria> criteriaCaptor = ArgumentCaptor.forClass(RegisteredUserCriteria.class);
        ArgumentCaptor<Pagination> paginationCaptor = ArgumentCaptor.forClass(Pagination.class);

        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        Pagination pagination = mock(Pagination.class);
        when(renderer.getSearchCriteria()).thenReturn(criteria);
        dataSource.retrieveFromBackEnd(23, pagination);
        verify(adminRpc).getRegisteredUsers(criteriaCaptor.capture(), paginationCaptor.capture(), isA(RpcCallback.class));
        assertSame(criteria, criteriaCaptor.getValue());
        assertSame(pagination, paginationCaptor.getValue());
    }

    /** Test GetRegisteredUsersCaller(). */
    @Test public void testGetRegisteredUsersCallback() {
        RegisteredUserDataSource dataSource = mock(RegisteredUserDataSource.class);
        GetRegisteredUsersCaller caller = new GetRegisteredUsersCaller(dataSource, 23);
        assertEquals("IAdminRpc", caller.getRpc());
        assertEquals("getRegisteredUsers", caller.getMethod());
        assertSame(dataSource, caller.getDataSource());
        assertEquals(23, caller.getStart());
    }
}
