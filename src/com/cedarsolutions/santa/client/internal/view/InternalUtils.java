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

import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.google.gwt.core.client.GWT;

/**
 * Utilities for internal views.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalUtils {

    /** Get an ExchangeState for display to the user. */
    public static String getExchangeStateForDisplay(ExchangeState exchangeState) {
        InternalConstants constants = GWT.create(InternalConstants.class);
        if (exchangeState == null) {
            return constants.state_new();
        } else {
            switch (exchangeState) {
            case NEW:
                return constants.state_new();
            case STARTED:
                return constants.state_started();
            case GENERATED:
                return constants.state_generated();
            case SENT:
                return constants.state_sent();
            default:
                return constants.state_new();
            }
        }
    }

}
