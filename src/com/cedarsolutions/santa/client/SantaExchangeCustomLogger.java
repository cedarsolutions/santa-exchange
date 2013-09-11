/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
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

import com.cedarsolutions.client.gwt.widget.GwtCustomLogger;
import com.google.gwt.core.client.GWT;

/**
 * Custom Mvp4g logger with a couple of different modes.
 *
 * <p>
 * The logger can either write to the GWT log, or can pop an alert
 * for each message.  It's configured via the SantaExchangeConfig
 * constants bundle.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchangeCustomLogger extends GwtCustomLogger {

    /** Create a custom logger. */
    public SantaExchangeCustomLogger() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);  // note: can't be injected 'cause Gin doesn't create this class
        this.setLoggingMode(config.customLogger_loggingMode());
        this.setLoggingPrefix(config.customLogger_loggingPrefix());
    }

}
