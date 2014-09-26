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
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.ADMIN_LOGIN;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.REGISTER_USER;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.USER_LOGIN;
import static com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey.MODULE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.server.dao.IRegisteredUserDao;
import com.cedarsolutions.santa.server.service.INotificationService;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.server.service.IGaeUserService;
import com.cedarsolutions.server.service.ISpringContextService;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for ClientSessionService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientSessionServiceTest {

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        ClientSessionService service = new ClientSessionService();
        assertNull(service.getGaeUserService());
        assertNull(service.getSpringContextService());
        assertNull(service.getNotificationService());
        assertNull(service.getAuditEventDao());
        assertNull(service.getRegisteredUserDao());

        IGaeUserService gaeUserService = mock(IGaeUserService.class);
        service.setGaeUserService(gaeUserService);
        assertSame(gaeUserService, service.getGaeUserService());

        ISpringContextService springContextService = mock(ISpringContextService.class);
        service.setSpringContextService(springContextService);
        assertSame(springContextService, service.getSpringContextService());

        INotificationService notificationService = mock(INotificationService.class);
        service.setNotificationService(notificationService);
        assertSame(notificationService, service.getNotificationService());

        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        service.setAuditEventDao(auditEventDao);
        assertSame(auditEventDao, service.getAuditEventDao());

        IRegisteredUserDao registeredUserDao = mock(IRegisteredUserDao.class);
        service.setRegisteredUserDao(registeredUserDao);
        assertSame(registeredUserDao, service.getRegisteredUserDao());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        IGaeUserService gaeUserService = mock(IGaeUserService.class);
        ISpringContextService springContextService = mock(ISpringContextService.class);
        INotificationService notificationService = mock(INotificationService.class);
        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        IRegisteredUserDao registeredUserDao = mock(IRegisteredUserDao.class);
        ClientSessionService service = new ClientSessionService();

        service.setGaeUserService(gaeUserService);
        service.setSpringContextService(springContextService);
        service.setNotificationService(notificationService);
        service.setAuditEventDao(auditEventDao);
        service.setRegisteredUserDao(registeredUserDao);
        service.afterPropertiesSet();

        try {
            service.setGaeUserService(null);
            service.setSpringContextService(springContextService);
            service.setNotificationService(notificationService);
            service.setAuditEventDao(auditEventDao);
            service.setRegisteredUserDao(registeredUserDao);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            service.setGaeUserService(gaeUserService);
            service.setSpringContextService(null);
            service.setNotificationService(notificationService);
            service.setAuditEventDao(auditEventDao);
            service.setRegisteredUserDao(registeredUserDao);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            service.setGaeUserService(gaeUserService);
            service.setSpringContextService(springContextService);
            service.setNotificationService(null);
            service.setAuditEventDao(auditEventDao);
            service.setRegisteredUserDao(registeredUserDao);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            service.setGaeUserService(gaeUserService);
            service.setSpringContextService(springContextService);
            service.setNotificationService(notificationService);
            service.setAuditEventDao(null);
            service.setRegisteredUserDao(registeredUserDao);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            service.setGaeUserService(gaeUserService);
            service.setSpringContextService(springContextService);
            service.setNotificationService(notificationService);
            service.setAuditEventDao(auditEventDao);
            service.setRegisteredUserDao(null);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test establishClientSession() with null user and null session id. */
    @Test public void testEstablishClientSession1() {
        ClientSessionService service = createService();

        Module module = Module.ROOT;
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn(null);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("<none>", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getRegisteredUserDao());
        verifyZeroInteractions(service.getNotificationService());
        verifyZeroInteractions(service.getAuditEventDao());
    }

    /** Test establishClientSession() with null user and empty session id. */
    @Test public void testEstablishClientSession2() {
        ClientSessionService service = createService();

        Module module = Module.ROOT;
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("");

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("<none>", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getRegisteredUserDao());
        verifyZeroInteractions(service.getNotificationService());
        verifyZeroInteractions(service.getAuditEventDao());
    }

    /** Test establishClientSession() with null user and non-null session id. */
    @Test public void testEstablishClientSession3() {
        ClientSessionService service = createService();

        Module module = Module.ROOT;
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getRegisteredUserDao());
        verifyZeroInteractions(service.getNotificationService());
        verifyZeroInteractions(service.getAuditEventDao());
    }

    /** Test establishClientSession() with non-null user and session id, first login (INTERNAL module, non-admin user). */
    @Test public void testEstablishClientSession4() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(false);
        currentUser.setFirstLogin(false);

        Module module = Module.INTERNAL;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(null);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);
        ArgumentCaptor<RegisteredUser> registeredUserInserted = ArgumentCaptor.forClass(RegisteredUser.class);
        ArgumentCaptor<RegisteredUser> registeredUserNotified = ArgumentCaptor.forClass(RegisteredUser.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertTrue(currentUser.isFirstLogin());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao()).insertRegisteredUser(registeredUserInserted.capture());
        verify(service.getNotificationService()).notifyRegisteredUser(registeredUserNotified.capture());

        assertEquals(currentUser.getUserId(), registeredUserInserted.getValue().getUserId());
        assertEquals(currentUser.getUserName(), registeredUserInserted.getValue().getUserName());
        assertAfter(now, registeredUserInserted.getValue().getRegistrationDate());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUserInserted.getValue().getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUserInserted.getValue().getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUserInserted.getValue().getFederatedIdentity());
        assertEquals(currentUser.getEmailAddress(), registeredUserInserted.getValue().getEmailAddress());
        assertEquals(1, registeredUserInserted.getValue().getLogins());
        assertEquals(registeredUserInserted.getValue().getRegistrationDate(), registeredUserInserted.getValue().getLastLogin());
        assertEquals(currentUser.isAdmin(), registeredUserInserted.getValue().isAdmin());
        assertEquals(false, registeredUserInserted.getValue().isLocked());

        assertEquals(registeredUserInserted.getValue(), registeredUserNotified.getValue());
        assertEquals(registeredUserInserted.getValue(), session.getRegisteredUser());

        verify(service.getAuditEventDao(), times(2)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, REGISTER_USER, currentUser, session, module);
        assertAuditEvent(auditEvent.getAllValues().get(1), now, USER_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, existing login (INTERNAL module, non-admin user). */
    @Test public void testEstablishClientSession5() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(false);
        currentUser.setFirstLogin(false);

        RegisteredUser registeredUser1 = ClientSessionService.createRegisteredUser(currentUser);
        RegisteredUser registeredUser2 = ClientSessionService.createRegisteredUser(currentUser);

        Module module = Module.INTERNAL;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(registeredUser1);
        when(service.getRegisteredUserDao().recordNewLogin(registeredUser1)).thenReturn(registeredUser2);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertFalse(currentUser.isFirstLogin());
        assertSame(registeredUser2, session.getRegisteredUser());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao(), never()).insertRegisteredUser(isA(RegisteredUser.class));
        verifyZeroInteractions(service.getNotificationService());

        verify(service.getAuditEventDao(), times(1)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, USER_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, first login (INTERNAL module, admin user). */
    @Test public void testEstablishClientSession6() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(true);
        currentUser.setFirstLogin(false);

        Module module = Module.INTERNAL;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(null);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);
        ArgumentCaptor<RegisteredUser> registeredUserInserted = ArgumentCaptor.forClass(RegisteredUser.class);
        ArgumentCaptor<RegisteredUser> registeredUserNotified = ArgumentCaptor.forClass(RegisteredUser.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertTrue(currentUser.isFirstLogin());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao()).insertRegisteredUser(registeredUserInserted.capture());
        verify(service.getNotificationService()).notifyRegisteredUser(registeredUserNotified.capture());

        assertEquals(currentUser.getUserId(), registeredUserInserted.getValue().getUserId());
        assertEquals(currentUser.getUserName(), registeredUserInserted.getValue().getUserName());
        assertAfter(now, registeredUserInserted.getValue().getRegistrationDate());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUserInserted.getValue().getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUserInserted.getValue().getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUserInserted.getValue().getFederatedIdentity());
        assertEquals(currentUser.getEmailAddress(), registeredUserInserted.getValue().getEmailAddress());
        assertEquals(1, registeredUserInserted.getValue().getLogins());
        assertEquals(registeredUserInserted.getValue().getRegistrationDate(), registeredUserInserted.getValue().getLastLogin());
        assertEquals(currentUser.isAdmin(), registeredUserInserted.getValue().isAdmin());
        assertEquals(false, registeredUserInserted.getValue().isLocked());

        assertEquals(registeredUserInserted.getValue(), registeredUserNotified.getValue());
        assertEquals(registeredUserInserted.getValue(), session.getRegisteredUser());

        verify(service.getAuditEventDao(), times(2)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, REGISTER_USER, currentUser, session, module);
        assertAuditEvent(auditEvent.getAllValues().get(1), now, ADMIN_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, existing login (INTERNAL module, admin user). */
    @Test public void testEstablishClientSession7() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(true);
        currentUser.setFirstLogin(false);

        RegisteredUser registeredUser1 = ClientSessionService.createRegisteredUser(currentUser);
        RegisteredUser registeredUser2 = ClientSessionService.createRegisteredUser(currentUser);

        Module module = Module.INTERNAL;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(registeredUser1);
        when(service.getRegisteredUserDao().recordNewLogin(registeredUser1)).thenReturn(registeredUser2);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertFalse(currentUser.isFirstLogin());
        assertSame(registeredUser2, session.getRegisteredUser());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao(), never()).insertRegisteredUser(isA(RegisteredUser.class));
        verifyZeroInteractions(service.getNotificationService());

        verify(service.getAuditEventDao(), times(1)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, ADMIN_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, first login (ADMIN module, non-admin user). */
    @Test public void testEstablishClientSession8() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(false);
        currentUser.setFirstLogin(false);

        Module module = Module.ADMIN;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(null);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);
        ArgumentCaptor<RegisteredUser> registeredUserInserted = ArgumentCaptor.forClass(RegisteredUser.class);
        ArgumentCaptor<RegisteredUser> registeredUserNotified = ArgumentCaptor.forClass(RegisteredUser.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertTrue(currentUser.isFirstLogin());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao()).insertRegisteredUser(registeredUserInserted.capture());
        verify(service.getNotificationService()).notifyRegisteredUser(registeredUserNotified.capture());

        assertEquals(currentUser.getUserId(), registeredUserInserted.getValue().getUserId());
        assertEquals(currentUser.getUserName(), registeredUserInserted.getValue().getUserName());
        assertAfter(now, registeredUserInserted.getValue().getRegistrationDate());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUserInserted.getValue().getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUserInserted.getValue().getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUserInserted.getValue().getFederatedIdentity());
        assertEquals(currentUser.getEmailAddress(), registeredUserInserted.getValue().getEmailAddress());
        assertEquals(1, registeredUserInserted.getValue().getLogins());
        assertEquals(registeredUserInserted.getValue().getRegistrationDate(), registeredUserInserted.getValue().getLastLogin());
        assertEquals(currentUser.isAdmin(), registeredUserInserted.getValue().isAdmin());
        assertEquals(false, registeredUserInserted.getValue().isLocked());

        assertEquals(registeredUserInserted.getValue(), registeredUserNotified.getValue());
        assertEquals(registeredUserInserted.getValue(), session.getRegisteredUser());

        verify(service.getAuditEventDao(), times(2)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, REGISTER_USER, currentUser, session, module);
        assertAuditEvent(auditEvent.getAllValues().get(1), now, USER_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, existing login (ADMIN module, non-admin user). */
    @Test public void testEstablishClientSession9() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(false);
        currentUser.setFirstLogin(false);

        RegisteredUser registeredUser1 = ClientSessionService.createRegisteredUser(currentUser);
        RegisteredUser registeredUser2 = ClientSessionService.createRegisteredUser(currentUser);

        Module module = Module.ADMIN;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(registeredUser1);
        when(service.getRegisteredUserDao().recordNewLogin(registeredUser1)).thenReturn(registeredUser2);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertFalse(currentUser.isFirstLogin());
        assertSame(registeredUser2, session.getRegisteredUser());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao(), never()).insertRegisteredUser(isA(RegisteredUser.class));
        verifyZeroInteractions(service.getNotificationService());

        verify(service.getAuditEventDao(), times(1)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, USER_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, first login (ADMIN module, admin user). */
    @Test public void testEstablishClientSession10() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(true);
        currentUser.setFirstLogin(false);

        Module module = Module.ADMIN;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(null);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);
        ArgumentCaptor<RegisteredUser> registeredUserInserted = ArgumentCaptor.forClass(RegisteredUser.class);
        ArgumentCaptor<RegisteredUser> registeredUserNotified = ArgumentCaptor.forClass(RegisteredUser.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertTrue(currentUser.isFirstLogin());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao()).insertRegisteredUser(registeredUserInserted.capture());
        verify(service.getNotificationService()).notifyRegisteredUser(registeredUserNotified.capture());

        assertEquals(currentUser.getUserId(), registeredUserInserted.getValue().getUserId());
        assertEquals(currentUser.getUserName(), registeredUserInserted.getValue().getUserName());
        assertAfter(now, registeredUserInserted.getValue().getRegistrationDate());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUserInserted.getValue().getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUserInserted.getValue().getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUserInserted.getValue().getFederatedIdentity());
        assertEquals(currentUser.getEmailAddress(), registeredUserInserted.getValue().getEmailAddress());
        assertEquals(1, registeredUserInserted.getValue().getLogins());
        assertEquals(registeredUserInserted.getValue().getRegistrationDate(), registeredUserInserted.getValue().getLastLogin());
        assertEquals(currentUser.isAdmin(), registeredUserInserted.getValue().isAdmin());
        assertEquals(false, registeredUserInserted.getValue().isLocked());

        assertEquals(registeredUserInserted.getValue(), registeredUserNotified.getValue());
        assertEquals(registeredUserInserted.getValue(), session.getRegisteredUser());

        verify(service.getAuditEventDao(), times(2)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, REGISTER_USER, currentUser, session, module);
        assertAuditEvent(auditEvent.getAllValues().get(1), now, ADMIN_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, existing login (ADMIN module, admin user). */
    @Test public void testEstablishClientSession11() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(true);
        currentUser.setFirstLogin(false);

        RegisteredUser registeredUser1 = ClientSessionService.createRegisteredUser(currentUser);
        RegisteredUser registeredUser2 = ClientSessionService.createRegisteredUser(currentUser);

        Module module = Module.ADMIN;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(registeredUser1);
        when(service.getRegisteredUserDao().recordNewLogin(registeredUser1)).thenReturn(registeredUser2);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertFalse(currentUser.isFirstLogin());
        assertSame(registeredUser2, session.getRegisteredUser());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao(), never()).insertRegisteredUser(isA(RegisteredUser.class));
        verifyZeroInteractions(service.getNotificationService());

        verify(service.getAuditEventDao(), times(1)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, ADMIN_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, first login (ROOT module, non-admin user). */
    @Test public void testEstablishClientSession12() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(false);
        currentUser.setFirstLogin(false);

        Module module = Module.ROOT;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(null);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);
        ArgumentCaptor<RegisteredUser> registeredUserInserted = ArgumentCaptor.forClass(RegisteredUser.class);
        ArgumentCaptor<RegisteredUser> registeredUserNotified = ArgumentCaptor.forClass(RegisteredUser.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertTrue(currentUser.isFirstLogin());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao()).insertRegisteredUser(registeredUserInserted.capture());
        verify(service.getNotificationService()).notifyRegisteredUser(registeredUserNotified.capture());

        assertEquals(currentUser.getUserId(), registeredUserInserted.getValue().getUserId());
        assertEquals(currentUser.getUserName(), registeredUserInserted.getValue().getUserName());
        assertAfter(now, registeredUserInserted.getValue().getRegistrationDate());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUserInserted.getValue().getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUserInserted.getValue().getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUserInserted.getValue().getFederatedIdentity());
        assertEquals(currentUser.getEmailAddress(), registeredUserInserted.getValue().getEmailAddress());
        assertEquals(1, registeredUserInserted.getValue().getLogins());
        assertEquals(registeredUserInserted.getValue().getRegistrationDate(), registeredUserInserted.getValue().getLastLogin());
        assertEquals(currentUser.isAdmin(), registeredUserInserted.getValue().isAdmin());
        assertEquals(false, registeredUserInserted.getValue().isLocked());

        assertEquals(registeredUserInserted.getValue(), registeredUserNotified.getValue());
        assertEquals(registeredUserInserted.getValue(), session.getRegisteredUser());

        verify(service.getAuditEventDao(), times(2)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, REGISTER_USER, currentUser, session, module);
        assertAuditEvent(auditEvent.getAllValues().get(1), now, USER_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, existing login (ROOT module, non-admin user). */
    @Test public void testEstablishClientSession13() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(false);
        currentUser.setFirstLogin(false);

        RegisteredUser registeredUser1 = ClientSessionService.createRegisteredUser(currentUser);
        RegisteredUser registeredUser2 = ClientSessionService.createRegisteredUser(currentUser);

        Module module = Module.ROOT;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(registeredUser1);
        when(service.getRegisteredUserDao().recordNewLogin(registeredUser1)).thenReturn(registeredUser2);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertFalse(currentUser.isFirstLogin());
        assertSame(registeredUser2, session.getRegisteredUser());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao(), never()).insertRegisteredUser(isA(RegisteredUser.class));
        verifyZeroInteractions(service.getNotificationService());

        verify(service.getAuditEventDao(), times(1)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, USER_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, first login (ROOT module, admin user). */
    @Test public void testEstablishClientSession14() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(true);
        currentUser.setFirstLogin(false);

        Module module = Module.ROOT;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(null);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);
        ArgumentCaptor<RegisteredUser> registeredUserInserted = ArgumentCaptor.forClass(RegisteredUser.class);
        ArgumentCaptor<RegisteredUser> registeredUserNotified = ArgumentCaptor.forClass(RegisteredUser.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertTrue(currentUser.isFirstLogin());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao()).insertRegisteredUser(registeredUserInserted.capture());
        verify(service.getNotificationService()).notifyRegisteredUser(registeredUserNotified.capture());

        assertEquals(currentUser.getUserId(), registeredUserInserted.getValue().getUserId());
        assertEquals(currentUser.getUserName(), registeredUserInserted.getValue().getUserName());
        assertAfter(now, registeredUserInserted.getValue().getRegistrationDate());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUserInserted.getValue().getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUserInserted.getValue().getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUserInserted.getValue().getFederatedIdentity());
        assertEquals(currentUser.getEmailAddress(), registeredUserInserted.getValue().getEmailAddress());
        assertEquals(1, registeredUserInserted.getValue().getLogins());
        assertEquals(registeredUserInserted.getValue().getRegistrationDate(), registeredUserInserted.getValue().getLastLogin());
        assertEquals(currentUser.isAdmin(), registeredUserInserted.getValue().isAdmin());
        assertEquals(false, registeredUserInserted.getValue().isLocked());

        assertEquals(registeredUserInserted.getValue(), registeredUserNotified.getValue());
        assertEquals(registeredUserInserted.getValue(), session.getRegisteredUser());

        verify(service.getAuditEventDao(), times(2)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, REGISTER_USER, currentUser, session, module);
        assertAuditEvent(auditEvent.getAllValues().get(1), now, ADMIN_LOGIN, currentUser, session, module);
    }

    /** Test establishClientSession() with non-null user and session id, existing login (ROOT module, admin user). */
    @Test public void testEstablishClientSession15() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setFederatedIdentity("federatedIdentity");
        currentUser.setAdmin(true);
        currentUser.setFirstLogin(false);

        RegisteredUser registeredUser1 = ClientSessionService.createRegisteredUser(currentUser);
        RegisteredUser registeredUser2 = ClientSessionService.createRegisteredUser(currentUser);

        Module module = Module.ROOT;
        Date now = DateUtils.getCurrentDate();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");
        when(service.getRegisteredUserDao().retrieveRegisteredUser("user")).thenReturn(registeredUser1);
        when(service.getRegisteredUserDao().recordNewLogin(registeredUser1)).thenReturn(registeredUser2);
        ArgumentCaptor<AuditEvent> auditEvent = ArgumentCaptor.forClass(AuditEvent.class);

        ClientSession session = service.establishClientSession(module, "destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        assertFalse(currentUser.isFirstLogin());
        assertSame(registeredUser2, session.getRegisteredUser());

        verify(service.getRegisteredUserDao()).retrieveRegisteredUser("user");
        verify(service.getRegisteredUserDao(), never()).insertRegisteredUser(isA(RegisteredUser.class));
        verifyZeroInteractions(service.getNotificationService());

        verify(service.getAuditEventDao(), times(1)).insertAuditEvent(auditEvent.capture());
        assertAuditEvent(auditEvent.getAllValues().get(0), now, ADMIN_LOGIN, currentUser, session, module);
    }

    /** Test invalidateClientSession(). */
    @Test public void testInvalidateClientSession() {
        ClientSessionService service = createService();

        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");

        Module module = Module.ROOT;
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");

        service.invalidateClientSession(module);
        verify(service.getSpringContextService()).invalidateCurrentSession();
        verifyZeroInteractions(service.getNotificationService());
        verifyZeroInteractions(service.getAuditEventDao());
    }

    /** Test retrieveClientSession() with no destination URL; null user and null session id. */
    @Test public void testRetrieveClientSessionNoDestination1() {
        ClientSessionService service = createService();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn(null);

        ClientSession session = service.retrieveClientSession();
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("<none>", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with no destination URL; null user and empty session id. */
    @Test public void testRetrieveClientSessionNoDestination2() {
        ClientSessionService service = createService();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("");

        ClientSession session = service.retrieveClientSession();
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("<none>", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with no destination URL; null user and non-null session id. */
    @Test public void testRetrieveClientSessionNoDestination3() {
        ClientSessionService service = createService();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");

        ClientSession session = service.retrieveClientSession();
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with no destination URL; non-null user and session id. */
    @Test public void testRetrieveClientSessionNoDestination4() {
        ClientSessionService service = createService();
        FederatedUser currentUser = new FederatedUser();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");

        ClientSession session = service.retrieveClientSession();
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with a destination URL; null user and null session id. */
    @Test public void testRetrieveClientSessionWithDestination1() {
        ClientSessionService service = createService();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn(null);

        ClientSession session = service.retrieveClientSession("destination");
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("<none>", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with a destination URL; null user and empty session id. */
    @Test public void testRetrieveClientSessionWithDestination2() {
        ClientSessionService service = createService();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("");

        ClientSession session = service.retrieveClientSession("destination");
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("<none>", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with a destination URL; null user and non-null session id. */
    @Test public void testRetrieveClientSessionWithDestination3() {
        ClientSessionService service = createService();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(null);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");

        ClientSession session = service.retrieveClientSession("destination");
        assertNotNull(session);
        assertEquals(null, session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertEquals(null, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
    }

    /** Test retrieveClientSession() with a destination URL; non-null user and session id. */
    @Test public void testRetrieveClientSessionWithDestination4() {
        ClientSessionService service = createService();
        FederatedUser currentUser = new FederatedUser();
        when(service.getGaeUserService().getCurrentUser()).thenReturn(currentUser);
        when(service.getSpringContextService().getCurrentSessionId()).thenReturn("1234");
        when(service.getGaeUserService().getLogoutUrl("destination")).thenReturn("http://destination");

        ClientSession session = service.retrieveClientSession("destination");
        assertNotNull(session);
        assertEquals("http://destination", session.getLogoutUrl());
        assertEquals("1234", session.getSessionId());
        assertSame(currentUser, session.getCurrentUser());
        verifyZeroInteractions(service.getAuditEventDao());
        verifyZeroInteractions(service.getNotificationService());
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

        assertNull(ClientSessionService.deriveUserId(null));
        assertNull(ClientSessionService.deriveUserId(session1));
        assertEquals("user", ClientSessionService.deriveUserId(session2));
    }

    /** Test deriveEmailAddress(). */
    @Test public void testDeriveEmailAddress() {
        FederatedUser user = new FederatedUser();
        user.setEmailAddress("email");

        ClientSession session1 = new ClientSession();
        session1.setSessionId("1");
        session1.setCurrentUser(null);

        ClientSession session2 = new ClientSession();
        session2.setSessionId("1");
        session2.setCurrentUser(user);

        assertNull(ClientSessionService.deriveEmailAddress(null));
        assertNull(ClientSessionService.deriveEmailAddress(session1));
        assertEquals("email", ClientSessionService.deriveEmailAddress(session2));
    }

    /** Test createRegisteredUser(). */
    @Test public void testCreateRegisteredUser() {
        FederatedUser currentUser = new FederatedUser();
        currentUser.setUserId("user");
        currentUser.setUserName("name");
        currentUser.setEmailAddress("email");
        currentUser.setAuthenticationDomain("gmail.com");
        currentUser.setOpenIdProvider(OpenIdProvider.GOOGLE);
        currentUser.setFederatedIdentity("federated");
        currentUser.setFirstLogin(false);
        currentUser.setAdmin(true);

        Date now = DateUtils.getCurrentDate();

        RegisteredUser registeredUser = ClientSessionService.createRegisteredUser(currentUser);
        assertNotNull(registeredUser);
        assertAfter(now, registeredUser.getRegistrationDate());
        assertEquals(currentUser.getUserId(), registeredUser.getUserId());
        assertEquals(currentUser.getUserName(), registeredUser.getUserName());
        assertEquals(currentUser.getEmailAddress(), registeredUser.getEmailAddress());
        assertEquals(currentUser.getAuthenticationDomain(), registeredUser.getAuthenticationDomain());
        assertEquals(currentUser.getOpenIdProvider(), registeredUser.getOpenIdProvider());
        assertEquals(currentUser.getFederatedIdentity(), registeredUser.getFederatedIdentity());
        assertEquals(currentUser.isAdmin(), registeredUser.isAdmin());
    }

    /** Assert that an audit event matches expectations. */
    private static void assertAuditEvent(AuditEvent actual, Date now, AuditEventType eventType,
                                         FederatedUser user, ClientSession session, Module module) {
        String userId = user == null ? null : user.getUserId();
        String emailAddress = user == null ? null : user.getEmailAddress();
        String sessionId = session == null ? null : session.getSessionId();
        assertAuditEvent(actual, now, eventType, userId, emailAddress, sessionId, module);
    }

    /** Assert that an audit event matches expectations. */
    private static void assertAuditEvent(AuditEvent actual, Date now, AuditEventType eventType,
                                         String userId, String emailAddress, String sessionId, Module module) {
        assertEquals(eventType, actual.getEventType());
        assertAfter(now, actual.getEventTimestamp());
        assertEquals(userId, actual.getUserId());
        assertEquals(sessionId, actual.getSessionId());

        assertEquals(MODULE, actual.getExtraData().get(0).getKey());
        assertEquals(module.toString(), actual.getExtraData().get(0).getValue());

        if (eventType == AuditEventType.REGISTER_USER) {
            assertEquals(ExtraDataKey.EMAIL_ADDRESS, actual.getExtraData().get(1).getKey());
            assertEquals(emailAddress, actual.getExtraData().get(1).getValue());
        }
    }

    /** Create a mocked service. */
    private static ClientSessionService createService() {
        IGaeUserService gaeUserService = mock(IGaeUserService.class);
        ISpringContextService springContextService = mock(ISpringContextService.class);
        INotificationService notificationService = mock(INotificationService.class);
        IAuditEventDao auditEventDao = mock(IAuditEventDao.class);
        IRegisteredUserDao registeredUserDao = mock(IRegisteredUserDao.class);

        ClientSessionService service = new ClientSessionService();
        service.setGaeUserService(gaeUserService);
        service.setSpringContextService(springContextService);
        service.setNotificationService(notificationService);
        service.setAuditEventDao(auditEventDao);
        service.setRegisteredUserDao(registeredUserDao);
        service.afterPropertiesSet();

        return service;
    }

}
