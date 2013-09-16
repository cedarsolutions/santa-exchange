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
package com.cedarsolutions.santa.server.service.impl;

import static com.cedarsolutions.junit.util.Assertions.assertAfter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;
import com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for AuditEventService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventServiceTest {

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        AuditEventService service = new AuditEventService();
        assertNull(service.getClientSessionService());
        assertNull(service.getAuditEventDao());

        IClientSessionService clientSessionRpc = mock(IClientSessionService.class);
        service.setClientSessionService(clientSessionRpc);
        assertSame(clientSessionRpc, service.getClientSessionService());

        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        service.setAuditEventDao(auditEventDao);
        assertSame(auditEventDao, service.getAuditEventDao());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        IClientSessionService clientSessionRpc = mock(IClientSessionService.class);
        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        AuditEventService service = new AuditEventService();

        service.setClientSessionService(clientSessionRpc);
        service.setAuditEventDao(auditEventDao);
        service.afterPropertiesSet();

        try {
            service.setClientSessionService(null);
            service.setAuditEventDao(auditEventDao);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            service.setClientSessionService(clientSessionRpc);
            service.setAuditEventDao(null);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test logAuditEvent(). */
    @Test public void testLogAuditEvent() {
        AuditEvent auditEvent = new AuditEvent();
        AuditEventService service = createService();
        service.logAuditEvent(auditEvent);
        verifyZeroInteractions(service.getClientSessionService());
        verify(service.getAuditEventDao()).insertAuditEvent(auditEvent);
    }

    /** Test buildAuditEvent(), vargargs. */
    @Test public void testBuildAuditEventVarargs() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        ExtraData extra = new ExtraData(ExtraDataKey.MODULE, "module");

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildAuditEvent(AuditEventType.LOCK_USER, extra);
        assertEquals(AuditEventType.LOCK_USER, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(1, event.getExtraData().size());
        assertEquals(extra, event.getExtraData().get(0));
    }

    /** Test buildAuditEvent(), list. */
    @Test public void testBuildAuditEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        ExtraData extra = new ExtraData(ExtraDataKey.MODULE, "module");
        List<ExtraData> list = new ArrayList<ExtraData>();
        list.add(extra);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildAuditEvent(AuditEventType.LOCK_USER, list);
        assertEquals(AuditEventType.LOCK_USER, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(1, event.getExtraData().size());
        assertEquals(extra, event.getExtraData().get(0));
    }

    /** Test buildDeleteUserEvent(). */
    @Test public void testBuildDeleteUserEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildDeleteUserEvent("operated", "email");
        assertEquals(AuditEventType.DELETE_USER, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(2, event.getExtraData().size());
        assertEquals(ExtraDataKey.USER_ID, event.getExtraData().get(0).getKey());
        assertEquals("operated", event.getExtraData().get(0).getValue());
        assertEquals(ExtraDataKey.EMAIL_ADDRESS, event.getExtraData().get(1).getKey());
        assertEquals("email", event.getExtraData().get(1).getValue());
    }

    /** Test buildLockUserEvent(). */
    @Test public void testBuildLockUserEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildLockUserEvent("operated", "email");
        assertEquals(AuditEventType.LOCK_USER, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(2, event.getExtraData().size());
        assertEquals(ExtraDataKey.USER_ID, event.getExtraData().get(0).getKey());
        assertEquals("operated", event.getExtraData().get(0).getValue());
        assertEquals(ExtraDataKey.EMAIL_ADDRESS, event.getExtraData().get(1).getKey());
        assertEquals("email", event.getExtraData().get(1).getValue());
    }

    /** Test buildUnlockUserEvent(). */
    @Test public void testBuildUnlockUserEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildUnlockUserEvent("operated", "email");
        assertEquals(AuditEventType.UNLOCK_USER, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(2, event.getExtraData().size());
        assertEquals(ExtraDataKey.USER_ID, event.getExtraData().get(0).getKey());
        assertEquals("operated", event.getExtraData().get(0).getValue());
        assertEquals(ExtraDataKey.EMAIL_ADDRESS, event.getExtraData().get(1).getKey());
        assertEquals("email", event.getExtraData().get(1).getValue());
    }

    /** Test buildCreateExchangeEvent(). */
    @Test public void testBuildCreateExchangeEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildCreateExchangeEvent(new Long(45));
        assertEquals(AuditEventType.CREATE_EXCHANGE, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(1, event.getExtraData().size());
        assertEquals(ExtraDataKey.EXCHANGE_ID, event.getExtraData().get(0).getKey());
        assertEquals("45", event.getExtraData().get(0).getValue());
    }

    /** Test buildDeleteExchangeEvent(). */
    @Test public void testBuildDeleteExchangeEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildDeleteExchangeEvent(new Long(45));
        assertEquals(AuditEventType.DELETE_EXCHANGE, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(1, event.getExtraData().size());
        assertEquals(ExtraDataKey.EXCHANGE_ID, event.getExtraData().get(0).getKey());
        assertEquals("45", event.getExtraData().get(0).getValue());
    }

    /** Test buildExchangeEmailEvent(). */
    @Test public void testBuildExchangeEmailEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildExchangeEmailEvent(new Long(45), 17);
        assertEquals(AuditEventType.EXCHANGE_EMAIL, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(2, event.getExtraData().size());
        assertEquals(ExtraDataKey.EXCHANGE_ID, event.getExtraData().get(0).getKey());
        assertEquals("45", event.getExtraData().get(0).getValue());
        assertEquals(ExtraDataKey.RECIPIENTS, event.getExtraData().get(1).getKey());
        assertEquals("17", event.getExtraData().get(1).getValue());
    }

    /** Test buildResendEmailsEvent(). */
    @Test public void testBuildResendEmailsEvent() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session = new ClientSession();
        session.setSessionId("1234");
        session.setCurrentUser(user);

        AuditEventService service = createService();
        when(service.getClientSessionService().retrieveClientSession()).thenReturn(session);

        Date now = DateUtils.getCurrentDate();
        AuditEvent event = service.buildResendEmailsEvent(new Long(45), 17);
        assertEquals(AuditEventType.RESEND_EMAILS, event.getEventType());
        assertAfter(now, event.getEventTimestamp());
        assertEquals("user", event.getUserId());
        assertEquals(session.getSessionId(), event.getSessionId());
        assertEquals(2, event.getExtraData().size());
        assertEquals(ExtraDataKey.EXCHANGE_ID, event.getExtraData().get(0).getKey());
        assertEquals("45", event.getExtraData().get(0).getValue());
        assertEquals(ExtraDataKey.RECIPIENTS, event.getExtraData().get(1).getKey());
        assertEquals("17", event.getExtraData().get(1).getValue());
    }

    /** Test deriveUserId(). */
    @Test public void testDeriveUserId() {
        FederatedUser user = new FederatedUser();
        user.setUserId("user");

        ClientSession session1 = new ClientSession();
        session1.setSessionId("1");
        session1.setCurrentUser(null);

        ClientSession session2 = new ClientSession();
        session2.setSessionId("1");
        session2.setCurrentUser(user);

        assertNull(AuditEventService.deriveUserId(null));
        assertNull(AuditEventService.deriveUserId(session1));
        assertEquals("user", AuditEventService.deriveUserId(session2));
    }

    /** Create a service, properly mocked for testing. */
    private static AuditEventService createService() {
        IClientSessionService clientSessionService = mock(IClientSessionService.class);
        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);

        AuditEventService service = new AuditEventService();
        service.setClientSessionService(clientSessionService);
        service.setAuditEventDao(auditEventDao);
        service.afterPropertiesSet();

        return service;
    }
}
