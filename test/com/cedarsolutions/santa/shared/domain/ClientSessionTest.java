/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.shared.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.shared.domain.FederatedUser;

/**
 * Unit tests for ClientSession.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientSessionTest {

    /** Test the constructor, getters, and setters. */
    @Test public void testConstructorGettersSetters() {
        ClientSession session = new ClientSession();
        assertNotNull(session);
        assertFalse(session.isInitialized());
        assertNull(session.getCurrentUser());
        assertNull(session.getRegisteredUser());
        assertNull(session.getSessionId());
        assertNull(session.getLogoutUrl());

        session.initialize();
        assertTrue(session.isInitialized());

        FederatedUser currentUser = new FederatedUser();
        session.setCurrentUser(currentUser);
        assertSame(currentUser, session.getCurrentUser());

        RegisteredUser registeredUser = new RegisteredUser();
        session.setRegisteredUser(registeredUser);
        assertSame(registeredUser, session.getRegisteredUser());

        session.setSessionId("session");
        assertEquals("session", session.getSessionId());

        session.setLogoutUrl("logout");
        assertEquals("logout", session.getLogoutUrl());

        session.currentUser.setFirstLogin(true);
        session.clearFirstLogin();
        assertFalse(session.currentUser.isFirstLogin());
    }

    /** Test clear(). */
    @Test public void testClear() {
        ClientSession session = new ClientSession();
        session.initialize();
        session.setCurrentUser(new FederatedUser());
        session.setSessionId("session");
        session.setLogoutUrl("logout");

        session.clear();
        assertFalse(session.isInitialized());
        assertNull(session.getCurrentUser());
        assertNull(session.getSessionId());
        assertNull(session.getLogoutUrl());
    }

    /** Test refresh(). */
    @Test public void testRefresh() {
        FederatedUser user1 = new FederatedUser();
        FederatedUser user2 = new FederatedUser();

        ClientSession session1 = new ClientSession();
        session1.initialize();
        session1.setCurrentUser(user1);
        session1.setSessionId("session1");
        session1.setLogoutUrl("logout1");

        ClientSession session2 = new ClientSession();
        session2.setCurrentUser(user2);
        session2.setSessionId("session2");
        session2.setLogoutUrl("logout2");

        session1.refresh(session2);
        assertTrue(session1.isInitialized());  // note: initialized is not taken from session1
        assertSame(user2, session1.getCurrentUser());
        assertEquals("session2", session1.getSessionId());
        assertEquals("logout2", session1.getLogoutUrl());
    }

    /** Test isLoggedIn(). */
    @Test public void testIsLoggedIn() {
        ClientSession session = new ClientSession();
        session.setCurrentUser(null);
        assertFalse(session.isLoggedIn());

        session.setCurrentUser(new FederatedUser());
        assertTrue(session.isLoggedIn());
    }

    /** Test isAdmin(). */
    @Test public void testIsAdmin() {
        ClientSession session = new ClientSession();
        session.setCurrentUser(null);
        assertFalse(session.isAdmin());

        session.setCurrentUser(new FederatedUser());
        session.getCurrentUser().setAdmin(false);
        assertFalse(session.isAdmin());

        session.setCurrentUser(new FederatedUser());
        session.getCurrentUser().setAdmin(true);
        assertTrue(session.isAdmin());
    }

    /** Test isLocked(). */
    @Test public void testIsLocked() {
        ClientSession session = new ClientSession();
        session.setRegisteredUser(null);
        assertFalse(session.isLocked());

        session.setRegisteredUser(new RegisteredUser());
        session.getRegisteredUser().setLocked(false);
        assertFalse(session.isLocked());

        session.setRegisteredUser(new RegisteredUser());
        session.getRegisteredUser().setLocked(true);
        assertTrue(session.isLocked());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        ClientSession session1;
        ClientSession session2;

        session1 = createClientSession();
        session2 = createClientSession();
        assertTrue(session1.equals(session2));
        assertTrue(session2.equals(session1));

        try {
            session1 = createClientSession();
            session2 = null;
            session1.equals(session2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            session1 = createClientSession();
            session2 = null;
            session1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        session1 = createClientSession();
        session2 = createClientSession();
        session2.initialized = true;
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));

        session1 = createClientSession();
        session2 = createClientSession();
        session2.currentUser.setUserId("X");
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));

        session1 = createClientSession();
        session2 = createClientSession();
        session2.registeredUser.setUserId("X");
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));

        session1 = createClientSession();
        session2 = createClientSession();
        session2.sessionId = "X";
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));

        session1 = createClientSession();
        session2 = createClientSession();
        session2.logoutUrl = "X";
        assertFalse(session1.equals(session2));
        assertFalse(session2.equals(session1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        ClientSession session1 = createClientSession();
        session1.initialized = true;

        ClientSession session2 = createClientSession();
        session2.currentUser.setUserId("X");

        ClientSession session3 = createClientSession();
        session3.registeredUser.setUserId("X");

        ClientSession session4 = createClientSession();
        session4.sessionId = "X";

        ClientSession session5 = createClientSession();
        session5.logoutUrl = "X";

        ClientSession session6 = createClientSession();
        session6.initialized = true;  // same a session1

        Map<ClientSession, String> map = new HashMap<ClientSession, String>();
        map.put(session1, "ONE");
        map.put(session2, "TWO");
        map.put(session3, "THREE");
        map.put(session4, "FOUR");
        map.put(session5, "FIVE");

        assertEquals("ONE", map.get(session1));
        assertEquals("TWO", map.get(session2));
        assertEquals("THREE", map.get(session3));
        assertEquals("FOUR", map.get(session4));
        assertEquals("FIVE", map.get(session5));
        assertEquals("ONE", map.get(session6));
    }

    /** Create a ClientSession for testing. */
    private static ClientSession createClientSession() {
        ClientSession session = new ClientSession();

        FederatedUser user = new FederatedUser();
        user.setUserId("12");

        RegisteredUser registered = new RegisteredUser();
        registered.setUserId("13");

        session.initialized = false;
        session.currentUser = user;
        session.registeredUser = registered;
        session.sessionId = "session";
        session.logoutUrl = "logoutUrl";

        return session;
    }
}
