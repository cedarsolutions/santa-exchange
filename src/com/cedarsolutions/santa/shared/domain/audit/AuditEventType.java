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
package com.cedarsolutions.santa.shared.domain.audit;

/**
 * Legal audit event types.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public enum AuditEventType {

    // NOTE: if you add items here, remember to add tooltips in AdminMessageConstants

    ADMIN_LOGIN,             // An admin user logged in to one of the authenticated modules
    USER_LOGIN,              // A user logged in to one of the authenticated modules
    REGISTER_USER,           // A user was registered due to their first login
    DELETE_USER,             // A user was deleted by an administrator
    LOCK_USER,               // A user was locked by an administrator
    UNLOCK_USER,             // A user was unlocked by an administrator
    CREATE_EXCHANGE,         // A new exchange was created by the owner
    DELETE_EXCHANGE,         // An exchange was deleted by the owner
    EXCHANGE_EMAIL,          // Emails were sent for an entire exchange
    RESEND_EMAILS;           // Emails were re-sent for some exchange participants

}
