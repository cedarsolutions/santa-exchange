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
package com.cedarsolutions.santa.client.internal.view;

import com.cedarsolutions.client.gwt.custom.tab.TabLayoutPanel;
import com.cedarsolutions.santa.client.common.widget.StandardDialog;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Previews a generated email.
 *
 * <p>
 * This implementation assumes that there is both an HTML
 * part and a plaintext part.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PreviewPopup extends StandardDialog {

    /** Reference to the UI binder. */
    private static final PopupUiBinder BINDER = GWT.create(PopupUiBinder.class);

    /** Reference to the id handler. */
    private static PopupIdHandler ID_HANDLER = GWT.create(PopupIdHandler.class);

    /** UI binder interface. */
    protected interface PopupUiBinder extends UiBinder<Widget, PreviewPopup> {
    }

    /** Id handler interface. */
    protected interface PopupIdHandler extends ElementIdHandler<PreviewPopup> {
    }

    // User interface fields fron the UI binder
    @UiField protected HTMLPanel htmlPanel;
    @UiField @WithElementId protected TabLayoutPanel tabPanel;
    @UiField @WithElementId protected Button closeButton;

    /** Create the view. */
    public PreviewPopup() {
        super();
        setWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        InternalConstants constants = GWT.create(InternalConstants.class);
        this.setText(constants.preview_title());
        this.closeButton.setText(constants.preview_close());
    }

    /** Show the pop-up. */
    public void showPopup(EmailMessage email) {
        while (this.tabPanel.getWidgetCount() > 0) {
            this.tabPanel.remove(0);
        }

        InternalConstants constants = GWT.create(InternalConstants.class);
        this.tabPanel.add(createHtmlContents(email), constants.preview_htmlTitle());
        this.tabPanel.add(createTextContents(email), constants.preview_textTitle());

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

    /** Create the htmlContents ScrollPanel. */
    private static ScrollPanel createHtmlContents(EmailMessage email) {
        String htmlPreview = generateHtmlPreview(email);  // safely escaped when necessary
        ScrollPanel htmlContents = new ScrollPanel();
        htmlContents.add(new HTML(htmlPreview));
        return htmlContents;
    }

    /** Create the textContents ScrollPanel. */
    private static ScrollPanel createTextContents(EmailMessage email) {
        String textPreview = generateTextPreview(email);  // safely escaped when necessary
        ScrollPanel textContents = new ScrollPanel();
        textContents.add(new HTML(textPreview));
        return textContents;
    }

    /** Generate the HTML preview, with all included text properly escaped. */
    protected static String generateHtmlPreview(EmailMessage email) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<html>\n<body>\n");
        buffer.append("<pre>\n");

        buffer.append("From: ");
        if (email != null && email.getSender() != null) {
            buffer.append(SafeHtmlUtils.htmlEscape(email.getSender().toString()));
        }
        buffer.append("\n");

        buffer.append("To: ");
        if (email != null && email.getRecipients() != null && !email.getRecipients().isEmpty()) {
            buffer.append(SafeHtmlUtils.htmlEscape(email.getRecipients().get(0).toString()));
        }
        buffer.append("\n");

        buffer.append("Subject: ");
        if (email != null && email.getSubject() != null) {
            buffer.append(SafeHtmlUtils.htmlEscape(email.getSubject()));
        }
        buffer.append("\n");

        buffer.append("</pre>\n");

        if (email != null && email.getHtml() != null) {
            buffer.append(email.getHtml());
        }

        buffer.append("</body>\n</html>\n");

        return buffer.toString();
    }

    /** Generate the text preview, with all included text properly escaped. */
    protected static String generateTextPreview(EmailMessage email) {
        StringBuffer buffer = new StringBuffer();

        buffer.append("<html>\n<body>\n");
        buffer.append("<pre>\n");

        buffer.append("From: ");
        if (email != null && email.getSender() != null) {
            buffer.append(SafeHtmlUtils.htmlEscape(email.getSender().toString()));
        }
        buffer.append("\n");

        buffer.append("To: ");
        if (email != null && email.getRecipients() != null && !email.getRecipients().isEmpty()) {
            buffer.append(SafeHtmlUtils.htmlEscape(email.getRecipients().get(0).toString()));
        }
        buffer.append("\n");

        buffer.append("Subject: ");
        if (email != null && email.getSubject() != null) {
            buffer.append(SafeHtmlUtils.htmlEscape(email.getSubject()));
        }
        buffer.append("\n");

        if (email != null && email.getPlaintext() != null) {
            buffer.append("\n");
            buffer.append(email.getPlaintext());
            buffer.append("\n");
        }

        buffer.append("</pre>\n");
        buffer.append("</body>\n</html>\n");

        return buffer.toString();
    }

}
