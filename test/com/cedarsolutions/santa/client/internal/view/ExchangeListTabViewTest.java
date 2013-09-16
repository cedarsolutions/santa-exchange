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

import static com.cedarsolutions.client.gwt.event.UnifiedEventType.CLICK_EVENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
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
import com.cedarsolutions.web.metadata.NativeEventType;
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
        order.verify(deleteEventHandler, never()).handleEvent(any(UnifiedEvent.class));
        verify(view, times(0)).showDeletePopup(anyInt());

        when(view.getDeleteHandler()).thenReturn(null);
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(any(UnifiedEvent.class));
        order.verify(view, times(0)).showDeletePopup(anyInt());

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(any(UnifiedEvent.class));
        verify(view, times(0)).showDeletePopup(anyInt());

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        selected.add(mock(Exchange.class));
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(any(UnifiedEvent.class));
        verify(view, times(1)).showDeletePopup(1);

        when(view.getDeleteHandler()).thenReturn(deleteEventHandler);
        selected.add(mock(Exchange.class));
        handler.onClick(event);
        order.verify(deleteEventHandler, never()).handleEvent(any(UnifiedEvent.class));
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
    @Test public void testRowClickHandler() {
        // Unfortunately, it's not possible to mock NativeEvent, so
        // we just test the handleSelectedRow() method instead of the
        // onCellPreview() method.  Better than nothing.

        ExchangeListTabView view = mock(ExchangeListTabView.class, Mockito.RETURNS_DEEP_STUBS);
        RowClickHandler handler = new RowClickHandler(view);
        assertSame(view, handler.getParent());

        InOrder order = Mockito.inOrder(view.getEditSelectedRowHandler());
        Exchange exchange = new Exchange();
        UnifiedEventWithContext<Exchange> row = new UnifiedEventWithContext<Exchange>(CLICK_EVENT, exchange);

        handler.handleSelectedRow(null, 0, null);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        handler.handleSelectedRow("blech", 0, null);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        handler.handleSelectedRow("blech", 5, null);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        exchange.setId(null);
        handler.handleSelectedRow("blech", 5, exchange);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        exchange.setId(12L);
        handler.handleSelectedRow("blech", 5, exchange);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        exchange.setId(null);
        handler.handleSelectedRow(NativeEventType.CLICK.getValue(), 0, exchange);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        exchange.setId(null);
        handler.handleSelectedRow(NativeEventType.CLICK.getValue(), 5, exchange);
        order.verify(view.getEditSelectedRowHandler()).handleEvent(row);

        exchange.setId(12L);
        handler.handleSelectedRow(NativeEventType.CLICK.getValue(), 0, exchange);
        verifyNoMoreInteractions(view.getEditSelectedRowHandler());

        exchange.setId(12L);
        handler.handleSelectedRow(NativeEventType.CLICK.getValue(), 5, exchange);
        order.verify(view.getEditSelectedRowHandler()).handleEvent(row);
    }

}
