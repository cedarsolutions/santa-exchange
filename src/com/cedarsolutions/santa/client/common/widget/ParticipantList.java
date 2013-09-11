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

import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;

/**
 * List box containing a set of participants.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ParticipantList extends DropdownList<Participant> {

    /** Create an empty list. */
    public ParticipantList() {
    }

    /** Set the participants that should be displayed. */
    public void setParticipants(ParticipantSet participants) {
        this.clear();

        if (participants != null && !participants.isEmpty()) {
            for (Participant participant : participants) {
                this.addDropdownItem(participant);
            }

            this.setSelectedValue(participants.get(0));
        }

        this.setVisibleItemCount(1);
    }

    /**
     * Get the key for a value.
     * @param value  Value to get the key for, not null
     * @return Key to associate with the passed-in value.
     */
    @Override
    protected String getKey(Participant value) {
        return String.valueOf(value.getId());
    }

    /**
     * Get the display string for a value.
     * @param value  Value to get the display string for, not null
     * @return Display string to be presented to user for this value.
     */
    @Override
    protected String getDisplay(Participant value) {
        return value.getName() + " (" + value.getEmailAddress() + ")";
    }

}
