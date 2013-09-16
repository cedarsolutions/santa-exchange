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

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;


/**
 * Dialog box that behaves in a standard way.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class StandardDialog extends DialogBox {

    /** Shared constructor. */
    protected StandardDialog() {
        setAnimationEnabled(false);
        setGlassEnabled(true);
        setStylePrimaryName(SantaExchangeStyles.STANDARD_DIALOG_STYLE);
    }

    /** Standardize the way the dialog is shown. */
    @Override
    public void show() {
        super.show();
        positionDialog();
    }

    /** Standardize the way the dialog is hidden. */
    @Override
    public void hide() {
        super.hide();
    }

    /** Position the dialog in a sensible place. */
    private void positionDialog() {
        // This follows the strategy in PopupPanel.center()

        double width = (double) (Window.getClientWidth() - getOffsetWidth());
        double height = (double) (Window.getClientHeight() - getOffsetHeight());

        int left = (int) (width * 0.5);
        int top = (int) (height * 0.1);

        setPopupPosition(Math.max(Window.getScrollLeft() + left, 0), Math.max(Window.getScrollTop() + top, 0));
    }

}
