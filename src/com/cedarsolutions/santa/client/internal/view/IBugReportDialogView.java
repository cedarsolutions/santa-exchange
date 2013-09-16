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

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.shared.domain.BugReport;

/**
 * View for bug report dialog.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IBugReportDialogView extends IViewWithValidation {

    /** Show the dialog, displaying a bug report. */
    void showDialog(BugReport bugReport);

    /** Show a validation error. */
    void showValidationError(InvalidDataException error);

    /** Hide the dialog. */
    void hideDialog();

    /** Set the event handler for the submit action. */
    void setSubmitEventHandler(ViewEventHandler submitEventHandler);

    /** Get the event handler for the submit action. */
    ViewEventHandler getSubmitEventHandler();

    /** Get the bug report from the dialog. */
    BugReport getBugReport();

}
