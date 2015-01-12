/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2015 Kenneth J. Pronovici.
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
import com.cedarsolutions.client.gwt.handler.AbstractViewEventClickHandler;
import com.cedarsolutions.client.gwt.module.view.ModulePageView;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.WithElementId;

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
    @UiField @WithElementId protected Label header;
    @UiField @WithElementId protected Label paragraph1;
    @UiField @WithElementId protected Label paragraph2;
    @UiField @WithElementId protected Button loginButton;

    // Other instance variables
    private ViewEventHandler loginEventHandler;

    /** Create the view. */
    public LoginRequiredPageView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ExternalConstants constants = GWT.create(ExternalConstants.class);

        this.header.setText(constants.loginRequired_headerText());

        this.paragraph1.setText(constants.loginRequired_paragraph1Text());
        this.paragraph2.setText(constants.loginRequired_paragraph2Text());

        this.loginButton.setText(constants.loginRequired_loginButtonText());
        this.loginButton.setTitle(constants.loginRequired_loginButtonTooltip());
        this.loginButton.addClickHandler(new LoginClickHandler(this));

        WidgetUtils.getInstance().clickOnEnter(this.loginButton);
    }

    /** Set the login event handler. */
    @Override
    public void setLoginEventHandler(ViewEventHandler loginEventHandler) {
        this.loginEventHandler = loginEventHandler;

    }

    /** Get the login event handler. */
    @Override
    public ViewEventHandler getLoginEventHandler() {
        return this.loginEventHandler;
    }

    /** Login click handler. */
    protected static class LoginClickHandler extends AbstractViewEventClickHandler<LoginRequiredPageView> {
        public LoginClickHandler(LoginRequiredPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getLoginEventHandler();
        }
    }

}
