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

import java.util.ArrayList;
import java.util.List;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.UnifiedEventWithContext;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.module.presenter.ModulePagePresenter;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.internal.InternalEventBus;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView;
import com.cedarsolutions.santa.client.internal.view.IEditExchangeTabView;
import com.cedarsolutions.santa.client.internal.view.InternalConstants;
import com.cedarsolutions.santa.client.rpc.IExchangeRpcAsync;
import com.cedarsolutions.santa.client.rpc.util.StandardRpcCaller;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.ErrorDescription;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for edit exchange tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = EditExchangeTabView.class)
public class EditExchangeTabPresenter extends ModulePagePresenter<IEditExchangeTabView, InternalEventBus> {

    /** Exchange edit manager. */
    private ExchangeEditManager manager;

    /** Exchange RPC. */
    private IExchangeRpcAsync exchangeRpc;

    /** Handle the start event so that the view gets bound. */
    public void onStart() {
    }

    /** Bind the user interface. */
    @Override
    public void bind() {
        view.setEditState(null); // will be filled later when events are handled
        view.setSaveHandler(new SaveHandler(this));
        view.setResetHandler(new ResetHandler(this));
        view.setReturnToListHandler(new ReturnToListHandler(this));
        view.setAddParticipantHandler(new AddParticipantHandler(this));
        view.setEditParticipantHandler(new EditParticipantHandler(this));
        view.setDeleteParticipantHandler(new DeleteParticipantHandler(this));
        view.setSendAllNotificationsHandler(new SendAllNotificationsHandler(this));
        view.setResendNotificationHandler(new ResendNotificationHandler(this));
        view.setPreviewHandler(new PreviewHandler(this));
    }

    /**
     * Edit the exchange identified by the exchange id.
     * @param exchangeId  Id of the exchange to edit
     */
    public void onShowEditExchangePage(long exchangeId) {
        RetrieveExchangeCaller caller = new RetrieveExchangeCaller(this);
        caller.setMethodArguments(exchangeId);
        caller.invoke();
    }

