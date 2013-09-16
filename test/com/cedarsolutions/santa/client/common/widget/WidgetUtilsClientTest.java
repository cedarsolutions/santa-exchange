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

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.core.client.GWT;

/**
 * Unit tests for WidgetUtils.
 *
 * <p>
 * Note that we can't really test the progress indicator methods,
 * because the unit tests don't use the real root HTML page that
 * contains our special div tags.  So, we could call the methods,
 * but they won't do anything useful.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class WidgetUtilsClientTest extends ClientTestCase {

    /** Test getApplicationVersion(). */
    public void testGetApplicationVersion() {
        SantaExchangeConfig santaExchangeConfig = GWT.create(SantaExchangeConfig.class);
        String expected = santaExchangeConfig.system_applicationName() +
                          " v" + santaExchangeConfig.system_versionNumber() +
                          " (" + santaExchangeConfig.system_releaseDate() + ") using HRD";
        assertEquals(expected, WidgetUtils.getInstance().getApplicationVersion());
    }

    /** Test getWndLocationHref(). */
    public void testGetWndLocationHref() {
        // Just make sure it doesn't blow up and we get something back
        assertNotNull(WidgetUtils.getInstance().getWndLocationHref());
    }

    // This test seems to cause strange problems with other tests; not sure exactly why
//    /** Test redirect(). */
//    public void testRedirect() {
//        // Redirect back to the current page, just to make sure the method doesn't blow up
//        WidgetUtils.getInstance().redirect(WidgetUtils.getInstance().getWndLocationHref());
//    }

    /** Test showErrorPopupSpecial(). */
    public void testShowErrorPopupSpecial() {
        // We can't really verify much; just make sure that the method call doesn't blow up
        ErrorDescription error = new ErrorDescription("Hello, world");
        WidgetUtils.getInstance().showErrorPopup(error);
    }

    /** Test getDestinationUrl(). */
    public void testGetDestinationUrl() {
        // Just make sure it looks semi-sensible
        String result = WidgetUtils.getInstance().getDestinationUrl("token");
        assertTrue(result.endsWith("#token"));
    }

}
