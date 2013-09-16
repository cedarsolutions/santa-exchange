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
package com.cedarsolutions.santa.client.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.cedarsolutions.santa.client.SantaExchangeEventTypes;
import com.cedarsolutions.santa.client.common.widget.WidgetUtils;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeEditManager;
import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;

/**
 * Unit tests for InternalHistoryConverter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class InternalHistoryConverterTest extends StubbedClientTestCase {

    /** Test isCrawlable(). */
    @Test public void testIsCrawlable() {
        InternalHistoryConverter converter = new InternalHistoryConverter();
        assertFalse(converter.isCrawlable());
    }

    /** Test the various event-specific handlers. */
    @Test public void testEventSpecificHandlers() {
        ExchangeEditManager manager = mock(ExchangeEditManager.class, Mockito.RETURNS_DEEP_STUBS);
        InternalHistoryConverter converter = new InternalHistoryConverter();
        converter.setManager(manager);

        Exchange exchange = new Exchange();
        exchange.setId(12L);

        ParticipantSet others = new ParticipantSet();

        assertEquals("", converter.onShowInternalLandingPage());
        assertEquals("", converter.onShowExchangeListPage());
        assertEquals("14", converter.onShowEditExchangePage(14L));

        when(manager.getEditState().getId()).thenReturn(3L);
        when(manager.getEditState().getParticipants()).thenReturn(others);
        Participant participant = new Participant(26L);
        assertEquals("3-26", converter.onShowEditParticipantPage(participant, false, others));
    }

    /** Test convertFromToken() for an arbitrary token. */
    @Test public void testConvertFromTokenArbitrary() {
        InternalHistoryConverter converter = new InternalHistoryConverter();
        String eventType = "type";
        String param = "param";
        InternalEventBus eventBus = mock(InternalEventBus.class);
        converter.convertFromToken(eventType, param, eventBus);
        verify(eventBus).dispatch(eventType);
    }

    /** Test convertFromToken() for INTERNAL_EDIT_EXCHANGE. */
    @Test public void testConvertFromTokenEditExchange() {
        ExchangeEditManager manager = mock(ExchangeEditManager.class, Mockito.RETURNS_DEEP_STUBS);
        InternalEventBus eventBus = mock(InternalEventBus.class);

        InternalHistoryConverter converter = new InternalHistoryConverter();
        converter.setManager(manager);

        InOrder order = Mockito.inOrder(eventBus, WidgetUtils.getInstance());

        when(manager.isActive()).thenReturn(false);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE, "8", eventBus);
        order.verify(eventBus).showEditExchangePage(8L);

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(8L);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE, "8", eventBus);
        order.verify(eventBus).editCurrentExchange();

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(7L);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE, "8", eventBus);
        order.verify(eventBus).showEditExchangePage(8L);

        when(manager.isActive()).thenReturn(false);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE, "bogus", eventBus);
        order.verify(eventBus).clearHistory();
        order.verify(eventBus).showLandingPage();
        order.verify(eventBus).showBookmarkNotFoundError();

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(7L);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE, "bogus", eventBus);
        order.verify(eventBus).clearHistory();
        order.verify(eventBus).showLandingPage();
        order.verify(eventBus).showBookmarkNotFoundError();
    }

    /** Test convertFromToken() for INTERNAL_EDIT_PARTICIPANT. */
    @Test public void testConvertFromTokenEditParticipant() {
        ExchangeEditManager manager = mock(ExchangeEditManager.class, Mockito.RETURNS_DEEP_STUBS);
        InternalEventBus eventBus = mock(InternalEventBus.class);

        ParticipantSet participants = new ParticipantSet();
        Participant participant = new Participant(26L);

        InternalHistoryConverter converter = new InternalHistoryConverter();
        converter.setManager(manager);

        InOrder order = Mockito.inOrder(eventBus, WidgetUtils.getInstance());

        when(manager.isActive()).thenReturn(false);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "8-3", eventBus);
        order.verify(eventBus).showEditExchangePage(8L);

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(9L);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "8-3", eventBus);
        order.verify(eventBus).showEditExchangePage(8L);

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(8L);
        when(manager.getEditState().getParticipantById(3L)).thenReturn(null);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "8-3", eventBus);
        order.verify(eventBus).editCurrentExchange();

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(8L);
        when(manager.getEditState().getParticipantById(3L)).thenReturn(participant);
        when(manager.getEditState().getParticipants()).thenReturn(participants);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "8-3", eventBus);
        order.verify(eventBus).showEditParticipantPage(participant, false, participants);

        when(manager.isActive()).thenReturn(false);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "bogus", eventBus);
        order.verify(eventBus).clearHistory();
        order.verify(eventBus).showLandingPage();
        order.verify(eventBus).showBookmarkNotFoundError();

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(9L);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "bogus", eventBus);
        order.verify(eventBus).clearHistory();
        order.verify(eventBus).showLandingPage();
        order.verify(eventBus).showBookmarkNotFoundError();

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(8L);
        when(manager.getEditState().getParticipantById(3L)).thenReturn(null);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "bogus", eventBus);
        order.verify(eventBus).clearHistory();
        order.verify(eventBus).showLandingPage();
        order.verify(eventBus).showBookmarkNotFoundError();

        when(manager.isActive()).thenReturn(true);
        when(manager.getEditState().getId()).thenReturn(8L);
        when(manager.getEditState().getParticipantById(3L)).thenReturn(participant);
        converter.convertFromToken(SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT, "bogus", eventBus);
        order.verify(eventBus).clearHistory();
        order.verify(eventBus).showLandingPage();
        order.verify(eventBus).showBookmarkNotFoundError();
    }
}
