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
package com.cedarsolutions.santa.client.internal.view;

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for PreviewPopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PreviewPopupClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        PreviewPopup popup = new PreviewPopup();
        assertEquals(constants.preview_close(), popup.closeButton.getText());
    }

    /** Test showPopup(). */
    public void testShowPopup() {
        PreviewPopup popup = new PreviewPopup();
        popup.showPopup(new EmailMessage());
        assertTrue(popup.isShowing());

        assertEquals(2, popup.tabPanel.getWidgetCount());
        // It's difficult to validate the panel contents or the tab title

        popup.onCloseClicked(null);  // doesn't matter what's passed in
        assertFalse(popup.isShowing());
    }

}
