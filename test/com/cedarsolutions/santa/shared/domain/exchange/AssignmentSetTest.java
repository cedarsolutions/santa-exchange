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
package com.cedarsolutions.santa.shared.domain.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for AssignmentSet.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AssignmentSetTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        AssignmentSet set = new AssignmentSet();
        assertNotNull(set);
        assertTrue(set.isEmpty());

        AssignmentSet copy = new AssignmentSet(null);
        assertNotNull(copy);
        assertTrue(copy.isEmpty());

        Participant participant1 = new Participant();
        Participant participant2 = new Participant();
        set.add(new Assignment(participant1, participant2));

        copy = new AssignmentSet(set);
        assertEquals(set, copy);
        assertNotSame(set, copy);

        // technically, we want to make sure that two equivalent assignments don't match
        // it's enough to check that no one assignment matches any other assignment
        for (Assignment copied : copy) {
            for (Assignment source : set) {
                assertNotSame(source, copied);
            }
        }
    }

    /** Test getGiftGiver() and getGiftReceiver(). */
    @Test public void tesGetGiftGiverAndReceiver() {
        Participant participant1 = new Participant(1L);
        Participant participant2 = new Participant(2L);
        Participant participant3 = new Participant(3L);

        Assignment assignment1 = new Assignment(participant1, participant2);
        Assignment assignment2 = new Assignment(participant2, participant3);

        AssignmentSet set = new AssignmentSet();
        set.add(assignment1);
        set.add(assignment2);

        assertTrue(set.contains(assignment1));
        assertTrue(set.contains(assignment2));

        assertEquals(null, set.getGiftGiver(participant1));
        assertEquals(participant1, set.getGiftGiver(participant2));
        assertEquals(participant2, set.getGiftGiver(participant3));

        assertEquals(participant2, set.getGiftReceiver(participant1));
        assertEquals(participant3, set.getGiftReceiver(participant2));
        assertEquals(null, set.getGiftReceiver(participant3));
    }

}
