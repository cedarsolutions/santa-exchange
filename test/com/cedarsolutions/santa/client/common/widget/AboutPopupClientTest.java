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

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

/**
 * Client-side unit tests for AboutPopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AboutPopupClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        WidgetConstants constants = GWT.create(WidgetConstants.class);

        AboutPopup popup = new AboutPopup();
        assertNotNull(popup);
        assertEquals(WidgetUtils.getInstance().getApplicationVersion(), popup.applicationVersion.getText());
        assertEquals(config.system_copyrightStatement(), popup.copyrightStatement.getText());
        assertEquals(constants.about_paragraph1(), popup.paragraph1.getText());
        assertEquals(constants.about_paragraph2(), popup.paragraph2.getText());
        assertEquals(constants.about_paragraph3(), popup.paragraph3.getText());
        assertEquals(constants.about_paragraph4(), popup.paragraph4.getText());
        assertNotNull(popup.paragraph5);
        assertEquals(constants.about_close(), popup.closeButton.getText());

        HTML html = (HTML) popup.paragraph5.getWidget(0);
        String format = constants.about_paragraph5TextFormat();
        String result = GwtStringUtils.format(format, config.system_apacheLicenseUrl(), config.system_sourceCodeUrl());
        assertEquals(result, html.getHTML());
    }

    /** Test showPopup(). */
    public void testShowPopup() {
        AboutPopup popup = new AboutPopup();
        popup.showPopup();
        assertTrue(popup.isShowing());
        popup.onCloseClicked(null);  // doesn't matter what's passed in
        assertFalse(popup.isShowing());
    }

}
