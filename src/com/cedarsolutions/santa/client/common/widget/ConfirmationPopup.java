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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Popup to confirm some action.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ConfirmationPopup extends StandardDialog {

    /** Reference to the UI binder. */
    private static final PopupUiBinder BINDER = GWT.create(PopupUiBinder.class);

    /** Reference to the id handler. */
    private static PopupIdHandler ID_HANDLER = GWT.create(PopupIdHandler.class);

    /** UI binder interface. */
    protected interface PopupUiBinder extends UiBinder<Widget, ConfirmationPopup> {
    }

    /** Id handler interface. */
    protected interface PopupIdHandler extends ElementIdHandler<ConfirmationPopup> {
    }

    // User interface fields fron the UI binder
    @UiField protected HTMLPanel htmlPanel;
    @UiField @WithElementId protected Label messageLabel;
    @UiField @WithElementId protected Button okButton;
    @UiField @WithElementId protected Button cancelButton;

    /** Event handler for OK button. */
    private ViewEventHandler okButtonHandler;

    /** Title to be shown. */
    private String title;

    /** Message to be shown. */
    private String message;

    /** Create the popup. */
    public ConfirmationPopup() {
        this("", "");
    }

    /** Create the popup. */
    public ConfirmationPopup(String title) {
        this(title, "");
    }

    /** Create the popup. */
    public ConfirmationPopup(String title, String message) {
        super();
        setWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        this.title = title;
        this.message = message;

        WidgetConstants constants = GWT.create(WidgetConstants.class);
        this.okButton.setText(constants.confirmationPopup_ok());
        this.cancelButton.setText(constants.confirmationPopup_cancel());
    }

    /** Show the pop-up. */
    public void showPopup() {
        this.show();
        this.setText(title);
        this.messageLabel.setText(message);
    }

    /** Handle the OK button. */
    @UiHandler("okButton")
    void onOkClicked(ClickEvent event) {
        hide();
        if (this.getOkButtonHandler() != null) {
            this.getOkButtonHandler().handleEvent(new UnifiedEvent(event));
        }
    }

    /** Handle the cancel button. */
    @UiHandler("cancelButton")
    void onCancelClicked(ClickEvent event) {
        hide();
    }

    /** Get the OK button. */
    public Button getOkButton() {
        return this.okButton;
    }

    /** Get the cancel button. */
    public Button getCancelButton() {
        return this.cancelButton;
    }

    /** Set the OK button event handler. */
    public void setOkButtonHandler(ViewEventHandler okButtonHandler) {
        this.okButtonHandler = okButtonHandler;
    }

    /** Get the OK button event handler. */
    public ViewEventHandler getOkButtonHandler() {
        return this.okButtonHandler;
    }

    /** Get the title. */
    @Override
    public String getTitle() {
        return this.title;
    }

    /** Set the title. */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /** Get the message. */
    public String getMessage() {
        return this.message;
    }

    /** Set the message. */
    public void setMessage(String message) {
        this.message = message;
    }

}
