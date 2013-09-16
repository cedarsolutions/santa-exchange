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
package com.cedarsolutions.santa.shared.domain.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * Unit tests for ExchangeKeyProvider.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ParticipantKeyProviderTest {

    /** Test ExchangeKeyProvider. */
    @Test public void testExchangeKeyProvider() {
        Participant item = mock(Participant.class);

        ParticipantKeyProvider provider = new ParticipantKeyProvider();

        assertNull(provider.getKey(null));

        when(item.getId()).thenReturn(null);
        assertNull(provider.getKey(item));

        when(item.getId()).thenReturn(2L);
        assertEquals(new Long(2L), provider.getKey(item));
    }

}
