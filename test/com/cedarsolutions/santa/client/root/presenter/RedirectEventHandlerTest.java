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
package com.cedarsolutions.santa.client.root.presenter;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Unit tests for RedirectEventHandler.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RedirectEventHandlerTest extends StubbedClientTestCase {

    /** Test onShowApplicationDashboard(). */
    @Test public void testOnShowApplicationDashboard() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        RedirectEventHandler handler = new RedirectEventHandler();
        handler.onShowApplicationDashboard();
        verify(WidgetUtils.getInstance()).redirect(config.system_dashboardUrl());
    }

    /** Test onShowSourceCode(). */
    @Test public void testOnShowSourceCode() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        RedirectEventHandler handler = new RedirectEventHandler();
        handler.onShowSourceCode();
        verify(WidgetUtils.getInstance()).redirect(config.system_sourceCodeUrl());
    }
}
