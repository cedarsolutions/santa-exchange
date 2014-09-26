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
package com.cedarsolutions.santa.client.internal.presenter;

import static com.cedarsolutions.junit.util.Assertions.assertOnlyMessage;
import static com.cedarsolutions.junit.util.Assertions.assertSummary;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.presenter.EditParticipantTabPresenter.CancelHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditParticipantTabPresenter.SaveHandler;
import com.cedarsolutions.santa.client.internal.view.IEditParticipantTabView;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.ErrorDescription;

/**
 * Unit tests for EditParticipantTabPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class EditParticipantTabPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        EditParticipantTabPresenter presenter = new EditParticipantTabPresenter();
        presenter.onStart(); // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        EditParticipantTabPresenter presenter = createPresenter();
        presenter.bind();
        verify(presenter.getView()).setSaveHandler(isA(SaveHandler.class));
        verify(presenter.getView()).setCancelHandler(isA(CancelHandler.class));
    }

    /** Test onShowEditParticipantPage(). */
    @Test public void testOnShowEditParticipantPage() {
        ParticipantSet participants = new ParticipantSet();

        EditParticipantTabPresenter presenter = createPresenter();

        Participant participant = new Participant();
        participant.setId(3L);

        Participant giver = new Participant();
        giver.setId(4L);

        Participant receiver = new Participant();
        receiver.setId(4L);

        AssignmentSet assignments = new AssignmentSet();
        assignments.add(new Assignment(giver, participant));
        assignments.add(new Assignment(participant, receiver));

        Exchange exchange = new Exchange();
        exchange.setId(2L);
        exchange.setAssignments(assignments);

        presenter.getManager().initialize(exchange);

        presenter.onShowEditParticipantPage(participant, true, participants);
        assertEquals(participant, presenter.getUndoState());
        assertEquals(true, presenter.getIsNew());
        assertNotSame(participant, presenter.getUndoState());
        verify(presenter.getView()).setEditState(participant, giver, receiver, participants);
        verify(presenter.getEventBus()).selectEditParticipantTab();
    }

    /** Test SaveHandler when the participant is found. */
    @Test public void testSaveHandlerParticipantFound() {
        EditParticipantTabPresenter presenter = createPresenter();
        SaveHandler handler = new SaveHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant1a = new Participant();
        participant1a.setId(1L);
        participant1a.setName("1a");
        participant1a.setNickname("nickname");
        participant1a.setEmailAddress("email");

        Participant participant1b = new Participant();
        participant1b.setId(1L);
        participant1b.setName("1b");
        participant1b.setNickname("nickname");
        participant1b.setEmailAddress("email");

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.setName("start");
        exchangeAtStart.getParticipants().add(participant1a);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("end");
        exchangeAtEnd.getParticipants().add(participant1b);

        presenter.getManager().initialize(exchangeAtStart);
        presenter.getManager().getEditState().setName("end");

        when(presenter.getView().getEditState()).thenReturn(participant1b);
        handler.handleEvent(null);  // actual event does not matter
        verify(presenter.getEventBus()).editCurrentExchange();
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test SaveHandler when the participant is not found. */
    @Test public void testSaveHandlerParticipantNotFound() {
        EditParticipantTabPresenter presenter = createPresenter();
        SaveHandler handler = new SaveHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participantEmpty = new Participant();

        Participant participantNoId = new Participant();
        participantNoId.setName("3");
        participantNoId.setNickname("nickname");
        participantNoId.setEmailAddress("email");

        Participant participant1a = new Participant();
        participant1a.setId(1L);
        participant1a.setName("1a");
        participant1a.setNickname("nickname");
        participant1a.setEmailAddress("email");

        Participant participant2a = new Participant();
        participant2a.setId(2L);
        participant2a.setName("2a");
        participant2a.setNickname("nickname");
        participant2a.setEmailAddress("email");

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.setName("start");
        exchangeAtStart.getParticipants().add(participant1a);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("end");
        exchangeAtEnd.getParticipants().add(participant1a);

        presenter.getManager().initialize(exchangeAtStart);
        presenter.getManager().getEditState().setName("end");

        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus());

        when(presenter.getView().getEditState()).thenReturn(participant2a);
        handler.handleEvent(null);  // actual event does not matter
        order.verify(presenter.getEventBus()).showErrorPopup(isA(ErrorDescription.class));
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());

        when(presenter.getView().getEditState()).thenReturn(participantEmpty);
        handler.handleEvent(null);  // actual event does not matter
        order.verify(presenter.getEventBus()).showErrorPopup(isA(ErrorDescription.class));
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());

        when(presenter.getView().getEditState()).thenReturn(participantNoId);
        handler.handleEvent(null);  // actual event does not matter
        order.verify(presenter.getEventBus()).showErrorPopup(isA(ErrorDescription.class));
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());

        when(presenter.getView().getEditState()).thenReturn(null);
        handler.handleEvent(null);  // actual event does not matter
        order.verify(presenter.getEventBus()).showErrorPopup(isA(ErrorDescription.class));
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test SaveHandler when the participant is not valid. */
    @Test public void testSaveHandlerParticipantNotValid() {
        EditParticipantTabPresenter presenter = createPresenter();
        SaveHandler handler = new SaveHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant1a = new Participant();
        participant1a.setId(1L);
        participant1a.setName("1a");

        Participant participant1b = new Participant();
        participant1b.setId(1L);
        participant1b.setName("1b");

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.setName("start");
        exchangeAtStart.getParticipants().add(participant1a);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("end");
        exchangeAtEnd.getParticipants().add(participant1a);

        presenter.getManager().initialize(exchangeAtStart);
        presenter.getManager().getEditState().setName("end");

        when(presenter.getView().getEditState()).thenReturn(participant1b);
        handler.handleEvent(null);  // actual event does not matter
        verify(presenter.getView()).showValidationError(isA(InvalidDataException.class));
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test SaveHandler validation. */
    @Test public void testSaveHandlerValidation() {
        Participant participant = null;

        participant = createParticipant();
        SaveHandler.validateParticipant(participant);

        try {
            SaveHandler.validateParticipant(null);
            fail("Expected CedarRuntimeException");
        } catch (CedarRuntimeException e) { }

        try {
            participant = createParticipant();
            participant.setId(null);
            SaveHandler.validateParticipant(participant);
            fail("Expected CedarRuntimeException"); // because the participant is seriously screwed up
        } catch (CedarRuntimeException e) { }

        participant = createParticipant();
        participant.setName("");
        assertFieldIsRequired(participant, "name");

        participant = createParticipant();
        participant.setNickname("");
        assertFieldIsRequired(participant, "nickname");

        participant = createParticipant();
        participant.setEmailAddress("");
        assertFieldIsRequired(participant, "emailAddress");
    }

    /** Test CancelHandler when the participant is new. */
    @Test public void testCancelHandlerNewParticipant() {
        EditParticipantTabPresenter presenter = createPresenter();
        CancelHandler handler = new CancelHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant1a = new Participant();
        participant1a.setId(1L);
        participant1a.setName("1a");

        Participant participant1b = new Participant();
        participant1b.setId(1L);
        participant1b.setName("1b");

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setName("start");
        exchangeAtStart.setId(1L);
        exchangeAtStart.getParticipants().add(participant1a);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setName("end");
        exchangeAtEnd.setId(1L);

        presenter.getManager().initialize(exchangeAtStart);
        presenter.getManager().getEditState().setName("end");

        presenter.setIsNew(true);
        presenter.setUndoState(participant1a);
        when(presenter.getView().getEditState()).thenReturn(participant1b);
        handler.handleEvent(null);  // actual event does not matter
        verify(presenter.getEventBus()).editCurrentExchange();
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test CancelHandler when the participant is not new. */
    @Test public void testCancelHandlerExistingParticipant() {
        EditParticipantTabPresenter presenter = createPresenter();
        CancelHandler handler = new CancelHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant1a = new Participant();
        participant1a.setId(1L);
        participant1a.setName("1a");

        Participant participant1b = new Participant();
        participant1b.setId(1L);
        participant1b.setName("1b");

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.setName("start");
        exchangeAtStart.getParticipants().add(participant1a);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("end");
        exchangeAtEnd.getParticipants().add(participant1a);

        presenter.getManager().initialize(exchangeAtStart);
        presenter.getManager().getEditState().setName("end");

        presenter.setIsNew(false);
        presenter.setUndoState(participant1a);
        when(presenter.getView().getEditState()).thenReturn(participant1b);
        handler.handleEvent(null);  // actual event does not matter
        verify(presenter.getEventBus()).editCurrentExchange();
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Create a valid participant for testing. */
    private static Participant createParticipant() {
        Participant participant = new Participant();
        participant.setId(1L);
        participant.setName("name");
        participant.setNickname("nickname");
        participant.setEmailAddress("email");
        return participant;
    }

    /** Create a presenter for testing. */
    private static EditParticipantTabPresenter createPresenter() {
        IEditParticipantTabView view = mock(IEditParticipantTabView.class);
        InternalEventBus eventBus = mock(InternalEventBus.class);
        ExchangeEditManager manager = new ExchangeEditManager();

        EditParticipantTabPresenter presenter = new EditParticipantTabPresenter();

        presenter.setEventBus(eventBus);
        assertSame(eventBus, presenter.getEventBus());

        presenter.setView(view);
        assertSame(view, presenter.getView());

        presenter.setManager(manager);
        assertSame(manager, presenter.getManager());

        return presenter;
    }

    /** Assert that a field is required. */
    private static void assertFieldIsRequired(Participant participant, String field) {
        try {
            SaveHandler.validateParticipant(participant);
            fail("Expected InvalidDataException for field [" + field + "]");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertOnlyMessage(e, REQUIRED, field);
        }
    }

}
