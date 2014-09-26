/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2014 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.root.presenter;

import java.util.ArrayList;
import java.util.List;

import com.cedarsolutions.santa.client.common.widget.ErrorPopup;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.event.shared.UmbrellaException;

/**
 * Global uncaught exception handler.
 * @see <a href="http://www.summa-tech.com/blog/2012/06/11/7-tips-for-exception-handling-in-gwt/">7 Tips</a>
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {

    /** Display an uncaught exception in a pop-up. */
    @Override
    public void onUncaughtException(Throwable uncaught) {
        // The goal is to have as few dependencies as possible.
        // Strings are not localized and we invoke the error pop-up directly.

        Throwable exception = unwrapUmbrella(uncaught);
        String message = "An user interface error was encountered.";
        List<String> supporting = new ArrayList<String>();
        supporting.add("This is a client-side error that occurred in the browser.");
        supporting.add("The stack trace below might be useful in tracking down the problem.");
        ErrorDescription error = new ErrorDescription(message, exception, supporting);

        new ErrorPopup().showError(error);
    }

    /** Unwrap the passed-in exception, if necessary. */
    private static Throwable unwrapUmbrella(Throwable e) {
        if (e instanceof UmbrellaException) {
            UmbrellaException ue = (UmbrellaException) e;
            if (ue.getCauses().size() == 1) {
                return unwrapUmbrella(ue.getCauses().iterator().next());
            }
        }

        return e;
    }

}
