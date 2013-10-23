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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
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
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.rpc.util.RpcCallback;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.AddParticipantHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.DeleteParticipantHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.EditParticipantHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.GeneratePreviewCaller;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.PreviewHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.ResendNotificationCaller;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.ResendNotificationHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.ResetHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.RetrieveExchangeCaller;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.ReturnToListCaller;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.ReturnToListHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.SaveExchangeCaller;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.SaveHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.SendAllNotificationsHandler;
import com.cedarsolutions.santa.client.internal.presenter.EditExchangeTabPresenter.SendNotificationsCaller;
import com.cedarsolutions.santa.client.internal.view.IEditExchangeTabView;
import com.cedarsolutions.santa.client.internal.view.InternalConstants;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.client.rpc.IExchangeRpcAsync;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Unit tests for EditExchangeTabPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@SuppressWarnings("unchecked")
public class EditExchangeTabPresenterTest extends StubbedClientTestCase {

    /** Test onStart(). */
    @Test public void testOnStart() {
        EditExchangeTabPresenter presenter = createPresenter();
        presenter.onStart(); // just make sure it doesn't blow up
    }

    /** Test bind(). */
    @Test public void testBind() {
        EditExchangeTabPresenter presenter = createPresenter();
        presenter.bind();
        verify(presenter.getView()).setSaveHandler(any(SaveHandler.class));
        verify(presenter.getView()).setResetHandler(any(ResetHandler.class));
        verify(presenter.getView()).setReturnToListHandler(any(ReturnToListHandler.class));
        verify(presenter.getView()).setAddParticipantHandler(any(AddParticipantHandler.class));
        verify(presenter.getView()).setEditParticipantHandler(any(EditParticipantHandler.class));
        verify(presenter.getView()).setDeleteParticipantHandler(any(DeleteParticipantHandler.class));
        verify(presenter.getView()).setSendAllNotificationsHandler(any(SendAllNotificationsHandler.class));
        verify(presenter.getView()).setResendNotificationHandler(any(ResendNotificationHandler.class));
        verify(presenter.getView()).setPreviewHandler(any(PreviewHandler.class));
    }

    /** Test onShowEditExchangePage(). */
    @Test public void testOnShowEditExchangePage() {
        EditExchangeTabPresenter presenter = createPresenter();

        ArgumentCaptor<Long> exchangeId = ArgumentCaptor.forClass(Long.class);
        presenter.onShowEditExchangePage(12L);
        InOrder order = Mockito.inOrder(WidgetUtils.getInstance(), presenter.getExchangeRpc());
        order.verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        order.verify(presenter.getExchangeRpc()).retrieveExchange(exchangeId.capture(), any(RpcCallback.class));
        assertEquals(new Long(12), exchangeId.getValue());
    }

    /** Test onEditCurrentExchange(). */
    @Test public void testOnEditCurrentExchange() {
        EditExchangeTabPresenter presenter = createPresenter();

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        presenter.getManager().initialize(exchangeAtStart);

        presenter.onEditCurrentExchange();
        verify(presenter.getView()).setEditState(exchangeAtStart);
        verify(presenter.getEventBus()).selectEditExchangeTab();
    }

