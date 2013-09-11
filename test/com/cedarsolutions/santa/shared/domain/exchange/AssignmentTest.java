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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit tests for Assignment.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AssignmentTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        Assignment assignment = new Assignment();
        assertNotNull(assignment);
        assertNull(assignment.getGiftGiver());
        assertNull(assignment.getGiftReceiver());

        Assignment copy = new Assignment(null);
        assertNotNull(copy);
        assertNull(copy.getGiftGiver());
        assertNull(copy.getGiftReceiver());

        copy = new Assignment(assignment);
        assertNotNull(copy);
        assertNull(copy.getGiftGiver());
        assertNull(copy.getGiftReceiver());

        assignment.setGiftGiver(new Participant(1L));
        assignment.setGiftReceiver(new Participant(2L));
        copy = new Assignment(assignment);
        assertEquals(assignment.getGiftGiver(), copy.getGiftGiver());
        assertNotSame(assignment.getGiftGiver(), copy.getGiftGiver());
        assertEquals(assignment.getGiftReceiver(), copy.getGiftReceiver());
        assertNotSame(assignment.getGiftReceiver(), copy.getGiftReceiver());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        Assignment assignment = new Assignment();

        Participant giftGiver = new Participant();
        assignment.setGiftGiver(giftGiver);
        assertSame(giftGiver, assignment.getGiftGiver());

        Participant giftReceiver = new Participant();
        assignment.setGiftReceiver(giftReceiver);
        assertSame(giftReceiver, assignment.getGiftReceiver());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        Assignment assignment1;
        Assignment assignment2;

        assignment1 = createAssignment();
        assignment2 = createAssignment();
        assertTrue(assignment1.equals(assignment2));
        assertTrue(assignment2.equals(assignment1));

        try {
            assignment1 = createAssignment();
            assignment2 = null;
            assignment1.equals(assignment2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            assignment1 = createAssignment();
            assignment2 = null;
            assignment1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        assignment1 = createAssignment();
        assignment2 = createAssignment();
        assignment2.setGiftGiver(new Participant(5L));
        assertFalse(assignment1.equals(assignment2));
        assertFalse(assignment2.equals(assignment1));

        assignment1 = createAssignment();
        assignment2 = createAssignment();
        assignment2.setGiftReceiver(new Participant(5L));
        assertFalse(assignment1.equals(assignment2));
        assertFalse(assignment2.equals(assignment1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        Assignment assignment1 = createAssignment();
        assignment1.setGiftGiver(new Participant(5L));

        Assignment assignment2 = createAssignment();
        assignment2.setGiftReceiver(new Participant(5L));

        Assignment assignment3 = createAssignment();
        assignment3.setGiftGiver(new Participant(5L));  // same as user1

        Map<Assignment, String> map = new HashMap<Assignment, String>();
        map.put(assignment1, "ONE");
        map.put(assignment2, "TWO");

        assertEquals("ONE", map.get(assignment1));
        assertEquals("TWO", map.get(assignment2));
        assertEquals("ONE", map.get(assignment3));
    }

    /** Create a Assignment for testing. */
    private static Assignment createAssignment() {
        Assignment assignment = new Assignment();

        assignment.setGiftGiver(new Participant(1L));
        assignment.setGiftReceiver(new Participant(2L));

        return assignment;
    }
}
