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
package com.cedarsolutions.santa.shared.domain.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

/**
 * Unit tests for RegisteredUserKeyProvider.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserKeyProviderTest {

    /** Test RegisteredUserKeyProvider. */
    @Test public void testRegisteredUserKeyProvider() {
        RegisteredUser item = mock(RegisteredUser.class);

        RegisteredUserKeyProvider provider = new RegisteredUserKeyProvider();

        assertNull(provider.getKey(null));

        when(item.getUserId()).thenReturn(null);
        assertNull(provider.getKey(item));

        when(item.getUserId()).thenReturn("user");
        assertEquals("user", provider.getKey(item));
    }

}
