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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventClickHandler;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * View for the login selector component.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class LoginSelector extends Composite {

    /** Reference to the UI binder. */
    private static WidgetUiBinder BINDER = GWT.create(WidgetUiBinder.class);

    /** Reference to the id handler. */
    private static WidgetIdHandler ID_HANDLER = GWT.create(WidgetIdHandler.class);

    /** UI binder interface. */
    protected interface WidgetUiBinder extends UiBinder<Widget, LoginSelector> {
    }

    /** Id handler interface. */
    protected interface WidgetIdHandler extends ElementIdHandler<LoginSelector> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected HTMLPanel openIdSection;
    @UiField @WithElementId protected Label openIdInstructions;
    @UiField @WithElementId protected DisclosurePanel openIdDisclosure;
    @UiField @WithElementId protected Label openIdExplanation1;
    @UiField @WithElementId protected Label openIdExplanation2;
    @UiField @WithElementId protected Label openIdExplanation3;
    @UiField @WithElementId protected OpenIdProviderList openIdList;
    @UiField @WithElementId protected Button openIdButton;
    @UiField @WithElementId protected HTMLPanel continueSection;
    @UiField @WithElementId protected DisclosurePanel continueDisclosure;
    @UiField @WithElementId protected Label continueExplanation;
    @UiField @WithElementId protected Button continueButton;

    // Other instance variables
    private boolean isLoggedIn;
    private ViewEventHandler continueEventHandler;
    private ViewEventHandler loginSelectorEventHandler;

    /** Create the view. */
    public LoginSelector() {
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        WidgetConstants constants = GWT.create(WidgetConstants.class);
        this.openIdInstructions.setText(constants.login_openIdInstructions());
        this.openIdDisclosure.getHeaderTextAccessor().setText(constants.login_openIdDisclosureText());
        this.openIdExplanation1.setText(constants.login_openIdExplanation1Text());
        this.openIdExplanation2.setText(constants.login_openIdExplanation2Text());
        this.openIdExplanation3.setText(constants.login_openIdExplanation3Text());
        this.openIdButton.setText(constants.login_openIdButtonText());
        this.continueDisclosure.getHeaderTextAccessor().setText(constants.login_continueDisclosureText());
        this.continueExplanation.setText(constants.login_continueExplanationText());
        this.continueButton.setText(constants.login_continueButtonText());
        this.setIsLoggedIn(false);
    }

    /** Get a reference to the continue button, useful for testing purposes. */
    public Button getContinueButton() {
        return this.continueButton;
    }

    /** Get a reference to the continue button, useful for testing purposes. */
    public Button getOpenIdButton() {
        return this.openIdButton;
    }

    /** Set whether a user is currently logged in. */
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        if (isLoggedIn) {
            this.openIdSection.setVisible(false);
            this.continueSection.setVisible(true);
        } else {
            this.openIdSection.setVisible(true);
            this.continueSection.setVisible(false);
        }
    }

    /** Indicate whether a user is currently logged in. */
    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    /** Set the event handler for the continue action. */
    public void setContinueEventHandler(ViewEventHandler continueEventHandler) {
        this.continueEventHandler = continueEventHandler;
        this.continueButton.addClickHandler(new ContinueClickHandler(this));
    }

    /** Get the currently-configured continue event handler, if any. */
    public ViewEventHandler getContinueEventHandler() {
        return this.continueEventHandler;
    }

    /** Set the event handler for the login selector. */
    public void setLoginSelectorEventHandler(ViewEventHandler loginSelectorEventHandler) {
        this.loginSelectorEventHandler = loginSelectorEventHandler;
        this.openIdButton.addClickHandler(new OpenIdClickHandler(this));
    }

    /** Get the currently-configured login selector event handler, if any. */
    public ViewEventHandler getLoginSelectorEventHandler() {
        return this.loginSelectorEventHandler;
    }

    /** Get the selected OpenId provider key. */
    public OpenIdProvider getSelectedProvider() {
        return this.openIdList.getSelectedObjectValue();
    }

    /** Continue click handler. */
    protected static class ContinueClickHandler extends AbstractViewEventClickHandler<LoginSelector> {
        public ContinueClickHandler(LoginSelector parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getContinueEventHandler();
        }
    }

    /** OpenId click handler. */
    protected static class OpenIdClickHandler extends AbstractViewEventClickHandler<LoginSelector> {
        public OpenIdClickHandler(LoginSelector parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getLoginSelectorEventHandler();
        }
    }
}
