/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2017 Kenneth J. Pronovici.
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

import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView;
import com.cedarsolutions.santa.client.internal.view.IEditParticipantTabView;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.shared.domain.ValidationErrors;
import com.cedarsolutions.util.gwt.GwtStringUtils;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for edit participant tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = EditParticipantTabView.class)
public class EditParticipantTabPresenter extends ModulePagePresenter<IEditParticipantTabView, InternalEventBus> {

    /** Exchange edit manager. */
    private ExchangeEditManager manager;

    /** Save off the exchange that is being edited, for undo purposes. */
    private Participant undoState;

    /** Whether this is a new participant. */
    private boolean isNew;

    /** Handle the start event so that the view gets bound. */
    public void onStart() {
    }

    /** Bind the user interface. */
    @Override
    public void bind() {
        view.setEditState(null, null, null, null); // will be filled later when events are handled
        view.setSaveHandler(new SaveHandler(this));
        view.setCancelHandler(new CancelHandler(this));
    }

    /**
     * Show the edit participant page.
     * @param participant   Participant to edit, already found the current exchange
     * @param isNew         Whether this is a new participant
     * @param participants  List of all participants in this exchange
     */
    public void onShowEditParticipantPage(Participant participant, boolean isNew, ParticipantSet participants) {
        this.setUndoState(participant);
        this.setIsNew(isNew);

        Participant giftGiver = null;
        Participant giftReceiver = null;
        Exchange editState = this.getManager().getEditState();
        if (editState.getAssignments() != null) {
            giftGiver = editState.getAssignments().getGiftGiver(participant);
            giftReceiver = editState.getAssignments().getGiftReceiver(participant);
        }

        this.getView().setEditState(participant, giftGiver, giftReceiver, participants);
        this.getEventBus().selectEditParticipantTab();
    }

    /** Get the undo state. */
    public Participant getUndoState() {
        return new Participant(this.undoState);
    }

    /** Set the undo state. */
    public void setUndoState(Participant undoState) {
        this.undoState = new Participant(undoState);
    }

    /** Get the isNew flag. */
    public boolean getIsNew() {
        return this.isNew;
    }

    /** Set the isNew flag. */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
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

    /** Save handler. */
    protected static class SaveHandler extends AbstractViewEventHandler<EditParticipantTabPresenter> {
        public SaveHandler(EditParticipantTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            try {
                Participant participant = this.getParent().getView().getEditState();
                validateParticipant(participant);

                if (!this.getParent().getManager().getEditState().replaceParticipant(participant)) {
                    throw new CedarRuntimeException("Did not find participant to replace in exchange");
                }

                this.getParent().getEventBus().editCurrentExchange();
            } catch (InvalidDataException e) {
                this.getParent().getView().showValidationError(e);
            } catch (Exception e) {
                ErrorDescription error = new ErrorDescription("Internal error saving participant: " + e.getMessage(), e);
                this.getParent().getEventBus().showErrorPopup(error);
            }
        }

        /**
         * Validate that participant fields are filled in properly.
         *
         * <p>
         * This is also done by the back-end on save.  However, we want to give
         * them some advance warning, because the back-end error will be more
         * general, like "all participants must have a nickname".  It's a good
         * thing to catch the general problem at the exchange level, but it's
         * better to give specific warnings as soon as possible.
         * </p>
         *
         * @param participant  Particpant to validate
         *
         * @throws InvalidDataException If the data does not pass validation
         * @throws CedarRuntimeException If the participant is null or somehow bogus
         */
        protected static void validateParticipant(Participant participant) {
            ValidationErrors details = new ValidationErrors(INVALID, "Participant is invalid");
            InvalidDataException exception = new InvalidDataException("Participant is invalid", details);

            if (participant == null || participant.getId() == null) {
                throw new CedarRuntimeException("Participant is empty or not set up properly.");
            }

            if (GwtStringUtils.isEmpty(participant.getName())) {
                details.addMessage(REQUIRED, "name", "Name is required");
            }

            if (GwtStringUtils.isEmpty(participant.getNickname())) {
                details.addMessage(REQUIRED, "nickname", "Nickname is required");
            }

            if (GwtStringUtils.isEmpty(participant.getEmailAddress())) {
                details.addMessage(REQUIRED, "emailAddress", "Email address is required");
            }

            if (!details.getMessages().isEmpty()) {
                throw exception;
            }
        }
    }

    /** Cancel handler. */
    protected static class CancelHandler extends AbstractViewEventHandler<EditParticipantTabPresenter> {
        public CancelHandler(EditParticipantTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            if (this.getParent().getIsNew()) {
                // If the participant is new, then it should be removed on cancel
                this.getParent().getManager().getEditState().removeParticipant(this.getParent().getUndoState());
            }

            // Note: no need to fall back to undo state, because the parent only
            // gets updated when the save event is handled.

            this.getParent().getEventBus().editCurrentExchange();
        }
    }

}
