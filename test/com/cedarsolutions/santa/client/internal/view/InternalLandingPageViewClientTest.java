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
package com.cedarsolutions.santa.client.internal.view;

import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for InternalLandingPageView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalLandingPageViewClientTest extends ClientTestCase {

    /** Test the constructor(). */
    public void testConstructor() {
        ExchangeListTabView exchangeListTab = new ExchangeListTabView();
        EditExchangeTabView editExchangeTab = new EditExchangeTabView();
        EditParticipantTabView editParticipantTab = new EditParticipantTabView();

        InternalLandingPageView view = new InternalLandingPageView(exchangeListTab, editExchangeTab, editParticipantTab);
        assertNotNull(view);
        assertNotNull(view.tabPanel);
        assertTrue(view.isFullScreen());
        assertSame(view.tabPanel, view.getTabPanel());
        assertSame(exchangeListTab, view.exchangeListTab);
        assertSame(editExchangeTab, view.editExchangeTab);
        assertSame(editParticipantTab, view.editParticipantTab);

        assertEquals(0, view.tabPanel.getWidgetCount());
    }

    /** Test the methods that select the tab. */
    public void testSelectTabMethods() {
        ExchangeListTabView exchangeListTab = new ExchangeListTabView();
        EditExchangeTabView editExchangeTab = new EditExchangeTabView();
        EditParticipantTabView editParticipantTab = new EditParticipantTabView();

        InternalLandingPageView view = new InternalLandingPageView(exchangeListTab, editExchangeTab, editParticipantTab);
        assertEquals(0, view.tabPanel.getWidgetCount());

        view.selectExchangeListTab();
        assertEquals(1, view.tabPanel.getWidgetCount());
        assertEquals(view.getTabPanel(), exchangeListTab.getParentPanel());

        view.selectEditExchangeTab();
        assertEquals(1, view.tabPanel.getWidgetCount());
        assertEquals(view.getTabPanel(), editExchangeTab.getParentPanel());

        view.selectEditParticipantTab();
        assertEquals(1, view.tabPanel.getWidgetCount());
        assertEquals(view.getTabPanel(), editParticipantTab.getParentPanel());
    }

}
