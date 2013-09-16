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
package com.cedarsolutions.santa.server.service;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.shared.domain.email.EmailMessage;

/**
 * Functionality manage Secret Santa exchanges.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IExchangeService {

    /**
     * Send the email message for an assignment.
     * @param exchange       Exchange that assignment is related to
     * @param assignment     Assignment to generate an email for
     * @param organizerOnly  Whether the email should be sent to the organizer only
     * @throws InvalidDataException If the exchange is not valid.
     */
    void sendMessage(Exchange exchange, Assignment assignment, boolean organizerOnly) throws InvalidDataException;

    /**
     * Send all email messages for a set of assignments.
     * @param exchange       Exchange that assignment is related to
     * @param assignments    Assignments to generate an email for
     * @param organizerOnly  Whether the email should be sent to the organizer only
     * @return The number of emails that were sent.
     * @throws InvalidDataException If the exchange is not valid.
     */
    int sendMessages(Exchange exchange, AssignmentSet assignments, boolean organizerOnly) throws InvalidDataException;

    /**
     * Generate assignments for the passed-in exchange.
     * @param exchange      Exchange to generate assignments for
     * @param autoConflict  Whether automatic conflict detection should be enabled
     * @return Randomly-generated set of assignment for this exchange.
     * @throws InvalidDataException If the exchange is not valid or assignments could not be generated
     */
    AssignmentSet generateAssignments(Exchange exchange, boolean autoConflict) throws InvalidDataException;

    /**
     * Generate a preview for an exchange notification email.
     * @param exchange      Exchange to operate on
     * @return A preview of the exchange notification email
     * @throws InvalidDataException If the exchange is not valid.
     */
    EmailMessage generatePreview(Exchange exchange) throws InvalidDataException;

}
