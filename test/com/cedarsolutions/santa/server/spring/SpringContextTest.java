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
package com.cedarsolutions.santa.server.spring;

import org.junit.Test;

import com.cedarsolutions.junit.spring.SpringAssertions;

/**
 * Check that the Spring context can be loaded.
 *
 * <p>
 * Sometimes, odd errors result from this test, and it's difficult to track
 * then down.  See notes in SpringAssertions for suggestions about what you
 * might want to look for.
 * </p>

 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SpringContextTest {

    /** Test that the context can be loaded. */
    @Test public void testLoadSpringContext() throws Exception {
        SpringAssertions.assertSpringContextValid("war/WEB-INF", "rpc-servlet.xml", "applicationContext.xml");
    }

}
