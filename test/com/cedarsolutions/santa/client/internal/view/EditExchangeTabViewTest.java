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
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.AddParticipantClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.DeleteParticipantClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.EmailColumn;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.NameColumn;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.PreviewClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.ResendNotificationClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.ResetClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.ResetConfirmHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.ReturnToListClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.RowClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.SaveClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.SendAllNotificationsClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.SendAllNotificationsConfirmHandler;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Unit tests for EditExchangeTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class EditExchangeTabViewTest extends StubbedClientTestCase {

    /** Test NameColumn. */
    @Test public void testNameColumn() {
        Participant item = mock(Participant.class);

        NameColumn column = new NameColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getName()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getName()).thenReturn("name");
        assertEquals("name", column.getValue(item));
    }

    /** Test EmailColumn. */
    @Test public void testEmailColumn() {
        Participant item = mock(Participant.class);

        EmailColumn column = new EmailColumn();
        assertTrue(column.getCell() instanceof TextCell);
        assertFalse(column.isSortable());

        assertEquals("", column.getValue(null));

        when(item.getEmailAddress()).thenReturn(null);
        assertEquals("", column.getValue(item));

        when(item.getEmailAddress()).thenReturn("email");
        assertEquals("email", column.getValue(item));
    }

    /** Test SaveClickHandler. */
    @Test public void testSaveClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        SaveClickHandler handler = new SaveClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getSaveHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler saveEventHandler = mock(ViewEventHandler.class);
        when(view.getSaveHandler()).thenReturn(saveEventHandler);
        handler.onClick(event);
        verify(saveEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test ResetClickHandler. */
    @Test public void testResetClickHandler() {
        ClickEvent event = mock(ClickEvent.class);
        ViewEventHandler resetEventHandler = mock(ViewEventHandler.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        ResetClickHandler handler = new ResetClickHandler(view);
        assertSame(view, handler.getParent());

        InOrder order = Mockito.inOrder(view, resetEventHandler);

        when(view.getResetHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up
        order.verify(resetEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view, never()).showResetConfirmPopup();

        when(view.getResetHandler()).thenReturn(resetEventHandler);
        handler.onClick(event);
        order.verify(resetEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view).showResetConfirmPopup();
    }

    /** Test ResetConfirmHandler. */
    @Test public void testResetConfirmHandler() {
        UnifiedEvent event = mock(UnifiedEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        ResetConfirmHandler handler = new ResetConfirmHandler(view);
        assertSame(view, handler.getParent());

        when(view.getResetHandler()).thenReturn(null);
        handler.handleEvent(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler resetConfirmEventHandler = mock(ViewEventHandler.class);
        when(view.getResetHandler()).thenReturn(resetConfirmEventHandler);
        handler.handleEvent(event);
        verify(resetConfirmEventHandler).handleEvent(captor.capture());
        assertSame(event, captor.getValue());
    }

    /** Test ReturnToListClickHandler. */
    @Test public void testReturnToListClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        ReturnToListClickHandler handler = new ReturnToListClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getReturnToListHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler returnToListEventHandler = mock(ViewEventHandler.class);
        when(view.getReturnToListHandler()).thenReturn(returnToListEventHandler);
        handler.onClick(event);
        verify(returnToListEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test AddParticipantClickHandler. */
    @Test public void testAddParticipantClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        AddParticipantClickHandler handler = new AddParticipantClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getAddParticipantHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler addParticipantEventHandler = mock(ViewEventHandler.class);
        when(view.getAddParticipantHandler()).thenReturn(addParticipantEventHandler);
        handler.onClick(event);
        verify(addParticipantEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test DeleteParticipantClickHandler. */
    @Test public void testDeleteParticipantClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        DeleteParticipantClickHandler handler = new DeleteParticipantClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getDeleteParticipantHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler deleteParticipantEventHandler = mock(ViewEventHandler.class);
        when(view.getDeleteParticipantHandler()).thenReturn(deleteParticipantEventHandler);
        handler.onClick(event);
        verify(deleteParticipantEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test SendAllNotificationsClickHandler. */
    @Test public void testSendAllNotificationsClickHandler() {
        ClickEvent event = mock(ClickEvent.class);
        ViewEventHandler sendAllNotificationsEventHandler = mock(ViewEventHandler.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class, Mockito.RETURNS_DEEP_STUBS);
        SendAllNotificationsClickHandler handler = new SendAllNotificationsClickHandler(view);
        assertSame(view, handler.getParent());

        AssignmentSet empty = new AssignmentSet();

        AssignmentSet notempty = new AssignmentSet();
        notempty.add(new Assignment());

        InOrder order = Mockito.inOrder(view, sendAllNotificationsEventHandler);

        when(view.getEditState().getAssignments()).thenReturn(notempty);
        when(view.getSendAllNotificationsHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up
        order.verify(sendAllNotificationsEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view, never()).showSendConfirmPopup();
        order.verify(view, never()).showResendConfirmPopup();

        when(view.getEditState().getAssignments()).thenReturn(null);
        when(view.getSendAllNotificationsHandler()).thenReturn(sendAllNotificationsEventHandler);
        handler.onClick(event);
        order.verify(sendAllNotificationsEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view).showSendConfirmPopup();
        order.verify(view, never()).showResendConfirmPopup();

        when(view.getEditState().getAssignments()).thenReturn(empty);
        when(view.getSendAllNotificationsHandler()).thenReturn(sendAllNotificationsEventHandler);
        handler.onClick(event);
        order.verify(sendAllNotificationsEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view).showSendConfirmPopup();
        order.verify(view, never()).showResendConfirmPopup();

        when(view.getEditState().getAssignments()).thenReturn(notempty);
        when(view.getSendAllNotificationsHandler()).thenReturn(sendAllNotificationsEventHandler);
        handler.onClick(event);
        order.verify(sendAllNotificationsEventHandler, never()).handleEvent(isA(UnifiedEvent.class));
        order.verify(view, never()).showSendConfirmPopup();
        order.verify(view).showResendConfirmPopup();
    }

    /** Test SendAllNotificationsConfirmHandler. */
    @Test public void testSendAllNotificationsConfirmHandler() {
        UnifiedEvent event = mock(UnifiedEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        SendAllNotificationsConfirmHandler handler = new SendAllNotificationsConfirmHandler(view);
        assertSame(view, handler.getParent());

        when(view.getSendAllNotificationsHandler()).thenReturn(null);
        handler.handleEvent(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler sendAllNotificationsConfirmEventHandler = mock(ViewEventHandler.class);
        when(view.getSendAllNotificationsHandler()).thenReturn(sendAllNotificationsConfirmEventHandler);
        handler.handleEvent(event);
        verify(sendAllNotificationsConfirmEventHandler).handleEvent(captor.capture());
        assertSame(event, captor.getValue());
    }

    /** Test ResendNotificationClickHandler. */
    @Test public void testResendNotificationClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        ResendNotificationClickHandler handler = new ResendNotificationClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getResendNotificationHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler resendNotificationEventHandler = mock(ViewEventHandler.class);
        when(view.getResendNotificationHandler()).thenReturn(resendNotificationEventHandler);
        handler.onClick(event);
        verify(resendNotificationEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test PreviewClickHandler. */
    @Test public void testPreviewClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditExchangeTabView view = mock(EditExchangeTabView.class);
        PreviewClickHandler handler = new PreviewClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getPreviewHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler previewEventHandler = mock(ViewEventHandler.class);
        when(view.getPreviewHandler()).thenReturn(previewEventHandler);
        handler.onClick(event);
        verify(previewEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test RowClickHandler. */
    @SuppressWarnings("unchecked")
    @Test public void testRowClickHandler() {
        ViewEventHandlerWithContext<Participant> editParticipantHandler = mock(ViewEventHandlerWithContext.class);
        EditExchangeTabView view = mock(EditExchangeTabView.class);
        when(view.getEditParticipantHandler()).thenReturn(editParticipantHandler);
        RowClickHandler handler = new RowClickHandler(view);
        assertSame(editParticipantHandler, handler.getViewEventHandler());
    }

}
