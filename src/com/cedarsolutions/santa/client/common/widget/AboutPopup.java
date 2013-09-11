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

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * "About" pop-up for the application.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AboutPopup extends StandardDialog {

    /** Reference to the UI binder. */
    private static final PopupUiBinder BINDER = GWT.create(PopupUiBinder.class);

    /** Reference to the id handler. */
    private static PopupIdHandler ID_HANDLER = GWT.create(PopupIdHandler.class);

    /** UI binder interface. */
    protected interface PopupUiBinder extends UiBinder<Widget, AboutPopup> {
    }

    /** Id handler interface. */
    protected interface PopupIdHandler extends ElementIdHandler<AboutPopup> {
    }

    // User interface fields fron the UI binder
    @UiField protected HTMLPanel htmlPanel;
    @UiField @WithElementId protected Label copyrightStatement;
    @UiField @WithElementId protected Label applicationVersion;
    @UiField @WithElementId protected Label paragraph1;
    @UiField @WithElementId protected Label paragraph2;
    @UiField @WithElementId protected Label paragraph3;
    @UiField @WithElementId protected Label paragraph4;
    @UiField @WithElementId protected HTMLPanel paragraph5;
    @UiField @WithElementId protected Button closeButton;

    /** Create the view. */
    public AboutPopup() {
        super();
        setWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);
        this.copyrightStatement.setText(config.system_copyrightStatement());
        this.applicationVersion.setText(WidgetUtils.getInstance().getApplicationVersion());

        WidgetConstants constants = GWT.create(WidgetConstants.class);
        this.setText(constants.about_title());
        this.paragraph1.setText(constants.about_paragraph1());
        this.paragraph2.setText(constants.about_paragraph2());
        this.paragraph3.setText(constants.about_paragraph3());
        this.paragraph4.setText(constants.about_paragraph4());

        String result = GwtStringUtils.format(constants.about_paragraph5TextFormat(),
                                              config.system_apacheLicenseUrl(),
                                              config.system_sourceCodeUrl());
        this.paragraph5.add(new HTML(result)); // safe even though it's not escaped

        this.closeButton.setText(constants.about_close());
    }

    /** Show the pop-up. */
    public void showPopup() {
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
