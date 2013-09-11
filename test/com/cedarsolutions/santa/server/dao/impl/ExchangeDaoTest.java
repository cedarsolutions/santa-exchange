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
package com.cedarsolutions.santa.server.dao.impl;

import static com.cedarsolutions.junit.util.Assertions.assertIteratorSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.dao.gae.impl.DaoObjectifyService;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.cedarsolutions.santa.shared.domain.exchange.Organizer;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.santa.shared.domain.exchange.TemplateConfig;
import com.cedarsolutions.shared.domain.email.EmailFormat;

/**
 * Unit tests for ExchangeDao.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeDaoTest extends DaoTestCase {

    /** Test the constructor, getters, and setters. */
    @Test public void testConstructorGettersSetters() {
        ExchangeDao dao = new ExchangeDao();
        assertNotNull(dao);
        assertNull(dao.getDaoObjectifyService());

        DaoObjectifyService daoObjectifyService = mock(DaoObjectifyService.class);
        dao.setDaoObjectifyService(daoObjectifyService);
        assertSame(daoObjectifyService, dao.getDaoObjectifyService());
    }

    /** Test the afterPropertiesSet() method. */
    @Test public void testAfterPropertiesSet() {
        ExchangeDao dao = new ExchangeDao();
        DaoObjectifyService daoObjectifyService = mock(DaoObjectifyService.class);

        dao.setDaoObjectifyService(daoObjectifyService);
        dao.afterPropertiesSet();

        try {
            dao.setDaoObjectifyService(null);
            dao.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test basic insert, retrieve, and delete operations. */
    @Test public void testBasicOperations() {
        ExchangeDao dao = new ExchangeDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        Exchange exchange = buildExchange("userId");

        Long exchangeId = dao.insertExchange(exchange);
        exchange.setId(exchangeId);  // set this so we can use equality check below
        Exchange retrieved = dao.retrieveExchange(exchangeId);
        assertEquals(exchange, retrieved);

        dao.deleteExchange(exchangeId);
        retrieved = dao.retrieveExchange(exchangeId);
        assertNull(retrieved);

        dao.deleteExchange(2222L);  // make sure it works even if the event doesn't exist
        dao.deleteExchange(exchange);  // make sure it works for an exchange that's already deleted

        exchangeId = dao.insertExchange(exchange);
        retrieved = dao.retrieveExchange(exchangeId);
        dao.deleteExchange(retrieved);
        retrieved = dao.retrieveExchange(exchangeId);
        assertNull(retrieved);
    }

    /** Test updateExchange. */
    @Test public void testUpdateExchange() {
        ExchangeDao dao = new ExchangeDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        Exchange exchange = buildExchange("userId");
        Long id = dao.insertExchange(exchange);
        exchange.setId(id);

        Exchange result = dao.retrieveExchange(id);
        assertEquals(exchange, result);

        exchange.setExchangeState(ExchangeState.GENERATED);  // spot-check just one field
        dao.updateExchange(exchange);
        result = dao.retrieveExchange(id);
        assertEquals(exchange, result);
    }

    /** Test retrieveExchanges(). */
    @Test public void retrieveExchanges() {
        ExchangeCriteria criteria;
        Iterator<Exchange> iterator;
        List<Exchange> list;

        ExchangeDao dao = new ExchangeDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        Exchange exchange1 = buildExchange("user1");
        Exchange exchange2 = buildExchange("user2");
        Exchange exchange3 = buildExchange("user3");
        Exchange exchange4 = buildExchange("user3");

        // Note: inserted out-of-order to check sorting
        Long exchangeId2 = dao.insertExchange(exchange2);
        Long exchangeId1 = dao.insertExchange(exchange1);
        Long exchangeId3 = dao.insertExchange(exchange3);
        Long exchangeId4 = dao.insertExchange(exchange4);

        // Fill in the returned ids, so equality checks work
        exchange1.setId(exchangeId1);
        exchange2.setId(exchangeId2);
        exchange3.setId(exchangeId3);
        exchange4.setId(exchangeId4);

        // they have to specify at least a user id
        criteria = buildCriteria(null);
        try {
            dao.retrieveExchanges(criteria);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) { }

        criteria = buildCriteria("user4");
        iterator = dao.retrieveExchanges(criteria);
        assertIteratorSize(0, iterator);

        criteria = buildCriteria("user4", exchange1.getId());
        iterator = dao.retrieveExchanges(criteria);
        assertIteratorSize(0, iterator);

        criteria = buildCriteria("user1");
        iterator = dao.retrieveExchanges(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("user1", list.get(0).getUserId());
        assertEquals(exchange1, list.get(0));

        criteria = buildCriteria("user2");
        iterator = dao.retrieveExchanges(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("user2", list.get(0).getUserId());
        assertEquals(exchange2, list.get(0));

        criteria = buildCriteria("user3");
        iterator = dao.retrieveExchanges(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals(exchange3, list.get(0));
        assertEquals(exchange4, list.get(1));

        criteria = buildCriteria("user3", exchange3.getId());
        iterator = dao.retrieveExchanges(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals(exchange3, list.get(0));

        criteria = buildCriteria("user3", exchange4.getId());
        iterator = dao.retrieveExchanges(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals(exchange4, list.get(0));

        criteria = buildCriteria("user3", exchange1.getId(), exchange3.getId(), exchange4.getId());
        iterator = dao.retrieveExchanges(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals(exchange3, list.get(0));
        assertEquals(exchange4, list.get(1));
    }

    /** Test retrieveExchanges(), checking pagination. */
    @Test public void retrieveExchangesPagination() {
        ExchangeCriteria criteria;
        PaginatedResults<Exchange> results;
        Pagination pagination;

        ExchangeDao dao = new ExchangeDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        Exchange exchange1 = buildExchange("user1");
        Exchange exchange2 = buildExchange("user1");
        Exchange exchange3 = buildExchange("user1");
        Exchange exchange4 = buildExchange("user1");
        Exchange exchange5 = buildExchange("user1");

        Long exchangeId1 = dao.insertExchange(exchange1);
        Long exchangeId2 = dao.insertExchange(exchange2);
        Long exchangeId3 = dao.insertExchange(exchange3);
        Long exchangeId4 = dao.insertExchange(exchange4);
        Long exchangeId5 = dao.insertExchange(exchange5);

        // Set the ids so we can use equality checks
        exchange1.setId(exchangeId1);
        exchange2.setId(exchangeId2);
        exchange3.setId(exchangeId3);
        exchange4.setId(exchangeId4);
        exchange5.setId(exchangeId5);

        pagination = new Pagination(2); // 2 elements per page
        criteria = buildCriteria("user1");

        results = dao.retrieveExchanges(criteria, pagination);
        assertEquals(2, results.size());
        assertEquals(exchange1, results.get(0));
        assertEquals(exchange2, results.get(1));

        results = dao.retrieveExchanges(criteria, results.getPagination().next());
        assertEquals(2, results.size());
        assertEquals(exchange3, results.get(0));
        assertEquals(exchange4, results.get(1));

        results = dao.retrieveExchanges(criteria, results.getPagination().next());
        assertEquals(1, results.size());
        assertEquals(exchange5, results.get(0));

        results = dao.retrieveExchanges(criteria, results.getPagination().next());
        assertEquals(1, results.size());
        assertEquals(exchange5, results.get(0));  // can't step off the end
    }

    /** Build user criteria for testing. */
    private static ExchangeCriteria buildCriteria(String userId, Long ... exchangeIds) {
        ExchangeCriteria criteria = new ExchangeCriteria();

        criteria.setUserId(userId);
        criteria.setExchangeIds(exchangeIds);

        return criteria;
    }

    /** Build a dummy exchange for testing. */
    private static Exchange buildExchange(String userId) {
        Organizer organizer = new Organizer();
        organizer.setName("organizer");
        organizer.setEmailAddress("organizer@example.com");
        organizer.setPhoneNumber("555-1212");

        TemplateConfig overrides = new TemplateConfig();
        overrides.setSenderName("Santa Exchange");
        overrides.setEmailFormat(EmailFormat.MULTIPART);
        overrides.setTemplateGroup("group");
        overrides.setTemplateGroup("name");

        ParticipantSet conflicts = new ParticipantSet();
        conflicts.add(new Participant(3L));

        Participant participant1 = new Participant(1L, "p1", "p1n", "p1@example.com", conflicts);
        Participant participant2 = new Participant(2L, "p2", "p2n", "p2@example.com", null);

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant1);
        participants.add(participant2);

        Assignment assignment1 = new Assignment(participant1, participant2);
        Assignment assignment2 = new Assignment(participant2, participant1);

        AssignmentSet assignments = new AssignmentSet();
        assignments.add(assignment1);
        assignments.add(assignment2);

        Exchange exchange = new Exchange();
        exchange.setUserId(userId);
        exchange.setExchangeState(ExchangeState.NEW);
        exchange.setName("name");
        exchange.setDateAndTime("dateAndTime");
        exchange.setTheme("theme");
        exchange.setCost("cost");
        exchange.setExtraInfo("extraInfo");
        exchange.setOrganizer(organizer);
        exchange.setTemplateOverrides(overrides);
        exchange.setParticipants(participants);
        exchange.setAssignments(assignments);

        return exchange;
    }

}
