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

import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for SantaExchangeCustomLogger.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchangeCustomLoggerClientTest extends ClientTestCase {

    /** Test log() just to be sure it runs. */
    public void testLog() {
        SantaExchangeCustomLogger logger = new SantaExchangeCustomLogger();
        logger.log("Hello", 1);
    }

}
