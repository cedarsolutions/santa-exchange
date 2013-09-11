/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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
import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.common.widget.LoginSelector;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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
    @UiField protected LoginSelector loginSelector;

    /** Create the view. */
    public ExternalLandingPageView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        ExternalConstants constants = GWT.create(ExternalConstants.class);
        this.header.setText(constants.landingPage_headerText());
        this.paragraph1.setText(constants.landingPage_paragraph1Text());
        this.paragraph2.setText(constants.landingPage_paragraph2Text());
        this.paragraph3.setText(constants.landingPage_paragraph3Text());

        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        String result = GwtStringUtils.format(constants.landingPage_paragraph4TextFormat(),
                                              config.system_apacheLicenseUrl(),
                                              config.system_sourceCodeUrl());
        this.paragraph4.add(new HTML(result));  // safe even though it's not escaped

        this.loginSelector.setIsLoggedIn(false);
    }

    /** Tell the view whether a user is currently logged in. */
    @Override
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.loginSelector.setIsLoggedIn(isLoggedIn);
    }

    /** Find out whether the view thinks anyone is logged in. */
    @Override
    public boolean getIsLoggedIn() {
        return this.loginSelector.getIsLoggedIn();
    }

    /** Set the event handler for the continue action. */
    @Override
    public void setContinueEventHandler(ViewEventHandler continueEventHandler) {
        this.loginSelector.setContinueEventHandler(continueEventHandler);
    }

    /** Get the continue event handler. */
    @Override
    public ViewEventHandler getContinueEventHandler() {
        return this.loginSelector.getContinueEventHandler();
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
