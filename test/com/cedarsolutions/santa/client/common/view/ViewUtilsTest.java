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
package com.cedarsolutions.santa.client.common.view;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.client.gwt.validation.ValidationUtils;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.widget.SantaExchangeStyles;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.shared.domain.FederatedUser;

/**
 * Unit tests for ViewUtils.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ViewUtilsTest extends StubbedClientTestCase {

    /** Test deriveDisplayedUsername() for FederatedUser input. */
    @Test public void testDeriveDisplayedUsernameUser() {
        ViewUtils utils = new ViewUtils();  // make sure to get the real class, not the auto-generated mock
        FederatedUser user = new FederatedUser();

        assertEquals("Unknown User", utils.deriveDisplayedUsername((FederatedUser) null));

        user.setUserName("");
        assertEquals("Unknown User", utils.deriveDisplayedUsername(user));

        user.setUserName("    ");
        assertEquals("Unknown User", utils.deriveDisplayedUsername(user));

        user.setUserName("user@example.com");
        assertEquals("user@example.com", utils.deriveDisplayedUsername(user));

        user.setUserName("xxxxxxxxxxxxxxxxx@yyyyyyyyyyyyyyy.com");
        assertEquals("xxxxxxxxxxxxxxxxx@yyyyyyyyyyyyyyy.com", utils.deriveDisplayedUsername(user));

        user.setUserName("xxxxxxxxxxxxxxxxxZ@yyyyyyyyyyyyyyy.com");
        assertEquals("xxxxxxxxxxxxxxxxxZ@yyyyyyyyyyyyyy[...]", utils.deriveDisplayedUsername(user));
    }

    /** Test deriveDisplayedUsername() for String input. */
    @Test public void testDeriveDisplayedUsernameString() {
        ViewUtils utils = new ViewUtils();  // make sure to get the real class, not the auto-generated mock

        assertEquals("Unknown User", utils.deriveDisplayedUsername((String) null));
        assertEquals("Unknown User", utils.deriveDisplayedUsername(""));
        assertEquals("Unknown User", utils.deriveDisplayedUsername("    "));

        assertEquals("user@example.com", utils.deriveDisplayedUsername("user@example.com"));
        assertEquals("xxxxxxxxxxxxxxxxx@yyyyyyyyyyyyyyy.com", utils.deriveDisplayedUsername("xxxxxxxxxxxxxxxxx@yyyyyyyyyyyyyyy.com"));
        assertEquals("xxxxxxxxxxxxxxxxxZ@yyyyyyyyyyyyyy[...]", utils.deriveDisplayedUsername("xxxxxxxxxxxxxxxxxZ@yyyyyyyyyyyyyyy.com"));
    }

    /** Test showValidationError(). */
    @Test public void testShowValidationError() {
        InvalidDataException error = new InvalidDataException("Hello");
        IViewWithValidation view = mock(IViewWithValidation.class);
        new ViewUtils().showValidationError(view, error);  // make sure to get the real class, not the auto-generated mock
        verify(ValidationUtils.getInstance()).showValidationError(view, error, SantaExchangeStyles.VALIDATION_ERROR_STYLE);
    }

}
