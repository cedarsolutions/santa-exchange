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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cedarsolutions.client.gwt.widget.ListItem;
import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.util.gwt.GwtExceptionUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Client-side unit tests for ErrorPopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ErrorPopupClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        ErrorPopup popup = new ErrorPopup();
        assertNotNull(popup);
    }

    /** Test showError() with no supporting text. */
    public void testShowError() {

        // Note that I can't fully test the logic here, because there's no good
        // way to vary the values in SantaExchangeConfig at runtime.  So, I'm
        // going to settle for a quick check that things seem to work.  Also,
        // note that you apparently can't get the width or height of a widget
        // after setting it.  So, I can't verify that behavior.

        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        WidgetConstants constants = GWT.create(WidgetConstants.class);

        List<String> supporting = new ArrayList<String>();
        supporting.add("One");
        supporting.add("Two");

        Exception exception = new Exception("Hello, world");

        ErrorDescription error = new ErrorDescription();
        error.setException(exception);
        error.setMessage("My message");
        error.setSupportingTextItems(supporting);

        ErrorPopup popup = new ErrorPopup();
        RootPanel.get().add(popup);
        assertFalse(popup.isShowing());

        popup.showError(error);
        assertTrue(popup.isShowing());

        assertEquals(constants.errorPopup_closeButtonText(), popup.closeButton.getText());
        assertEquals(constants.errorPopup_closeButtonTooltip(), popup.closeButton.getTitle());
        assertEquals(constants.errorPopup_messageTextHeader(), popup.messageTextHeader.getText());
        assertEquals("My message", popup.messageText.getText());

        assertTrue(popup.supportingTextHeader.isVisible());
        assertTrue(popup.supportingTextList.isVisible());
        assertEquals(constants.errorPopup_supportingTextHeader(), popup.supportingTextHeader.getText());

        List<String> supportingTextResult = new ArrayList<String>();
        Iterator<Widget> iterator = popup.supportingTextList.iterator();
        while (iterator.hasNext()) {
            ListItem item = (ListItem) iterator.next();
            supportingTextResult.add(item.getText());
        }

        assertEquals(2, supportingTextResult.size());
        assertEquals("One", supportingTextResult.get(0));
        assertEquals("Two", supportingTextResult.get(1));

        if (!config.errorPopup_displayExceptions()) {
            assertFalse(popup.exceptionTextScrollPanel.isVisible());
            assertFalse(popup.exceptionTextHeader.isVisible());
            assertFalse(popup.exceptionText.isVisible());
        } else {
            assertTrue(popup.exceptionTextScrollPanel.isVisible());
            assertTrue(popup.exceptionTextHeader.isVisible());
            assertTrue(popup.exceptionText.isVisible());
            assertEquals(constants.errorPopup_exceptionTextHeader(), popup.exceptionTextHeader.getText());
            assertEquals(GwtExceptionUtils.generateStackTrace(exception), popup.exceptionText.getText());
        }

        popup.onCloseClicked(null);  // doesn't matter what's passed in
        assertFalse(popup.isShowing());
    }

}
