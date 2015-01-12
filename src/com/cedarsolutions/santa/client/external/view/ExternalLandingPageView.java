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
import com.cedarsolutions.client.gwt.handler.AbstractViewEventClickHandler;
import com.cedarsolutions.client.gwt.module.view.ModulePageView;
import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * View for the external (public-facing) landing page.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExternalLandingPageView extends ModulePageView implements IExternalLandingPageView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, ExternalLandingPageView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<ExternalLandingPageView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Label header;
    @UiField @WithElementId protected Label paragraph1;
    @UiField @WithElementId protected Label paragraph2;
    @UiField @WithElementId protected Label paragraph3;
    @UiField @WithElementId protected HTMLPanel paragraph4;
    @UiField @WithElementId protected Label paragraph5;
    @UiField @WithElementId protected Button loginButton;

    // Other instance variables
    private ViewEventHandler loginEventHandler;

    /** Create the view. */
    public ExternalLandingPageView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        ExternalConstants constants = GWT.create(ExternalConstants.class);
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        String paragraph4Html = GwtStringUtils.format(constants.landingPage_paragraph4TextFormat(),
                                                      config.system_apacheLicenseUrl(),
                                                      config.system_sourceCodeUrl());

        this.header.setText(constants.landingPage_headerText());
        this.paragraph1.setText(constants.landingPage_paragraph1Text());
        this.paragraph2.setText(constants.landingPage_paragraph2Text());
        this.paragraph3.setText(constants.landingPage_paragraph3Text());
        this.paragraph4.add(new HTML(paragraph4Html));  // safe even though it's not escaped
        this.paragraph5.setText(constants.landingPage_paragraph5Text());

        this.loginButton.setText(constants.landingPage_loginButtonText());
        this.loginButton.setTitle(constants.landingPage_loginButtonTooltip());
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
    protected static class LoginClickHandler extends AbstractViewEventClickHandler<ExternalLandingPageView> {
        public LoginClickHandler(ExternalLandingPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getLoginEventHandler();
        }
    }

}
