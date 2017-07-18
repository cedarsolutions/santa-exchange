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

import java.util.List;

import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;

/**
 * Log auditable events in the system.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IAuditEventService {

    /**
     * Log the passed-in audit event to the datastore.
     * @param auditEvent    Audit event to log
     */
    void logAuditEvent(AuditEvent auditEvent);

    /**
     * Build an audit event, including context information.
     * @param eventType   Event type
     * @param extraData   Extra event data
     * @return Newly-created audit event.
     */
    AuditEvent buildAuditEvent(AuditEventType eventType, ExtraData... extraData);

    /**
     * Build an audit event, including context information.
     * @param eventType   Event type
     * @param extraData   Extra event data
     * @return Newly-created audit event.
     */
    AuditEvent buildAuditEvent(AuditEventType eventType, List<ExtraData> extraData);

    /**
     * Build a delete user event.
     * @param userId        User id of the user that was deleted
     * @param emailAddress  Email address of the user that was deleted
     */
    AuditEvent buildDeleteUserEvent(String userId, String emailAddress);

    /**
     * Build a lock user event.
     * @param userId        User id of the user that was locked
     * @param emailAddress  Email address of the user that was locked
     */
    AuditEvent buildLockUserEvent(String userId, String emailAddress);

    /**
     * Build an unlock user event.
     * @param userId        User id of the user that was unlocked
     * @param emailAddress  Email address of the user that was locked
     */
    AuditEvent buildUnlockUserEvent(String userId, String emailAddress);

    /**
     * Build a create exchange event.
     * @param exchangeId  Exchange id of the exchange that was created
     */
    AuditEvent buildCreateExchangeEvent(Long exchangeId);

    /**
     * Build a delete exchange event.
     * @param exchangeId  Exchange id of the exchange that was deleted
     */
    AuditEvent buildDeleteExchangeEvent(Long exchangeId);

    /**
     * Build a exchange email event.
     * @param exchangeId  Exchange id of the exchange for which emails were sent
     * @param recipients  Number of recipients that emails were generated for
     */
    AuditEvent buildExchangeEmailEvent(Long exchangeId, int recipients);

    /**
     * Build a resend emails event.
     * @param exchangeId  Exchange id of the exchange for which emails were sent
     * @param recipients  Number of recipients that emails were generated for
     */
    AuditEvent buildResendEmailsEvent(Long exchangeId, int recipients);

}
