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

import static com.cedarsolutions.santa.client.SantaExchangeEventTypes.INTERNAL_EDIT_EXCHANGE;
import static com.cedarsolutions.santa.client.SantaExchangeEventTypes.INTERNAL_EDIT_PARTICIPANT;

import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.santa.client.internal.presenter.ExchangeEditManager;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter for the logged in module.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@History(type = HistoryConverterType.DEFAULT)
public class InternalHistoryConverter implements HistoryConverter<InternalEventBus> {

    /** Exchange edit manager. */
    private ExchangeEditManager manager;

    /** Get the history token for showInternalLandingPage(), always empty. */
    public String onShowInternalLandingPage() {
        return "";  // nothing needs to be added to the token
    }

    /** Get the history token for showExchangeListPage(), always empty. */
    public String onShowExchangeListPage() {
        return "";  // nothing needs to be added to the token
    }

    /** Get the history token for showEditExchangePage(), the exchange id. */
    public String onShowEditExchangePage(long exchangeId) {
        return String.valueOf(exchangeId);
    }

    /** Get the history token for showEditParticipantPage(), the exchange id and participant id. */
    public String onShowEditParticipantPage(Participant participant, boolean isNew, ParticipantSet others) {
        String exchangeId = String.valueOf(this.getManager().getEditState().getId());
        String participantId = String.valueOf(participant.getId());
        return exchangeId + "-" + participantId;
    }

    /** This portion of the site cannot be crawled. */
    @Override
    public boolean isCrawlable() {
        return false;
    }

    /** Convert an event type and token into an event on the event bus. */
    @Override
    public void convertFromToken(String eventType, String param, InternalEventBus eventBus) {
        try {
            boolean dispatched = dispatchEditExchange(eventType, param, eventBus);
            if (!dispatched) {
                eventBus.dispatch(eventType);
            }
        } catch (Exception e) {
            eventBus.clearHistory();
            eventBus.showLandingPage();
            eventBus.showBookmarkNotFoundError();
        }
    }

    /**
     * Dispatch history tokens related to the edit exchange feature.
     *
     * <p>
     * The goal here is to do the "safest" thing we can with a history token, so
     * that the user doesn't get themselves into unexpected trouble by moving
     * backwards and forwards using the browser buttons.  If at all possible,
     * we should rely on the state maintained by the exchange edit manager.
     * We only want to reload data from the back-end if we have to, since that
     * action could cause the user to lose data that they're currently editing
     * but have not yet saved.
     * </p>
     *
     * @return True if the passed-in event was dispatched, false otherwise.
     */
    protected boolean dispatchEditExchange(String eventType, String param, InternalEventBus eventBus) {
        if (INTERNAL_EDIT_EXCHANGE.equals(eventType)) {
            return dispatchInternalEditExchange(eventType, param, eventBus);
        } else if (INTERNAL_EDIT_PARTICIPANT.equals(eventType)) {
            return dispatchInternalEditParticipant(eventType, param, eventBus);
        } else {
            return false; // no, this event was not dispatched
        }
    }

    /** Dispatch history tokens for INTERNAL_EDIT_EXCHANGE. */
    private boolean dispatchInternalEditExchange(String eventType, String param, InternalEventBus eventBus) {
        if (!GwtStringUtils.isEmpty(param) && param.matches("^[0-9]+$")) {
            long exchangeId = Long.valueOf(param);
            if (this.getManager().isActive()) {
                if (this.getManager().getEditState().getId().equals(exchangeId)) {
                    eventBus.editCurrentExchange();
                    return true;  // yes, we dispatched this event
                } else {
                    this.getManager().clear();
                    eventBus.showEditExchangePage(exchangeId);
                    return true;  // yes, we dispatched this event
                }
            } else {
                eventBus.showEditExchangePage(exchangeId);
                return true;  // yes, we dispatched this event
            }
        } else {
            GWT.log("For event " + eventType + ", parameter is invalid: " + param);
            throw new CedarRuntimeException("For event " + eventType + ", parameter is invalid: " + param);
        }
    }

    /** Dispatch history tokens for INTERNAL_EDIT_PARTICIPANT. */
    private boolean dispatchInternalEditParticipant(String eventType, String param, InternalEventBus eventBus) {
        if (!GwtStringUtils.isEmpty(param) && param.matches("^[0-9]+[-][0-9]+$")) {
            String[] values = param.split("-");
            long exchangeId = Long.valueOf(values[0]);
            long participantId = Long.valueOf(values[1]);
            if (this.getManager().isActive()) {
                if (this.getManager().getEditState().getId().equals(exchangeId)) {
                    Participant participant = this.getManager().getEditState().getParticipantById(participantId);
                    if (participant != null) {
                        eventBus.showEditParticipantPage(participant, false, this.getManager().getEditState().getParticipants());
                        return true;  // yes, we dispatched this event
                    } else {
                        eventBus.editCurrentExchange();
                        return true;  // yes, we dispatched this event
                    }
                } else {
                    this.getManager().clear();
                    eventBus.showEditExchangePage(exchangeId);
                    return true;  // yes, we dispatched this event
                }
            } else {
                eventBus.showEditExchangePage(exchangeId);
                return true;  // yes, we dispatched this event
            }
        } else {
            GWT.log("For event " + eventType + ", parameter is invalid: " + param);
            throw new CedarRuntimeException("For event " + eventType + ", parameter is invalid: " + param);
        }
    }

    /** Get the exchange edit manager. */
    public ExchangeEditManager getManager() {
        return this.manager;
    }

    /** Set the exchange edit manager. */
    @Inject
    public void setManager(ExchangeEditManager manager) {
        this.manager = manager;
    }
}
