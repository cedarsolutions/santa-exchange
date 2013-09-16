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
 * Set of participants in an exchange.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ParticipantSet extends ArrayList<Participant> implements Serializable {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Default constructor. */
    public ParticipantSet() {
        super();
    }

    /** Copy constructor. */
    public ParticipantSet(List<Participant> participants) {
        this();
        if (participants != null) {
            for (Participant source : participants) {
                Participant participant = new Participant(source);
                this.add(participant);
            }
        }
    }

}
