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

import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for AdminLandingPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AdminLandingPageViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        HomeTabView homeTab = new HomeTabView();
        AuditTabView auditTab = new AuditTabView();
        UserTabView userTab = new UserTabView();

        AdminLandingPageView view = new AdminLandingPageView(homeTab, auditTab, userTab);
        assertNotNull(view);
        assertSame(homeTab, view.homeTab);
        assertSame(auditTab, view.auditTab);
        assertSame(userTab, view.userTab);
        assertNotNull(view.tabPanel);
        assertTrue(view.isFullScreen());
        assertSame(view.tabPanel, view.getTabPanel());

        assertEquals(3, view.tabPanel.getWidgetCount());
        assertEquals(view.getTabPanel(), homeTab.getParentPanel());
        assertEquals(0, homeTab.getTabIndex());
        assertEquals(view.getTabPanel(), auditTab.getParentPanel());
        assertEquals(1, auditTab.getTabIndex());
        assertEquals(view.getTabPanel(), userTab.getParentPanel());
        assertEquals(2, userTab.getTabIndex());
    }

    /** Test selectHomeTab(). */
    public void testSelectHomeTab() {
        HomeTabView homeTab = new HomeTabView();
        AuditTabView auditTab = new AuditTabView();
        UserTabView userTab = new UserTabView();
        AdminLandingPageView view = new AdminLandingPageView(homeTab, auditTab, userTab);
        view.selectHomeTab(); // just make sure it doesn't blow up; not much can be verified
    }

    /** Test selectAuditTab(). */
    public void testSelectAuditTab() {
        HomeTabView homeTab = new HomeTabView();
        AuditTabView auditTab = new AuditTabView();
        UserTabView userTab = new UserTabView();
        AdminLandingPageView view = new AdminLandingPageView(homeTab, auditTab, userTab);
        view.selectAuditTab(); // just make sure it doesn't blow up; not much can be verified
    }

    /** Test selectUserTab(). */
    public void testSelectUserTab() {
        HomeTabView homeTab = new HomeTabView();
        AuditTabView auditTab = new AuditTabView();
        UserTabView userTab = new UserTabView();
        AdminLandingPageView view = new AdminLandingPageView(homeTab, auditTab, userTab);
        view.selectUserTab(); // just make sure it doesn't blow up; not much can be verified
    }

}
