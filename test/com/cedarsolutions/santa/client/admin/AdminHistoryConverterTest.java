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
package com.cedarsolutions.santa.client.admin;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

/**
 * Unit tests for AdminHistoryConverter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminHistoryConverterTest {

    /** Test isCrawlable(). */
    @Test public void testIsCrawlable() {
        AdminHistoryConverter converter = new AdminHistoryConverter();
        assertFalse(converter.isCrawlable());
    }

    /** Test convertFromToken(). */
    @Test public void testConvertFromToken() {
        AdminHistoryConverter converter = new AdminHistoryConverter();
        String eventType = "type";
        String param = "param";
        AdminEventBus eventBus = mock(AdminEventBus.class);
        converter.convertFromToken(eventType, param, eventBus);
        verify(eventBus).dispatch(eventType);
    }

}
