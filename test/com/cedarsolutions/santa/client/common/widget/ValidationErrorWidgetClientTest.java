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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.junit.ClientTestCase;

/**
 * Client-side unit tests for ValidationErrorWidget.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ValidationErrorWidgetClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        ValidationErrorWidget widget = new ValidationErrorWidget();
        assertNotNull(widget);
        assertNotNull(widget.validationErrorPanel);
        assertNotNull(widget.validationErrorSummary);
        assertNotNull(widget.validationErrorList);
    }

    /** Test hide(), show(), and isDisplayed(). */
    public void testHideShow() {
        ValidationErrorWidget widget = new ValidationErrorWidget();
        assertFalse(widget.isVisible());

        widget.hide();
        assertFalse(widget.isVisible());

        widget.show();
        assertTrue(widget.isVisible());

        widget.show();
        assertTrue(widget.isVisible());

        widget.hide();
        assertFalse(widget.isVisible());
    }

    /** Test error summary. */
    public void testErrorSummary() {
        ValidationErrorWidget widget = new ValidationErrorWidget();
        assertEquals("", widget.getErrorSummary());

        widget.clearErrorSummary();
        assertEquals("", widget.getErrorSummary());

        widget.setErrorSummary("summary");
        assertEquals("summary", widget.getErrorSummary());

        widget.setErrorSummary("");
        assertEquals("", widget.getErrorSummary());

        widget.setErrorSummary("whatever");
        assertEquals("whatever", widget.getErrorSummary());

        widget.setErrorSummary(null);
        assertEquals("", widget.getErrorSummary());

        widget.setErrorSummary("whatever");
        widget.clearErrorSummary();
        assertEquals("", widget.getErrorSummary());
    }

    /** Test error list. */
    public void testErrorList() {
        ValidationErrorWidget widget = new ValidationErrorWidget();
        assertEquals(0, widget.getErrorList().size());

        widget.clearErrorList();
        assertEquals(0, widget.getErrorList().size());

        widget.addError("");
        assertEquals(0, widget.getErrorList().size());

        widget.addError(null);
        assertEquals(0, widget.getErrorList().size());

        widget.clearErrorList();
        assertEquals(0, widget.getErrorList().size());

        widget.addError("whatever");
        assertEquals(1, widget.getErrorList().size());
        assertEquals("whatever", widget.getErrorList().get(0));

        widget.addError("another");
        assertEquals(2, widget.getErrorList().size());
        assertEquals("whatever", widget.getErrorList().get(0));
        assertEquals("another", widget.getErrorList().get(1));

        widget.clearErrorList();
        assertEquals(0, widget.getErrorList().size());
    }

}
