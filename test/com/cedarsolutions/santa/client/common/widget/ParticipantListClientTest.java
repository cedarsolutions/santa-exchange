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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;

/**
 * Client-side unit tests for ParticipantList.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ParticipantListClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        ParticipantList list = new ParticipantList();
        assertNotNull(list);
        assertEquals(0, list.getVisibleItemCount());
        assertEquals(0, list.getItemCount());
    }

    /** Test the setParticipants() method. */
    public void testSetParticipants() {
        Participant participant1 = new Participant();
        participant1.setId(1L);
        participant1.setName("name1");
        participant1.setEmailAddress("email1");

        Participant participant2 = new Participant();
        participant2.setId(2L);
        participant2.setName("name2");
        participant2.setEmailAddress("email2");

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant1);
        participants.add(participant2);

        ParticipantList list = new ParticipantList();
        list.setParticipants(participants);

        assertEquals(1, list.getVisibleItemCount());
        assertEquals(2, list.getItemCount());
        assertEquals(participant1, list.getSelectedValue());
        assertEquals("name1 (email1)", list.getItemText(0));
        assertEquals("name2 (email2)", list.getItemText(1));
    }

    /** Check that every value can be selected. */
    public void testSetSelectedValue() {
        Participant participant1 = new Participant();
        participant1.setId(1L);
        participant1.setName("name1");
        participant1.setEmailAddress("email1");

        Participant participant2 = new Participant();
        participant2.setId(2L);
        participant2.setName("name2");
        participant2.setEmailAddress("email2");

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant1);
        participants.add(participant2);

        ParticipantList list = new ParticipantList();
        list.setParticipants(participants);

        list.setSelectedValue(null);
        assertEquals(participant1, list.getSelectedValue());

        list.setSelectedValue(participant2);
        assertEquals(participant2, list.getSelectedValue());

        list.setSelectedValue(participant1);
        assertEquals(participant1, list.getSelectedValue());
    }

}
