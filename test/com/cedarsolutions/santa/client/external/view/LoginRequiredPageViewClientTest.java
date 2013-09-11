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
package com.cedarsolutions.santa.client.external.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit test for LoginRequiredPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class LoginRequiredPageViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        ExternalConstants constants = GWT.create(ExternalConstants.class);
        LoginRequiredPageView view = new LoginRequiredPageView();
        assertNotNull(view);
        assertEquals(constants.loginRequired_headerText(), view.header.getText());
        assertEquals(constants.loginRequired_loginRequiredText(), view.paragraph.getText());
        assertFalse(view.loginSelector.getIsLoggedIn());
        assertNull(view.loginSelector.getLoginSelectorEventHandler());
    }

    /** Test setLoginSelectorEvenHandler(). */
    public void testSetLoginSelectorEventHandler() {
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        LoginRequiredPageView view = new LoginRequiredPageView();
        view.setLoginSelectorEventHandler(eventHandler);
        assertSame(eventHandler, view.getLoginSelectorEventHandler());
        assertSame(eventHandler, view.loginSelector.getLoginSelectorEventHandler());
    }

    /** Test getSelectedProvider(). */
    public void testGetSelectedProvider() {
        LoginRequiredPageView view = new LoginRequiredPageView();
        assertEquals(view.loginSelector.getSelectedProvider(), view.getSelectedProvider());
    }
}
