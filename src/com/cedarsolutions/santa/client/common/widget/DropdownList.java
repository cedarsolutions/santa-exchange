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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.client.gwt.widget.AbstractDropdownList;
import com.google.gwt.core.client.GWT;

/**
 * Standard dropdown list.
 * @param <T> Type of the list, which MUST have a valid equals()/hashCode() implementation.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class DropdownList<T> extends AbstractDropdownList<T> {

    /** Get the display value used for the "any" key (null). */
    @Override
    protected String getAnyDisplay() {
        WidgetConstants constants = GWT.create(WidgetConstants.class);
        return constants.dropdown_Any();
    }

}
