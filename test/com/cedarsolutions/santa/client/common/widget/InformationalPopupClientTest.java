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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for InformationalPopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InformationalPopupClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        InformationalPopup popup = new InformationalPopup();
        assertNotNull(popup);
        assertEquals("", popup.getTitle());
        assertEquals("", popup.getMessage());
        assertFalse(popup.isShowing());

        popup = new InformationalPopup("title");
        assertNotNull(popup);
        assertEquals("title", popup.getTitle());
        assertEquals("", popup.getMessage());
        assertFalse(popup.isShowing());

        popup = new InformationalPopup("title", "message");
        assertNotNull(popup);
        assertEquals("title", popup.getTitle());
        assertEquals("message", popup.getMessage());
        assertFalse(popup.isShowing());
    }

    /** Test getters and setters. */
    public void testGettersSetters() {
        InformationalPopup popup = new InformationalPopup();

        assertSame(popup.closeButton, popup.getCloseButton());

        popup.setTitle("title");
        assertEquals("title", popup.getTitle());

        popup.setMessage("message");
        assertEquals("message", popup.getMessage());
    }

    /** Test showPopup(). */
    public void testShowPopup() {
        InformationalPopup popup = new InformationalPopup("title", "message");
        try {
            popup.showPopup();
            assertTrue(popup.isShowing());
        } finally {
            // Because leaving the popup showing seems to screw up other tests in the suite
            popup.hide();
            assertFalse(popup.isShowing());
        }
    }

    /** Test onCloseClicked(). */
    public void testOnCloseClicked() {
        InformationalPopup popup = new InformationalPopup("title", "message");
        popup.showPopup();
        clickButton(popup.getCloseButton());
        assertFalse(popup.isShowing());
    }

}
