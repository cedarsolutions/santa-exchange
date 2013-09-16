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
package com.cedarsolutions.santa.server.rpc.impl;

import static com.cedarsolutions.junit.util.Assertions.assertOnlyMessage;
import static com.cedarsolutions.junit.util.Assertions.assertSummary;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.NULL;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.server.dao.IRegisteredUserDao;
import com.cedarsolutions.santa.server.service.IAuditEventService;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for AdminRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminRpcTest {

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        AdminRpc rpc = new AdminRpc();
        assertNull(rpc.getAuditEventService());
        assertNull(rpc.getAuditEventDao());
        assertNull(rpc.getRegisteredUserDao());

        IAuditEventService registeredUserService = mock(IAuditEventService.class);
        rpc.setAuditEventService(registeredUserService);
        assertSame(registeredUserService, rpc.getAuditEventService());

        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        rpc.setAuditEventDao(auditEventDao);
        assertSame(auditEventDao, rpc.getAuditEventDao());

        IRegisteredUserDao registeredUserDao = mock(IRegisteredUserDao.class);
        rpc.setRegisteredUserDao(registeredUserDao);
        assertSame(registeredUserDao, rpc.getRegisteredUserDao());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        AdminRpc rpc = new AdminRpc();
        IAuditEventService auditEventService = mock(IAuditEventService.class);
        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        IRegisteredUserDao registeredUserDao = mock(IRegisteredUserDao.class);

        rpc.setAuditEventService(auditEventService);
        rpc.setAuditEventDao(auditEventDao);
        rpc.setRegisteredUserDao(registeredUserDao);
        rpc.afterPropertiesSet();

        try {
            rpc.setAuditEventService(null);
            rpc.setAuditEventDao(auditEventDao);
            rpc.setRegisteredUserDao(registeredUserDao);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            rpc.setAuditEventService(auditEventService);
            rpc.setAuditEventDao(null);
            rpc.setRegisteredUserDao(registeredUserDao);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            rpc.setAuditEventService(auditEventService);
            rpc.setAuditEventDao(auditEventDao);
            rpc.setRegisteredUserDao(null);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test getAuditEvents(). */
    @SuppressWarnings("unchecked")
    @Test public void testGetAuditEvents() {
        AdminRpc rpc = createRpc();
        PaginatedResults<AuditEvent> results = mock(PaginatedResults.class);
        AuditEventCriteria criteria = createValidCriteria();
        Pagination pagination = new Pagination();
        when(rpc.getAuditEventDao().retrieveAuditEvents(criteria, pagination)).thenReturn(results);
        assertSame(results, rpc.getAuditEvents(criteria, pagination));
    }

    /** Test getAuditEvents() when an exception is thrown. */
    @Test public void testGetAuditEventsException() {
        AdminRpc rpc = createRpc();

        AuditEventCriteria criteria = createValidCriteria();
        Pagination pagination = new Pagination();
        RuntimeException exception = new RuntimeException("Hello");
        when(rpc.getAuditEventDao().retrieveAuditEvents(criteria, pagination)).thenThrow(exception);

        try {
            rpc.getAuditEvents(criteria, pagination);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test getAuditEvents() when an InvalidDataException is thrown. */
    @Test public void testGetAuditEventsInvalidDataException() {
        AdminRpc rpc = createRpc();

        AuditEventCriteria criteria = createValidCriteria();
        Pagination pagination = new Pagination();
        InvalidDataException exception = new InvalidDataException("Hello");
        when(rpc.getAuditEventDao().retrieveAuditEvents(criteria, pagination)).thenThrow(exception);

        try {
            rpc.getAuditEvents(criteria, pagination);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

    /** Test getRegisteredUsers(). */
    @SuppressWarnings("unchecked")
    @Test public void testGetRegisteredUsers() {
        AdminRpc rpc = createRpc();
        PaginatedResults<RegisteredUser> results = mock(PaginatedResults.class);
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        Pagination pagination = new Pagination();
        when(rpc.getRegisteredUserDao().retrieveRegisteredUsers(criteria, pagination)).thenReturn(results);
        assertSame(results, rpc.getRegisteredUsers(criteria, pagination));
    }

    /** Test getRegisteredUsers() when an exception is thrown. */
    @Test public void testGetRegisteredUsersException() {
        AdminRpc rpc = createRpc();

        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        Pagination pagination = new Pagination();
        RuntimeException exception = new RuntimeException("Hello");
        when(rpc.getRegisteredUserDao().retrieveRegisteredUsers(criteria, pagination)).thenThrow(exception);

        try {
            rpc.getRegisteredUsers(criteria, pagination);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test getRegisteredUsers() when an InvalidDataException is thrown. */
    @Test public void testGetRegisteredUsersInvalidDataException() {
        AdminRpc rpc = createRpc();

        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        Pagination pagination = new Pagination();
        InvalidDataException exception = new InvalidDataException("Hello");
        when(rpc.getRegisteredUserDao().retrieveRegisteredUsers(criteria, pagination)).thenThrow(exception);

        try {
            rpc.getRegisteredUsers(criteria, pagination);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

    /** Test deleteRegisteredUsers(). */
    @Test public void testDeleteRegisteredUsers() {
        RegisteredUser record1 = new RegisteredUser();
        record1.setUserId("record1");
        record1.setEmailAddress("email1");

        RegisteredUser record2 = new RegisteredUser();
        record2.setUserId("record2");
        record2.setEmailAddress("email2");

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        records.add(record1);
        records.add(record2);

        AuditEvent event1 = mock(AuditEvent.class);
        AuditEvent event2 = mock(AuditEvent.class);

        AdminRpc rpc = createRpc();
        when(rpc.getAuditEventService().buildDeleteUserEvent("record1", "email1")).thenReturn(event1);
        when(rpc.getAuditEventService().buildDeleteUserEvent("record2", "email2")).thenReturn(event2);

        rpc.deleteRegisteredUsers(records);
        InOrder order = Mockito.inOrder(rpc.getAuditEventService(), rpc.getRegisteredUserDao());
        order.verify(rpc.getRegisteredUserDao()).deleteRegisteredUser(record1);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event1);
        order.verify(rpc.getRegisteredUserDao()).deleteRegisteredUser(record2);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event2);
    }

    /** Test deleteRegisteredUsers() when an exception is thrown. */
    @Test public void testDeleteRegisteredUsersException() {
        RegisteredUser record1 = new RegisteredUser();
        record1.setUserId("record1");
        record1.setEmailAddress("email1");

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        records.add(record1);

        AdminRpc rpc = createRpc();
        RuntimeException exception = new RuntimeException("Hello");
        doThrow(exception).when(rpc.getRegisteredUserDao()).deleteRegisteredUser(record1);

        try {
            rpc.deleteRegisteredUsers(records);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test lockRegisteredUsers(). */
    @Test public void testLockRegisteredUsers() {
        RegisteredUser record1 = new RegisteredUser();
        record1.setUserId("record1");
        record1.setEmailAddress("email1");

        RegisteredUser record2 = new RegisteredUser();
        record2.setUserId("record2");
        record2.setEmailAddress("email2");

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        records.add(record1);
        records.add(record2);

        AuditEvent event1 = mock(AuditEvent.class);
        AuditEvent event2 = mock(AuditEvent.class);

        AdminRpc rpc = createRpc();
        when(rpc.getAuditEventService().buildLockUserEvent("record1", "email1")).thenReturn(event1);
        when(rpc.getAuditEventService().buildLockUserEvent("record2", "email2")).thenReturn(event2);

        rpc.lockRegisteredUsers(records);
        InOrder order = Mockito.inOrder(rpc.getAuditEventService(), rpc.getRegisteredUserDao());
        order.verify(rpc.getRegisteredUserDao()).lockRegisteredUser(record1);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event1);
        order.verify(rpc.getRegisteredUserDao()).lockRegisteredUser(record2);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event2);
    }

    /** Test lockRegisteredUsers() when an exception is thrown. */
    @Test public void testLockRegisteredUsersException() {
        RegisteredUser record1 = new RegisteredUser();
        record1.setUserId("record1");

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        records.add(record1);

        AdminRpc rpc = createRpc();
        RuntimeException exception = new RuntimeException("Hello");
        doThrow(exception).when(rpc.getRegisteredUserDao()).lockRegisteredUser(record1);

        try {
            rpc.lockRegisteredUsers(records);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test unlockRegisteredUsers(). */
    @Test public void testUnlockRegisteredUsers() {
        RegisteredUser record1 = new RegisteredUser();
        record1.setUserId("record1");
        record1.setEmailAddress("email1");

        RegisteredUser record2 = new RegisteredUser();
        record2.setUserId("record2");
        record2.setEmailAddress("email2");

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        records.add(record1);
        records.add(record2);

        AuditEvent event1 = mock(AuditEvent.class);
        AuditEvent event2 = mock(AuditEvent.class);

        AdminRpc rpc = createRpc();
        when(rpc.getAuditEventService().buildUnlockUserEvent("record1", "email1")).thenReturn(event1);
        when(rpc.getAuditEventService().buildUnlockUserEvent("record2", "email2")).thenReturn(event2);

        rpc.unlockRegisteredUsers(records);
        InOrder order = Mockito.inOrder(rpc.getAuditEventService(), rpc.getRegisteredUserDao());
        order.verify(rpc.getRegisteredUserDao()).unlockRegisteredUser(record1);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event1);
        order.verify(rpc.getRegisteredUserDao()).unlockRegisteredUser(record2);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event2);
    }

    /** Test unlockRegisteredUsers() when an exception is thrown. */
    @Test public void testUnlockRegisteredUsersException() {
        RegisteredUser record1 = new RegisteredUser();
        record1.setUserId("record1");

        List<RegisteredUser> records = new ArrayList<RegisteredUser>();
        records.add(record1);

        AdminRpc rpc = createRpc();
        RuntimeException exception = new RuntimeException("Hello");
        doThrow(exception).when(rpc.getRegisteredUserDao()).unlockRegisteredUser(record1);

        try {
            rpc.unlockRegisteredUsers(records);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test validateCriteria(). */
    @Test public void testValidateCriteria() {
        try {
            AdminRpc.validateCriteria(null);
            fail("Expected InvalidDataException.");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertOnlyMessage(e, NULL);
        }

        try {
            AuditEventCriteria criteria = new AuditEventCriteria();
            AdminRpc.validateCriteria(criteria);
            fail("Expected InvalidDataException.");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertOnlyMessage(e, REQUIRED, "startDate");
        }

        AuditEventCriteria criteria = new AuditEventCriteria();

        criteria.setStartDate(DateUtils.getCurrentDate());
        criteria.setEndDate(null);
        AdminRpc.validateCriteria(criteria);

        criteria.setStartDate(null);
        criteria.setEndDate(DateUtils.getCurrentDate());
        AdminRpc.validateCriteria(criteria);

        criteria.setStartDate(DateUtils.getCurrentDate());
        criteria.setEndDate(DateUtils.getCurrentDate());
        AdminRpc.validateCriteria(criteria);
    }

    /** Create valid audit event search criteria. */
    private static AuditEventCriteria createValidCriteria() {
        AuditEventCriteria criteria = new AuditEventCriteria();
        criteria.setEndDate(DateUtils.getCurrentDate());
        return criteria;
    }

    /** Create a mocked service. */
    private static AdminRpc createRpc() {
        IAuditEventService auditEventService = mock(IAuditEventService.class);
        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        IRegisteredUserDao registeredUserDao = mock(IRegisteredUserDao.class);

        AdminRpc rpc = new AdminRpc();
        rpc.setAuditEventService(auditEventService);
        rpc.setAuditEventDao(auditEventDao);
        rpc.setRegisteredUserDao(registeredUserDao);
        rpc.afterPropertiesSet();

        return rpc;
    }

}
