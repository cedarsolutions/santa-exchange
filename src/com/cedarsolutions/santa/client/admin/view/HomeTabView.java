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

import com.cedarsolutions.client.gwt.module.view.ModuleTabView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Admin home page tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Singleton
public class HomeTabView extends ModuleTabView implements IHomeTabView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, HomeTabView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<HomeTabView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Label paragraph1;
    @UiField @WithElementId protected Label paragraph2;

    // Other instance variables
    private String historyToken;

    /** Create the view. */
    public HomeTabView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        AdminConstants constants = GWT.create(AdminConstants.class);
        this.paragraph1.setText(constants.homeTab_paragraph1());
        this.paragraph2.setText(constants.homeTab_paragraph2());
    }

    /** Set the history token. */
    @Override
    public void setHistoryToken(String historyToken) {
        this.historyToken = historyToken;
    }

    /** Get the history token for this tab. */
    @Override
    public String getHistoryToken() {
        return this.historyToken;
    }

}
