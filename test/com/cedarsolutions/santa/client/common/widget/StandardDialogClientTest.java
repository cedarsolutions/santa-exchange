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

import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for StandardDialog.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class StandardDialogClientTest extends ClientTestCase {

    /** Test constructor. */
    public void testConstructor() {
        ConcreteDialog dialog = new ConcreteDialog();
        assertEquals(SantaExchangeStyles.STANDARD_DIALOG_STYLE, dialog.getStylePrimaryName());
    }

    /** Concrete dialog to test with. */
    private static class ConcreteDialog extends StandardDialog {
        ConcreteDialog() {
            super();
        }
    }

}
