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
package com.cedarsolutions.santa.shared.domain.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for Organizer.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class OrganizerTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        Organizer organizer = new Organizer();
        assertNotNull(organizer);
        assertNull(organizer.getName());
        assertNull(organizer.getEmailAddress());
        assertNull(organizer.getPhoneNumber());

        Organizer copy = new Organizer(null);
        assertNotNull(organizer);
        assertNull(organizer.getName());
        assertNull(organizer.getEmailAddress());
        assertNull(organizer.getPhoneNumber());

        organizer = new Organizer("name", "email", "phone");
        assertNotNull(organizer);
        assertEquals("name", organizer.getName());
        assertEquals("email", organizer.getEmailAddress());
        assertEquals("phone", organizer.getPhoneNumber());

        copy = new Organizer(organizer);
        assertEquals("name", copy.getName());
        assertEquals("email", copy.getEmailAddress());
        assertEquals("phone", copy.getPhoneNumber());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        Organizer organizer = new Organizer();

        organizer.setName("1");
        assertEquals("1", organizer.getName());

        organizer.setEmailAddress("2");
        assertEquals("2", organizer.getEmailAddress());

        organizer.setPhoneNumber("3");
        assertEquals("3", organizer.getPhoneNumber());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        Organizer organizer1;
        Organizer organizer2;

        organizer1 = createOrganizer();
        organizer2 = createOrganizer();
        assertTrue(organizer1.equals(organizer2));
        assertTrue(organizer2.equals(organizer1));

        try {
            organizer1 = createOrganizer();
            organizer2 = null;
            organizer1.equals(organizer2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            organizer1 = createOrganizer();
            organizer2 = null;
            organizer1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        organizer1 = createOrganizer();
        organizer2 = createOrganizer();
        organizer2.setName("X");
        assertFalse(organizer1.equals(organizer2));
        assertFalse(organizer2.equals(organizer1));

        organizer1 = createOrganizer();
        organizer2 = createOrganizer();
        organizer2.setEmailAddress("X");
        assertFalse(organizer1.equals(organizer2));
        assertFalse(organizer2.equals(organizer1));

        organizer1 = createOrganizer();
        organizer2 = createOrganizer();
        organizer2.setPhoneNumber("X");
        assertFalse(organizer1.equals(organizer2));
        assertFalse(organizer2.equals(organizer1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        Organizer organizer1 = createOrganizer();
        organizer1.setName("X");

        Organizer organizer2 = createOrganizer();
        organizer2.setEmailAddress("X");

        Organizer organizer3 = createOrganizer();
        organizer3.setPhoneNumber("X");

        Organizer organizer4 = createOrganizer();
        organizer4.setName("X"); // same as organizer1

        Map<Organizer, String> map = new HashMap<Organizer, String>();
        map.put(organizer1, "ONE");
        map.put(organizer2, "TWO");
        map.put(organizer3, "THREE");

        assertEquals("ONE", map.get(organizer1));
        assertEquals("TWO", map.get(organizer2));
        assertEquals("THREE", map.get(organizer3));
        assertEquals("ONE", map.get(organizer4));
    }

    /** Create a Organizer for testing. */
    private static Organizer createOrganizer() {
        Organizer organizer = new Organizer();

        organizer.setName("name");
        organizer.setEmailAddress("email");
        organizer.setPhoneNumber("phone");

        return organizer;
    }
}
