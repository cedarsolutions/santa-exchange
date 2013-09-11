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
import com.cedarsolutions.client.gwt.handler.AbstractClickHandler;
import com.cedarsolutions.client.gwt.module.view.ModulePageView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Account locked page view.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AccountLockedPageView extends ModulePageView implements IAccountLockedPageView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, AccountLockedPageView> {
    }

    // User interface fields fron the UI binder
    @UiField protected Label header;
    @UiField protected Label paragraph;
    @UiField @WithElementId protected Button continueButton;

    // Other instance variables
    protected ViewEventHandler continueEventHandler;

    /** Create the view. */
    public AccountLockedPageView() {
        this.initWidget(BINDER.createAndBindUi(this));
        ExternalConstants constants = GWT.create(ExternalConstants.class);
        this.header.setText(constants.accountLocked_headerText());
        this.paragraph.setText(constants.accountLocked_accountLockedText());

        this.continueButton.setText(constants.accountLocked_continueButton());
        this.continueButton.setTitle(constants.accountLocked_continueTooltip());
        this.continueButton.addClickHandler(new ContinueClickHandler(this));
    }

    /** Set the event handler for the continue action. */
    @Override
    public void setContinueEventHandler(ViewEventHandler continueEventHandler) {
        this.continueEventHandler = continueEventHandler;
    }

    /** Get the event handler for the continue action. */
    @Override
    public ViewEventHandler getContinueEventHandler() {
        return this.continueEventHandler;
    }

    /** Continue click handler. */
    protected static class ContinueClickHandler extends AbstractClickHandler<AccountLockedPageView> {
        public ContinueClickHandler(AccountLockedPageView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getContinueEventHandler();
        }
    }

}
