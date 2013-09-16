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
 * Unit tests for Exchange.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        Exchange exchange = new Exchange();
        assertNotNull(exchange);
        assertNull(exchange.getId());
        assertNull(exchange.getUserId());
        assertNull(exchange.getExchangeState());
        assertNull(exchange.getName());
        assertNull(exchange.getDateAndTime());
        assertNull(exchange.getTheme());
        assertNull(exchange.getCost());
        assertNull(exchange.getExtraInfo());
        assertNotNull(exchange.getOrganizer());
        assertNotNull(exchange.getTemplateOverrides());
        assertTrue(exchange.getParticipants().isEmpty());
        assertNull(exchange.getAssignments());

        Exchange copy = new Exchange(null);
        assertNotNull(copy);
        assertNull(copy.getId());
        assertNull(copy.getUserId());
        assertNull(copy.getExchangeState());
        assertNull(copy.getName());
        assertNull(copy.getDateAndTime());
        assertNull(copy.getTheme());
        assertNull(copy.getCost());
        assertNull(copy.getExtraInfo());
        assertNotNull(copy.getOrganizer());
        assertNotNull(copy.getTemplateOverrides());
        assertTrue(copy.getParticipants().isEmpty());
        assertNull(copy.getAssignments());

        exchange.setId(1L);
        exchange.setUserId("user");
        exchange.setExchangeState(ExchangeState.GENERATED);
        exchange.setName("name");
        exchange.setDateAndTime("date");
        exchange.setTheme("theme");
        exchange.setCost("cost");
        exchange.setExtraInfo("extraInfo");
        exchange.setOrganizer(new Organizer("name", "email", "phone"));
        exchange.setTemplateOverrides(new TemplateConfig());
        exchange.getParticipants().add(new Participant(1L));
        exchange.setAssignments(null);

        copy = new Exchange(exchange);
        assertEquals(exchange, copy);
        assertNotSame(exchange.getOrganizer(), copy.getOrganizer());
        assertNotSame(exchange.getTemplateOverrides(), copy.getTemplateOverrides());
        assertNotSame(exchange.getParticipants(), copy.getParticipants());
        assertNull(copy.getAssignments());

        exchange.setId(1L);
        exchange.setUserId("user");
        exchange.setExchangeState(ExchangeState.GENERATED);
        exchange.setName("name");
        exchange.setDateAndTime("date");
        exchange.setTheme("theme");
        exchange.setCost("cost");
        exchange.setExtraInfo("extraInfo");
        exchange.setOrganizer(new Organizer("name", "email", "phone"));
        exchange.setTemplateOverrides(new TemplateConfig());
        exchange.getParticipants().add(new Participant(1L));
        exchange.setAssignments(new AssignmentSet());
        exchange.getAssignments().add(new Assignment(new Participant(2L), new Participant(3L)));

        copy = new Exchange(exchange);
        assertEquals(exchange, copy);
        assertNotSame(exchange.getOrganizer(), copy.getOrganizer());
        assertNotSame(exchange.getTemplateOverrides(), copy.getTemplateOverrides());
        assertNotSame(exchange.getParticipants(), copy.getParticipants());
        assertNotSame(exchange.getAssignments(), copy.getAssignments());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        Exchange exchange = new Exchange();

        exchange.setId(12L);
        assertEquals(new Long(12), exchange.getId());

        exchange.setUserId("userId");
        assertEquals("userId", exchange.getUserId());

        exchange.setUserId("blech");
        assertEquals("blech", exchange.getUserId());

        exchange.setExchangeState(ExchangeState.GENERATED);
        assertEquals(ExchangeState.GENERATED, exchange.getExchangeState());

        exchange.setName("0");
        assertEquals("0", exchange.getName());

        exchange.setDateAndTime("1");
        assertEquals("1", exchange.getDateAndTime());

        exchange.setTheme("2");
        assertEquals("2", exchange.getTheme());

        exchange.setCost("3");
        assertEquals("3", exchange.getCost());

        exchange.setExtraInfo("4");
        assertEquals("4", exchange.getExtraInfo());

        Organizer organizer = new Organizer();
        exchange.setOrganizer(organizer);
        assertEquals(organizer, exchange.getOrganizer());

        TemplateConfig emailConfig = new TemplateConfig();
        exchange.setTemplateOverrides(emailConfig);
        assertSame(emailConfig, exchange.getTemplateOverrides());

        ParticipantSet participants = new ParticipantSet();
        exchange.setParticipants(participants);
        assertSame(participants, exchange.getParticipants());

        AssignmentSet assignments = new AssignmentSet();
        exchange.setAssignments(assignments);
        assertSame(assignments, exchange.getAssignments());
    }

    /** Test getNextParticipantId(). */
    @Test public void testGetNextParticipantId() {
        Exchange exchange = new Exchange();

        exchange.setParticipants(null);
        assertEquals(1, exchange.getNextParticipantId());

        exchange.setParticipants(new ParticipantSet());
        assertEquals(1, exchange.getNextParticipantId());

        exchange.getParticipants().add(new Participant(14L));
        assertEquals(15, exchange.getNextParticipantId());

        exchange.getParticipants().add(new Participant(3L));
        assertEquals(15, exchange.getNextParticipantId());

        exchange.getParticipants().add(new Participant(-1L));
        assertEquals(15, exchange.getNextParticipantId());

        exchange.getParticipants().add(new Participant(15L));
        assertEquals(16, exchange.getNextParticipantId());

        exchange.getParticipants().add(new Participant(993L));
        assertEquals(994L, exchange.getNextParticipantId());
    }

    /** Test replaceParticipant(). */
    @Test public void testReplaceParticipant() {
        Participant participant1 = new Participant();
        participant1.setId(null);
        participant1.setName(null);

        Participant participant2a = new Participant();
        participant2a.setId(2L);
        participant2a.setName("2a");

        Participant participant2b = new Participant();
        participant2b.setId(2L);
        participant2b.setName("2b");

        Participant participant3 = new Participant();
        participant3.setId(3L);
        participant3.setName("3");

        Exchange exchange = new Exchange();
        assertTrue(exchange.getParticipants().isEmpty());

        assertFalse(exchange.replaceParticipant(null));
        assertTrue(exchange.getParticipants().isEmpty());

        assertFalse(exchange.replaceParticipant(participant1));
        assertTrue(exchange.getParticipants().isEmpty());

        assertFalse(exchange.replaceParticipant(participant2a));
        assertTrue(exchange.getParticipants().isEmpty());

        exchange.getParticipants().add(participant1);
        assertFalse(exchange.replaceParticipant(participant1));  // ignored because of null id
        assertEquals(1, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));

        exchange.getParticipants().add(participant2a);
        assertTrue(exchange.replaceParticipant(participant2a));
        assertEquals(2, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));
        assertEquals(participant2a, exchange.getParticipants().get(1));

        assertTrue(exchange.replaceParticipant(participant2b));
        assertEquals(2, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));
        assertEquals(participant2b, exchange.getParticipants().get(1));

        assertFalse(exchange.replaceParticipant(participant3));
        assertEquals(2, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));
        assertEquals(participant2b, exchange.getParticipants().get(1));

        exchange.setParticipants(null);
        assertFalse(exchange.replaceParticipant(participant3));
    }

    /** Test removeParticipant(). */
    @Test public void testRemoveParticipant() {
        Participant participant1 = new Participant();
        participant1.setId(null);
        participant1.setName(null);

        Participant participant2a = new Participant();
        participant2a.setId(2L);
        participant2a.setName("2a");

        Participant participant2b = new Participant();
        participant2b.setId(2L);
        participant2b.setName("2b");

        Participant participant3 = new Participant();
        participant3.setId(3L);
        participant3.setName("3");

        Exchange exchange = new Exchange();
        assertTrue(exchange.getParticipants().isEmpty());

        assertFalse(exchange.removeParticipant(null));
        assertTrue(exchange.getParticipants().isEmpty());

        assertFalse(exchange.removeParticipant(participant1));
        assertTrue(exchange.getParticipants().isEmpty());

        assertFalse(exchange.removeParticipant(participant2a));
        assertTrue(exchange.getParticipants().isEmpty());

        exchange.getParticipants().add(participant1);
        assertFalse(exchange.removeParticipant(participant1));  // ignored because of null id
        assertEquals(1, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));

        exchange.getParticipants().add(participant2a);
        assertTrue(exchange.removeParticipant(participant2a));
        assertEquals(1, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));

        exchange.getParticipants().add(participant2a);
        assertTrue(exchange.removeParticipant(participant2b));
        assertEquals(1, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));

        assertFalse(exchange.removeParticipant(participant3));
        assertEquals(1, exchange.getParticipants().size());
        assertEquals(participant1, exchange.getParticipants().get(0));

        exchange.setParticipants(null);
        assertFalse(exchange.removeParticipant(participant3));
    }

    /** Test getParticipantById(). */
    @Test public void testGetParticipantById() {
        Participant participant1 = new Participant();
        participant1.setId(null);
        participant1.setName(null);

        Participant participant2a = new Participant();
        participant2a.setId(2L);
        participant2a.setName("2a");

        Participant participant2b = new Participant();
        participant2b.setId(2L);
        participant2b.setName("2b");

        Participant participant3 = new Participant();
        participant3.setId(3L);
        participant3.setName("3");

        Exchange exchange = new Exchange();

        assertNull(exchange.getParticipantById(2L));
        assertNull(exchange.getParticipantById(3L));

        exchange.getParticipants().add(participant1);
        assertNull(exchange.getParticipantById(2L));
        assertNull(exchange.getParticipantById(3L));

        exchange.getParticipants().add(participant2a);
        assertSame(participant2a, exchange.getParticipantById(2L));
        assertNull(exchange.getParticipantById(3L));

        exchange.getParticipants().add(participant2b);
        assertSame(participant2a, exchange.getParticipantById(2L));
        assertNull(exchange.getParticipantById(3L));

        exchange.getParticipants().add(participant3);
        assertSame(participant2a, exchange.getParticipantById(2L));
        assertSame(participant3, exchange.getParticipantById(3L));

        exchange.setParticipants(null);
        assertNull(exchange.getParticipantById(2L));
        assertNull(exchange.getParticipantById(3L));
    }

    /** Test equals(). */
    @Test public void testEquals() {
        Exchange exchange1;
        Exchange exchange2;

        exchange1 = createExchange();
        exchange2 = createExchange();
        assertTrue(exchange1.equals(exchange2));
        assertTrue(exchange2.equals(exchange1));

        try {
            exchange1 = createExchange();
            exchange2 = null;
            exchange1.equals(exchange2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            exchange1 = createExchange();
            exchange2 = null;
            exchange1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setId(0L);
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setUserId("X");
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setExchangeState(ExchangeState.SENT);
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setName("X");
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setDateAndTime("X");
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setTheme("X");
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setCost("X");
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setExtraInfo("X");
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setOrganizer(null);
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setTemplateOverrides(null);
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setParticipants(null);
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));

        exchange1 = createExchange();
        exchange2 = createExchange();
        exchange2.setAssignments(null);
        assertFalse(exchange1.equals(exchange2));
        assertFalse(exchange2.equals(exchange1));
   }

    /** Test hashCode(). */
    @Test public void testHashCode() {

        Exchange exchange1 = createExchange();
        exchange1.setId(0L);

        Exchange exchange2 = createExchange();
        exchange2.setUserId("X");

        Exchange exchange3 = createExchange();
        exchange3.setExchangeState(ExchangeState.SENT);

        Exchange exchange4 = createExchange();
        exchange4.setName("X");

        Exchange exchange5 = createExchange();
        exchange5.setDateAndTime("X");

        Exchange exchange6 = createExchange();
        exchange6.setTheme("X");

        Exchange exchange7 = createExchange();
        exchange7.setCost("X");

        Exchange exchange8 = createExchange();
        exchange8.setExtraInfo("X");

        Exchange exchange9 = createExchange();
        exchange9.setOrganizer(null);

        Exchange exchange10 = createExchange();
        exchange10.setTemplateOverrides(null);

        Exchange exchange11 = createExchange();
        exchange11.setParticipants(null);

        Exchange exchange12 = createExchange();
        exchange12.setAssignments(null);

        Exchange exchange13 = createExchange();
        exchange13.setId(0L); // same as exchange1

        Map<Exchange, String> map = new HashMap<Exchange, String>();
        map.put(exchange1, "ONE");
        map.put(exchange2, "TWO");
        map.put(exchange3, "THREE");
        map.put(exchange4, "FOUR");
        map.put(exchange5, "FIVE");
        map.put(exchange6, "SIX");
        map.put(exchange7, "SEVEN");
        map.put(exchange8, "EIGHT");
        map.put(exchange9, "NINE");
        map.put(exchange10, "TEN");
        map.put(exchange11, "ELEVEN");
        map.put(exchange12, "TWELVE");

        assertEquals("ONE", map.get(exchange1));
        assertEquals("TWO", map.get(exchange2));
        assertEquals("THREE", map.get(exchange3));
        assertEquals("FOUR", map.get(exchange4));
        assertEquals("FIVE", map.get(exchange5));
        assertEquals("SIX", map.get(exchange6));
        assertEquals("SEVEN", map.get(exchange7));
        assertEquals("EIGHT", map.get(exchange8));
        assertEquals("NINE", map.get(exchange9));
        assertEquals("TEN", map.get(exchange10));
        assertEquals("ELEVEN", map.get(exchange11));
        assertEquals("TWELVE", map.get(exchange12));
        assertEquals("ONE", map.get(exchange13));
    }

    /** Create a Exchange for testing. */
    private static Exchange createExchange() {
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
        Participant participant2 = new Participant(1L, "p2", "p2n", "p2@example.com", null);

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant1);
        participants.add(participant2);

        Assignment assignment1 = new Assignment(participant1, participant2);
        Assignment assignment2 = new Assignment(participant2, participant1);

        AssignmentSet assignments = new AssignmentSet();
        assignments.add(assignment1);
        assignments.add(assignment2);

        Exchange exchange = new Exchange();
        exchange.setId(12L);
        exchange.setUserId("userId");
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
