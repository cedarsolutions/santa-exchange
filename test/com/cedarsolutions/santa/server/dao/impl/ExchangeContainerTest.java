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
package com.cedarsolutions.santa.server.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.cedarsolutions.santa.shared.domain.exchange.Organizer;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.santa.shared.domain.exchange.TemplateConfig;
import com.cedarsolutions.shared.domain.email.EmailFormat;

/**
 * Unit tests for ExchangeContainer.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeContainerTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        ExchangeContainer container = new ExchangeContainer();
        assertNotNull(container);
        assertNull(container.getId());
        assertNull(container.getUserId());
        assertNull(container.getSerialized());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        ExchangeContainer container = new ExchangeContainer();

        container.setId(12L);
        assertEquals(new Long(12), container.getId());

        container.setUserId("user");
        assertEquals("user", container.getUserId());

        container.setSerialized("serialized");
        assertEquals("serialized", container.getSerialized());
    }

    /** Test fromValue()/toValue() for null values. */
    @Test public void testFromToNull() {
        ExchangeContainer container = new ExchangeContainer();

        assertNull(container.getSerialized());
        assertNull(container.toValue());

        try {
            container.fromValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }
    }

    /** Test fromValue()/toValue() round trip. */
    @Test public void testRoundTrip() {
        Exchange input = createExchange();
        ExchangeContainer container = new ExchangeContainer();

        container.fromValue(input);
        assertEquals(input.getId(), container.getId());
        assertEquals(input.getUserId(), container.getUserId());
        assertFalse(StringUtils.isEmpty(container.getSerialized()));

        Exchange result = container.toValue();
        assertEquals(input, result);
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
        Participant participant2 = new Participant(2L, "p2", "p2n", "p2@example.com", null);

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
