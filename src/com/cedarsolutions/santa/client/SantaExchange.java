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
package com.cedarsolutions.santa.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.mvp4g.client.Mvp4gModule;

/**
 * Entry point for the SantaExchange application.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class SantaExchange implements EntryPoint {

    @Override
    public final void onModuleLoad() {
        // Note that child modules are also rendered in RootPresenter.onReplaceRootBody()
        Mvp4gModule module = (Mvp4gModule) GWT.create(Mvp4gModule.class);
        module.createAndStartModule();
        RootLayoutPanel.get().add((IsWidget) module.getStartView());
    }

}
