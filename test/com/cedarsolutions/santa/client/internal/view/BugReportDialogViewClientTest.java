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

import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.shared.domain.ValidationErrors;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for BugReportDialogView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportDialogViewClientTest extends ClientTestCase {

    /** Test constructor. */
    public void testConstructor() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        BugReportDialogView view = new BugReportDialogView();
        assertNotNull(view);

        assertNull(view.reportDate);
        assertNull(view.applicationVersion);
        assertNull(view.submittingUser);
        assertNull(view.submitEventHandler);

        assertEquals("", view.reportDateText.getText());
        assertEquals("", view.applicationVersionText.getText());
        assertEquals("", view.emailAddress.getText());
        assertEquals("", view.problemSummary.getText());
        assertEquals("", view.detailedDescription.getText());

        assertEquals(constants.bugReport_instructions(), view.instructionsLabel.getText());
        assertEquals(constants.bugReport_reportDate(), view.reportDateLabel.getText());
        assertEquals(constants.bugReport_applicationVersion(), view.applicationVersionLabel.getText());
        assertEquals(constants.bugReport_emailAddress(), view.emailAddressLabel.getText());
        assertEquals(constants.bugReport_problemSummary(), view.problemSummaryLabel.getText());
        assertEquals(constants.bugReport_detailedDescription(), view.detailedDescriptionLabel.getText());
        assertEquals(constants.bugReport_optional(), view.optionalLabel.getText());
        assertEquals(constants.bugReport_submit(), view.submitButton.getText());
        assertEquals(constants.bugReport_cancel(), view.cancelButton.getText());
    }

    /** Test showDialog()/hideDialog(). */
    public void testShowHideDialog() {
        FederatedUser submittingUser = new FederatedUser();
        submittingUser.setUserName("name");

        BugReport bugReport = new BugReport();
        bugReport.setReportDate(GwtDateUtils.createDate(2011, 12, 14, 23, 16));
        bugReport.setApplicationVersion("version");
        bugReport.setSubmittingUser(submittingUser);
        bugReport.setEmailAddress("email");
        bugReport.setProblemSummary("summary");
        bugReport.setDetailedDescription("description");

        BugReportDialogView view = new BugReportDialogView();
        assertFalse(view.isShowing());

        view.showDialog(bugReport);
        assertTrue(view.isShowing());

        assertEquals(bugReport.getReportDate(), view.reportDate);
        assertEquals(bugReport.getApplicationVersion(), view.applicationVersion);
        assertSame(bugReport.getSubmittingUser(), view.submittingUser);
        assertEquals(GwtDateUtils.formatDate(bugReport.getReportDate()), view.reportDateText.getText());
        assertEquals(bugReport.getApplicationVersion(), view.applicationVersionText.getText());
        assertEquals(bugReport.getEmailAddress(), view.emailAddress.getText());
        assertEquals(bugReport.getProblemSummary(), view.problemSummary.getText());
        assertEquals(bugReport.getDetailedDescription(), view.detailedDescription.getText());

        view.hideDialog();
        assertFalse(view.isShowing());

        // make sure nothing gets cleared just because the dialog was hidden
        assertEquals(bugReport.getReportDate(), view.reportDate);
        assertEquals(bugReport.getApplicationVersion(), view.applicationVersion);
        assertSame(bugReport.getSubmittingUser(), view.submittingUser);
        assertEquals(GwtDateUtils.formatDate(bugReport.getReportDate()), view.reportDateText.getText());
        assertEquals(bugReport.getApplicationVersion(), view.applicationVersionText.getText());
        assertEquals(bugReport.getEmailAddress(), view.emailAddress.getText());
        assertEquals(bugReport.getProblemSummary(), view.problemSummary.getText());
        assertEquals(bugReport.getDetailedDescription(), view.detailedDescription.getText());
    }

    /** Test showValidationError(). */
    public void testShowValidationError() {
        InternalMessageConstants constants = GWT.create(InternalMessageConstants.class);

        BugReportDialogView view = new BugReportDialogView();

        view.showValidationError(null);
        assertFalse(view.validationErrorWidget.isVisible());
        assertEquals("", view.validationErrorWidget.getErrorSummary());

        LocalizableMessage detail = new LocalizableMessage(REQUIRED, "problemSummary", "it's required");
        LocalizableMessage summary = new LocalizableMessage(INVALID, "it's invalid");
        ValidationErrors errors = new ValidationErrors(summary);
        errors.addMessage(detail);
        InvalidDataException error = new InvalidDataException("whatever", errors);
        view.showValidationError(error);
        assertTrue(view.validationErrorWidget.isVisible());
        assertEquals(constants.bugReport_invalid(), view.validationErrorWidget.getErrorSummary());
        assertEquals(1, view.validationErrorWidget.getErrorList().size());
        assertEquals(constants.bugReport_required_problemSummary(), view.validationErrorWidget.getErrorList().get(0));
    }

    /** Test getBugReport(). */
    public void testGetBugReport() {
        FederatedUser submittingUser = new FederatedUser();
        submittingUser.setUserName("name");

        BugReport bugReport = new BugReport();
        bugReport.setReportDate(GwtDateUtils.createDate(2011, 12, 14, 23, 16));
        bugReport.setApplicationVersion("version");
        bugReport.setSubmittingUser(submittingUser);
        bugReport.setEmailAddress("email");
        bugReport.setProblemSummary("summary");
        bugReport.setDetailedDescription("description");

        BugReportDialogView view = new BugReportDialogView();
        view.showDialog(bugReport);

        view.emailAddress.setText("userEmail");
        view.problemSummary.setText("userSummary");
        view.detailedDescription.setText("userDescription");

        BugReport copy = view.getBugReport();
        assertEquals(bugReport.getReportDate(), copy.getReportDate());
        assertEquals(bugReport.getApplicationVersion(), copy.getApplicationVersion());
        assertSame(bugReport.getSubmittingUser(), copy.getSubmittingUser());
        assertEquals(view.emailAddress.getText(), copy.getEmailAddress());
        assertEquals(view.problemSummary.getText(), copy.getProblemSummary());
        assertEquals(view.detailedDescription.getText(), copy.getDetailedDescription());
    }

    /** Test onCancelClicked(). */
    public void testOnCancelClicked() {
        BugReportDialogView view = new BugReportDialogView();
        view.showDialog(new BugReport());
        view.onCancelClicked(null); // event is not used
        assertFalse(view.isShowing());
    }

    /** Test onSubmitClicked() and the event handler getters/setters. */
    public void testOnSubmitClicked() {
        StubbedEventHandler eventHandler = new StubbedEventHandler();

        BugReportDialogView view = new BugReportDialogView();
        view.setSubmitEventHandler(eventHandler);
        assertSame(eventHandler, view.getSubmitEventHandler());

        view.onSubmitClicked(null); // event is not used
        assertNotNull(eventHandler.event);
        assertEquals(UnifiedEventType.BUTTON_EVENT, eventHandler.event.getEventType());
    }

    /** Stubbed event handler for testing onSubmitClicked(). */
    private static class StubbedEventHandler implements ViewEventHandler {
        protected UnifiedEvent event;

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.event = event;
        }
    }
}
