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
package com.cedarsolutions.santa.client.internal.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotSupportedException;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.NameColumn;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.StateColumn;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for ExchangeListTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeListTabViewClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        ExchangeListTabView view = new ExchangeListTabView();
        assertWidgetsSetProperly(view);
        assertTableConfiguredProperly(view);
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        ExchangeListTabView view = new ExchangeListTabView();

        StubbedViewEventHandler refreshEventHandler = new StubbedViewEventHandler();
        view.setRefreshEventHandler(refreshEventHandler);
        assertSame(refreshEventHandler, view.getRefreshEventHandler());

        StubbedViewEventHandler criteriaResetEventHandler = new StubbedViewEventHandler();
        view.setCriteriaResetEventHandler(criteriaResetEventHandler);
        assertSame(criteriaResetEventHandler, view.getCriteriaResetEventHandler());

        StubbedViewEventHandler deleteHandler = new StubbedViewEventHandler();
        view.setDeleteHandler(deleteHandler);
        assertSame(deleteHandler, view.getDeleteHandler());
        clickButton(view.deleteButton);
        clickButton(view.getDeletePopup().getCancelButton());
        assertFalse(deleteHandler.handledEvent());
        clickButton(view.deleteButton);
        clickButton(view.getDeletePopup().getOkButton());
        assertTrue(deleteHandler.handledEvent());

        StubbedViewEventHandler createHandler = new StubbedViewEventHandler();
        view.setCreateHandler(createHandler);
        assertSame(createHandler, view.getCreateHandler());
        clickButton(view.createButton);
        assertTrue(createHandler.handledEvent());

        ViewEventHandlerWithContext<Exchange> editExchangeHandler = new StubbedViewEventHandlerWithContext<Exchange>();
        view.setEditSelectedRowHandler(editExchangeHandler);
        assertSame(editExchangeHandler, view.getEditSelectedRowHandler());
    }

    /** Test showValidationError(). */
    public void testShowValidationError() {
        try {
            ExchangeListTabView view = new ExchangeListTabView();
            view.showValidationError(new InvalidDataException());
            fail("Expected NotSupportedException");
        } catch (NotSupportedException e) { }
    }

    /** Test getSelectedRecords(). */
    public void testGetSelectedRecords() {
        ExchangeListTabView view = new ExchangeListTabView();
        assertNotNull(view.getSelectedRecords());  // just make sure that it doesn't blow up
    }

    /** Test get/set search criteria. */
    public void testSearchCriteria() {
        ExchangeListTabView view = new ExchangeListTabView();

        assertFalse(view.hasSearchCriteria());
        ExchangeCriteria result = view.getSearchCriteria();
        assertNull(result);

        ExchangeCriteria criteria = new ExchangeCriteria();
        view.setSearchCriteria(criteria);
        assertTrue(view.hasSearchCriteria());
        result = view.getSearchCriteria();
        assertSame(criteria, result);
    }

    /** Test showDeletePopup(). */
    public void testShowDeletePopup() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        ExchangeListTabView view = new ExchangeListTabView();
        assertFalse(view.deletePopup.isShowing());

        view.showDeletePopup(0);
        assertTrue(view.deletePopup.isShowing());
        assertEquals(constants.exchangeList_confirmDeleteMessageSeveral(), view.deletePopup.getMessage());
        clickButton(view.deletePopup.getOkButton());
        assertFalse(view.deletePopup.isShowing());

        view.showDeletePopup(1);
        assertTrue(view.deletePopup.isShowing());
        assertEquals(constants.exchangeList_confirmDeleteMessageOne(), view.deletePopup.getMessage());
        clickButton(view.deletePopup.getOkButton());
        assertFalse(view.deletePopup.isShowing());

        view.showDeletePopup(2);
        assertTrue(view.deletePopup.isShowing());
        assertEquals(constants.exchangeList_confirmDeleteMessageSeveral(), view.deletePopup.getMessage());
        clickButton(view.deletePopup.getOkButton());
        assertFalse(view.deletePopup.isShowing());

        view.showDeletePopup(50);
        assertTrue(view.deletePopup.isShowing());
        assertEquals(constants.exchangeList_confirmDeleteMessageSeveral(), view.deletePopup.getMessage());
        clickButton(view.deletePopup.getOkButton());
        assertFalse(view.deletePopup.isShowing());
    }

    /** Assert that all of the widgets are configured properly. */
    private static void assertWidgetsSetProperly(ExchangeListTabView view) {
        InternalConstants constants = GWT.create(InternalConstants.class);

        assertNotNull(view.createButton);
        assertEquals(constants.exchangeList_createButton(), view.createButton.getText());

        assertNotNull(view.deleteButton);
        assertEquals(constants.exchangeList_deleteButton(), view.deleteButton.getText());

        assertSame(view.deletePopup, view.getDeletePopup());
    }

    /** Assert that the table is configured properly. */
    private static void assertTableConfiguredProperly(ExchangeListTabView view) {
        InternalConstants constants = GWT.create(InternalConstants.class);

        assertEquals(constants.exchangeList_pageSize(), view.getPageSize());
        assertSame(view.table, view.getDisplay());
        assertSame(view.table, view.getTable());

        assertNotNull(view.table);
        assertNotNull(view.pager);
        assertTrue(view.table.hasSelectionColumn());
        assertEquals(constants.exchangeList_noRows(), view.table.getNoRowsMessage());

        assertEquals(3, view.table.getColumnCount());
        assertSelectionColumnAtIndex(view.table, 0);
        assertTableColumnValid(view.table, 1, NameColumn.class, constants.exchangeList_nameTitle());
        assertTableColumnValid(view.table, 2, StateColumn.class, constants.exchangeList_stateTitle());
    }

}