    /** Test SaveHandler. */
    @Test public void testSaveHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        SaveHandler handler = new SaveHandler(presenter);
        assertSame(presenter, handler.getParent());

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        ArgumentCaptor<Exchange> exchangeCaptor = ArgumentCaptor.forClass(Exchange.class);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(null); // actual event doesn't matter
        InOrder order = Mockito.inOrder(WidgetUtils.getInstance(), presenter.getExchangeRpc());
        order.verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        order.verify(presenter.getExchangeRpc()).saveExchange(exchangeCaptor.capture(), any(RpcCallback.class));
        assertEquals(exchangeAtEnd, exchangeCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test ResetHandler. */
    @Test public void testResetHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        ResetHandler handler = new ResetHandler(presenter);
        assertSame(presenter, handler.getParent());

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(null);  // actual event doesn't matter
        verify(presenter.getView()).setEditState(exchangeAtStart);
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test ReturnToListHandler. */
    @Test public void testReturnToListHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        ReturnToListHandler handler = new ReturnToListHandler(presenter);
        assertSame(presenter, handler.getParent());

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        ArgumentCaptor<Exchange> exchangeCaptor = ArgumentCaptor.forClass(Exchange.class);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(null); // actual event doesn't matter
        InOrder order = Mockito.inOrder(WidgetUtils.getInstance(), presenter.getExchangeRpc());
        order.verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        order.verify(presenter.getExchangeRpc()).saveExchange(exchangeCaptor.capture(), any(RpcCallback.class));
        assertEquals(exchangeAtEnd, exchangeCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test AddParticipantHandler. */
    @Test public void testAddParticipantHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        AddParticipantHandler handler = new AddParticipantHandler(presenter);
        assertSame(presenter, handler.getParent());

        InternalConstants constants = GWT.create(InternalConstants.class);
        Participant participant = new Participant(1L);  // because there are no participants in startEditState
        participant.setName(constants.editExchange_newParticipantName());

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.getParticipants().add(participant);

        presenter.getManager().initialize(exchangeAtStart);

        UnifiedEventWithContext<Participant> event = new UnifiedEventWithContext<Participant>((UnifiedEventType) null, participant);

        ArgumentCaptor<Participant> participantCaptor = ArgumentCaptor.forClass(Participant.class);
        ArgumentCaptor<Boolean> isNewCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<ParticipantSet> participantsCaptor = ArgumentCaptor.forClass(ParticipantSet.class);

        handler.handleEvent(event);
        verify(presenter.getEventBus()).showEditParticipantPage(participantCaptor.capture(), isNewCaptor.capture(), participantsCaptor.capture());
        assertEquals(participant, participantCaptor.getValue());
        assertEquals(true, isNewCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
        assertSame(presenter.getManager().getEditState().getParticipants(), participantsCaptor.getValue());
    }

    /** Test EditParticipantHandler. */
    @Test public void testEditParticipantHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        EditParticipantHandler handler = new EditParticipantHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant = new Participant(1L);  // because there are no participants in startEditState

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.getParticipants().add(participant);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");
        exchangeAtEnd.getParticipants().add(participant);

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");
        exchangeInView.getParticipants().add(participant);

        presenter.getManager().initialize(exchangeAtStart);

        UnifiedEventWithContext<Participant> event = new UnifiedEventWithContext<Participant>((UnifiedEventType) null, participant);

        ArgumentCaptor<Participant> participantCaptor = ArgumentCaptor.forClass(Participant.class);
        ArgumentCaptor<Boolean> isNewCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<ParticipantSet> participantsCaptor = ArgumentCaptor.forClass(ParticipantSet.class);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(event);
        verify(presenter.getEventBus()).showEditParticipantPage(participantCaptor.capture(), isNewCaptor.capture(), participantsCaptor.capture());
        assertEquals(participant, participantCaptor.getValue());
        assertEquals(false, isNewCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
        assertSame(presenter.getManager().getEditState().getParticipants(), participantsCaptor.getValue());
    }

    /** Test DeleteParticipantHandler. */
    @Test public void testDeleteParticipantHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        DeleteParticipantHandler handler = new DeleteParticipantHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant = new Participant();
        participant.setId(42L);

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.getParticipants().add(participant);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");
        exchangeInView.getParticipants().add(participant);

        presenter.getManager().initialize(exchangeAtStart);

        ParticipantSet selected = new ParticipantSet();
        selected.add(participant);
        when(presenter.getView().getSelectedParticipants()).thenReturn(selected);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(null);  // actual event doesn't matter
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test SendAllNotificationsHandler. */
    @Test public void testSendAllNotificationsHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        SendAllNotificationsHandler handler = new SendAllNotificationsHandler(presenter);
        assertSame(presenter, handler.getParent());

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        ArgumentCaptor<Exchange> exchangeCaptor = ArgumentCaptor.forClass(Exchange.class);

        UnifiedEvent event = mock(UnifiedEvent.class);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(event);
        InOrder order = Mockito.inOrder(WidgetUtils.getInstance(), presenter.getExchangeRpc());
        order.verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        order.verify(presenter.getExchangeRpc()).sendNotifications(exchangeCaptor.capture(), any(RpcCallback.class));
        assertEquals(exchangeAtEnd, exchangeCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test ResendNotificationHandler. */
    @Test public void testResendNotificationHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        ResendNotificationHandler handler = new ResendNotificationHandler(presenter);
        assertSame(presenter, handler.getParent());

        Participant participant = new Participant();
        participant.setId(42L);

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);
        exchangeAtStart.getParticipants().add(participant);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");
        exchangeAtStart.getParticipants().add(participant);

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");
        exchangeAtStart.getParticipants().add(participant);

        presenter.getManager().initialize(exchangeAtStart);

        ParticipantSet selected = new ParticipantSet();
        selected.add(participant);
        when(presenter.getView().getSelectedParticipants()).thenReturn(selected);

        ArgumentCaptor<Exchange> exchangeCaptor = ArgumentCaptor.forClass(Exchange.class);
        ArgumentCaptor<ParticipantSet> participantsCaptor = ArgumentCaptor.forClass(ParticipantSet.class);
        UnifiedEvent event = mock(UnifiedEvent.class);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(event);
        InOrder order = Mockito.inOrder(WidgetUtils.getInstance(), presenter.getExchangeRpc());
        order.verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        order.verify(presenter.getExchangeRpc()).resendNotification(exchangeCaptor.capture(), participantsCaptor.capture(), any(RpcCallback.class));
        assertEquals(exchangeAtEnd, exchangeCaptor.getValue());
        assertEquals(selected, participantsCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test PreviewHandler. */
    @Test public void testPreviewHandler() {
        EditExchangeTabPresenter presenter = createPresenter();
        PreviewHandler handler = new PreviewHandler(presenter);
        assertSame(presenter, handler.getParent());

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange exchangeInView = new Exchange();
        exchangeInView.setId(1L);
        exchangeInView.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        ArgumentCaptor<Exchange> exchangeCaptor = ArgumentCaptor.forClass(Exchange.class);

        when(presenter.getView().getEditState()).thenReturn(exchangeInView);
        handler.handleEvent(null); // actual event doesn't matter
        InOrder order = Mockito.inOrder(WidgetUtils.getInstance(), presenter.getExchangeRpc());
        order.verify(WidgetUtils.getInstance()).showPleaseWaitProgressIndicator();
        order.verify(presenter.getExchangeRpc()).generatePreview(exchangeCaptor.capture(), any(RpcCallback.class));
        assertEquals(exchangeAtEnd, exchangeCaptor.getValue());
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test RetrieveExchangeCaller. */
    @Test public void testRetrieveExchangeCaller() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        EditExchangeTabPresenter presenter = createPresenter();
        RetrieveExchangeCaller caller = new RetrieveExchangeCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("retrieveExchange", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        caller.setMethodArguments(15L);
        assertEquals(15L, caller.exchangeId);

        AsyncCallback<Exchange> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).retrieveExchange(15L, callback);

        ArgumentCaptor<ErrorDescription> error = ArgumentCaptor.forClass(ErrorDescription.class);
        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus(), WidgetUtils.getInstance());

        Exchange exchange = new Exchange();
        exchange.setId(1L);

        caller.onSuccessResult(exchange);
        order.verify(presenter.getEventBus()).editCurrentExchange();
        assertEquals(exchange, presenter.getManager().getUndoState());
        assertEquals(exchange, presenter.getManager().getEditState());

        caller.onSuccessResult(null);
        order.verify(presenter.getEventBus()).showExchangeListPage();
        order.verify(presenter.getEventBus()).showErrorPopup(error.capture());
        order.verify(presenter.getView(), never()).setEditState(exchange);
        order.verify(presenter.getEventBus(), never()).selectEditExchangeTab();
        assertEquals(constants.editExchange_exchangeNotFound(), error.getValue().getMessage());
        assertEquals(1, error.getValue().getSupportingTextItems().size());
        assertEquals(constants.editExchange_chooseAnotherExchange(), error.getValue().getSupportingTextItems().get(0));

        caller.onUnhandledError(new Exception());
        order.verify(WidgetUtils.getInstance()).showErrorPopup(any(ErrorDescription.class));
        order.verify(presenter.getEventBus()).showExchangeListPage();
    }

    /** Test SaveExchangeCaller. */
    @Test public void testSaveExchangeCaller() {
        EditExchangeTabPresenter presenter = createPresenter();
        SaveExchangeCaller caller = new SaveExchangeCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("saveExchange", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        Exchange exchange = new Exchange();
        caller.setMethodArguments(exchange);
        assertSame(exchange, caller.exchange);

        AsyncCallback<Exchange> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).saveExchange(exchange, callback);

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange result = new Exchange();
        result.setId(1L);
        result.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        caller.onSuccessResult(result);
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());

        InvalidDataException caught = new InvalidDataException();
        caller.onValidationError(caught);
        verify(presenter.getView()).showValidationError(caught);
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test SendNotificationsCaller. */
    @Test public void testSendNotificationsCaller() {
        EditExchangeTabPresenter presenter = createPresenter();
        SendNotificationsCaller caller = new SendNotificationsCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("sendNotifications", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertFalse(caller.isMarkedRetryable());

        Exchange exchange = new Exchange();
        caller.setMethodArguments(exchange);
        assertSame(exchange, caller.exchange);

        AsyncCallback<Exchange> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).sendNotifications(exchange, callback);

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange result = new Exchange();
        result.setId(1L);
        result.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        caller.onSuccessResult(result);
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        verify(presenter.getView()).showSendSuccessfulPopup();
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());

        InvalidDataException caught = new InvalidDataException();
        caller.onValidationError(caught);
        verify(presenter.getView()).showValidationError(caught);
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test ResendNotificationCaller. */
    @Test public void testResendNotificationCaller() {
        EditExchangeTabPresenter presenter = createPresenter();
        ResendNotificationCaller caller = new ResendNotificationCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("resendNotification", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertFalse(caller.isMarkedRetryable());

        Exchange exchange = new Exchange();
        ParticipantSet participants = new ParticipantSet();
        caller.setMethodArguments(exchange, participants);
        assertSame(exchange, caller.exchange);
        assertSame(participants, caller.participants);

        AsyncCallback<Exchange> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).resendNotification(exchange, participants, callback);

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange exchangeAtEnd = new Exchange();
        exchangeAtEnd.setId(1L);
        exchangeAtEnd.setName("name");

        Exchange result = new Exchange();
        result.setId(1L);
        result.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        caller.onSuccessResult(result);
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        verify(presenter.getView()).showSendSuccessfulPopup();
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());

        InvalidDataException caught = new InvalidDataException();
        caller.onValidationError(caught);
        verify(presenter.getView()).showValidationError(caught);
        verify(presenter.getView()).setEditState(exchangeAtEnd);
        assertEquals(exchangeAtStart, presenter.getManager().getUndoState());
        assertEquals(exchangeAtEnd, presenter.getManager().getEditState());
    }

    /** Test ReturnToListCaller. */
    @Test public void testReturnToListCaller() {
        EditExchangeTabPresenter presenter = createPresenter();
        ReturnToListCaller caller = new ReturnToListCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("saveExchange", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        Exchange exchange = new Exchange();
        caller.setMethodArguments(exchange);
        assertSame(exchange, caller.exchange);

        AsyncCallback<Exchange> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).saveExchange(exchange, callback);

        Exchange exchangeAtStart = new Exchange();
        exchangeAtStart.setId(1L);

        Exchange result = new Exchange();
        result.setId(1L);
        result.setName("name");

        presenter.getManager().initialize(exchangeAtStart);

        caller.onSuccessResult(result);
        verify(presenter.getView()).setEditState(null);
        verify(presenter.getEventBus()).showExchangeListPage();
        assertEquals(null, presenter.getManager().getUndoState());
        assertEquals(null, presenter.getManager().getEditState());

        InvalidDataException caught = new InvalidDataException();
        caller.onValidationError(caught);
        verify(presenter.getView()).showValidationError(caught);
    }

    /** Test GeneratePreviewCaller. */
    @Test public void testGeneratePreviewCaller() {
        EditExchangeTabPresenter presenter = createPresenter();
        GeneratePreviewCaller caller = new GeneratePreviewCaller(presenter);
        assertNotNull(caller);
        assertEquals("IExchangeRpc", caller.getRpc());
        assertEquals("generatePreview", caller.getMethod());
        assertSame(presenter, caller.parent);
        assertTrue(caller.isMarkedRetryable());

        Exchange exchange = new Exchange();
        caller.setMethodArguments(exchange);
        assertSame(exchange, caller.exchange);

        AsyncCallback<EmailMessage> callback = mock(AsyncCallback.class);
        caller.invoke(callback);
        verify(presenter.getExchangeRpc()).generatePreview(exchange, callback);

        EmailMessage email = mock(EmailMessage.class);
        caller.onSuccessResult(email);
        InOrder order = Mockito.inOrder(presenter.getView(), presenter.getEventBus());
        order.verify(presenter.getView()).showPreview(email);

        InvalidDataException caught = new InvalidDataException();
        caller.onValidationError(caught);
        verify(presenter.getView()).showValidationError(caught);
    }

    /** Create a presenter for testing. */
    private static EditExchangeTabPresenter createPresenter() {
        IEditExchangeTabView view = mock(IEditExchangeTabView.class);
        IExchangeRpcAsync exchangeRpc = mock(IExchangeRpcAsync.class);
        InternalEventBus eventBus = mock(InternalEventBus.class);
        ExchangeEditManager manager = new ExchangeEditManager();

        EditExchangeTabPresenter presenter = new EditExchangeTabPresenter();

        presenter.setEventBus(eventBus);
        assertSame(eventBus, presenter.getEventBus());

        presenter.setView(view);
        assertSame(view, presenter.getView());

        presenter.setExchangeRpc(exchangeRpc);
        assertSame(exchangeRpc, presenter.getExchangeRpc());

        presenter.setManager(manager);
        assertSame(manager, presenter.getManager());

        return presenter;
    }

}
