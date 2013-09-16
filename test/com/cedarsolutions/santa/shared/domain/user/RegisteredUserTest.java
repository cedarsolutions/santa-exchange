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
package com.cedarsolutions.santa.shared.domain.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for RegisteredUser.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserTest {

    /** Test the default constructor. */
    @Test public void testDefaultConstructor() {
        RegisteredUser registeredUser = new RegisteredUser();
        assertNotNull(registeredUser);
        assertNull(registeredUser.getUserId());
        assertNull(registeredUser.getUserName());
        assertNull(registeredUser.getRegistrationDate());
        assertNull(registeredUser.getEmailAddress());
        assertNull(registeredUser.getAuthenticationDomain());
        assertNull(registeredUser.getOpenIdProvider());
        assertNull(registeredUser.getFederatedIdentity());
        assertEquals(0, registeredUser.getLogins());
        assertNull(registeredUser.getLastLogin());
        assertFalse(registeredUser.isAdmin());
        assertFalse(registeredUser.isLocked());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        RegisteredUser registeredUser = new RegisteredUser();

        registeredUser.setUserId("userId");
        assertEquals("userId", registeredUser.getUserId());

        registeredUser.setUserName("userName");
        assertEquals("userName", registeredUser.getUserName());

        registeredUser.setRegistrationDate(DateUtils.createDate(2011, 12, 14));
        assertEquals(DateUtils.createDate(2011, 12, 14), registeredUser.getRegistrationDate());

        registeredUser.setEmailAddress("email");
        assertEquals("email", registeredUser.getEmailAddress());

        registeredUser.setAuthenticationDomain("gmail.com");
        assertEquals("gmail.com", registeredUser.getAuthenticationDomain());

        registeredUser.setOpenIdProvider(OpenIdProvider.GOOGLE);
        assertEquals(OpenIdProvider.GOOGLE, registeredUser.getOpenIdProvider());

        registeredUser.setFederatedIdentity("federatedIdentity");
        assertEquals("federatedIdentity", registeredUser.getFederatedIdentity());

        registeredUser.setLogins(12);
        assertEquals(12, registeredUser.getLogins());

        registeredUser.setLastLogin(DateUtils.createDate(2011, 12, 15));
        assertEquals(DateUtils.createDate(2011, 12, 15), registeredUser.getLastLogin());

        registeredUser.setAdmin(true);
        assertTrue(registeredUser.isAdmin());

        registeredUser.setLocked(true);
        assertTrue(registeredUser.isLocked());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        RegisteredUser user1;
        RegisteredUser user2;

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        assertTrue(user1.equals(user2));
        assertTrue(user2.equals(user1));

        try {
            user1 = createRegisteredUser();
            user2 = null;
            user1.equals(user2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            user1 = createRegisteredUser();
            user2 = null;
            user1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setUserId("X");
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setUserName("X");
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setRegistrationDate(DateUtils.getCurrentDate());
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setEmailAddress("X");
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setAuthenticationDomain("X");
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setOpenIdProvider(OpenIdProvider.AOL);
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setFederatedIdentity("X");
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setLogins(0);
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setLastLogin(DateUtils.getCurrentDate());
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setAdmin(false);
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));

        user1 = createRegisteredUser();
        user2 = createRegisteredUser();
        user2.setLocked(false);
        assertFalse(user1.equals(user2));
        assertFalse(user2.equals(user1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        RegisteredUser user1 = createRegisteredUser();
        user1.setUserId("X");

        RegisteredUser user2 = createRegisteredUser();
        user2.setUserName("X");

        RegisteredUser user3 = createRegisteredUser();
        user3.setRegistrationDate(DateUtils.getCurrentDate());

        RegisteredUser user4 = createRegisteredUser();
        user4.setEmailAddress("X");

        RegisteredUser user5 = createRegisteredUser();
        user5.setAuthenticationDomain("X");

        RegisteredUser user6 = createRegisteredUser();
        user6.setOpenIdProvider(OpenIdProvider.AOL);

        RegisteredUser user7 = createRegisteredUser();
        user7.setFederatedIdentity("X");

        RegisteredUser user8 = createRegisteredUser();
        user8.setLogins(0);

        RegisteredUser user9 = createRegisteredUser();
        user9.setLastLogin(DateUtils.getCurrentDate());

        RegisteredUser user10 = createRegisteredUser();
        user10.setAdmin(false);

        RegisteredUser user11 = createRegisteredUser();
        user11.setLocked(false);

        RegisteredUser user12 = createRegisteredUser();
        user12.setUserId("X");  // same as user1

        Map<RegisteredUser, String> map = new HashMap<RegisteredUser, String>();
        map.put(user1, "ONE");
        map.put(user2, "TWO");
        map.put(user3, "THREE");
        map.put(user4, "FOUR");
        map.put(user5, "FIVE");
        map.put(user6, "SIX");
        map.put(user7, "SEVEN");
        map.put(user8, "EIGHT");
        map.put(user9, "NINE");
        map.put(user10, "TEN");
        map.put(user11, "ELEVEN");

        assertEquals("ONE", map.get(user1));
        assertEquals("TWO", map.get(user2));
        assertEquals("THREE", map.get(user3));
        assertEquals("FOUR", map.get(user4));
        assertEquals("FIVE", map.get(user5));
        assertEquals("SIX", map.get(user6));
        assertEquals("SEVEN", map.get(user7));
        assertEquals("EIGHT", map.get(user8));
        assertEquals("NINE", map.get(user9));
        assertEquals("TEN", map.get(user10));
        assertEquals("ELEVEN", map.get(user11));
        assertEquals("ONE", map.get(user12));
    }

    /** Create a RegisteredUser for testing. */
    private static RegisteredUser createRegisteredUser() {
        RegisteredUser user = new RegisteredUser();

        user.setUserId("userId");
        user.setUserName("userName");
        user.setRegistrationDate(DateUtils.createDate(2011, 12, 14));
        user.setEmailAddress("emailAddress");
        user.setAuthenticationDomain("domain");
        user.setOpenIdProvider(OpenIdProvider.GOOGLE);
        user.setFederatedIdentity("identity");
        user.setLogins(12);
        user.setLastLogin(DateUtils.createDate(2011, 12, 15));
        user.setAdmin(true);
        user.setLocked(true);

        return user;
    }
}
