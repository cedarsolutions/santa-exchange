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

import com.cedarsolutions.shared.domain.email.EmailFormat;

/**
 * Unit tests for Participant.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ParticipantTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        ParticipantSet conflicts = new ParticipantSet();
        conflicts.add(new Participant(0L));

        Participant participant = new Participant();
        assertNotNull(participant);
        assertNull(participant.getId());
        assertNull(participant.getName());
        assertNull(participant.getNickname());
        assertNull(participant.getEmailAddress());
        assertEquals(new TemplateConfig(), participant.getTemplateOverrides());
        assertConflictsEquivalent(new ParticipantSet(), participant);

        participant = new Participant(1L);
        assertNotNull(participant);
        assertEquals(new Long(1L), participant.getId());
        assertEquals(null, participant.getName());
        assertEquals(null, participant.getNickname());
        assertEquals(null, participant.getEmailAddress());
        assertEquals(new TemplateConfig(), participant.getTemplateOverrides());
        assertConflictsEquivalent(new ParticipantSet(), participant);

        participant = new Participant(1L, "name", "nickname", "email");
        assertNotNull(participant);
        assertEquals(new Long(1L), participant.getId());
        assertEquals("name", participant.getName());
        assertEquals("nickname", participant.getNickname());
        assertEquals("email", participant.getEmailAddress());
        assertEquals(new TemplateConfig(), participant.getTemplateOverrides());
        assertConflictsEquivalent(new ParticipantSet(), participant);

        participant = new Participant(1L, "name", "nickname", "email", conflicts);
        assertNotNull(participant);
        assertEquals(new Long(1L), participant.getId());
        assertEquals("name", participant.getName());
        assertEquals("nickname", participant.getNickname());
        assertEquals("email", participant.getEmailAddress());
        assertEquals(new TemplateConfig(), participant.getTemplateOverrides());
        assertConflictsEquivalent(conflicts, participant);

        Participant source = new Participant(1L, "name", "nickname", "email", conflicts);
        participant = new Participant(source);
        assertNotNull(participant);
        assertEquals(new Long(1L), participant.getId());
        assertEquals("name", participant.getName());
        assertEquals("nickname", participant.getNickname());
        assertEquals("email", participant.getEmailAddress());
        assertEquals(new TemplateConfig(), participant.getTemplateOverrides());
        assertConflictsEquivalent(conflicts, participant);

        Participant copy = new Participant((Participant) null);
        assertNotNull(copy);
        assertNull(copy.getId());
        assertNull(copy.getName());
        assertNull(copy.getNickname());
        assertNull(copy.getEmailAddress());
        assertEquals(new TemplateConfig(), copy.getTemplateOverrides());
        assertConflictsEquivalent(new ParticipantSet(), copy);

        TemplateConfig templateOverrides = new TemplateConfig();
        templateOverrides.setEmailFormat(EmailFormat.MULTIPART);
        source = new Participant(1L, "name", "nickname", "email", conflicts);
        source.setTemplateOverrides(templateOverrides);
        copy = new Participant(source);
        assertNotNull(copy);
        assertEquals(new Long(1L), copy.getId());
        assertEquals("name", copy.getName());
        assertEquals("nickname", copy.getNickname());
        assertEquals("email", copy.getEmailAddress());
        assertEquals(templateOverrides, copy.getTemplateOverrides());
        assertConflictsEquivalent(conflicts, copy);
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        Participant participant = new Participant();

        participant.setId(0L);
        assertEquals(new Long(0L), participant.getId());

        participant.setName("1");
        assertEquals("1", participant.getName());

        participant.setNickname("2");
        assertEquals("2", participant.getNickname());

        participant.setEmailAddress("3");
        assertEquals("3", participant.getEmailAddress());

        TemplateConfig emailConfig = new TemplateConfig();
        participant.setTemplateOverrides(emailConfig);
        assertSame(emailConfig, participant.getTemplateOverrides());

        ParticipantSet conflicts = new ParticipantSet();
        participant.setConflicts(conflicts);
        assertSame(conflicts, participant.getConflicts());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        Participant participant1;
        Participant participant2;

        participant1 = createParticipant();
        participant2 = createParticipant();
        assertTrue(participant1.equals(participant2));
        assertTrue(participant2.equals(participant1));

        try {
            participant1 = createParticipant();
            participant2 = null;
            participant1.equals(participant2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            participant1 = createParticipant();
            participant2 = null;
            participant1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        participant1 = createParticipant();
        participant2 = createParticipant();
        participant2.setId(5L);
        assertFalse(participant1.equals(participant2));
        assertFalse(participant2.equals(participant1));

        participant1 = createParticipant();
        participant2 = createParticipant();
        participant2.setName("X");
        assertFalse(participant1.equals(participant2));
        assertFalse(participant2.equals(participant1));

        participant1 = createParticipant();
        participant2 = createParticipant();
        participant2.setEmailAddress("X");
        assertFalse(participant1.equals(participant2));
        assertFalse(participant2.equals(participant1));

        participant1 = createParticipant();
        participant2 = createParticipant();
        participant2.setTemplateOverrides(null);
        assertFalse(participant1.equals(participant2));
        assertFalse(participant2.equals(participant1));

        participant1 = createParticipant();
        participant2 = createParticipant();
        participant2.setConflicts(null);
        assertFalse(participant1.equals(participant2));
        assertFalse(participant2.equals(participant1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        Participant participant1 = createParticipant();
        participant1.setId(5L);

        Participant participant2 = createParticipant();
        participant2.setName("X");

        Participant participant3 = createParticipant();
        participant3.setEmailAddress("X");

        Participant participant4 = createParticipant();
        participant4.setTemplateOverrides(null);

        Participant participant5 = createParticipant();
        participant5.setConflicts(null);

        Participant participant6 = createParticipant();
        participant6.setId(5L);

        Map<Participant, String> map = new HashMap<Participant, String>();
        map.put(participant1, "ONE");
        map.put(participant2, "TWO");
        map.put(participant3, "THREE");
        map.put(participant4, "FOUR");
        map.put(participant5, "FIVE");

        assertEquals("ONE", map.get(participant1));
        assertEquals("TWO", map.get(participant2));
        assertEquals("THREE", map.get(participant3));
        assertEquals("FOUR", map.get(participant4));
        assertEquals("FIVE", map.get(participant5));
        assertEquals("ONE", map.get(participant6));
    }

    /** Test addConflict() and removeConflict(). */
    @Test public void testAddRemoveConflict() {
        Participant participant = createParticipant();

        Participant conflict1 = new Participant();
        conflict1.setId(1L);

        Participant conflict2 = new Participant();
        conflict2.setId(2L);

        Participant conflict3 = new Participant();
        conflict3.setId(1L); // same as conflict1

        assertTrue(participant.getConflicts().isEmpty());
        participant.removeConflict(conflict1);
        participant.removeConflict(conflict2);
        participant.removeConflict(conflict3);

        participant.addConflict(conflict1);
        assertEquals(1, participant.getConflicts().size());
        assertSame(conflict1, participant.getConflicts().get(0));

        participant.addConflict(conflict2);
        assertEquals(2, participant.getConflicts().size());
        assertSame(conflict1, participant.getConflicts().get(0));
        assertSame(conflict2, participant.getConflicts().get(1));

        participant.addConflict(conflict3);
        assertEquals(2, participant.getConflicts().size());
        assertSame(conflict1, participant.getConflicts().get(0));
        assertSame(conflict2, participant.getConflicts().get(1));

        participant.removeConflict(conflict3);
        assertEquals(1, participant.getConflicts().size());
        assertSame(conflict2, participant.getConflicts().get(0));

        participant.removeConflict(conflict1);
        assertEquals(1, participant.getConflicts().size());
        assertSame(conflict2, participant.getConflicts().get(0));

        participant.removeConflict(conflict2);
        assertTrue(participant.getConflicts().isEmpty());
    }

    /** Create a Participant for testing. */
    private static Participant createParticipant() {
        Participant participant = new Participant();

        participant.setId(1L);
        participant.setName("name");
        participant.setNickname("nick");
        participant.setEmailAddress("email");
        participant.setTemplateOverrides(new TemplateConfig());
        participant.setConflicts(new ParticipantSet());

        return participant;
    }

    /** Assert that participants are equivalent, but not the same. */
    private static void assertConflictsEquivalent(ParticipantSet conflicts, Participant participant) {
        assertEquals(conflicts, participant.getConflicts());
        for (Participant copied : participant.getConflicts()) {
            for (Participant source : conflicts) {
                assertNotSame(source, copied);
            }
        }
    }

}
