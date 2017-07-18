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

import static com.cedarsolutions.junit.util.Assertions.assertAfter;
import static com.cedarsolutions.junit.util.Assertions.assertIteratorSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
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
import com.cedarsolutions.dao.domain.SortOrder;
import com.cedarsolutions.dao.gae.impl.DaoObjectifyService;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.DateUtils;
import com.cedarsolutions.util.RandomNumberUtils;

/**
 * Unit tests for RegisteredUserDao.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserDaoTest extends DaoTestCase {

    /** Test the constructor, getters, and setters. */
    @Test public void testConstructorGettersSetters() {
        RegisteredUserDao dao = new RegisteredUserDao();
        assertNotNull(dao);
        assertNull(dao.getDaoObjectifyService());

        DaoObjectifyService daoObjectifyService = mock(DaoObjectifyService.class);
        dao.setDaoObjectifyService(daoObjectifyService);
        assertSame(daoObjectifyService, dao.getDaoObjectifyService());
    }

    /** Test the afterPropertiesSet() method. */
    @Test public void testAfterPropertiesSet() {
        RegisteredUserDao dao = new RegisteredUserDao();
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
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        String userId = RandomNumberUtils.generateRandomAlphanumericString(10);
        RegisteredUser registeredUser = createRegisteredUser(userId);

        RegisteredUser retrieved = dao.retrieveRegisteredUser(userId);
        assertNull(retrieved);

        try {
            // update can never insert a new record
            dao.updateRegisteredUser(registeredUser);
            fail("Expected DaoException");
        } catch (DaoException e) {
            retrieved = dao.retrieveRegisteredUser(userId);
            assertNull(retrieved);
        }

        dao.insertRegisteredUser(registeredUser);

        retrieved = dao.retrieveRegisteredUser(userId);
        assertEquals(registeredUser, retrieved);

        registeredUser.setEmailAddress("email");
        dao.updateRegisteredUser(registeredUser);

        retrieved = dao.retrieveRegisteredUser(registeredUser.getUserId());
        assertEquals(registeredUser, retrieved);

        dao.deleteRegisteredUser("blech");  // make sure it works even if the user doesn't exist

        dao.deleteRegisteredUser(registeredUser.getUserId());

        retrieved = dao.retrieveRegisteredUser(registeredUser.getUserId());
        assertNull(retrieved);

        dao.insertRegisteredUser(registeredUser);
        retrieved = dao.retrieveRegisteredUser(userId);
        assertEquals(registeredUser, retrieved);
        dao.deleteRegisteredUser(registeredUser);
        retrieved = dao.retrieveRegisteredUser(registeredUser.getUserId());
        assertNull(retrieved);
    }

    /** Test unlockRegisteredUser(). */
    @Test public void testUnlockRegisteredUser() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user1 = buildUser("userId1", "userNameA");
        RegisteredUser user2 = buildUser("userId2", "userNameB");
        dao.insertRegisteredUser(user1);

        user1.setLocked(true);
        RegisteredUser result1 = dao.unlockRegisteredUser(user1);
        assertEquals(user1.getUserId(), result1.getUserId());
        assertFalse(result1.isLocked());

        RegisteredUser result2 = dao.retrieveRegisteredUser(result1.getUserId());
        assertEquals(result1, result2);

        try {
            dao.unlockRegisteredUser(user2);
            fail("Expected DaoException");
        } catch (DaoException e) { }
    }

    /** Test lockRegisteredUser(). */
    @Test public void testLockRegisteredUser() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user1 = buildUser("userId1", "userNameA");
        RegisteredUser user2 = buildUser("userId2", "userNameB");
        dao.insertRegisteredUser(user1);

        user1.setLocked(false);
        RegisteredUser result1 = dao.lockRegisteredUser(user1);
        assertEquals(user1.getUserId(), result1.getUserId());
        assertTrue(result1.isLocked());

        RegisteredUser result2 = dao.retrieveRegisteredUser(result1.getUserId());
        assertEquals(result1, result2);

        try {
            dao.lockRegisteredUser(user2);
            fail("Expected DaoException");
        } catch (DaoException e) { }
    }

    /** Test recordNewLogin(). */
    @Test public void testRecordNewLogin() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user1 = buildUser("userId1", "userNameA");
        RegisteredUser user2 = buildUser("userId2", "userNameB");
        dao.insertRegisteredUser(user1);

        Date now = DateUtils.getCurrentDate();
        RegisteredUser result1 = dao.recordNewLogin(user1);
        assertEquals(user1.getUserId(), result1.getUserId());
        assertEquals(user1.getLogins() + 1, result1.getLogins());
        assertAfter(now, result1.getLastLogin());

        RegisteredUser result2 = dao.retrieveRegisteredUser(result1.getUserId());
        assertEquals(result1, result2);

        try {
            dao.recordNewLogin(user2);
            fail("Expected DaoException");
        } catch (DaoException e) { }
    }

    /** Test retrieveRegisteredUsers(), focusing on user id. */
    @Test public void retrieveRegisteredUsersUserId() {
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA");
        RegisteredUser user2 = buildUser("userId2", "userNameB");
        RegisteredUser user3 = buildUser("userId3", "userNameC");
        RegisteredUser user4 = buildUser("userId4", "userNameE");
        RegisteredUser user5 = buildUser("userId5", "userNameD");

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria = buildCriteriaWithUserIds((String[]) null);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        criteria = buildCriteriaWithUserIds("userId1");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId1", list.get(0).getUserId());

        criteria = buildCriteriaWithUserIds("userId3");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId3", list.get(0).getUserId());

        criteria = buildCriteriaWithUserIds("userId1", "userId3");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
    }

    /** Test retrieveRegisteredUsers(), focusing on user name. */
    @Test public void retrieveRegisteredUsersUserName() {
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA");
        RegisteredUser user2 = buildUser("userId2", "userNameB");
        RegisteredUser user3 = buildUser("userId3", "userNameC");
        RegisteredUser user4 = buildUser("userId4", "userNameA");
        RegisteredUser user5 = buildUser("userId5", "userNameB");

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria = buildCriteriaWithUserNames((String[]) null);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId2", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId3", list.get(4).getUserId());

        criteria = buildCriteriaWithUserNames("userNameA");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());

        criteria = buildCriteriaWithUserNames("userNameC");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId3", list.get(0).getUserId());

        criteria = buildCriteriaWithUserNames("userNameA", "userNameC");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
    }

    /** Test retrieveRegisteredUsers(), focusing on open id provider. */
    @Test public void retrieveRegisteredUsersOpenIdProvider() {
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", OpenIdProvider.GOOGLE);
        RegisteredUser user2 = buildUser("userId2", "userNameB", OpenIdProvider.AOL);
        RegisteredUser user3 = buildUser("userId3", "userNameC", OpenIdProvider.MYOPENID);
        RegisteredUser user4 = buildUser("userId4", "userNameE", OpenIdProvider.GOOGLE);
        RegisteredUser user5 = buildUser("userId5", "userNameD", OpenIdProvider.AOL);

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria = buildCriteriaWithOpenId((OpenIdProvider[]) null);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        criteria = buildCriteriaWithOpenId(OpenIdProvider.GOOGLE);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());

        criteria = buildCriteriaWithOpenId(OpenIdProvider.MYOPENID);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId3", list.get(0).getUserId());

        criteria = buildCriteriaWithOpenId(OpenIdProvider.GOOGLE, OpenIdProvider.MYOPENID);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId4", list.get(2).getUserId());
    }

    /** Test retrieveRegisteredUsers(), focusing on admin flag. */
    @Test public void retrieveRegisteredUsersAdmin() {
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", true, false);
        RegisteredUser user2 = buildUser("userId2", "userNameB", false, false);
        RegisteredUser user3 = buildUser("userId3", "userNameC", true, false);
        RegisteredUser user4 = buildUser("userId4", "userNameE", true, false);
        RegisteredUser user5 = buildUser("userId5", "userNameD", false, false);

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria = buildCriteriaWithAdmin(true);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId4", list.get(2).getUserId());

        criteria = buildCriteriaWithAdmin(false);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId5", list.get(1).getUserId());
    }

    /** Test retrieveRegisteredUsers(), focusing on locked flag. */
    @Test public void retrieveRegisteredUsersLocked() {
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", false, true);
        RegisteredUser user2 = buildUser("userId2", "userNameB", false, false);
        RegisteredUser user3 = buildUser("userId3", "userNameC", false, true);
        RegisteredUser user4 = buildUser("userId4", "userNameE", false, true);
        RegisteredUser user5 = buildUser("userId5", "userNameD", false, false);

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria = buildCriteriaWithLocked(true);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId4", list.get(2).getUserId());

        criteria = buildCriteriaWithLocked(false);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId5", list.get(1).getUserId());
    }

    /** Test retrieveRegisteredUsers(), focusing on date range. */
    @Test public void retrieveRegisteredUsersDateRange() {
        Date start;
        Date end;
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", "2011-06-14T17:00:00.000");
        RegisteredUser user2 = buildUser("userId2", "userNameB", "2011-06-14T17:01:00.000");
        RegisteredUser user3 = buildUser("userId3", "userNameC", "2011-06-14T17:00:00.001");
        RegisteredUser user4 = buildUser("userId4", "userNameE", "2011-06-14T17:01:59.999");
        RegisteredUser user5 = buildUser("userId5", "userNameD", "2011-06-14T17:02:00.000");

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        start = null;
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        start = null;
        end = DateUtils.createDate("2011-06-14T16:59:59.999");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        assertIteratorSize(0, iterator);

        start = DateUtils.createDate("2011-06-14T17:02:00.001");
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        assertIteratorSize(0, iterator);

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.001");
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(4, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId5", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:01.000");
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId5", list.get(1).getUserId());
        assertEquals("userId4", list.get(2).getUserId());

        start = DateUtils.createDate("2011-06-14T17:01:59.999");
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId5", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());

        start = DateUtils.createDate("2011-06-14T17:02:00.000");
        end = null;
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId5", list.get(0).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:02:00.000");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:01:59.999");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(4, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.000");
        end = DateUtils.createDate("2011-06-14T17:01:59.998");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(3, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.001");
        end = DateUtils.createDate("2011-06-14T17:01:59.998");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(2, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.002");
        end = DateUtils.createDate("2011-06-14T17:01:59.998");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId2", list.get(0).getUserId());

        start = DateUtils.createDate("2011-06-14T17:00:00.002");
        end = DateUtils.createDate("2011-06-14T17:00:59.999");
        criteria = buildCriteriaWithDates(start, end);
        iterator = dao.retrieveRegisteredUsers(criteria);
        assertIteratorSize(0, iterator);
    }

    /** Test retrieveRegisteredUsers(), including all indexed fields. */
    @Test public void retrieveRegisteredUsersAllIndexes() {
        RegisteredUserCriteria criteria;
        Iterator<RegisteredUser> iterator;
        List<RegisteredUser> list;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", OpenIdProvider.GOOGLE, true, true, "2011-06-14T17:00:00.000", 4, "2011-12-01T19:32:00.000");
        RegisteredUser user2 = buildUser("userId2", "userNameB", OpenIdProvider.AOL, false, true, "2011-06-14T17:01:00.000", 1, "2011-12-01T19:32:00.002");
        RegisteredUser user3 = buildUser("userId3", "userNameC", OpenIdProvider.MYOPENID, true, false, "2011-06-14T17:00:00.001", 3, "2011-12-01T19:32:00.001");
        RegisteredUser user4 = buildUser("userId4", "userNameE", OpenIdProvider.GOOGLE, true, false, "2011-06-14T17:01:59.999", 2, "2011-12-01T19:32:00.003");
        RegisteredUser user5 = buildUser("userId5", "userNameD", OpenIdProvider.AOL, false, true, "2011-06-14T17:02:00.000", 5, "2011-12-01T19:32:00.004");

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria = buildCriteria("userId3", "userNameC", OpenIdProvider.MYOPENID, true, false, "2011-06-14T17:00:00.000", "2011-06-14T17:00:01.001");
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(1, iterator);
        assertEquals("userId3", list.get(0).getUserId());
    }

    /** Test retrieveRegisteredUsers(), spot-checking pagination. */
    @Test public void retrieveRegisteredUsersPagination() {
        RegisteredUserCriteria criteria;
        Pagination pagination;
        PaginatedResults<RegisteredUser> results;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", OpenIdProvider.GOOGLE, true, true, "2011-06-14T17:00:00.000", 4, "2011-12-01T19:32:00.000");
        RegisteredUser user2 = buildUser("userId2", "userNameB", OpenIdProvider.AOL, false, true, "2011-06-14T17:01:00.000", 1, "2011-12-01T19:32:00.002");
        RegisteredUser user3 = buildUser("userId3", "userNameC", OpenIdProvider.MYOPENID, true, false, "2011-06-14T17:00:00.001", 3, "2011-12-01T19:32:00.001");
        RegisteredUser user4 = buildUser("userId4", "userNameE", OpenIdProvider.GOOGLE, true, false, "2011-06-14T17:01:59.999", 2, "2011-12-01T19:32:00.003");
        RegisteredUser user5 = buildUser("userId5", "userNameD", OpenIdProvider.AOL, false, true, "2011-06-14T17:02:00.000", 5, "2011-12-01T19:32:00.004");

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        pagination = new Pagination(2);  // each page with 2 elements
        criteria = new RegisteredUserCriteria();  // no restrictions

        results = dao.retrieveRegisteredUsers(criteria, pagination);
        assertEquals(2, results.size());
        assertEquals("userId1", results.get(0).getUserId());
        assertEquals("userId2", results.get(1).getUserId());

        results = dao.retrieveRegisteredUsers(criteria, results.getPagination().next());
        assertEquals(2, results.size());
        assertEquals("userId3", results.get(0).getUserId());
        assertEquals("userId5", results.get(1).getUserId());

        results = dao.retrieveRegisteredUsers(criteria, results.getPagination().next());
        assertEquals(1, results.size());
        assertEquals("userId4", results.get(0).getUserId());
    }

    /** Test retrieveRegisteredUsers(), checking sort behavior. */
    @Test public void retrieveRegisteredUsersSort() {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();

        Iterator<RegisteredUser> iterator = null;
        List<RegisteredUser> list = null;

        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getDaoObjectifyService());
        dao.afterPropertiesSet();

        // note that some names and dates are out-of-order to test sorting
        RegisteredUser user1 = buildUser("userId1", "userNameA", OpenIdProvider.GOOGLE, true, true, "2011-06-14T17:00:00.000", 4, "2011-12-01T19:32:00.000");
        RegisteredUser user2 = buildUser("userId2", "userNameB", OpenIdProvider.AOL, false, true, "2011-06-14T17:01:00.000", 1, "2011-12-01T19:32:00.002");
        RegisteredUser user3 = buildUser("userId3", "userNameC", OpenIdProvider.MYOPENID, true, false, "2011-06-14T17:00:00.001", 3, "2011-12-01T19:32:00.001");
        RegisteredUser user4 = buildUser("userId4", "userNameE", OpenIdProvider.GOOGLE, true, false, "2011-06-14T17:01:59.999", 2, "2011-12-01T19:32:00.003");
        RegisteredUser user5 = buildUser("userId5", "userNameD", OpenIdProvider.AOL, false, true, "2011-06-14T17:02:00.000", 5, "2011-12-01T19:32:00.004");

        // Note: inserted out-of-order to check sorting
        dao.insertRegisteredUser(user2);
        dao.insertRegisteredUser(user1);
        dao.insertRegisteredUser(user4);
        dao.insertRegisteredUser(user5);
        dao.insertRegisteredUser(user3);

        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.USER_ID);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.USER_ID);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId5", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId2", list.get(3).getUserId());
        assertEquals("userId1", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.USER_NAME);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId5", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.USER_NAME);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId4", list.get(0).getUserId());
        assertEquals("userId5", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId2", list.get(3).getUserId());
        assertEquals("userId1", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.REGISTRATION_DATE);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId2", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.REGISTRATION_DATE);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId5", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId2", list.get(2).getUserId());
        assertEquals("userId3", list.get(3).getUserId());
        assertEquals("userId1", list.get(4).getUserId());

        // note: as October 2011, the secondary sort appears to be insert order.
        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.OPEN_ID_PROVIDER);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId5", list.get(1).getUserId());
        assertEquals("userId1", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());
        assertEquals("userId3", list.get(4).getUserId());

        // note: as October 2011, the secondary sort appears to be insert order.
        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.OPEN_ID_PROVIDER);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId3", list.get(0).getUserId());
        assertEquals("userId1", list.get(1).getUserId());
        assertEquals("userId4", list.get(2).getUserId());
        assertEquals("userId2", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.LOGINS);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId1", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.LOGINS);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId5", list.get(0).getUserId());
        assertEquals("userId1", list.get(1).getUserId());
        assertEquals("userId3", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());
        assertEquals("userId2", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.LAST_LOGIN);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId2", list.get(2).getUserId());
        assertEquals("userId4", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.LAST_LOGIN);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId5", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId2", list.get(2).getUserId());
        assertEquals("userId3", list.get(3).getUserId());
        assertEquals("userId1", list.get(4).getUserId());

        // note: as October 2011, the secondary sort appears to be insert order.
        // false appears to sort higher than true
        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.ADMIN);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId2", list.get(0).getUserId());
        assertEquals("userId5", list.get(1).getUserId());
        assertEquals("userId1", list.get(2).getUserId());
        assertEquals("userId3", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());

        // note: as October 2011, the secondary sort appears to be insert order.
        // false appears to sort higher than true
        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.ADMIN);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId3", list.get(1).getUserId());
        assertEquals("userId4", list.get(2).getUserId());
        assertEquals("userId2", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        // note: as October 2011, the secondary sort appears to be insert order.
        // false appears to sort higher than true
        criteria.setSortOrder(SortOrder.ASCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.LOCKED);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId3", list.get(0).getUserId());
        assertEquals("userId4", list.get(1).getUserId());
        assertEquals("userId1", list.get(2).getUserId());
        assertEquals("userId2", list.get(3).getUserId());
        assertEquals("userId5", list.get(4).getUserId());

        // note: as October 2011, the secondary sort appears to be insert order.
        // false appears to sort higher than true
        criteria.setSortOrder(SortOrder.DESCENDING);
        criteria.setSortColumn(RegisteredUserSortColumn.LOCKED);
        iterator = dao.retrieveRegisteredUsers(criteria);
        list = assertIteratorSize(5, iterator);
        assertEquals("userId1", list.get(0).getUserId());
        assertEquals("userId2", list.get(1).getUserId());
        assertEquals("userId5", list.get(2).getUserId());
        assertEquals("userId3", list.get(3).getUserId());
        assertEquals("userId4", list.get(4).getUserId());
    }

    /** Test insertRegisteredUser() when an exception is thrown. */
    @Test public void testInsertRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser registeredUser = new RegisteredUser();
        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().put(registeredUser)).thenThrow(exception);

        try {
            dao.insertRegisteredUser(registeredUser);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test insertRegisteredUser() when a DaoException is thrown. */
    @Test public void testInsertRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser registeredUser = new RegisteredUser();
        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().put(registeredUser)).thenThrow(exception);

        try {
            dao.insertRegisteredUser(registeredUser);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveRegisteredUser() when an exception is thrown. */
    @Test public void testRetrieveRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.retrieveRegisteredUser("id");
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test retrieveRegisteredUser() when a DaoException is thrown. */
    @Test public void testRetrieveRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.retrieveRegisteredUser("id");
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Test deleteRegisteredUser() when an exception is thrown. */
    @Test public void testDeleteRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.deleteRegisteredUser("id");
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test deleteRegisteredUser() when a DaoException is thrown. */
    @Test public void testDeleteRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.deleteRegisteredUser("id");
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test updateRegisteredUser() when an exception is thrown. */
    @Test public void testUpdateRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.updateRegisteredUser(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test updateRegisteredUser() when a DaoException is thrown. */
    @Test public void testUpdateRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.updateRegisteredUser(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test lockRegisteredUser() when an exception is thrown. */
    @Test public void testLockRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.lockRegisteredUser(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test lockRegisteredUser() when a DaoException is thrown. */
    @Test public void testLockRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.lockRegisteredUser(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test unlockRegisteredUser() when an exception is thrown. */
    @Test public void testUnlockRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.unlockRegisteredUser(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test unlockRegisteredUser() when a DaoException is thrown. */
    @Test public void testUnlockRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.unlockRegisteredUser(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test recordNewLoginRegisteredUser() when an exception is thrown. */
    @Test public void testRecordNewLoginRegisteredUserException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.recordNewLogin(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test recordNewLoginRegisteredUser() when a DaoException is thrown. */
    @Test public void testRecordNewLoginRegisteredUserDaoException() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RegisteredUser user = new RegisteredUser();
        user.setUserId("id");

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectifyWithTransaction().find(RegisteredUser.class, "id")).thenThrow(exception);

        try {
            dao.recordNewLogin(user);
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
            verify(dao.getDaoObjectifyService().getObjectifyWithTransaction()).rollback();
        }
    }

    /** Test retrieveRegisteredUsers(RegisteredUserCriteria, Pagination) when an exception is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveRegisteredUsersException1() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveRegisteredUsers(new RegisteredUserCriteria(), new Pagination());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test retrieveRegisteredUsers(RegisteredUserCriteria, Pagination) when an InvalidDataException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveRegisteredUsersException2() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        InvalidDataException exception = new InvalidDataException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveRegisteredUsers(new RegisteredUserCriteria(), new Pagination());
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveRegisteredUsers(RegisteredUserCriteria, Pagination) when a DaoException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveRegisteredUsersException3() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveRegisteredUsers(new RegisteredUserCriteria(), new Pagination());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveRegisteredUsers(RegisteredUserCriteria) when an exception is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveRegisteredUsersException4() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        RuntimeException exception = new RuntimeException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveRegisteredUsers(new RegisteredUserCriteria());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e.getCause());
        }
    }

    /** Test retrieveRegisteredUsers(RegisteredUserCriteria) when an InvalidDataException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveRegisteredUsersException5() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        InvalidDataException exception = new InvalidDataException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveRegisteredUsers(new RegisteredUserCriteria());
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSame(exception, e);
        }
    }

    /** Test retrieveRegisteredUsers(RegisteredUserCriteria) when a DaoException is thrown. */
    @SuppressWarnings("unchecked")
    @Test public void testRetrieveRegisteredUsersException6() {
        RegisteredUserDao dao = new RegisteredUserDao();
        dao.setDaoObjectifyService(getMockedDaoObjectifyService());
        dao.afterPropertiesSet();

        DaoException exception = new DaoException("Hello");
        when(dao.getDaoObjectifyService().getObjectify().query(any(Class.class), any(Pagination.class))).thenThrow(exception);

        try {
            dao.retrieveRegisteredUsers(new RegisteredUserCriteria());
            fail("Expected DaoException");
        } catch (DaoException e) {
            assertSame(exception, e);
        }
    }

    /** Build a registered user with enough data for us to tell that persisting worked. */
    private static RegisteredUser createRegisteredUser(String userId) {
        RegisteredUser registeredUser = new RegisteredUser();

        registeredUser.setUserId(userId);
        registeredUser.setUserName("name");
        registeredUser.setEmailAddress("email");
        registeredUser.setAuthenticationDomain("gmail.com");
        registeredUser.setOpenIdProvider(OpenIdProvider.GOOGLE);
        registeredUser.setFederatedIdentity("federated");
        registeredUser.setAdmin(true);

        return registeredUser;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteriaWithDates(Date startDate, Date endDate) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setStartDate(startDate);
        criteria.setEndDate(endDate);
        return criteria;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteriaWithUserNames(String... userNames) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setUserNames(userNames);
        return criteria;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteriaWithUserIds(String... userIds) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setUserIds(userIds);
        return criteria;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteriaWithOpenId(OpenIdProvider... openIdProviders) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setOpenIdProviders(openIdProviders);
        return criteria;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteriaWithAdmin(boolean admin) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setAdmin(admin);
        return criteria;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteriaWithLocked(boolean locked) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setLocked(locked);
        return criteria;
    }

    /** Build criteria for testing. */
    private static RegisteredUserCriteria buildCriteria(String userId, String userName, OpenIdProvider openIdProvider,
                                                              boolean admin, boolean locked, String startDate, String endDate) {
        RegisteredUserCriteria criteria = new RegisteredUserCriteria();
        criteria.setUserIds(userId);
        criteria.setUserNames(userName);
        criteria.setOpenIdProviders(openIdProvider);
        criteria.setAdmin(admin);
        criteria.setLocked(locked);
        criteria.setStartDate(DateUtils.createDate(startDate));
        criteria.setEndDate(DateUtils.createDate(endDate));
        return criteria;
    }

    /** Build a registered user for testing. */
    private static RegisteredUser buildUser(String userId, String userName) {
        RegisteredUser user = new RegisteredUser();
        user.setUserId(userId);
        user.setUserName(userName);
        return user;
    }

    /** Build a registered user for testing. */
    private static RegisteredUser buildUser(String userId, String userName, String registrationDate) {
        RegisteredUser user = new RegisteredUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setRegistrationDate(DateUtils.createDate(registrationDate));
        return user;
    }

    /** Build a registered user for testing. */
    private static RegisteredUser buildUser(String userId, String userName, OpenIdProvider openIdProvider) {
        RegisteredUser user = new RegisteredUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setOpenIdProvider(openIdProvider);
        return user;
    }

    /** Build a registered user for testing. */
    private static RegisteredUser buildUser(String userId, String userName, boolean admin, boolean locked) {
        RegisteredUser user = new RegisteredUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setAdmin(admin);
        user.setLocked(locked);
        return user;
    }

    /** Build a registered user for testing. */
    private static RegisteredUser buildUser(String userId, String userName, OpenIdProvider openIdProvider,
                                            boolean admin, boolean locked, String registrationDate,
                                            int logins, String lastLogin) {
        RegisteredUser user = new RegisteredUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setOpenIdProvider(openIdProvider);
        user.setAdmin(admin);
        user.setLocked(locked);
        user.setRegistrationDate(DateUtils.createDate(registrationDate));
        user.setLogins(logins);
        user.setLastLogin(DateUtils.createDate(lastLogin));
        return user;
    }
}
