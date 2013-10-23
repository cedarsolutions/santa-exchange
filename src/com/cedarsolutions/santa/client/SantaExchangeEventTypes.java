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
package com.cedarsolutions.santa.client;

/**
 * Constants for event type names that have to be shared.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchangeEventTypes {

    public static final String ADMIN_MODULE = "admin";
    public static final String INTERNAL_MODULE = "internal";
    public static final String EXTERNAL_MODULE = "external";

    public static final String START = "start";
    public static final String INIT = "init";
    public static final String CLEAR_SESSION = "clearSession";

    public static final String REPLACE_ROOT_BODY = "replaceRootBody";

    public static final String LANDING_PAGE = "landingPage";
    public static final String EXTERNAL_LANDING_PAGE = "externalLandingPage";
    public static final String ADMIN_LANDING_PAGE = "adminLandingPage";
    public static final String INTERNAL_LANDING_PAGE = "internalLandingPage";

    public static final String INTERNAL_EXCHANGE_LIST = "exchangeList";
    public static final String INTERNAL_EDIT_EXCHANGE = "editExchange";
    public static final String INTERNAL_EDIT_PARTICIPANT = "editParticipant";

    public static final String ADMIN_HOME_TAB = "adminHome";
    public static final String ADMIN_USER_TAB = "adminUser";
    public static final String ADMIN_AUDIT_TAB = "adminAudit";

    public static final String ACCOUNT_LOCKED_PAGE = "accountLocked";
    public static final String RPC_TEST_PAGE = "rpcTest";

}
