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
package com.cedarsolutions.santa.client.internal.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.validation.IValidationErrorWidget;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.santa.client.common.widget.StandardDialog;
import com.cedarsolutions.santa.client.common.widget.ValidationErrorWidget;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.cedarsolutions.util.gwt.GwtLocalizationUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * View for bug report dialog.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportDialogView extends StandardDialog implements IBugReportDialogView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, BugReportDialogView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<BugReportDialogView> {
    }

    // User interface fields fron the UI binder
    @UiField protected HTMLPanel htmlPanel;
    @UiField protected ValidationErrorWidget validationErrorWidget;
    @UiField @WithElementId protected Label instructionsLabel;
    @UiField @WithElementId protected Label reportDateLabel;
    @UiField @WithElementId protected Label reportDateText;
    @UiField @WithElementId protected Label applicationVersionLabel;
    @UiField @WithElementId protected Label applicationVersionText;
    @UiField @WithElementId protected Label emailAddressLabel;
    @UiField @WithElementId protected TextBox emailAddress;
    @UiField @WithElementId protected Label problemSummaryLabel;
    @UiField @WithElementId protected TextBox problemSummary;
    @UiField @WithElementId protected Label detailedDescriptionLabel;
    @UiField @WithElementId protected TextArea detailedDescription;
    @UiField @WithElementId protected Label optionalLabel;
    @UiField @WithElementId protected Button submitButton;
    @UiField @WithElementId protected Button cancelButton;

    // Other instance variables
    protected Date reportDate;
    protected FederatedUser submittingUser;
    protected String applicationVersion;
    protected ViewEventHandler submitEventHandler;

    /** Create the view. */
    public BugReportDialogView() {
        super();
        setWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);

        InternalConstants constants = GWT.create(InternalConstants.class);
        this.setText(constants.bugReport_title());
        this.instructionsLabel.setText(constants.bugReport_instructions());
        this.reportDateLabel.setText(constants.bugReport_reportDate());
        this.applicationVersionLabel.setText(constants.bugReport_applicationVersion());
        this.emailAddressLabel.setText(constants.bugReport_emailAddress());
        this.problemSummaryLabel.setText(constants.bugReport_problemSummary());
        this.detailedDescriptionLabel.setText(constants.bugReport_detailedDescription());
        this.optionalLabel.setText(constants.bugReport_optional());
        this.submitButton.setText(constants.bugReport_submit());
        this.cancelButton.setText(constants.bugReport_cancel());
    }

    /** Get the validation error widget. */
    @Override
    public IValidationErrorWidget getValidationErrorWidget() {
        return this.validationErrorWidget;
    }

    /** Translate a localizable message using this view's resources. */
    @Override
    public String translate(LocalizableMessage message) {
        InternalMessageConstants constants = GWT.create(InternalMessageConstants.class);
        return GwtLocalizationUtils.translate(message, constants, "bugReport");
    }

    /** Get a mapping from field name to input element. */
    @Override
    public Map<String, UIObject> getValidatedFieldMap() {
        Map<String, UIObject> fieldMap = new HashMap<String, UIObject>();
        fieldMap.put("emailAddress", this.emailAddress);
        fieldMap.put("problemSummary", this.problemSummary);
        fieldMap.put("detailedDescription", this.detailedDescription);
        return fieldMap;
    }

    /** Show the dialog. */
    @Override
    public void showDialog(BugReport bugReport) {
        this.validationErrorWidget.hide();

        this.reportDate = bugReport.getReportDate();
        this.submittingUser = bugReport.getSubmittingUser();
        this.applicationVersion = bugReport.getApplicationVersion();

        this.reportDateText.setText(GwtDateUtils.formatDate(bugReport.getReportDate()));
        this.applicationVersionText.setText(this.applicationVersion);
        this.emailAddress.setText(bugReport.getEmailAddress());
        this.problemSummary.setText(bugReport.getProblemSummary());
        this.detailedDescription.setText(bugReport.getDetailedDescription());

        WidgetUtils.getInstance().setFocusAfterDisplay(this.emailAddress);
        this.show();
    }

    /** Show a validation error. */
    @Override
    public void showValidationError(InvalidDataException error) {
        ViewUtils.getInstance().showValidationError(this, error);
    }

    /** Hide the dialog. */
    @Override
    public void hideDialog() {
        this.hide();
    }

    /** Handle the cancel button. */
    @UiHandler("cancelButton")
    void onCancelClicked(ClickEvent event) {
        this.hideDialog();
    }

    /** Handle the submit button. */
    @UiHandler("submitButton")
    void onSubmitClicked(ClickEvent event) {
        this.submitEventHandler.handleEvent(new UnifiedEvent(UnifiedEventType.BUTTON_EVENT));
    }

    /** Set the event handler for the submit action. */
    @Override
    public void setSubmitEventHandler(ViewEventHandler submitEventHandler) {
        this.submitEventHandler = submitEventHandler;
    }

    /** Get the event handler for the submit action. */
    @Override
    public ViewEventHandler getSubmitEventHandler() {
        return this.submitEventHandler;
    }

    /** Get the bug report from the dialog. */
    @Override
    public BugReport getBugReport() {
        BugReport bugReport = new BugReport();

        bugReport.setReportDate(this.reportDate);
        bugReport.setApplicationVersion(this.applicationVersion);
        bugReport.setSubmittingUser(this.submittingUser);
        bugReport.setEmailAddress(this.emailAddress.getText());
        bugReport.setProblemSummary(this.problemSummary.getText());
        bugReport.setDetailedDescription(this.detailedDescription.getText());

        return bugReport;
    }

}
