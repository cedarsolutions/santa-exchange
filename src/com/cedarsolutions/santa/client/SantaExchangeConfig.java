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

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Localized constants used project-wide.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface SantaExchangeConfig extends ConstantsWithLookup {

    /** Application name. */
    @DefaultStringValue("SantaExchange")
    String system_applicationName();

    /** Application version number. */
    @DefaultStringValue("0.9.30")
    String system_versionNumber();

    /** Application release date. */
    @DefaultStringValue("27 Sep 2013")
    String system_releaseDate();

    /** Copyright statement for the application. */
    @DefaultStringValue("Copyright (c) 2013 Kenneth J. Pronovici.  All rights reserved.")
    String system_copyrightStatement();

    /** Public application URL. */
    @DefaultStringValue("https://santa-exchange.appspot.com/")
    String system_applicationUrl();

    /** Public source code URL. */
    @DefaultStringValue("http://code.google.com/p/santa-exchange/")
    String system_sourceCodeUrl();

    /** URL for the Apache v2.0 license. */
    @DefaultStringValue("http://www.apache.org/licenses/LICENSE-2.0")
    String system_apacheLicenseUrl();

    /** URL for the application's AppEngine dashboard (available to administrators). */
    @DefaultStringValue("https://appengine.google.com/dashboard?&app_id=santa-exchange-hrd")
    String system_dashboardUrl();

    /** System-wide RPC timeout, in milliseconds. */
    @DefaultIntValue(30000)
    int system_defaultRpcTimeoutMs();

    /** Timeout for the XSRF RPC call, in milliseconds. */
    // This is so high because GAE is sometimes very slow when booting.
    // The XSRF RPC call is the first one that happens after boot, and
    // it sometimes times out when the limit is set to 30 seconds.
    @DefaultIntValue(45000)
    int system_xsrfRpcTimeoutMs();

    /** Whether ErrorPopup should display exception stack traces. */
    @DefaultBooleanValue(true)
    boolean errorPopup_displayExceptions();

    /** Whether ErrorPopup should log error messages via the GWT logger. */
    @DefaultBooleanValue(true)
    boolean errorPopup_logMessages();

    /** Which display mode should be used by the custom logger. */
    @DefaultStringValue(SantaExchangeCustomLogger.GWT_MODE)
    String customLogger_loggingMode();

    /** Logging prefix used by the custom logger. */
    @DefaultStringValue("SantaExchange: ")
    String customLogger_loggingPrefix();

}
