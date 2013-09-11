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

import java.util.List;

import com.cedarsolutions.client.gwt.widget.UnorderedList;
import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.util.gwt.GwtExceptionUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Generalized pop-up that can be used to display errors.
 *
 * <p>
 * Note: although you can cut-and-paste exception text out of this
 * pop-up using the right-click context menu, the CTRL-C action
 * does not work.  I'm not sure why, and I haven't found a workaround.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ErrorPopup extends StandardDialog {

    /** Reference to the UI binder. */
    private static final PopupUiBinder BINDER = GWT.create(PopupUiBinder.class);

    /** Reference to the id handler. */
    private static PopupIdHandler ID_HANDLER = GWT.create(PopupIdHandler.class);

    /** UI binder interface. */
    protected interface PopupUiBinder extends UiBinder<Widget, ErrorPopup> {
    }

    /** Id handler interface. */
    protected interface PopupIdHandler extends ElementIdHandler<ErrorPopup> {
    }

    // User interface fields fron the UI binder
    @UiField protected HTMLPanel htmlPanel;
    @UiField @WithElementId protected Button closeButton;
    @UiField @WithElementId protected Label messageText;
    @UiField @WithElementId protected Label messageTextHeader;
    @UiField @WithElementId protected ScrollPanel exceptionTextScrollPanel;
    @UiField @WithElementId protected Label exceptionTextHeader;
    @UiField @WithElementId protected Label exceptionText;
    @UiField @WithElementId protected Label supportingTextHeader;
    @UiField @WithElementId protected UnorderedList supportingTextList;

    /** Create a new error popup. */
    public ErrorPopup() {
        super();
        setWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
    }

    /**
     * Show error information via the dialog.
     * @param error Error to be displayed
     */
    public void showError(ErrorDescription error) {
        if (error != null) {
            String exceptionString = error.getException() == null ? null : GwtExceptionUtils.generateStackTrace(error.getException());
            this.showError(error.getMessage(), exceptionString, error.getSupportingTextItems());
        }
    }

    /**
     * Show error information via the dialog.
     * @param message              Error message
     * @param exception            Exception string, or null
     * @param supportingTextItems  List of supporting text items, or null
     */
    private void showError(String message, String exception, List<String> supportingTextItems) {
        final String defaultPanelWidth = "34em";
        final String exceptionPanelWidth = "48em";
        final String exceptionPanelHeight = "30em";

        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        WidgetConstants constants = GWT.create(WidgetConstants.class);

        // It's useful to have error popups also display in the GWT log, for debugging purposes
        if (config.errorPopup_logMessages()) {
            GWT.log("Error: " + message);
            if (supportingTextItems != null && !supportingTextItems.isEmpty()) {
                for (String supporting : supportingTextItems) {
                    GWT.log("   " + supporting);
                }
            }
        }

        this.setText(constants.errorPopup_dialogTitle());

        this.htmlPanel.setWidth(defaultPanelWidth);

        this.closeButton.setText(constants.errorPopup_closeButtonText());
        this.closeButton.setTitle(constants.errorPopup_closeButtonTooltip());

        this.messageTextHeader.setText(constants.errorPopup_messageTextHeader());
        this.messageText.setText(message);

        this.supportingTextHeader.setVisible(false);
        this.supportingTextList.setVisible(false);
        if (supportingTextItems != null && !supportingTextItems.isEmpty()) {
            this.supportingTextHeader.setVisible(true);
            this.supportingTextList.setVisible(true);
            this.supportingTextList.clear();
            this.supportingTextHeader.setText(constants.errorPopup_supportingTextHeader());
            for (String supporting : supportingTextItems) {
                this.supportingTextList.add(supporting);
            }
        }

        this.exceptionTextScrollPanel.setVisible(false);
        this.exceptionTextHeader.setVisible(false);
        this.exceptionText.setVisible(false);
        if (exception != null && config.errorPopup_displayExceptions()) {
            this.exceptionTextScrollPanel.setVisible(true);
            this.exceptionTextScrollPanel.setHeight(exceptionPanelHeight);
            this.exceptionTextHeader.setVisible(true);
            this.exceptionText.setVisible(true);
            this.htmlPanel.setWidth(exceptionPanelWidth);
            this.exceptionTextHeader.setText(constants.errorPopup_exceptionTextHeader());
            this.exceptionText.setText(exception);
        }

        this.show();
    }

    /** Handle the close button. */
    @UiHandler("closeButton")
    void onCloseClicked(ClickEvent event) {
        hide();
    }

    /** Use the popup's key preview hooks to close the dialog when enter or escape is pressed. */
    @Override
    protected void onPreviewNativeEvent(NativePreviewEvent preview) {
        super.onPreviewNativeEvent(preview);
        WidgetUtils.getInstance().closeOnEnterOrEscape(this, preview);
    }
}
