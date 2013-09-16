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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.google.gwt.core.client.GWT;

/**
 * List box containing yes/no choices.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class YesNoList extends DropdownList<Boolean> {

    /** Create the list. */
    public YesNoList() {
        this(false);
    }

    /** Create the list, optionally adding the "any" choice. */
    public YesNoList(boolean addAny) {
        if (addAny) {
            this.addDropdownItemAny();
        }

        this.addDropdownItem(TRUE);
        this.addDropdownItem(FALSE);

        this.setVisibleItemCount(1);
    }

    /** Get the display value for an item. */
    @Override
    protected String getDisplay(Boolean value) {
        WidgetConstants constants = GWT.create(WidgetConstants.class);
        if (Boolean.TRUE.equals(value)) {
            return constants.yesNoList_yes();
        } else {
            return constants.yesNoList_no();
        }
    }

}