    /** Edit the current exchange via the exchange edit manager. */
    public void onEditCurrentExchange() {
        this.getView().setEditState(this.getManager().getEditState());
        this.getEventBus().selectEditExchangeTab();
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

    /** Get the exchangeRpc. */
    public IExchangeRpcAsync getExchangeRpc() {
        return this.exchangeRpc;
    }

    /** Set the exchangeRpc. */
    @Inject
    public void setExchangeRpc(IExchangeRpcAsync exchangeRpc) {
        this.exchangeRpc = exchangeRpc;
    }

    /** Save handler. */
    protected static class SaveHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public SaveHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());
            SaveExchangeCaller caller = new SaveExchangeCaller(this.getParent());
            caller.setMethodArguments(this.getParent().getManager().getEditState());
            caller.invoke();
        }
    }

    /** Reset handler. */
    protected static class ResetHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public ResetHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().undo();
            this.getParent().getView().setEditState(this.getParent().getManager().getEditState());
        }
    }

    /** ReturnToList handler. */
    protected static class ReturnToListHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public ReturnToListHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());
            ReturnToListCaller caller = new ReturnToListCaller(this.getParent());
            caller.setMethodArguments(this.getParent().getManager().getEditState());
            caller.invoke();
        }
    }

    /** AddParticipant handler. */
    protected static class AddParticipantHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public AddParticipantHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());

            InternalConstants constants = GWT.create(InternalConstants.class);
            Participant participant = new Participant();
            participant.setId(this.getParent().getManager().getEditState().getNextParticipantId());
            participant.setName(constants.editExchange_newParticipantName());
            this.getParent().getManager().getEditState().getParticipants().add(participant);

            this.getParent().getEventBus().showEditParticipantPage(participant, true, this.getParent().getManager().getEditState().getParticipants());
        }
    }

    /** EditParticipant handler. */
    protected static class EditParticipantHandler extends AbstractViewEventHandlerWithContext<EditExchangeTabPresenter, Participant> {
        public EditParticipantHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEventWithContext<Participant> event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());
            Participant participant = event.getContext();
            ParticipantSet participants = this.getParent().getManager().getEditState().getParticipants();
            this.getParent().getEventBus().showEditParticipantPage(participant, false, participants);
        }
    }

    /** DeleteParticipant handler. */
    protected static class DeleteParticipantHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public DeleteParticipantHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());

            ParticipantSet selected = this.getParent().getView().getSelectedParticipants();
            if (!selected.isEmpty()) {
                for (Participant participant : selected) {
                    this.getParent().getManager().getEditState().removeParticipant(participant);
                }
            }

            this.getParent().getView().setEditState(this.getParent().getManager().getEditState());
        }
    }

    /** SendAllNotifications handler. */
    protected static class SendAllNotificationsHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public SendAllNotificationsHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());
            SendNotificationsCaller caller = new SendNotificationsCaller(this.getParent());
            caller.setMethodArguments(this.getParent().getManager().getEditState());
            caller.invoke();
        }
    }

    /** ResendNotification handler. */
    protected static class ResendNotificationHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public ResendNotificationHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());
            ParticipantSet selected = this.getParent().getView().getSelectedParticipants();
            if (!selected.isEmpty()) {
                ResendNotificationCaller caller = new ResendNotificationCaller(this.getParent());
                caller.setMethodArguments(this.getParent().getManager().getEditState(), selected);
                caller.invoke();
            }
        }
    }

    /** Preview handler. */
    protected static class PreviewHandler extends AbstractViewEventHandler<EditExchangeTabPresenter> {
        public PreviewHandler(EditExchangeTabPresenter parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            this.getParent().getManager().getEditState().refresh(this.getParent().getView().getEditState());
            GeneratePreviewCaller caller = new GeneratePreviewCaller(this.getParent());
            caller.setMethodArguments(this.getParent().getManager().getEditState());
            caller.invoke();
        }
    }

    /** Caller for IExchangeRpc.retrieveExchange(). */
    protected static class RetrieveExchangeCaller extends StandardRpcCaller<IExchangeRpcAsync, Exchange> {
        protected EditExchangeTabPresenter parent;
        protected long exchangeId;

        public RetrieveExchangeCaller(EditExchangeTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "retrieveExchange");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(long exchangeId) {
            this.exchangeId = exchangeId;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Exchange> callback) {
            async.retrieveExchange(this.exchangeId, callback);
        }

        @Override
        public void onUnhandledError(Throwable caught) {
            super.onUnhandledError(caught);
            this.parent.getEventBus().showExchangeListPage();
        }

        @Override
        public void onSuccessResult(Exchange result) {
            if (result == null) {
                this.parent.getManager().clear();
                this.parent.getEventBus().showExchangeListPage();
                this.parent.getEventBus().showErrorPopup(createNotFoundError());
            } else {
                this.parent.getManager().initialize(result);
                this.parent.getEventBus().editCurrentExchange();
            }
        }

        private static ErrorDescription createNotFoundError() {
            InternalConstants constants = GWT.create(InternalConstants.class);
            String message = constants.editExchange_exchangeNotFound();

            List<String> supporting = new ArrayList<String>();
            supporting.add(constants.editExchange_chooseAnotherExchange());

            return new ErrorDescription(message, supporting);
        }
    }

    /** Caller for IExchangeRpc.sendNotifications(). */
    protected static class SendNotificationsCaller extends StandardRpcCaller<IExchangeRpcAsync, Exchange> {
        protected EditExchangeTabPresenter parent;
        protected Exchange exchange;

        public SendNotificationsCaller(EditExchangeTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "sendNotifications");
            this.parent = parent;
            this.markNotRetryable();  // it's NOT safe to retry this RPC call
        }

        public void setMethodArguments(Exchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Exchange> callback) {
            async.sendNotifications(this.exchange, callback);
        }

        @Override
        public void onSuccessResult(Exchange result) {
            this.parent.getManager().getEditState().refresh(result);
            this.parent.getView().setEditState(this.parent.getManager().getEditState());
            this.parent.getView().showSendSuccessfulPopup();
        }

        @Override
        public boolean onValidationError(InvalidDataException caught) {
            this.parent.getView().showValidationError(caught);
            return true;
        }
    }

    /** Caller for IExchangeRpc.resendNotification(). */
    protected static class ResendNotificationCaller extends StandardRpcCaller<IExchangeRpcAsync, Exchange> {
        protected EditExchangeTabPresenter parent;
        protected Exchange exchange;
        protected ParticipantSet participants;

        public ResendNotificationCaller(EditExchangeTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "resendNotification");
            this.parent = parent;
            this.markNotRetryable();  // it's NOT safe to retry this RPC call
        }

        public void setMethodArguments(Exchange exchange, ParticipantSet participants) {
            this.exchange = exchange;
            this.participants = participants;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Exchange> callback) {
            async.resendNotification(this.exchange, this.participants, callback);
        }

        @Override
        public void onSuccessResult(Exchange result) {
            this.parent.getManager().getEditState().refresh(result);
            this.parent.getView().setEditState(this.parent.getManager().getEditState());
            this.parent.getView().showSendSuccessfulPopup();
        }

        @Override
        public boolean onValidationError(InvalidDataException caught) {
            this.parent.getView().showValidationError(caught);
            return true;
        }
    }

    /** Call for IExchangeRpc.saveExchange(), for return to list. */
    protected static class ReturnToListCaller extends StandardRpcCaller<IExchangeRpcAsync, Exchange> {
        protected EditExchangeTabPresenter parent;
        protected Exchange exchange;

        public ReturnToListCaller(EditExchangeTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "saveExchange");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(Exchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Exchange> callback) {
            async.saveExchange(this.exchange, callback);
        }

        @Override
        public void onSuccessResult(Exchange result) {
            this.parent.getView().setEditState(null);
            this.parent.getManager().clear();
            this.parent.getEventBus().showExchangeListPage();
        }

        @Override
        public boolean onValidationError(InvalidDataException caught) {
            this.parent.getView().showValidationError(caught);
            return true;
        }
    }

    /** Caller for IExchangeRpc.saveExchange(). */
    protected static class SaveExchangeCaller extends StandardRpcCaller<IExchangeRpcAsync, Exchange> {
        protected EditExchangeTabPresenter parent;
        protected Exchange exchange;

        public SaveExchangeCaller(EditExchangeTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "saveExchange");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(Exchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<Exchange> callback) {
            async.saveExchange(this.exchange, callback);
        }

        @Override
        public void onSuccessResult(Exchange result) {
            this.parent.getManager().getEditState().refresh(result);
            this.parent.getView().setEditState(this.parent.getManager().getEditState());
        }

        @Override
        public boolean onValidationError(InvalidDataException caught) {
            this.parent.getView().showValidationError(caught);
            return true;
        }
    }

    /** Caller for IExchangeRpc.generatePreview(). */
    protected static class GeneratePreviewCaller extends StandardRpcCaller<IExchangeRpcAsync, EmailMessage> {
        protected EditExchangeTabPresenter parent;
        protected Exchange exchange;

        public GeneratePreviewCaller(EditExchangeTabPresenter parent) {
            super(parent.getExchangeRpc(), "IExchangeRpc", "generatePreview");
            this.parent = parent;
            this.markRetryable();  // it's safe to retry this RPC call
        }

        public void setMethodArguments(Exchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public void invokeRpcMethod(IExchangeRpcAsync async, AsyncCallback<EmailMessage> callback) {
            async.generatePreview(this.exchange, callback);
        }

        @Override
        public void onSuccessResult(EmailMessage result) {
            this.parent.getView().showPreview(result);
        }

        @Override
        public boolean onValidationError(InvalidDataException caught) {
            this.parent.getView().showValidationError(caught);
            return true;
        }
    }
}
