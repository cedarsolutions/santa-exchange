/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2014 Kenneth J. Pronovici.
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
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for OpenIdProviderList.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class OpenIdProviderListClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        OpenIdProviderList list = new OpenIdProviderList();
        assertNotNull(list);
        assertEquals(1, list.getVisibleItemCount());
        assertEquals(OpenIdProviderList.DEFAULT_SELECTION, list.getSelectedObjectValue());
        assertEquals(5, list.getItemCount());

        list = new OpenIdProviderList(true);
        assertNotNull(list);
        assertEquals(1, list.getVisibleItemCount());
        assertEquals(null, list.getSelectedObjectValue());
        assertEquals(6, list.getItemCount());
    }

    /** Check that every value can be selected. */
    public void testSetSelectedValue() {
        OpenIdProviderList list = new OpenIdProviderList(true);

        list.setSelectedObjectValue(null);
        assertEquals(null, list.getSelectedObjectValue());

        list.setSelectedObjectValue(OpenIdProvider.UNKNOWN);
        assertEquals(null, list.getSelectedObjectValue());

        list.setSelectedObjectValue(OpenIdProvider.GOOGLE);
        assertEquals(OpenIdProvider.GOOGLE, list.getSelectedObjectValue());

        list.setSelectedObjectValue(OpenIdProvider.YAHOO);
        assertEquals(OpenIdProvider.YAHOO, list.getSelectedObjectValue());

        list.setSelectedObjectValue(OpenIdProvider.MYSPACE);
        assertEquals(OpenIdProvider.MYSPACE, list.getSelectedObjectValue());

        list.setSelectedObjectValue(OpenIdProvider.AOL);
        assertEquals(OpenIdProvider.AOL, list.getSelectedObjectValue());

        list.setSelectedObjectValue(OpenIdProvider.MYOPENID);
        assertEquals(OpenIdProvider.MYOPENID, list.getSelectedObjectValue());
    }

    /** Check that every provider name is localized properly. */
    public void testGetProviderName() {
        WidgetConstants constants = GWT.create(WidgetConstants.class);
        assertEquals("null", OpenIdProviderList.getProviderName(null));
        assertEquals(OpenIdProvider.UNKNOWN.toString(), OpenIdProviderList.getProviderName(OpenIdProvider.UNKNOWN));
        assertEquals(constants.openId_Google(), OpenIdProviderList.getProviderName(OpenIdProvider.GOOGLE));
        assertEquals(constants.openId_Yahoo(), OpenIdProviderList.getProviderName(OpenIdProvider.YAHOO));
        assertEquals(constants.openId_MySpace(), OpenIdProviderList.getProviderName(OpenIdProvider.MYSPACE));
        assertEquals(constants.openId_AOL(), OpenIdProviderList.getProviderName(OpenIdProvider.AOL));
        assertEquals(constants.openId_myOpenId(), OpenIdProviderList.getProviderName(OpenIdProvider.MYOPENID));
    }
}
