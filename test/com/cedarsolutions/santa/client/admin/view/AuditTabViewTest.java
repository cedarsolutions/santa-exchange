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
package com.cedarsolutions.santa.client.admin.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.ClearClickHandler;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.EventIdColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.EventTimestampColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.EventTypeColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.ExtraDataColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.RefreshClickHandler;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.SessionIdColumn;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.UserIdCell;
import com.cedarsolutions.santa.client.admin.view.AuditTabView.UserIdColumn;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;
import com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey;
import com.cedarsolutions.util.DateUtils;
import com.cedarsolutions.web.metadata.NativeEventType;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Unit tests for AuditTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditTabViewTest extends StubbedClientTestCase {

    /** Test EventIdColumn. */
    @Test public void testEventIdColumn() {
        AuditEvent item = mock(AuditEvent.class);

        EventIdColumn column = new EventIdColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getEventId()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getEventId()).thenReturn(42L);
        assertEquals("42", column.getValue(item));
    }

    /** Test EventTypeColumn. */
    @Test public void testEventTypeColumn() {
        AdminMessageConstants constants = GWT.create(AdminMessageConstants.class);
        AuditEvent item = mock(AuditEvent.class);

        EventTypeColumn column = new EventTypeColumn();
        assertFalse(column.isSortable());

        assertEquals("", column.getStringValue(null));
        assertEquals(null, column.getTooltip(item));

        when(item.getEventType()).thenReturn(null);
        assertEquals("", column.getStringValue(item));
        assertEquals(null, column.getTooltip(item));

        when(item.getEventType()).thenReturn(AuditEventType.ADMIN_LOGIN);
        assertEquals("ADMIN_LOGIN", column.getStringValue(item));
        assertEquals(constants.auditEventTooltip_ADMIN_LOGIN(), column.getTooltip(item));
    }

    /** Test EventTimestampColumn. */
    @Test public void testEventTimestampColumn() {
        AuditEvent item = mock(AuditEvent.class);

        EventTimestampColumn column = new EventTimestampColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getEventTimestamp()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getEventTimestamp()).thenReturn(DateUtils.createDate(2011, 12, 14, 8, 32, 14, 992));
        assertEquals("2011-12-14T08:32:14,992", column.getValue(item));
    }

    /** Test UserIdColumn. */
    @Test public void testUserIdColumn() {
        AuditEvent item = mock(AuditEvent.class);
        AuditTabView parent = mock(AuditTabView.class);

        UserIdColumn column = new UserIdColumn(parent);
        assertTrue(column.getCell() instanceof UserIdCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getUserId()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getUserId()).thenReturn("id");
        assertEquals("id", column.getValue(item));
    }

    /** Test SessionIdColumn. */
    @Test public void testSessionIdColumn() {
        AuditEvent item = mock(AuditEvent.class);

        SessionIdColumn column = new SessionIdColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getSessionId()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getSessionId()).thenReturn("id");
        assertEquals("id", column.getValue(item));
    }

    /** Test ExtraDataColumn. */
    @Test public void testExtraDataColumn() {
        AuditEvent item = mock(AuditEvent.class);

        ExtraDataColumn column = new ExtraDataColumn();
        assertTrue(column.getCell() instanceof SafeHtmlCell);
        assertFalse(column.isSortable());

        assertEquals(SafeHtmlUtils.fromTrustedString(""), column.getValue(null));

        when(item.getExtraData()).thenReturn(null);
        assertEquals(SafeHtmlUtils.fromTrustedString(""), column.getValue(item));

        List<ExtraData> extraData = new ArrayList<ExtraData>();
        when(item.getExtraData()).thenReturn(extraData);
        assertEquals(SafeHtmlUtils.fromTrustedString(""), column.getValue(item));

        extraData.add(new ExtraData(ExtraDataKey.MODULE, "module"));
        assertEquals(SafeHtmlUtils.fromTrustedString("MODULE=module<br/>"), column.getValue(item));

        extraData.add(new ExtraData(ExtraDataKey.EXCHANGE_ID, 42L));
        assertEquals(SafeHtmlUtils.fromTrustedString("MODULE=module<br/>EXCHANGE_ID=42<br/>"), column.getValue(item));

        extraData.add(new ExtraData(ExtraDataKey.EMAIL_ADDRESS, (String) null));
        assertEquals(SafeHtmlUtils.fromTrustedString("MODULE=module<br/>EXCHANGE_ID=42<br/>EMAIL_ADDRESS=<br/>"), column.getValue(item));
    }

    /** Test UserIdCell. */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test public void testUserIdCell() {
        AuditTabView parent = mock(AuditTabView.class);
        UserIdCell cell = new UserIdCell(parent);
        assertEquals(parent, cell.parent);

        when(parent.getUserIdSelectedHandler()).thenReturn(null);
        cell.handleBrowserEvent(NativeEventType.CLICK.getValue(), "userId");

        ArgumentCaptor<UnifiedEventWithContext> captor = ArgumentCaptor.forClass(UnifiedEventWithContext.class);
        ViewEventHandlerWithContext<String> userIdSelectedHandler = mock(ViewEventHandlerWithContext.class);
        when(parent.getUserIdSelectedHandler()).thenReturn(userIdSelectedHandler);
        cell.handleBrowserEvent("blech", "userId");
        verify(userIdSelectedHandler, never()).handleEvent(captor.capture());

        cell.handleBrowserEvent(NativeEventType.CLICK.getValue(), "userId");
        verify(userIdSelectedHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame("userId", captor.getValue().getContext());
    }

    /** Test RefreshClickHandler. */
    @Test public void testRefreshClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        AuditTabView view = mock(AuditTabView.class);
        RefreshClickHandler handler = new RefreshClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getRefreshEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler refreshEventHandler = mock(ViewEventHandler.class);
        when(view.getRefreshEventHandler()).thenReturn(refreshEventHandler);
        handler.onClick(event);
        verify(refreshEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test ClearClickHandler. */
    @Test public void testClearClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        AuditTabView view = mock(AuditTabView.class);
        ClearClickHandler handler = new ClearClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getCriteriaResetEventHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler criteriaResetEventHandler = mock(ViewEventHandler.class);
        when(view.getCriteriaResetEventHandler()).thenReturn(criteriaResetEventHandler);
        handler.onClick(event);
        verify(criteriaResetEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

}
