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

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.root.RootEventBus;
import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

/**
 * Handle events that require a redirect to an external URL.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@EventHandler
public class RedirectEventHandler extends BaseEventHandler<RootEventBus> {

    /** Show the application dashboard. */
    public void onShowApplicationDashboard() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        WidgetUtils.getInstance().redirect(config.system_dashboardUrl());
    }

    /** Show the source code page. */
    public void onShowSourceCode() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        WidgetUtils.getInstance().redirect(config.system_sourceCodeUrl());
    }

}
