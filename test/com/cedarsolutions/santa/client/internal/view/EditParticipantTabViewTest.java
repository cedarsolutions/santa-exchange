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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventType;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.AddConflictClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.CancelClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.DeleteConflictClickHandler;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.EmailColumn;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.NameColumn;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.SaveClickHandler;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * Unit test for EditParticipantTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class EditParticipantTabViewTest extends StubbedClientTestCase {

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

        EditParticipantTabView view = mock(EditParticipantTabView.class);
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

    /** Test CancelClickHandler. */
    @Test public void testCancelClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditParticipantTabView view = mock(EditParticipantTabView.class);
        CancelClickHandler handler = new CancelClickHandler(view);
        assertSame(view, handler.getParent());

        when(view.getCancelHandler()).thenReturn(null);
        handler.onClick(event);  // just make sure it doesn't blow up

        ArgumentCaptor<UnifiedEvent> captor = ArgumentCaptor.forClass(UnifiedEvent.class);
        ViewEventHandler cancelEventHandler = mock(ViewEventHandler.class);
        when(view.getCancelHandler()).thenReturn(cancelEventHandler);
        handler.onClick(event);
        verify(cancelEventHandler).handleEvent(captor.capture());
        assertEquals(UnifiedEventType.CLICK_EVENT, captor.getValue().getEventType());
        assertSame(event, captor.getValue().getClickEvent());
    }

    /** Test AddConflictClickHandler. */
    @Test public void testAddConflictClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditParticipantTabView view = mock(EditParticipantTabView.class);
        AddConflictClickHandler handler = new AddConflictClickHandler(view);
        assertSame(view, handler.getParent());

        handler.onClick(event);
        verify(view).addSelectedConflict();
    }

    /** Test DeleteConflictClickHandler. */
    @Test public void testDeleteConflictClickHandler() {
        ClickEvent event = mock(ClickEvent.class);

        EditParticipantTabView view = mock(EditParticipantTabView.class);
        DeleteConflictClickHandler handler = new DeleteConflictClickHandler(view);
        assertSame(view, handler.getParent());

        handler.onClick(event);
        verify(view).deleteSelectedConflicts();
    }

}
