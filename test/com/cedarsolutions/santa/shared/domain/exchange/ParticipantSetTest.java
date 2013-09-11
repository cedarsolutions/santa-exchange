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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for ParticipantSet.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ParticipantSetTest {

    /** Test the constructors. */
    @Test public void testConstructor() {
        ParticipantSet set = new ParticipantSet();
        assertNotNull(set);
        assertTrue(set.isEmpty());

        ParticipantSet copy = new ParticipantSet(null);
        assertNotNull(copy);
        assertTrue(copy.isEmpty());

        Participant participant = new Participant();
        set.add(participant);

        copy = new ParticipantSet(set);
        assertEquals(set, copy);
        assertNotSame(set, copy);

        // technically, we want to make sure that two equivalent participants don't match
        // it's enough to check that no one participants matches any other assignment
        for (Participant copied : copy) {
            for (Participant source : set) {
                assertNotSame(source, copied);
            }
        }
    }

}
