/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2014 Kenneth J. Pronovici.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.santa.client.common.widget.ConfirmationPopup;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.CreateClickHandler;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.DeleteClickHandler;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.DeleteConfirmHandler;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.NameColumn;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.RowClickHandler;
import com.cedarsolutions.santa.client.internal.view.ExchangeListTabView.StateColumn;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Unit tests for ExchangeListTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeListTabViewTest extends StubbedClientTestCase {

    /** Test NameColumn. */
    @Test public void testNameColumn() {
        Exchange item = mock(Exchange.class);

        NameColumn column = new NameColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getName()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getName()).thenReturn("name");
        assertEquals("name", column.getValue(item));
    }

    /** Test StateColumn. */
    @Test public void testStateColumn() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        Exchange item = mock(Exchange.class);

        StateColumn column = new StateColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getExchangeState()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getExchangeState()).thenReturn(ExchangeState.NEW);
        assertEquals(constants.state_new(), column.getValue(item));

        when(item.getExchangeState()).thenReturn(ExchangeState.STARTED);
        assertEquals(constants.state_started(), column.getValue(item));

        when(item.getExchangeState()).thenReturn(ExchangeState.GENERATED);
        assertEquals(constants.state_generated(), column.getValue(item));

        when(item.getExchangeState()).thenReturn(ExchangeState.SENT);
        assertEquals(constants.state_sent(), column.getValue(item));
    }

    /** Test CreateClickHandler. */
    @Test public void testCreateClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        ExchangeListTabView view = mock(ExchangeListTabView.class);
        CreateClickHandler handler = new CreateClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getCreateHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler createEventHandler = mock(ViewEventHandler.class);
        when(view.getCreateHandler()).thenReturn(createEventHandler);
        handler.onClick(event);
        verify(createEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test DeleteClickHandler. */
    @Test public void testDeleteClickHandler() {
        ViewEventHandler deleteEventHandler = mock(ViewEventHandler.class);

        ConfirmationPopup popup = mock(ConfirmationPopup.class);
        ExchangeListTabView view = mock(ExchangeListTabView.class, Mockito.RETURNS_DEEP_STUBS);
        when(view.getDeletePopup()).thenReturn(popup);

        DeleteClickHandler handler = new DeleteClickHandler(view);
        assertSame(view, handler.getParent());

        ClickEvent event = mock(ClickEvent.class);
        List<Exchange> selected = new ArrayList<Exchange>();
        when(view.getTable().getSelectedRecords()).thenReturn(selected);

        InOrder order = Mockito.inOrder(view, deleteEventHandler);

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        handler.onClick(null);  // just make sure it doesn't blow up
        order.verify(deleteEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        verify(view, times(0)).showDeletePopup(anyInt());

        when(view.getDeleteHandler()).thenReturn(null);
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view, times(0)).showDeletePopup(anyInt());

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        verify(view, times(0)).showDeletePopup(anyInt());

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        selected.add(mock(Exchange.class));
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        verify(view, times(1)).showDeletePopup(1);

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        selected.add(mock(Exchange.class));
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        verify(view, times(1)).showDeletePopup(2);
    }

    /** Test DeleteConfirmHandler. */
    @Test public void testDeleteConfirmHandler() {
        UnifiedEvent event = mock(UnifiedEvent.class);

        ExchangeListTabView view = mock(ExchangeListTabView.class);
        DeleteConfirmHandler handler = new DeleteConfirmHandler(view);
        assertSame(view, handler.getParent());

        when(view.getDeleteHandler()).thenReturn(null);
        handler.handleEvent(event);

        ViewEventHandler deleteConfirmEventHandler = mock(ViewEventHandler.class);
        when(view.getDeleteHandler()).thenReturn(deleteConfirmEventHandler);
        handler.handleEvent(event);
        verify(deleteConfirmEventHandler).handleEvent(event);
    }

    /** Test RowClickHandler. */
    @SuppressWarnings("unchecked")
    @Test public void testRowClickHandler() {
        ViewEventHandlerWithContext<Exchange> editSelectedRowHandler = mock(ViewEventHandlerWithContext.class);
        ExchangeListTabView view = mock(ExchangeListTabView.class);
        when(view.getEditSelectedRowHandler()).thenReturn(editSelectedRowHandler);
        RowClickHandler handler = new RowClickHandler(view);
        assertSame(editSelectedRowHandler, handler.getViewEventHandler());
    }

}
