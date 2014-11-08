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
 * Client-side unit tests for YesNoList.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class YesNoListClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        YesNoList list = new YesNoList();
        assertNotNull(list);
        assertEquals(1, list.getVisibleItemCount());
        assertEquals(2, list.getItemCount());

        list = new YesNoList(true);
        assertNotNull(list);
        assertEquals(1, list.getVisibleItemCount());
        assertEquals(3, list.getItemCount());
    }

    /** Check that every value can be selected. */
    public void testsetSelectedObjectValue() {
        YesNoList list = new YesNoList(true);

        list.setSelectedObjectValue(null);
        assertEquals(null, list.getSelectedObjectValue());

        list.setSelectedObjectValue(Boolean.TRUE);
        assertEquals(Boolean.TRUE, list.getSelectedObjectValue());

        list.setSelectedObjectValue(Boolean.FALSE);
        assertEquals(Boolean.FALSE, list.getSelectedObjectValue());
    }

}
