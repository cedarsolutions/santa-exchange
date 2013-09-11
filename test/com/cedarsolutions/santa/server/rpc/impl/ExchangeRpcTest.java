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
package com.cedarsolutions.santa.server.rpc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.RpcSecurityException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.server.dao.IExchangeDao;
import com.cedarsolutions.santa.server.service.IAuditEventService;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.server.service.IExchangeService;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;

/**
 * Unit tests for ExchangeRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeRpcTest {

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        ExchangeRpc rpc = new ExchangeRpc();
        assertNull(rpc.getAuditEventService());
        assertNull(rpc.getExchangeService());
        assertNull(rpc.getClientSessionService());
        assertNull(rpc.getExchangeDao());

        IAuditEventService registeredUserService = mock(IAuditEventService.class);
        rpc.setAuditEventService(registeredUserService);
        assertSame(registeredUserService, rpc.getAuditEventService());

        IExchangeService exchangeService = mock(IExchangeService.class);
        rpc.setExchangeService(exchangeService);
        assertSame(exchangeService, rpc.getExchangeService());

        IClientSessionService clientSessionService = mock(IClientSessionService.class);
        rpc.setClientSessionService(clientSessionService);
        assertSame(clientSessionService, rpc.getClientSessionService());

        IExchangeDao exchangeDao = mock(IExchangeDao.class);
        rpc.setExchangeDao(exchangeDao);
        assertSame(exchangeDao, rpc.getExchangeDao());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        ExchangeRpc rpc = new ExchangeRpc();
        IAuditEventService auditEventService = mock(IAuditEventService.class);
        IExchangeService exchangeService = mock(IExchangeService.class);
        IClientSessionService clientSessionService = mock(IClientSessionService.class);
        IExchangeDao exchangeDao = mock(IExchangeDao.class);

        rpc.setAuditEventService(auditEventService);
        rpc.setExchangeService(exchangeService);
        rpc.setClientSessionService(clientSessionService);
        rpc.setExchangeDao(exchangeDao);
        rpc.afterPropertiesSet();

        try {
            rpc.setAuditEventService(null);
            rpc.setExchangeService(exchangeService);
            rpc.setClientSessionService(clientSessionService);
            rpc.setExchangeDao(exchangeDao);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            rpc.setAuditEventService(auditEventService);
            rpc.setExchangeService(null);
            rpc.setClientSessionService(clientSessionService);
            rpc.setExchangeDao(exchangeDao);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            rpc.setAuditEventService(auditEventService);
            rpc.setExchangeService(exchangeService);
            rpc.setClientSessionService(null);
            rpc.setExchangeDao(exchangeDao);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            rpc.setAuditEventService(auditEventService);
            rpc.setExchangeService(exchangeService);
            rpc.setClientSessionService(clientSessionService);
            rpc.setExchangeDao(null);
            rpc.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test retrieveExchange(). */
    @Test public void testRetrieveExchange() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        Exchange exchange = new Exchange();
        exchange.setId(1L);

        when(rpc.getExchangeDao().retrieveExchange(1L)).thenReturn(null);
        assertNull(rpc.retrieveExchange(1L));

        try {
            exchange.setUserId("that other guy");
            when(rpc.getExchangeDao().retrieveExchange(1L)).thenReturn(exchange);
            rpc.retrieveExchange(1L);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        exchange.setUserId("me");
        when(rpc.getExchangeDao().retrieveExchange(1L)).thenReturn(exchange);
        Exchange result = rpc.retrieveExchange(1L);
        assertSame(exchange, result);
    }

    /** Test retrieveExchange() exception conditions. */
    @Test public void testRetrieveExchangeExceptions() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        CedarRuntimeException cause = new CedarRuntimeException("Hello");
        when(rpc.getExchangeDao().retrieveExchange(2L)).thenThrow(cause);

        try {
            rpc.retrieveExchange(2L);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(cause, e.getCause());
        }
    }

    /** Test getExchanges(). */
    @SuppressWarnings("unchecked")
    @Test public void testGetExchanges() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        // this is invalid because the user id does not match the session
        ExchangeCriteria invalid = new ExchangeCriteria();
        invalid.setUserId("that other guy");
        invalid.setExchangeIds(1L, 2L, 3L);

        // this is valid because the user id does match the session
        ExchangeCriteria valid = new ExchangeCriteria();
        valid.setUserId("me");
        valid.setExchangeIds(1L, 2L, 3L);

        Pagination pagination = new Pagination();
        PaginatedResults<Exchange> results = mock(PaginatedResults.class);
        when(rpc.getExchangeDao().retrieveExchanges(valid, pagination)).thenReturn(results);

        try {
            rpc.getExchanges(invalid, pagination);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        assertSame(results, rpc.getExchanges(valid, pagination));
    }

    /** Test getExchanges() exception conditions. */
    @Test public void testGetExchangesException() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        ExchangeCriteria criteria = new ExchangeCriteria();
        criteria.setUserId("me");
        criteria.setExchangeIds(1L, 2L, 3L);

        Pagination pagination = new Pagination();

        CedarRuntimeException cause = new CedarRuntimeException("Hello");
        when(rpc.getExchangeDao().retrieveExchanges(criteria, pagination)).thenThrow(cause);

        try {
            rpc.getExchanges(criteria, pagination);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(cause, e.getCause());
        }
    }

    /** Test createExchange(). */
    @Test public void testCreateExchange() {
        AuditEvent event = mock(AuditEvent.class);

        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");
        when(rpc.getExchangeDao().insertExchange(Mockito.any(Exchange.class))).thenReturn(42L);
        when(rpc.getAuditEventService().buildCreateExchangeEvent(42L)).thenReturn(event);

        Exchange exchange = rpc.createExchange("name");
        InOrder order = Mockito.inOrder(rpc.getExchangeDao(), rpc.getAuditEventService());
        order.verify(rpc.getExchangeDao()).insertExchange(exchange);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event);

        assertEquals(new Long(42), exchange.getId());
        assertEquals("me", exchange.getUserId());
        assertEquals(ExchangeState.NEW, exchange.getExchangeState());
        assertEquals("name", exchange.getName());
        assertNull(exchange.getDateAndTime());
        assertNull(exchange.getTheme());
        assertNull(exchange.getCost());
        assertNull(exchange.getExtraInfo());
        assertNotNull(exchange.getOrganizer());
        assertNotNull(exchange.getTemplateOverrides());
        assertTrue(exchange.getParticipants().isEmpty());
        assertNull(exchange.getAssignments());
    }

    /** Test createExchange() exception conditions. */
    @Test public void testCreateExchangeException() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        RpcSecurityException securityException = new RpcSecurityException("security");
        Exchange securityExchange = new Exchange();
        securityExchange.setUserId("me");
        securityExchange.setName("security");
        securityExchange.setExchangeState(ExchangeState.NEW);
        when(rpc.getExchangeDao().insertExchange(securityExchange)).thenThrow(securityException);

        try {
            rpc.createExchange("security");
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        CedarRuntimeException runtimeException = new CedarRuntimeException("runtime");
        Exchange runtimeExchange = new Exchange();
        runtimeExchange.setUserId("me");
        runtimeExchange.setName("runtime");
        runtimeExchange.setExchangeState(ExchangeState.NEW);
        when(rpc.getExchangeDao().insertExchange(runtimeExchange)).thenThrow(runtimeException);

        try {
            rpc.createExchange("runtime");
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(runtimeException, e.getCause());
        }
    }

    /** Test deleteExchanges(). */
    @Test public void testDeleteExchanges() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        Exchange exchange1 = new Exchange();
        exchange1.setId(1L);
        exchange1.setUserId("someone else");

        Exchange exchange2 = new Exchange();
        exchange2.setId(2L);
        exchange2.setUserId("me");

        Exchange exchange3 = new Exchange();
        exchange3.setId(3L);
        exchange3.setUserId("me");

        when(rpc.getExchangeDao().retrieveExchange(1L)).thenReturn(exchange1);
        when(rpc.getExchangeDao().retrieveExchange(2L)).thenReturn(exchange2);
        when(rpc.getExchangeDao().retrieveExchange(3L)).thenReturn(exchange3);

        List<Exchange> records = new ArrayList<Exchange>();
        records.add(exchange1);
        records.add(exchange2);
        records.add(exchange3);

        AuditEvent event1 = mock(AuditEvent.class);
        AuditEvent event2 = mock(AuditEvent.class);
        when(rpc.getAuditEventService().buildDeleteExchangeEvent(2L)).thenReturn(event1);
        when(rpc.getAuditEventService().buildDeleteExchangeEvent(3L)).thenReturn(event2);

        // blows up because exchange1 is in the list and has a different owner
        try {
            rpc.deleteExchanges(records);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        // works once we take exchange1 out of the list
        records.remove(0);
        rpc.deleteExchanges(records);
        InOrder order = Mockito.inOrder(rpc.getExchangeDao(), rpc.getAuditEventService());
        order.verify(rpc.getExchangeDao(), times(0)).deleteExchange(exchange1);
        order.verify(rpc.getExchangeDao()).deleteExchange(exchange2);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event1);
        order.verify(rpc.getExchangeDao()).deleteExchange(exchange3);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event2);
    }

    /** Test deleteExchanges() exception conditions. */
    @Test public void testDeleteExchangesException() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        Exchange exchange = new Exchange();
        exchange.setId(4L);
        exchange.setUserId("me");

        CedarRuntimeException cause = new CedarRuntimeException("Hello");
        doThrow(cause).when(rpc.getExchangeDao()).deleteExchange(exchange);

        try {
            List<Exchange> records = new ArrayList<Exchange>();
            records.add(exchange);
            rpc.deleteExchanges(records);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(cause, e.getCause());
        }
    }

    /** Test saveExchange(). */
    @Test public void testSaveExchange() {
        ExchangeRpc rpc = createRpc();
        Exchange exchange = mock(Exchange.class);
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        // blows up because the exchange has the wrong user id (doesn't match session)
        try {
            when(exchange.getUserId()).thenReturn("bogus");
            rpc.saveExchange(exchange);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        InOrder order = Mockito.inOrder(exchange, rpc.getExchangeDao());

        // If the state is NEW, it gets moved to STARTED when the update happens
        when(exchange.getUserId()).thenReturn("me");
        when(exchange.getExchangeState()).thenReturn(ExchangeState.NEW);
        rpc.saveExchange(exchange);
        order.verify(exchange).setExchangeState(ExchangeState.STARTED);
        order.verify(rpc.getExchangeDao()).updateExchange(exchange);

        // If the state is not NEW, it stays as-is
        when(exchange.getUserId()).thenReturn("me");
        when(exchange.getExchangeState()).thenReturn(ExchangeState.GENERATED);
        rpc.saveExchange(exchange);
        order.verify(exchange, times(0)).setExchangeState(ExchangeState.STARTED);
        order.verify(rpc.getExchangeDao()).updateExchange(exchange);
    }

    /** Test saveExchange() exception conditions. */
    @Test public void testSaveExchangeException() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        Exchange exchange = new Exchange();
        exchange.setId(12L);
        exchange.setUserId("me");
        CedarRuntimeException cause = new CedarRuntimeException("Hello");
        doThrow(cause).when(rpc.getExchangeDao()).updateExchange(exchange);

        try {
            rpc.saveExchange(exchange);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(cause, e.getCause());
        }
    }

    /** Test sendNotifications(). */
    @Test public void testSendNotifications() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        Exchange exchange = mock(Exchange.class);
        when(exchange.getId()).thenReturn(42L);

        Assignment assignment1 = mock(Assignment.class);
        AssignmentSet assignments1 = new AssignmentSet();
        assignments1.add(assignment1);

        Assignment assignment2 = mock(Assignment.class);
        AssignmentSet assignments2 = new AssignmentSet();
        assignments1.add(assignment2);

        AuditEvent event1 = mock(AuditEvent.class);
        when(rpc.getExchangeService().sendMessages(exchange, assignments1, false)).thenReturn(15);  // so we can tell recipients count comes from here
        when(rpc.getAuditEventService().buildExchangeEmailEvent(42L, 15)).thenReturn(event1);   // if event gets returned, method was invoked properly

        AuditEvent event2 = mock(AuditEvent.class);
        when(rpc.getExchangeService().sendMessages(exchange, assignments2, false)).thenReturn(13);  // so we can tell recipients count comes from here
        when(rpc.getAuditEventService().buildExchangeEmailEvent(42L, 13)).thenReturn(event2);   // if event gets returned, method was invoked properly

        // blows up because the exchange has the wrong user id (doesn't match session)
        try {
            when(exchange.getUserId()).thenReturn("bogus");
            rpc.sendNotifications(exchange);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        // blows up because the exchange has the wrong user id (doesn't match session)
        try {
            when(exchange.getUserId()).thenReturn("bogus");
            rpc.sendNotifications(exchange);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        // works because session and exchange match
        // this time, we get valid assignments on first pass (auto-conflict true)
        when(exchange.getUserId()).thenReturn("me");
        when(rpc.getExchangeService().generateAssignments(exchange, true)).thenReturn(assignments1);
        when(rpc.getExchangeService().generateAssignments(exchange, false)).thenReturn(assignments2);
        Exchange result = rpc.sendNotifications(exchange);
        assertSame(exchange, result); // get back the same object, just updated per validations below
        InOrder order = Mockito.inOrder(exchange, rpc.getExchangeDao(), rpc.getExchangeService(), rpc.getAuditEventService());
        order.verify(rpc.getExchangeService()).generateAssignments(exchange, true);
        order.verify(rpc.getExchangeService()).sendMessages(exchange, assignments1, false);
        order.verify(exchange).setAssignments(assignments1);
        order.verify(exchange).setExchangeState(ExchangeState.SENT);
        order.verify(rpc.getExchangeDao()).updateExchange(exchange);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event1);

        // works because session and exchange match
        // this time, we get valid assignments on second pass (auto-conflict false)
        when(exchange.getUserId()).thenReturn("me");
        when(rpc.getExchangeService().generateAssignments(exchange, true)).thenThrow(new InvalidDataException("whatever"));
        when(rpc.getExchangeService().generateAssignments(exchange, false)).thenReturn(assignments2);
        result = rpc.sendNotifications(exchange);
        assertSame(exchange, result); // get back the same object, just updated per validations below
        order = Mockito.inOrder(exchange, rpc.getExchangeDao(), rpc.getExchangeService(), rpc.getAuditEventService());
        order.verify(rpc.getExchangeService()).generateAssignments(exchange, true);
        order.verify(rpc.getExchangeService()).generateAssignments(exchange, false);
        order.verify(rpc.getExchangeService()).sendMessages(exchange, assignments2, false);
        order.verify(exchange).setAssignments(assignments2);
        order.verify(exchange).setExchangeState(ExchangeState.SENT);
        order.verify(rpc.getExchangeDao()).updateExchange(exchange);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event2);
    }

    /** Test sendNotifications() exception conditions. */
    @Test public void testSendNotificationsException() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        InvalidDataException invalidException1 = new InvalidDataException("invalid1");
        InvalidDataException invalidException2 = new InvalidDataException("invalid2");
        Exchange invalidExchange = new Exchange();
        invalidExchange.setId(12L);
        invalidExchange.setUserId("me");
        when(rpc.getExchangeService().generateAssignments(invalidExchange, true)).thenThrow(invalidException1);
        when(rpc.getExchangeService().generateAssignments(invalidExchange, false)).thenThrow(invalidException2);

        try {
            rpc.sendNotifications(invalidExchange);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(invalidException2, e);
        }

        CedarRuntimeException runtimeException = new CedarRuntimeException("runtime");
        Exchange runtimeExchange = new Exchange();
        runtimeExchange.setId(13L);
        runtimeExchange.setUserId("me");
        when(rpc.getExchangeService().generateAssignments(runtimeExchange, true)).thenThrow(runtimeException);

        try {
            rpc.sendNotifications(runtimeExchange);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(runtimeException, e.getCause());
        }
    }

    /** Test resendNotification(). */
    @Test public void testResendNotification() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        Exchange exchange = mock(Exchange.class, Mockito.RETURNS_DEEP_STUBS);
        when(exchange.getId()).thenReturn(42L);

        Participant giver = mock(Participant.class);
        Participant receiver = mock(Participant.class);
        ParticipantSet participants = new ParticipantSet();
        participants.add(giver);

        Assignment assignment = new Assignment(giver, receiver);
        AssignmentSet assignments = new AssignmentSet();
        assignments.add(assignment);

        AuditEvent event = mock(AuditEvent.class);
        when(rpc.getAuditEventService().buildResendEmailsEvent(42L, 1)).thenReturn(event);   // if event gets returned, method was invoked properly

        // blows up because the exchange has the wrong user id (doesn't match session)
        try {
            when(exchange.getUserId()).thenReturn("bogus");
            rpc.resendNotification(exchange, participants);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        // Nothing happens here because the participant isn't in the exchange already
        when(exchange.getUserId()).thenReturn("me");
        when(exchange.getAssignments().getGiftReceiver(giver)).thenReturn(null);
        Exchange result = rpc.resendNotification(exchange, participants);
        assertSame(exchange, result); // get back the same object
        verifyNoMoreInteractions(rpc.getExchangeDao());
        verifyNoMoreInteractions(rpc.getExchangeService());

        // This works because the passed-in participant has an assignment
        when(exchange.getUserId()).thenReturn("me");
        when(exchange.getAssignments().getGiftReceiver(giver)).thenReturn(receiver);
        result = rpc.resendNotification(exchange, participants);
        assertSame(exchange, result); // get back the same object
        InOrder order = Mockito.inOrder(rpc.getExchangeService(), rpc.getExchangeDao(), rpc.getAuditEventService());
        order.verify(rpc.getExchangeService()).sendMessages(exchange, assignments, false);
        order.verify(rpc.getAuditEventService()).logAuditEvent(event);
    }

    /** Test resendNotification() for exception conditions (case 1). */
    @Test public void testResendNotificationException1() {
        ExchangeRpc rpc = createRpc();
        Exchange exchange = mock(Exchange.class);

        Participant giver = mock(Participant.class);
        Participant receiver = mock(Participant.class);
        ParticipantSet participants = new ParticipantSet();
        participants.add(giver);
        participants.add(receiver);

        CedarRuntimeException runtimeException = new CedarRuntimeException("runtime");
        when(exchange.getUserId()).thenThrow(runtimeException);

        try {
            rpc.resendNotification(exchange, participants);
        } catch (ServiceException e) {
            assertSame(runtimeException, e.getCause());
        }
    }

    /** Test resendNotification() for exception conditions (case 2). */
    @Test public void testResendNotificationException2() {
        ExchangeRpc rpc = createRpc();
        Exchange exchange = mock(Exchange.class);

        Participant giver = mock(Participant.class);
        Participant receiver = mock(Participant.class);
        ParticipantSet participants = new ParticipantSet();
        participants.add(giver);
        participants.add(receiver);

        InvalidDataException invalidException = new InvalidDataException("invalid");
        when(exchange.getUserId()).thenThrow(invalidException);

        try {
            rpc.resendNotification(exchange, participants);
        } catch (InvalidDataException e) {
            assertSame(invalidException, e);
        }
    }

    /** Test generatePreview(). */
    @Test public void testGeneratePreview() {
        ExchangeRpc rpc = createRpc();
        Exchange exchange = mock(Exchange.class);
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        // blows up because the exchange has the wrong user id (doesn't match session)
        try {
            when(exchange.getUserId()).thenReturn("bogus");
            rpc.generatePreview(exchange);
            fail("Expected RpcSecurityException");
        } catch (RpcSecurityException e) { }

        when(exchange.getUserId()).thenReturn("me");
        rpc.generatePreview(exchange);
        verify(rpc.getExchangeService()).generatePreview(exchange);
    }

    /** Test generatePreview() exception conditions. */
    @Test public void testGeneratePreviewException() {
        ExchangeRpc rpc = createRpc();
        when(rpc.getClientSessionService().retrieveClientSession().getCurrentUser().getUserId()).thenReturn("me");

        InvalidDataException invalidException = new InvalidDataException("runtime");
        Exchange invalidExchange = new Exchange();
        invalidExchange.setId(12L);
        invalidExchange.setUserId("me");
        when(rpc.getExchangeService().generatePreview(invalidExchange)).thenThrow(invalidException);

        try {
            rpc.generatePreview(invalidExchange);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(invalidException, e);
        }

        CedarRuntimeException runtimeException = new CedarRuntimeException("runtime");
        Exchange runtimeExchange = new Exchange();
        runtimeExchange.setId(13L);
        runtimeExchange.setUserId("me");
        when(rpc.getExchangeService().generatePreview(runtimeExchange)).thenThrow(runtimeException);

        try {
            rpc.generatePreview(runtimeExchange);
            fail("Expected ServiceException");
        } catch (ServiceException e) {
            assertSame(runtimeException, e.getCause());
        }
    }

    /** Create a mocked service. */
    private static ExchangeRpc createRpc() {
        IAuditEventService auditEventService = mock(IAuditEventService.class);
        IExchangeService exchangeService = mock(IExchangeService.class);
        IClientSessionService clientSessionService = mock(IClientSessionService.class, Mockito.RETURNS_DEEP_STUBS);
        IExchangeDao exchangeDao = mock(IExchangeDao.class);

        ExchangeRpc rpc = new ExchangeRpc();
        rpc.setAuditEventService(auditEventService);
        rpc.setExchangeService(exchangeService);
        rpc.setClientSessionService(clientSessionService);
        rpc.setExchangeDao(exchangeDao);
        rpc.afterPropertiesSet();

        return rpc;
    }

}
