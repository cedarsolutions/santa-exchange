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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for WelcomePopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class WelcomePopupClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        WidgetConstants constants = GWT.create(WidgetConstants.class);
        WelcomePopup popup = new WelcomePopup();
        assertNotNull(popup);
        assertEquals(constants.welcome_paragraph1(), popup.paragraph1.getText());
        assertEquals(constants.welcome_paragraph2(), popup.paragraph2.getText());
        assertEquals(constants.welcome_paragraph3(), popup.paragraph3.getText());
        assertEquals(constants.welcome_close(), popup.closeButton.getText());
    }

    /** Test showPopup(). */
    public void testShowPopup() {
        WelcomePopup popup = new WelcomePopup();
        popup.showPopup();
        assertTrue(popup.isShowing());
        popup.onCloseClicked(null);  // doesn't matter what's passed in
        assertFalse(popup.isShowing());
    }

}
