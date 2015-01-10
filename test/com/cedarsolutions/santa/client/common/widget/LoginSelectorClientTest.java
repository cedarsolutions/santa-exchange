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
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Client-side unit tests for LoginSelector.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class LoginSelectorClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        WidgetConstants constants = GWT.create(WidgetConstants.class);

        LoginSelector selector = new LoginSelector();
        assertNotNull(selector);

        assertFalse(selector.getIsLoggedIn());
        assertNull(selector.getContinueEventHandler());
        assertNull(selector.getLoginSelectorEventHandler());

        assertNotNull(selector.openIdList);
        assertEquals(OpenIdProviderList.DEFAULT_SELECTION, selector.getSelectedProvider());

        assertTrue(selector.openIdSection.isVisible());
        assertFalse(selector.openIdDisclosure.isOpen());
        assertFalse(selector.continueSection.isVisible());
        assertFalse(selector.continueDisclosure.isOpen());

        assertEquals(constants.login_openIdInstructions(), selector.openIdInstructions.getText());
        assertEquals(constants.login_openIdDisclosureText(), selector.openIdDisclosure.getHeaderTextAccessor().getText());
        assertEquals(constants.login_openIdExplanation1Text(), selector.openIdExplanation1.getText());
        assertEquals(constants.login_openIdExplanation2Text(), selector.openIdExplanation2.getText());
        assertEquals(constants.login_openIdExplanation3Text(), selector.openIdExplanation3.getText());
        assertEquals(constants.login_openIdButtonText(), selector.openIdButton.getText());
        assertEquals(constants.login_continueExplanationText(), selector.continueExplanation.getText());
        assertEquals(constants.login_continueButtonText(), selector.continueButton.getText());
    }

    /** Test setIsLoggedIn(). */
    public void testSetIsLoggedIn() {
        LoginSelector selector = new LoginSelector();

        selector.setIsLoggedIn(true);
        assertTrue(selector.getIsLoggedIn());
        assertFalse(selector.openIdSection.isVisible());
        assertTrue(selector.continueSection.isVisible());

        selector.setIsLoggedIn(false);
        assertFalse(selector.getIsLoggedIn());
        assertTrue(selector.openIdSection.isVisible());
        assertFalse(selector.continueSection.isVisible());
    }

    /** Test setContinueEventHandler(). */
    public void testSetContinueEventHandler() {
        LoginSelector selector = new LoginSelector();
        RootPanel.get().add(selector);  // if you don't add the selector to the panel, nothing happens

        StubbedViewEventHandler eventHandler = new StubbedViewEventHandler();
        selector.setContinueEventHandler(eventHandler);
        assertSame(eventHandler, selector.getContinueEventHandler());
        clickButton(selector.continueButton);
        assertTrue(eventHandler.handledEvent());
    }

    /** Test setLoginSelectorEventHandler(). */
    public void testLoginSelectorEventHandler() {
        LoginSelector selector = new LoginSelector();
        RootPanel.get().add(selector);  // if you don't add the selector to the panel, nothing happens

        StubbedViewEventHandler eventHandler = new StubbedViewEventHandler();
        selector.setLoginSelectorEventHandler(eventHandler);
        assertSame(eventHandler, selector.getLoginSelectorEventHandler());
        clickButton(selector.openIdButton);
        assertTrue(eventHandler.handledEvent());
    }

}
