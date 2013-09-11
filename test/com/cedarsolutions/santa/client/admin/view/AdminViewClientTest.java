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
package com.cedarsolutions.santa.client.admin.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for AdminView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        AdminConstants constants = GWT.create(AdminConstants.class);

        AdminView view = new AdminView();
        assertNotNull(view);
        assertNotNull(view.title);
        assertNotNull(view.mainMenu);
        assertNotNull(view.internalLandingPageItem);
        assertNotNull(view.dashboardItem);
        assertNotNull(view.logoutItem);
        assertNotNull(view.moduleBody);

        assertEquals(null, view.getCurrentUser());
        assertNull(view.getLogoutEventHandler());

        assertEquals(constants.header_title(), view.title.getText());
        assertEquals("", view.mainMenu.getText());
        assertEquals(constants.header_internalLandingPage(), view.internalLandingPageItem.getText());
        assertEquals(constants.header_dashboard(), view.dashboardItem.getText());
        assertEquals(constants.header_sourceCode(), view.sourceCodeItem.getText());
        assertEquals(constants.header_logout(), view.logoutItem.getText());
    }

    /** Test getModuleBody(). */
    public void testGetModuleBody() {
        AdminView view = new AdminView();
        assertSame(view.moduleBody, view.getModuleBody());
    }

    /** Test setCurrentUser(). */
    public void testSetCurrentUser() {
        FederatedUser currentUser = new FederatedUser();
        AdminView view = new AdminView();

        currentUser.setUserName("ken");
        view.setCurrentUser(currentUser);
        assertSame(currentUser, view.getCurrentUser());
        assertEquals("ken", view.mainMenu.getText());

        currentUser.setUserName("12345678901234567890123456789012345678");
        view.setCurrentUser(currentUser);
        assertSame(currentUser, view.getCurrentUser());
        assertEquals("123456789012345678901234567890123[...]", view.mainMenu.getText());  // prove that ViewUtils is invoked
    }

    /** Test setInternalLandingPageEventHandler(). */
    public void testSetInternalLandingPageEventHandler() {
        AdminView view = new AdminView();
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        view.setInternalLandingPageEventHandler(eventHandler);  // unfortunately, there's no way to verify this sets the underlying widget properly
        assertSame(eventHandler, view.getInternalLandingPageEventHandler());
    }

    /** Test setDashboardEventHandler(). */
    public void testSetDashboardEventHandler() {
        AdminView view = new AdminView();
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        view.setDashboardEventHandler(eventHandler);  // unfortunately, there's no way to verify this sets the underlying widget properly
        assertSame(eventHandler, view.getDashboardEventHandler());
    }

    /** Test setSourceCodeEventHandler(). */
    public void testSetSourceCodeEventHandler() {
        AdminView view = new AdminView();
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        view.setSourceCodeEventHandler(eventHandler);  // unfortunately, there's no way to verify this sets the underlying widget properly
        assertSame(eventHandler, view.getSourceCodeEventHandler());
    }

    /** Test setLogoutEventHandler(). */
    public void testSetLogoutEventHandler() {
        AdminView view = new AdminView();
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        view.setLogoutEventHandler(eventHandler);  // unfortunately, there's no way to verify this sets the underlying widget properly
        assertSame(eventHandler, view.getLogoutEventHandler());
    }

    /** Test setAboutEventHandler(). */
    public void testSetAboutEventHandler() {
        AdminView view = new AdminView();
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        view.setAboutEventHandler(eventHandler);  // unfortunately, there's no way to verify this sets the underlying widget properly
        assertSame(eventHandler, view.getAboutEventHandler());
    }

    /** Test setBugReportEventHandler(). */
    public void testSetBugReportEventHandler() {
        AdminView view = new AdminView();
        ViewEventHandler eventHandler = new StubbedViewEventHandler();
        view.setBugReportEventHandler(eventHandler);  // unfortunately, there's no way to verify this sets the underlying widget properly
        assertSame(eventHandler, view.getBugReportEventHandler());
    }

}
