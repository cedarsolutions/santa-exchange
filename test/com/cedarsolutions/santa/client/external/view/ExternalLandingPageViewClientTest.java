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
package com.cedarsolutions.santa.client.external.view;

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

/**
 * Client-side unit tests for ExternalLandingPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalLandingPageViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        ExternalConstants constants = GWT.create(ExternalConstants.class);

        ExternalLandingPageView view = new ExternalLandingPageView();
        assertNotNull(view);
        assertEquals(constants.landingPage_headerText(), view.header.getText());
        assertEquals(constants.landingPage_paragraph1Text(), view.paragraph1.getText());
        assertEquals(constants.landingPage_paragraph2Text(), view.paragraph2.getText());
        assertEquals(constants.landingPage_paragraph3Text(), view.paragraph3.getText());
        assertNotNull(view.paragraph4);
        assertFalse(view.getIsLoggedIn());
        assertNull(view.loginSelector.getContinueEventHandler());
        assertNull(view.loginSelector.getLoginSelectorEventHandler());

        HTML html = (HTML) view.paragraph4.getWidget(0);
        String format = constants.landingPage_paragraph4TextFormat();
        String result = GwtStringUtils.format(format, config.system_apacheLicenseUrl(), config.system_sourceCodeUrl());
        assertEquals(result, html.getHTML());
    }

    /** Test setIsLoggedIn(). */
    public void testIsLoggedIn() {
        ExternalLandingPageView view = new ExternalLandingPageView();

        view.setIsLoggedIn(false);
        assertFalse(view.getIsLoggedIn());

        view.setIsLoggedIn(true);
        assertTrue(view.getIsLoggedIn());
    }

    /** Test setContinueEventHandler(). */
    public void testSetContinueEventHandler() {
        StubbedViewEventHandler eventHandler = new StubbedViewEventHandler();
        ExternalLandingPageView view = new ExternalLandingPageView();
        view.setContinueEventHandler(eventHandler);
        assertSame(eventHandler, view.getContinueEventHandler());
        assertSame(eventHandler, view.loginSelector.getContinueEventHandler());
        clickButton(view.loginSelector.getContinueButton());
        assertFalse(eventHandler.getEvents().isEmpty());
    }

    /** Test setLoginSelectorEventHandler(). */
    public void testSetLoginSelectorEventHandler() {
        StubbedViewEventHandler eventHandler = new StubbedViewEventHandler();
        ExternalLandingPageView view = new ExternalLandingPageView();
        view.setLoginSelectorEventHandler(eventHandler);
        assertSame(eventHandler, view.getLoginSelectorEventHandler());
        assertSame(eventHandler, view.loginSelector.getLoginSelectorEventHandler());
        clickButton(view.loginSelector.getOpenIdButton());
        assertFalse(eventHandler.getEvents().isEmpty());
    }

    /** Test getSelectedProvider(). */
    public void testGetSelectedProvider() {
        ExternalLandingPageView view = new ExternalLandingPageView();
        assertEquals(view.loginSelector.getSelectedProvider(), view.getSelectedProvider());
    }

}
