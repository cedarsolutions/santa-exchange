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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Unit tests for SantaExchangeCustomLogger.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchangeCustomLoggerTest extends StubbedClientTestCase {

    /** Test the constructor. */
    @Test public void testConstructor() {
        SantaExchangeConfig config = (SantaExchangeConfig) GWT.create(SantaExchangeConfig.class);
        SantaExchangeCustomLogger logger = new SantaExchangeCustomLogger();
        assertEquals(config.customLogger_loggingMode(), logger.getLoggingMode());
        assertEquals(config.customLogger_loggingPrefix(), logger.getLoggingPrefix());
    }

}
