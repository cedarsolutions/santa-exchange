/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.junit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cedarsolutions.client.gwt.custom.datepicker.DateBox;
import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.widget.DataTable;
import com.cedarsolutions.client.gwt.widget.DataTable.SelectionColumn;
import com.cedarsolutions.client.gwt.widget.DataTable.SelectionHeader;
import com.cedarsolutions.util.gwt.GwtDateUtils;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Test case that all client tests inherit from.
 *
 * <p>
 * When invoked, client-side tests use a customized JUnit test runner
 * provided by Google.  The test runner starts up a servlet container and
 * invokes the client side code in that container with a stubbed browser.
 * </p>
 *
 * <p>
 * To run tests for an individual class, right-click on the class and choose
 * Run As &gt; GWT JUnit Test.  Sometimes, Eclipse gets confused and doesn't
 * offer the Run As &gt; GWT JUnit Test option.  To work around this, open the
 * class in the Java editor and and right click on the class name instead.
 * </p>
 *
 * <p>
 * For more information about test suites, code coverage, etc. see
 * <code>doc/README.tests.</code>
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public abstract class ClientTestCase extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.cedarsolutions.santa.SantaExchange";
    }

    /**
     * Simulate a click on the indicated button.
     * @see <a href="http://stackoverflow.com/questions/4609074/surely-theres-a-way-to-simulate-a-click-on-a-hasclickhandlers-object-in-gwt">Stack Overflow</a>
     * @see <a href="http://stackoverflow.com/questions/1238355/firing-click-event-from-code-in-gwt">Stack Overflow</a>
     */
    protected static void clickButton(HasHandlers button) {
        NativeEvent event = Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false);
        DomEvent.fireNativeEvent(event, button);
    }

    /** Assert that the date format is valid for a date box. */
    protected static void assertDateFormatValid(DateBox element, String format) {
        Date date = GwtDateUtils.createDate(2011, 12, 14, 1, 32);
        assertEquals(GwtDateUtils.formatDate(date, format), element.getFormat().format(element, date));
    }

    /** Assert that a table column is valid. */
    @SuppressWarnings("rawtypes")
    protected static void assertSelectionColumnAtIndex(DataTable table, int index) {
        assertEquals("For index " + index + ",", SelectionColumn.class, table.getColumn(index).getClass());
        assertEquals("For index " + index + ",", SelectionHeader.class, table.getColumnHeader(index).getClass());
        assertEquals("For index " + index + ",", null, table.getColumnFooter(index));
    }

    /** Assert that a table column is valid. */
    @SuppressWarnings("rawtypes")
    protected static void assertTableColumnValid(DataTable table, int index, Class column, String header) {
        assertTableColumnValid(table, index, column, header, null);
    }

    /** Assert that a table column is valid. */
    @SuppressWarnings("rawtypes")
    protected static void assertTableColumnValid(DataTable table, int index, Class column, String header, String footer) {
        assertEquals("For index " + index + ",", column, table.getColumn(index).getClass());
        assertEquals("For index " + index + ",", header, table.getColumnHeader(index));
        assertEquals("For index " + index + ",", footer, table.getColumnFooter(index));
    }

    /** Stubbed view handler. */
    protected static class StubbedViewEventHandler implements ViewEventHandler {

        private List<UnifiedEvent> events = new ArrayList<UnifiedEvent>();

        public StubbedViewEventHandler() {
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.events.add(event);
        }

        public List<UnifiedEvent> getEvents() {
            return events;
        }

        public boolean handledEvent() {
            return !this.events.isEmpty();
        }
    }

    /**
     * Stubbed view handler.
     * @param <T> Type of the event context
     */
    protected static class StubbedViewEventHandlerWithContext<T> implements ViewEventHandlerWithContext<T> {

        private List<UnifiedEventWithContext<T>> events = new ArrayList<UnifiedEventWithContext<T>>();

        public StubbedViewEventHandlerWithContext() {
        }

        @Override
        public void handleEvent(UnifiedEventWithContext<T> event) {
            this.events.add(event);
        }

        public List<UnifiedEventWithContext<T>> getEvents() {
            return events;
        }

        public boolean handledEvent() {
            return !this.events.isEmpty();
        }
    }

}
