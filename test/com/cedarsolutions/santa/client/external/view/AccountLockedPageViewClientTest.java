/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.external.view;

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for AccountLockedPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AccountLockedPageViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        ExternalConstants constants = GWT.create(ExternalConstants.class);
        AccountLockedPageView view = new AccountLockedPageView();
        assertNotNull(view);

        assertEquals(constants.accountLocked_headerText(), view.header.getText());
        assertEquals(constants.accountLocked_accountLockedText(), view.paragraph.getText());

        assertEquals(constants.accountLocked_continueButton(), view.continueButton.getText());
        assertEquals(constants.accountLocked_continueTooltip(), view.continueButton.getTitle());
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        AccountLockedPageView view = new AccountLockedPageView();
        StubbedViewEventHandler continueEventHandler = new StubbedViewEventHandler();
        view.setContinueEventHandler(continueEventHandler);
        assertSame(continueEventHandler, view.getContinueEventHandler());
        clickButton(view.continueButton);
        assertTrue(continueEventHandler.handledEvent());
    }

}
