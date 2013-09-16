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

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for ConfirmationPopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ConfirmationPopupClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        ConfirmationPopup popup = new ConfirmationPopup();
        assertNotNull(popup);
        assertEquals("", popup.getTitle());
        assertEquals("", popup.getMessage());
        assertNull(popup.getOkButtonHandler());
        assertFalse(popup.isShowing());

        popup = new ConfirmationPopup("title");
        assertNotNull(popup);
        assertEquals("title", popup.getTitle());
        assertEquals("", popup.getMessage());
        assertNull(popup.getOkButtonHandler());
        assertFalse(popup.isShowing());

        popup = new ConfirmationPopup("title", "message");
        assertNotNull(popup);
        assertEquals("title", popup.getTitle());
        assertEquals("message", popup.getMessage());
        assertNull(popup.getOkButtonHandler());
        assertFalse(popup.isShowing());
    }

    /** Test getters and setters. */
    public void testGettersSetters() {
        ConfirmationPopup popup = new ConfirmationPopup();

        assertSame(popup.okButton, popup.getOkButton());
        assertSame(popup.cancelButton, popup.getCancelButton());

        popup.setTitle("title");
        assertEquals("title", popup.getTitle());

        popup.setMessage("message");
        assertEquals("message", popup.getMessage());

        ViewEventHandler handler = new StubbedViewEventHandler();
        popup.setOkButtonHandler(handler);
        assertSame(handler, popup.getOkButtonHandler());
    }

    /** Test showPopup(). */
    public void testShowPopup() {
        ConfirmationPopup popup = new ConfirmationPopup("title", "message");
        try {
            popup.showPopup();
            assertTrue(popup.isShowing());
        } finally {
            // Because leaving the popup showing seems to screw up other tests in the suite
            popup.hide();
            assertFalse(popup.isShowing());
        }
    }

    /** Test onOkClicked(). */
    public void testOnOkClicked() {
        ConfirmationPopup popup = new ConfirmationPopup("title", "message");
        popup.showPopup();
        clickButton(popup.getOkButton());
        assertFalse(popup.isShowing());

        StubbedViewEventHandler handler = new StubbedViewEventHandler();
        popup.setOkButtonHandler(handler);
        popup.showPopup();
        clickButton(popup.getOkButton());
        assertFalse(popup.isShowing());
        assertTrue(handler.handledEvent());
    }

    /** Test onCancelClicked(). */
    public void testOnCancelClicked() {
        ConfirmationPopup popup = new ConfirmationPopup("title", "message");
        popup.showPopup();
        clickButton(popup.getCancelButton());
        assertFalse(popup.isShowing());

        StubbedViewEventHandler handler = new StubbedViewEventHandler();
        popup.setOkButtonHandler(handler);
        popup.showPopup();
        clickButton(popup.getCancelButton());
        assertFalse(popup.isShowing());
        assertFalse(handler.handledEvent());
    }

}
