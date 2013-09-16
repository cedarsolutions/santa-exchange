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
package com.cedarsolutions.santa.client.admin.view;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Message-constants used by this module.
 *
 * <p>
 * These are implemented as constants because there is no lookup mechanism for
 * messages.  Screen validation would be painful without lookup, so this is
 * what seems to make the most sense.  The down-side is that there's no
 * built-in argument formatting like with a real Messages interface.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface AdminMessageConstants extends ConstantsWithLookup {


    /* ********************
     *  Audit tab messages
     * ********************/

    @DefaultStringValue("Provide either start date, end date, or both.")
    String auditTab_required_startDate();


    /* ********************************
     *  Tooltips for audit event types
     * ********************************/

    @DefaultStringValue("An admin user logged into one of the authenticated modules")
    String auditEventTooltip_ADMIN_LOGIN();

    @DefaultStringValue("A user logged in to one of the authenticated modules")
    String auditEventTooltip_USER_LOGIN();

    @DefaultStringValue("A user was registered due to their first login")
    String auditEventTooltip_REGISTER_USER();

    @DefaultStringValue("A user was deleted by an administrator")
    String auditEventTooltip_DELETE_USER();

    @DefaultStringValue("A user was locked by an administrator")
    String auditEventTooltip_LOCK_USER();

    @DefaultStringValue("A user was unlocked by an administrator")
    String auditEventTooltip_UNLOCK_USER();

    @DefaultStringValue("A new exchange was created")
    String auditEventTooltip_CREATE_EXCHANGE();

    @DefaultStringValue("An exchange was deleted")
    String auditEventTooltip_DELETE_EXCHANGE();

    @DefaultStringValue("Emails were sent for an entire exchange")
    String auditEventTooltip_EXCHANGE_EMAIL();

    @DefaultStringValue("Emails were re-sent for some exchange participants")
    String auditEventTooltip_RESEND_EMAILS();

}
