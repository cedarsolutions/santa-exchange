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
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for HomeTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class HomeTabViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        AdminConstants constants = GWT.create(AdminConstants.class);
        HomeTabView view = new HomeTabView();
        assertNotNull(view);
        assertEquals(constants.homeTab_paragraph1(), view.paragraph1.getText());
        assertEquals(constants.homeTab_paragraph2(), view.paragraph2.getText());
    }

    /** Test get/setHistoryToken(). */
    public void testGetSetHistoryToken() {
        HomeTabView view = new HomeTabView();
        assertNull(view.getHistoryToken());
        view.setHistoryToken("history");
        assertEquals("history", view.getHistoryToken());
    }

}
