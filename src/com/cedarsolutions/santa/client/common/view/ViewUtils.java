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
package com.cedarsolutions.santa.client.common.view;

import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.client.gwt.validation.ValidationUtils;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.SantaExchangeConstants;
import com.cedarsolutions.santa.client.common.widget.SantaExchangeStyles;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;

/**
 * Common view utilities.
 *
 * <p>
 * Unlike a lot of other utility classes, which typically provide static
 * methods, this class is a singleton.
 * </p>
 *
 * <p>
 * It's difficult to mock static method calls, but I really want that option
 * for some of these methods.  Normally, I would fall back on dependency
 * injection to solve this problem.  However, it's not always possible to
 * inject an instance of this class into all of the places where it needs to be
 * used.  Making the class a singleton works around that problem.
 * </p>
 *
 * <p>
 * For testing, the stubbed client test framework is wired into the
 * GWT.create() call within getInstance().  The framework "automagically"
 * generates a mocked version of this class for use by unit tests.  However,
 * keep in mind that even the mock object is a singleton, so you must remember
 * to reset the mock between test cases.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ViewUtils {

    /** Singleton instance. */
    private static ViewUtils INSTANCE;

    /** Default constructor is protected so class cannot be instantiated. */
    protected ViewUtils() {
    }

    /** Get an instance of this class to use. */
    public static synchronized ViewUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = GWT.create(ViewUtils.class);
        }

        return INSTANCE;
    }

    /**
     * Derive the displayed username that's in the top menu bar.
     * @param user  User to use when deriving username
     * @return Displayed username for header's menu bar.
     */
    public String deriveDisplayedUsername(FederatedUser user) {
        SantaExchangeConstants constants = GWT.create(SantaExchangeConstants.class);
        if (user == null) {
            return constants.systemWide_unknownUserDisplayedUsername();
        } else {
            return deriveDisplayedUsername(user.getUserName());
        }
    }

    /**
     * Derive the displayed username that's in the top menu bar.
     *
     * <p>
     * The largest username that fits on the top menu bar is something the size
     * of "kenneth.pronovici@cedar-solutions.com" (roughly 37 characters, and
     * admittedly a fairly long email address).  If the passed-in username is
     * longer than that, we'll truncate it and put "[...]" at the end.
     * </p>
     *
     * @param username  Username to use as source
     *
     * @return Displayed username for header's menu bar.
     */
    public String deriveDisplayedUsername(String username) {
        SantaExchangeConstants constants = GWT.create(SantaExchangeConstants.class);
        if (GwtStringUtils.isEmpty(username)) {
            return constants.systemWide_unknownUserDisplayedUsername();
        } else {
            if (username.length() <= 37) {
                return username;
            } else {
                username = GwtStringUtils.truncate(username, 33);
                return username + "[...]";
            }
        }
    }

    /**
     * Show a validation error on a view that supports it.
     * @param view  View to operate on
     * @param error Validation error to show
     */
    public void showValidationError(IViewWithValidation view, InvalidDataException error) {
        ValidationUtils.getInstance().showValidationError(view, error, SantaExchangeStyles.VALIDATION_ERROR_STYLE);
    }

    /**
     * Clear error decorations from all fields and hide any validation errors.
     * @param view  View to operate on
     */
    public void clearValidationErrors(IViewWithValidation view) {
        ValidationUtils.getInstance().clearValidationErrors(view, SantaExchangeStyles.VALIDATION_ERROR_STYLE);
    }

}
