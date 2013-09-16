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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Set of assignments for an exchange.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AssignmentSet extends ArrayList<Assignment> implements Serializable {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Default constructor. */
    public AssignmentSet() {
        super();
    }

    /** Copy constructor. */
    public AssignmentSet(List<Assignment> assignments) {
        this();
        if (assignments != null) {
            for (Assignment source : assignments) {
                Assignment assignment = new Assignment(source);
                this.add(assignment);
            }
        }
    }

    /**
     * Get the giver that is assigned to a receiver, if any.
     * @param  giftReceiver   Receiver to check for
     * @return Giver that is assigned to the receiver, or null none is assigned.
     */
    public Participant getGiftGiver(Participant giftReceiver) {
        Participant giftGiver = null;

        if (giftReceiver.getId() != null) {
            for (Assignment assignment : this) {
                if (giftReceiver.getId().equals(assignment.getGiftReceiver().getId())) {
                    giftGiver = assignment.getGiftGiver();
                    break;
                }
            }
        }

        return giftGiver;
    }

    /**
     * Get the receiver that is assigned to a giver, if any.
     * @param  giftGiver   Receiver to check for
     * @return Giver that is assigned to the receiver, or null none is assigned.
     */
    public Participant getGiftReceiver(Participant giftGiver) {
        Participant giftReceiver = null;

        if (giftGiver.getId() != null) {
            for (Assignment assignment : this) {
                if (giftGiver.getId().equals(assignment.getGiftGiver().getId())) {
                    giftReceiver = assignment.getGiftReceiver();
                    break;
                }
            }
        }

        return giftReceiver;
    }

}
