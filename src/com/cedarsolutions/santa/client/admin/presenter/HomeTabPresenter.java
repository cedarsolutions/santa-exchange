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
package com.cedarsolutions.santa.client.admin.presenter;

import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.santa.client.admin.AdminEventBus;
import com.cedarsolutions.santa.client.admin.view.HomeTabView;
import com.cedarsolutions.santa.client.admin.view.IHomeTabView;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for home tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = HomeTabView.class)
public class HomeTabPresenter extends ModulePagePresenter<IHomeTabView, AdminEventBus> {

    /** Handle the start event so that the view gets bound. */
    public void onStart() {
    }

    /** Bind the user interface. */
    @Override
    public void bind() {
        this.view.setHistoryToken(this.getTokenGenerator().selectHomeTab());
    }

}
