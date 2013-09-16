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
package com.cedarsolutions.santa.client.external.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.ModulePageView;
import com.cedarsolutions.santa.client.common.widget.LoginSelector;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Page to display when the user must log in.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class LoginRequiredPageView extends ModulePageView implements ILoginRequiredPageView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, LoginRequiredPageView> {
    }

    // User interface fields fron the UI binder
    @UiField protected Label header;
    @UiField protected Label paragraph;
    @UiField protected LoginSelector loginSelector;

    /** Create the view. */
    public LoginRequiredPageView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ExternalConstants constants = GWT.create(ExternalConstants.class);
        this.header.setText(constants.loginRequired_headerText());
        this.paragraph.setText(constants.loginRequired_loginRequiredText());
        this.loginSelector.setIsLoggedIn(false);  // we never use this option on this page
    }

    /** Set the event handler for the login selector. */
    @Override
    public void setLoginSelectorEventHandler(ViewEventHandler loginSelectorEventHandler) {
        this.loginSelector.setLoginSelectorEventHandler(loginSelectorEventHandler);
    }

    /** Get the login selector event handler. */
    @Override
    public ViewEventHandler getLoginSelectorEventHandler() {
        return this.loginSelector.getLoginSelectorEventHandler();
    }

    /** Get the selected OpenId provider key. */
    @Override
    public OpenIdProvider getSelectedProvider() {
        return this.loginSelector.getSelectedProvider();
    }

}
