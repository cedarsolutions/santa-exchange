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
package com.cedarsolutions.santa.client.internal.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.google.gwt.core.client.GWT;

/**
 * Unit tests for InternalUtils.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalUtilsTest extends StubbedClientTestCase {

    /** Test getExchangeStateForDisplay(). */
    @Test public void testGetExchangeStateForDisplay() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        assertEquals(constants.state_new(), InternalUtils.getExchangeStateForDisplay(null));
        assertEquals(constants.state_new(), InternalUtils.getExchangeStateForDisplay(ExchangeState.NEW));
        assertEquals(constants.state_started(), InternalUtils.getExchangeStateForDisplay(ExchangeState.STARTED));
        assertEquals(constants.state_generated(), InternalUtils.getExchangeStateForDisplay(ExchangeState.GENERATED));
        assertEquals(constants.state_sent(), InternalUtils.getExchangeStateForDisplay(ExchangeState.SENT));
    }

}
