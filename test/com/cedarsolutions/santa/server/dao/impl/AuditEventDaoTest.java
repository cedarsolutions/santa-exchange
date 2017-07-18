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
package com.cedarsolutions.santa.server.dao.impl;

import static com.cedarsolutions.junit.util.Assertions.assertIteratorSize;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.ADMIN_LOGIN;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.DELETE_USER;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.LOCK_USER;
import static com.cedarsolutions.santa.shared.domain.audit.AuditEventType.REGISTER_USER;
import static com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey.MODULE;
import static com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.dao.gae.impl.DaoObjectifyService;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventCriteria;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for AuditEventDao.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventDaoTest extends DaoTestCase {

    /** Test the constructor, getters, and setters. */
    @Test public void testConstructorGettersSetters() {
        AuditEventDao dao = new AuditEventDao();
        assertNotNull(dao);
        assertNull(dao.getDaoObjectifyService());

        DaoObjectifyService daoObjectifyService = mock(DaoObjectifyService.class);
        dao.setDaoObjectifyService(daoObjectifyService);
        assertSame(daoObjectifyService, dao.getDaoObjectifyService());
    }

    /** Test the afterPropertiesSet() method. */
    @Test public void testAfterPropertiesSet() {
        AuditEventDao dao = new AuditEventDao();
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
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        ClientSession session = new ClientSession();
        session.setSessionId("12345");

        ExtraData extra1 = new ExtraData(MODULE, "module");
        ExtraData extra2 = new ExtraData(USER_ID, "user");

        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setEventType(ADMIN_LOGIN);
        auditEvent.setExtraData(extra1, extra2);
        auditEvent.setSessionId(session.getSessionId());

        Long eventId = dao.insertAuditEvent(auditEvent);
        AuditEvent retrieved = dao.retrieveAuditEvent(eventId);
        assertEquals(eventId, retrieved.getEventId());
        auditEvent.setEventId(eventId);  // set the correct event id so we can just use .equals() below
        assertEquals(auditEvent, retrieved);

        dao.deleteAuditEvent(2222L);  // make sure it works even if the event doesn't exist

        dao.deleteAuditEvent(eventId);
        retrieved = dao.retrieveAuditEvent(eventId);
        assertNull(retrieved);

        eventId = dao.insertAuditEvent(auditEvent);
        retrieved = dao.retrieveAuditEvent(eventId);
        assertEquals(eventId, retrieved.getEventId());
        dao.deleteAuditEvent(retrieved);
        retrieved = dao.retrieveAuditEvent(eventId);
        assertNull(retrieved);
    }

    /** Test retrieveAuditEvents(), focusing on date range. */
    @Test public void retrieveAuditEventsDateRange() {
        Date start;
        Date end;
        AuditEventCriteria criteria;
        Iterator<AuditEvent> iterator;
        List<AuditEvent> list;

        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent1 = buildEvent("event1", "2011-06-14T17:00:00.000");
        AuditEvent auditEvent2 = buildEvent("event2", "2011-06-14T17:00:00.001");
        AuditEvent auditEvent3 = buildEvent("event3", "2011-06-14T17:01:00.000");
        AuditEvent auditEvent4 = buildEvent("event4", "2011-06-14T17:01:59.999");
        AuditEvent auditEvent5 = buildEvent("event5", "2011-06-14T17:02:00.000");

        // Note: inserted out-of-order to check sorting
        dao.insertAuditEvent(auditEvent3);
        dao.insertAuditEvent(auditEvent1);
        dao.insertAuditEvent(auditEvent4);
        dao.insertAuditEvent(auditEvent5);
        dao.insertAuditEvent(auditEvent2);

        try {
            start = null;
            end = null;
            criteria = buildCriteria(start, end);
            iterator = dao.retrieveAuditEvents(criteria);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) { }

        start = null;
        end = DateUtils.createDate("2011-06-14T16:59:59.999");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        assertIteratorSize(0, iterator);

        start = DateUtils.createDate("2011-06-14T17:02:00.001");
        end = null;
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        assertIteratorSize(0, iterator);

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = null;
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.001");
        end = null;
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(4, iterator);
        assertEquals("event2", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(3).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:01.000");
        end = null;
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("event3", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(2).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:01:59.999");
        end = null;
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("event4", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(1).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:02:00.000");
        end = null;
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event5", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:02:00.000");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:01:59.999");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(4, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:01:59.998");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.001");
        end = DateUtils.createDate("2011-06-14T17:01:59.998");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("event2", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(1).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.002");
        end = DateUtils.createDate("2011-06-14T17:01:59.998");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event3", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.002");
        end = DateUtils.createDate("2011-06-14T17:00:59.999");
        criteria = buildCriteria(start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        assertIteratorSize(0, iterator);
    }

    /** Test retrieveAuditEvents(), focusing on event type. */
    @Test public void retrieveAuditEventsEventType() {
        AuditEventCriteria criteria;
        Iterator<AuditEvent> iterator;
        List<AuditEvent> list;

        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent1 = buildEvent(REGISTER_USER, "event1", "2011-06-14T17:00:00.000");
        AuditEvent auditEvent2 = buildEvent(DELETE_USER, "event2", "2011-06-14T17:00:00.001");
        AuditEvent auditEvent3 = buildEvent(LOCK_USER, "event3", "2011-06-14T17:01:00.000");
        AuditEvent auditEvent4 = buildEvent(REGISTER_USER, "event4", "2011-06-14T17:01:59.999");
        AuditEvent auditEvent5 = buildEvent(DELETE_USER, "event5", "2011-06-14T17:02:00.000");

        // Note: inserted out-of-order to check sorting
        dao.insertAuditEvent(auditEvent3);
        dao.insertAuditEvent(auditEvent1);
        dao.insertAuditEvent(auditEvent4);
        dao.insertAuditEvent(auditEvent5);
        dao.insertAuditEvent(auditEvent2);

        criteria = buildCriteria(ADMIN_LOGIN);
        iterator = dao.retrieveAuditEvents(criteria);
        assertIteratorSize(0, iterator);

        criteria = buildCriteria(REGISTER_USER);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(1).getExtraData().get(0).getValue());

        criteria = buildCriteria(LOCK_USER);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event3", list.get(0).getExtraData().get(0).getValue());

        criteria = buildCriteria(DELETE_USER);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("event2", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(1).getExtraData().get(0).getValue());

        criteria = buildCriteria(REGISTER_USER, LOCK_USER);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(2).getExtraData().get(0).getValue());

        criteria = buildCriteria(REGISTER_USER, LOCK_USER, DELETE_USER);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());

        criteria = buildCriteria(REGISTER_USER, LOCK_USER, DELETE_USER, ADMIN_LOGIN);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());

        criteria.setEventTypes();
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());
    }

    /** Test retrieveAuditEvents(), focusing on user id. */
    @Test public void retrieveAuditEventsUserId() {
        AuditEventCriteria criteria;
        Iterator<AuditEvent> iterator;
        List<AuditEvent> list;

        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent1 = buildEvent("user1", "event1", "2011-06-14T17:00:00.000");
        AuditEvent auditEvent2 = buildEvent("user2", "event2", "2011-06-14T17:00:00.001");
        AuditEvent auditEvent3 = buildEvent("user3", "event3", "2011-06-14T17:01:00.000");
        AuditEvent auditEvent4 = buildEvent("user1", "event4", "2011-06-14T17:01:59.999");
        AuditEvent auditEvent5 = buildEvent("user2", "event5", "2011-06-14T17:02:00.000");

        // Note: inserted out-of-order to check sorting
        dao.insertAuditEvent(auditEvent3);
        dao.insertAuditEvent(auditEvent1);
        dao.insertAuditEvent(auditEvent4);
        dao.insertAuditEvent(auditEvent5);
        dao.insertAuditEvent(auditEvent2);

        criteria = buildCriteria("user4");
        iterator = dao.retrieveAuditEvents(criteria);
        assertIteratorSize(0, iterator);

        criteria = buildCriteria("user4", "user5");
        iterator = dao.retrieveAuditEvents(criteria);
        assertIteratorSize(0, iterator);

        criteria = buildCriteria("user1");
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(1).getExtraData().get(0).getValue());

        criteria = buildCriteria("user3");
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event3", list.get(0).getExtraData().get(0).getValue());

        criteria.setUserIds();
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());
    }

    /** Test retrieveAuditEvents(), for queries with all indexed fields. */
    @Test public void retrieveAuditEventsAllIndexes() {
        Date start;
        Date end;
        AuditEventCriteria criteria;
        Iterator<AuditEvent> iterator;
        List<AuditEvent> list;

        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent1 = buildEvent(REGISTER_USER, "user1", "event1", "2011-06-14T17:00:00.000");
        AuditEvent auditEvent2 = buildEvent(DELETE_USER, "user1", "event2", "2011-06-14T17:00:00.001");
        AuditEvent auditEvent3 = buildEvent(LOCK_USER, "user2", "event3", "2011-06-14T17:01:00.000");
        AuditEvent auditEvent4 = buildEvent(REGISTER_USER, "user2", "event4", "2011-06-14T17:01:59.999");
        AuditEvent auditEvent5 = buildEvent(DELETE_USER, "user3", "event5", "2011-06-14T17:02:00.000");

        // Note: inserted out-of-order to check sorting
        dao.insertAuditEvent(auditEvent3);
        dao.insertAuditEvent(auditEvent1);
        dao.insertAuditEvent(auditEvent4);
        dao.insertAuditEvent(auditEvent5);
        dao.insertAuditEvent(auditEvent2);

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:00:00.000");
        criteria = buildCriteria(REGISTER_USER, "user1", start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.001");
        end = DateUtils.createDate("2011-06-14T17:00:00.001");
        criteria = buildCriteria(DELETE_USER, "user1", start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event2", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:01:00.000");
        end = DateUtils.createDate("2011-06-14T17:01:00.000");
        criteria = buildCriteria(LOCK_USER, "user2", start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event3", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:01:59.999");
        end = DateUtils.createDate("2011-06-14T17:01:59.999");
        criteria = buildCriteria(REGISTER_USER, "user2", start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event4", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:02:00.000");
        end = DateUtils.createDate("2011-06-14T17:02:00.000");
        criteria = buildCriteria(DELETE_USER, "user3", start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event5", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:00:00.000");
        criteria = buildCriteria(new AuditEventType[] { REGISTER_USER, DELETE_USER, },
                                 new String[] { "user1", }, start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:00:00.001");
        criteria = buildCriteria(new AuditEventType[] { REGISTER_USER, DELETE_USER, },
                                 new String[] { "user1", }, start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:02:00.000");
        criteria = buildCriteria(new AuditEventType[] { REGISTER_USER, DELETE_USER, },
                                 new String[] { "user1", "user2", }, start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(2).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:02:00.000");
        criteria = buildCriteria(new AuditEventType[] { REGISTER_USER, DELETE_USER, },
                                 new String[] { "user1", "user2", "user3", }, start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(4, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(3).getExtraData().get(0).getValue());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:02:00.000");
        criteria = buildCriteria(new AuditEventType[] { REGISTER_USER, DELETE_USER, LOCK_USER, },
                                 new String[] { "user1", "user2", "user3", }, start, end);
        iterator = dao.retrieveAuditEvents(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("event1", list.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", list.get(1).getExtraData().get(0).getValue());
        assertEquals("event3", list.get(2).getExtraData().get(0).getValue());
        assertEquals("event4", list.get(3).getExtraData().get(0).getValue());
        assertEquals("event5", list.get(4).getExtraData().get(0).getValue());
    }

    /** Test retrieveAuditEvents(), checking pagination. */
    @Test public void retrieveAuditEventsPagination() {
        Date start;
        Date end;
        Pagination pagination;
        AuditEventCriteria criteria;
        PaginatedResults<AuditEvent> results;

        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent1 = buildEvent(DELETE_USER, "user1", "event1", "2011-06-14T17:01:00.000");
        AuditEvent auditEvent2 = buildEvent(DELETE_USER, "user1", "event2", "2011-06-14T17:02:00.000");
        AuditEvent auditEvent3 = buildEvent(REGISTER_USER, "user2", "event3", "2011-06-14T17:03:00.000");
        AuditEvent auditEvent4 = buildEvent(DELETE_USER, "user2", "event4", "2011-06-14T17:04:00.000");
        AuditEvent auditEvent5 = buildEvent(DELETE_USER, "user3", "event5", "2011-06-14T17:05:00.000");
        AuditEvent auditEvent6 = buildEvent(REGISTER_USER, "user3", "event6", "2011-06-14T17:06:00.000");
        AuditEvent auditEvent7 = buildEvent(REGISTER_USER, "user3", "event7", "2011-06-14T17:07:00.000");
        AuditEvent auditEvent8 = buildEvent(DELETE_USER, "user3", "event8", "2011-06-14T17:08:00.000");
        AuditEvent auditEvent9 = buildEvent(DELETE_USER, "user3", "event9", "2011-06-14T17:09:00.000");
        AuditEvent auditEvent10 = buildEvent(DELETE_USER, "user3", "event10", "2011-06-14T17:10:00.000");
        AuditEvent auditEvent11 = buildEvent(REGISTER_USER, "user3", "event11", "2011-06-14T17:11:00.000");

        // Note: inserted out-of-order to check sorting
        dao.insertAuditEvent(auditEvent3);
        dao.insertAuditEvent(auditEvent1);
        dao.insertAuditEvent(auditEvent9);
        dao.insertAuditEvent(auditEvent4);
        dao.insertAuditEvent(auditEvent7);
        dao.insertAuditEvent(auditEvent5);
        dao.insertAuditEvent(auditEvent2);
        dao.insertAuditEvent(auditEvent6);
        dao.insertAuditEvent(auditEvent11);
        dao.insertAuditEvent(auditEvent8);
        dao.insertAuditEvent(auditEvent10);

        start = DateUtils.createDate("2011-06-14T17:01:00.000");
        end = DateUtils.createDate("2011-06-14T17:11:00.000");
        criteria = buildCriteria(DELETE_USER, start, end);

        pagination = new Pagination(2); // 2 elements per page
        results = dao.retrieveAuditEvents(criteria, pagination);
        assertEquals(2, results.size());
        assertEquals("event1", results.get(0).getExtraData().get(0).getValue());
        assertEquals("event2", results.get(1).getExtraData().get(0).getValue());

        results = dao.retrieveAuditEvents(criteria, results.getPagination().next());
        assertEquals(2, results.size());
        assertEquals("event4", results.get(0).getExtraData().get(0).getValue());
        assertEquals("event5", results.get(1).getExtraData().get(0).getValue());

        results = dao.retrieveAuditEvents(criteria, results.getPagination().next());
        assertEquals(2, results.size());
        assertEquals("event8", results.get(0).getExtraData().get(0).getValue());
        assertEquals("event9", results.get(1).getExtraData().get(0).getValue());

        results = dao.retrieveAuditEvents(criteria, results.getPagination().next());
        assertEquals(1, results.size());
        assertEquals("event10", results.get(0).getExtraData().get(0).getValue());

        results = dao.retrieveAuditEvents(criteria, results.getPagination().next());
        assertEquals(1, results.size());
        assertEquals("event10", results.get(0).getExtraData().get(0).getValue());  // can't step off the end
    }

    /** Test insertAuditEvent() when an exception is thrown. */
    @Test public void testInsertAuditEventException() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent = new AuditEvent();
        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().put(auditEvent)).thenThrow(exception);

        try {
            dao.insertAuditEvent(auditEvent);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test insertAuditEvent() when a DaoException is thrown. */
    @Test public void testInsertAuditEventDaoException() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        AuditEvent auditEvent = new AuditEvent();
        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().put(auditEvent)).thenThrow(exception);

        try {
            dao.insertAuditEvent(auditEvent);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveAuditEvent() when an exception is thrown. */
    @Test public void testRetrieveAuditEventException() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(AuditEvent.class, 12L)).thenThrow(exception);

        try {
            dao.retrieveAuditEvent(12L);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test retrieveAuditEvent() when a DaoException is thrown. */
    @Test public void testRetrieveAuditEventDaoException() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(AuditEvent.class, 12L)).thenThrow(exception);

        try {
            dao.retrieveAuditEvent(12L);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Test deleteAuditEvent() when an exception is thrown. */
    @Test public void testDeleteAuditEventException() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(AuditEvent.class, 12L)).thenThrow(exception);

        try {
            dao.deleteAuditEvent(12L);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test deleteAuditEvent() when a DaoException is thrown. */
    @Test public void testDeleteAuditEventDaoException() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(AuditEvent.class, 12L)).thenThrow(exception);

        try {
            dao.deleteAuditEvent(12L);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test retrieveAuditEvents(AuditEventCriteria, Pagination) when an exception is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveAuditEventsException1() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveAuditEvents(new AuditEventCriteria(), new Pagination());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test retrieveAuditEvents(AuditEventCriteria, Pagination) when an InvalidDataException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveAuditEventsException2() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        InvalidDataException exception = new InvalidDataException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveAuditEvents(new AuditEventCriteria(), new Pagination());
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveAuditEvents(AuditEventCriteria, Pagination) when a DaoException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveAuditEventsException3() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveAuditEvents(new AuditEventCriteria(), new Pagination());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveAuditEvents(AuditEventCriteria) when an exception is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveAuditEventsException4() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveAuditEvents(new AuditEventCriteria());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test retrieveAuditEvents(AuditEventCriteria) when an InvalidDataException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveAuditEventsException5() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        InvalidDataException exception = new InvalidDataException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveAuditEvents(new AuditEventCriteria());
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveAuditEvents(AuditEventCriteria) when a DaoException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveAuditEventsException6() {
        AuditEventDao dao = new AuditEventDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveAuditEvents(new AuditEventCriteria());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Build date criteria for testing. */
    private static AuditEventCriteria buildCriteria(Date start, Date end) {
        AuditEventCriteria criteria = new AuditEventCriteria();
        criteria.setStartDate(start);
        criteria.setEndDate(end);
        return criteria;
    }

    /** Build date criteria for testing. */
    private static AuditEventCriteria buildCriteria(AuditEventType eventType, String userId, Date start, Date end) {
        AuditEventCriteria criteria = new AuditEventCriteria();
        criteria.setStartDate(start);
        criteria.setEndDate(end);
        criteria.setEventTypes(eventType);
        criteria.setUserIds(userId);
        return criteria;
    }

    /** Build date criteria for testing. */
    private static AuditEventCriteria buildCriteria(AuditEventType[] eventTypes, String[] userIds, Date start, Date end) {
        AuditEventCriteria criteria = new AuditEventCriteria();
        criteria.setStartDate(start);
        criteria.setEndDate(end);
        criteria.setEventTypes(eventTypes);
        criteria.setUserIds(userIds);
        return criteria;
    }

    /** Build date criteria for testing. */
    private static AuditEventCriteria buildCriteria(AuditEventType eventType, Date start, Date end) {
        AuditEventCriteria criteria = new AuditEventCriteria();
        criteria.setStartDate(start);
        criteria.setEndDate(end);
        criteria.setEventTypes(eventType);
        return criteria;
    }

    /** Build event type criteria for testing. */
    private static AuditEventCriteria buildCriteria(AuditEventType... eventTypes) {
        AuditEventCriteria criteria = new AuditEventCriteria();

        // Have to set some date, so hardcode the range we know we're creating
        criteria.setStartDate(DateUtils.createDate("2011-06-14T17:00:00.000"));
        criteria.setEndDate(DateUtils.createDate("2011-06-14T17:02:00.000"));

        criteria.setEventTypes(eventTypes);

        return criteria;
    }

    /** Build user id criteria for testing. */
    private static AuditEventCriteria buildCriteria(String... userIds) {
        AuditEventCriteria criteria = new AuditEventCriteria();

        // Have to set some date, so hardcode the range we know we're creating
        criteria.setStartDate(DateUtils.createDate("2011-06-14T17:00:00.000"));
        criteria.setEndDate(DateUtils.createDate("2011-06-14T17:02:00.000"));

        criteria.setUserIds(userIds);

        return criteria;
    }

    /** Build a dummy event for testing. */
    private static AuditEvent buildEvent(String module, String date) {
        return buildEvent(AuditEventType.ADMIN_LOGIN, module, date);
    }

    /** Build a dummy event for testing. */
    private static AuditEvent buildEvent(AuditEventType eventType, String module, String date) {
        return buildEvent(eventType, null, module, date);
    }

    /** Build a dummy event for testing. */
    private static AuditEvent buildEvent(String userId, String module, String date) {
        return buildEvent(ADMIN_LOGIN, userId, module, date);
    }

    /** Build a dummy event for testing. */
    private static AuditEvent buildEvent(AuditEventType eventType, String userId, String module, String date) {
        Date eventTimestamp = DateUtils.createDate(date);
        ExtraData extraData = new ExtraData(MODULE, module);

        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setEventType(eventType);
        auditEvent.setUserId(userId);
        auditEvent.setSessionId("1234");
        auditEvent.setExtraData(extraData);
        auditEvent.setEventTimestamp(eventTimestamp);

        return auditEvent;
    }

}
