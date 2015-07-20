/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2015 Kenneth J. Pronovici.
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



import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.client.gwt.event.UnifiedEvent;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.handler.AbstractClickHandler;
import com.cedarsolutions.client.gwt.handler.AbstractRowClickViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventClickHandler;
import com.cedarsolutions.client.gwt.handler.AbstractViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.ModuleTabView;
import com.cedarsolutions.client.gwt.validation.IValidationErrorWidget;
import com.cedarsolutions.client.gwt.widget.table.DataTable;
import com.cedarsolutions.client.gwt.widget.table.DataTablePager;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.santa.client.common.widget.ConfirmationPopup;
import com.cedarsolutions.santa.client.common.widget.InformationalPopup;
import com.cedarsolutions.santa.client.common.widget.ValidationErrorWidget;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantKeyProvider;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.cedarsolutions.util.gwt.GwtLocalizationUtils;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.inject.Singleton;
import com.gwtplatform.idhandler.client.ElementIdHandler;
import com.gwtplatform.idhandler.client.WithElementId;

/**
 * Exchange list tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Singleton
public class EditExchangeTabView extends ModuleTabView implements IEditExchangeTabView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, EditExchangeTabView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<EditExchangeTabView> {
    }

    // User interface fields fron the UI binder
    @UiField @WithElementId protected Button saveButton;
    @UiField @WithElementId protected Button resetButton;
    @UiField @WithElementId protected Button returnToListButton;
    @UiField @WithElementId protected Button addParticipantButton;
    @UiField @WithElementId protected Button deleteParticipantButton;
    @UiField @WithElementId protected Button sendAllNotificationsButton;
    @UiField @WithElementId protected Button resendNotificationButton;
    @UiField @WithElementId protected Button previewButton;
    @UiField protected ValidationErrorWidget validationErrorWidget;
    @UiField @WithElementId protected Label stateTitleLabel;
    @UiField @WithElementId protected Label stateDisplayLabel;
    @UiField @WithElementId protected Label exchangeNameLabel;
    @UiField @WithElementId protected TextBox exchangeNameInput;
    @UiField @WithElementId protected Label dateTimeLabel;
    @UiField @WithElementId protected TextBox dateTimeInput;
    @UiField @WithElementId protected Label themeLabel;
    @UiField @WithElementId protected TextBox themeInput;
    @UiField @WithElementId protected Label costLabel;
    @UiField @WithElementId protected TextBox costInput;
    @UiField @WithElementId protected Label organizerNameLabel;
    @UiField @WithElementId protected TextBox organizerNameInput;
    @UiField @WithElementId protected Label organizerEmailLabel;
    @UiField @WithElementId protected TextBox organizerEmailInput;
    @UiField @WithElementId protected Label organizerPhoneLabel;
    @UiField @WithElementId protected TextBox organizerPhoneInput;
    @UiField @WithElementId protected Label extraInfoLabel;
    @UiField @WithElementId protected TextArea extraInfoInput;
    @UiField @WithElementId protected Label optionalLabel;
    @UiField @WithElementId protected Label instructionsLabel;
    @UiField(provided = true) @WithElementId protected DataTable<Participant> table;
    @UiField(provided = true) @WithElementId protected DataTablePager pager;

    // Other instance variables
    protected Exchange editState; // saves the current edit state for values that are not edited
    protected PreviewPopup previewPopup;
    protected ConfirmationPopup resetConfirmPopup;
    protected ConfirmationPopup sendConfirmPopup;
    protected ConfirmationPopup resendConfirmPopup;
    protected InformationalPopup sendSuccessfulPopup;
    private ViewEventHandler saveHandler;
    private ViewEventHandler resetEventHandler;
    private ViewEventHandler returnToListHandler;
    private ViewEventHandler addParticipantHandler;
    private ViewEventHandlerWithContext<Participant> editParticipantHandler;
    private ViewEventHandler deleteParticipantHandler;
    private ViewEventHandler sendAllNotificationsHandler;
    private ViewEventHandler resendNotificationHandler;
    private ViewEventHandler previewHandler;
    private ListDataProvider<Participant> dataProvider = new ListDataProvider<Participant>();

    /** Create the view. */
    public EditExchangeTabView() {
        this.createProvidedWidgets();  // do this before binder, otherwise we get NPE
        this.initWidget(BINDER.createAndBindUi(this));
        ID_HANDLER.generateAndSetIds(this);
        this.setupBinderWidgets();
    }

    /** Get the validation error widget. */
    @Override
    public IValidationErrorWidget getValidationErrorWidget() {
        return this.validationErrorWidget;
    }

    /** Translate a localizable message using this view's resources. */
    @Override
    public String translate(LocalizableMessage message) {
        InternalMessageConstants constants = GWT.create(InternalMessageConstants.class);
        return GwtLocalizationUtils.translate(message, constants, "editExchange");
    }

    /** Get a mapping from field name to input element. */
    @Override
    public Map<String, UIObject> getValidatedFieldMap() {
        Map<String, UIObject> map = new HashMap<String, UIObject>();
        map.put("name", this.exchangeNameInput);
        map.put("dateAndTime", this.dateTimeInput);
        map.put("theme", this.themeInput);
        map.put("cost", this.costInput);
        map.put("organizerName", this.organizerNameInput);
        map.put("organizerEmailAddress", this.organizerEmailInput);
        map.put("organizerPhoneNumber", this.organizerPhoneInput);
        map.put("extraInfo", this.extraInfoInput);
        return map;
    }

    /** Show a validation error. */
    @Override
    public void showValidationError(InvalidDataException error) {
        ViewUtils.getInstance().showValidationError(this, error);
    }

    /** Set the save handler. */
    @Override
    public void setSaveHandler(ViewEventHandler saveHandler) {
        this.saveHandler = saveHandler;
    }

    /** Get the save handler. */
    @Override
    public ViewEventHandler getSaveHandler() {
        return this.saveHandler;
    }

    /** Set the reset handler. */
    @Override
    public void setResetHandler(ViewEventHandler resetEventHandler) {
        this.resetEventHandler = resetEventHandler;
    }

    /** Get the reset handler. */
    @Override
    public ViewEventHandler getResetHandler() {
        return this.resetEventHandler;
    }

    /** Set the return to list handler. */
    @Override
    public void setReturnToListHandler(ViewEventHandler returnToListHandler) {
        this.returnToListHandler = returnToListHandler;
    }

    /** Get the return to list handler. */
    @Override
    public ViewEventHandler getReturnToListHandler() {
        return this.returnToListHandler;
    }

    /** Set the add participant handler. */
    @Override
    public void setAddParticipantHandler(ViewEventHandler addParticipantHandler) {
        this.addParticipantHandler = addParticipantHandler;
    }

    /** Get the add participant handler. */
    @Override
    public ViewEventHandler getAddParticipantHandler() {
        return this.addParticipantHandler;
    }

    /** Set the edit participant handler. */
    @Override
    public void setEditParticipantHandler(ViewEventHandlerWithContext<Participant> editParticipantHandler) {
        this.editParticipantHandler = editParticipantHandler;
    }

    /** Get the edit participant handler. */
    @Override
    public ViewEventHandlerWithContext<Participant> getEditParticipantHandler() {
        return this.editParticipantHandler;
    }

    /** Set the delete participant handler. */
    @Override
    public void setDeleteParticipantHandler(ViewEventHandler deleteParticipantHandler) {
        this.deleteParticipantHandler = deleteParticipantHandler;
    }

    /** Get the delete participant handler. */
    @Override
    public ViewEventHandler getDeleteParticipantHandler() {
        return this.deleteParticipantHandler;
    }

    /** Set the send all notifications handler. */
    @Override
    public void setSendAllNotificationsHandler(ViewEventHandler sendAllNotificationsHandler) {
        this.sendAllNotificationsHandler = sendAllNotificationsHandler;
    }

    /** Get the send all notifications handler. */
    @Override
    public ViewEventHandler getSendAllNotificationsHandler() {
        return this.sendAllNotificationsHandler;
    }

    /** Set the resend notification handler. */
    @Override
    public void setResendNotificationHandler(ViewEventHandler resendNotificationHandler) {
        this.resendNotificationHandler = resendNotificationHandler;
    }

    /** Get the resend notification handler. */
    @Override
    public ViewEventHandler getResendNotificationHandler() {
        return this.resendNotificationHandler;
    }

    /** Set the preview handler. */
    @Override
    public void setPreviewHandler(ViewEventHandler previewHandler) {
        this.previewHandler = previewHandler;
    }

    /** Get the preview handler. */
    @Override
    public ViewEventHandler getPreviewHandler() {
        return this.previewHandler;
    }

    /** Get the selected participants. */
    @Override
    public ParticipantSet getSelectedParticipants() {
        return new ParticipantSet(this.table.getSelectedRecords());
    }

    /** Get the configured page size. */
    protected int getPageSize() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        return constants.editExchange_pageSize();
    }

    /** Show a preview. */
    @Override
    public void showPreview(EmailMessage preview) {
        this.previewPopup.showPopup(preview);
    }

    /** Set the exchange to be edited. */
    @Override
    public void setEditState(Exchange exchange) {
        this.editState = new Exchange(exchange);

        this.dataProvider.setList(this.editState.getParticipants());
        this.table.setVisibleRangeAndClearData(new Range(0, this.getPageSize()), true);
        this.table.selectNone();

        this.stateDisplayLabel.setText(InternalUtils.getExchangeStateForDisplay(this.editState.getExchangeState()));
        this.exchangeNameInput.setText(this.editState.getName());
        this.dateTimeInput.setText(this.editState.getDateAndTime());
        this.themeInput.setText(this.editState.getTheme());
        this.costInput.setText(this.editState.getCost());
        this.extraInfoInput.setText(this.editState.getExtraInfo());
        this.organizerNameInput.setText(this.editState.getOrganizer().getName());
        this.organizerEmailInput.setText(this.editState.getOrganizer().getEmailAddress());
        this.organizerPhoneInput.setText(this.editState.getOrganizer().getPhoneNumber());

        ViewUtils.getInstance().clearValidationErrors(this);
    }

    /** Get the exchange that is being edited, in its current state. */
    @Override
    public Exchange getEditState() {
        Exchange exchange = new Exchange(this.editState);

        exchange.setName(this.exchangeNameInput.getText());
        exchange.setDateAndTime(this.dateTimeInput.getText());
        exchange.setTheme(this.themeInput.getText());
        exchange.setCost(this.costInput.getText());
        exchange.setExtraInfo(this.extraInfoInput.getText());
        exchange.getOrganizer().setName(organizerNameInput.getText());
        exchange.getOrganizer().setEmailAddress(organizerEmailInput.getText());
        exchange.getOrganizer().setPhoneNumber(organizerPhoneInput.getText());

        return exchange;
    }

    /** Show the reset confirm popup. */
    protected void showResetConfirmPopup() {
        this.resetConfirmPopup.showPopup();
    }

    /** Show the send confirm popup. */
    protected void showSendConfirmPopup() {
        this.sendConfirmPopup.showPopup();
    }

    /** Show the resend confirm popup. */
    protected void showResendConfirmPopup() {
        this.resendConfirmPopup.showPopup();
    }

    /** Show the "send was successful" popup. */
    @Override
    public void showSendSuccessfulPopup() {
        this.sendSuccessfulPopup.showPopup();
    }

    /** Create and set up the provided widgets that aren't controlled by the UI binder. */
    private void createProvidedWidgets() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        this.previewPopup = new PreviewPopup();

        this.resetConfirmPopup = new ConfirmationPopup(constants.editExchange_resetConfirmTitle(),
                                                       constants.editExchange_resetConfirmMessage());
        this.resetConfirmPopup.setOkButtonHandler(new ResetConfirmHandler(this));

        this.sendConfirmPopup = new ConfirmationPopup(constants.editExchange_sendConfirmTitle(),
                                                      constants.editExchange_sendConfirmMessage());
        this.sendConfirmPopup.setOkButtonHandler(new SendAllNotificationsConfirmHandler(this));

        this.resendConfirmPopup = new ConfirmationPopup(constants.editExchange_resendConfirmTitle(),
                                                        constants.editExchange_resendConfirmMessage());
        this.resendConfirmPopup.setOkButtonHandler(new SendAllNotificationsConfirmHandler(this));

        this.sendSuccessfulPopup = new InformationalPopup(constants.editExchange_sendSuccessfulTitle(),
                                                          constants.editExchange_sendSuccessfulMessage());

        this.setupTable();
    }

    /** Set up the widgets that are controlled by the UI binder. */
    private void setupBinderWidgets() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        this.stateTitleLabel.setText(constants.editExchange_stateLabel());

        this.exchangeNameLabel.setText(constants.editExchange_exchangeNameLabel());
        this.exchangeNameLabel.setTitle(constants.editExchange_exchangeNameTooltip());
        this.exchangeNameInput.setTitle(constants.editExchange_exchangeNameTooltip());

        this.dateTimeLabel.setText(constants.editExchange_dateTimeLabel());
        this.dateTimeLabel.setTitle(constants.editExchange_dateTimeTooltip());
        this.dateTimeInput.setTitle(constants.editExchange_dateTimeTooltip());

        this.themeLabel.setText(constants.editExchange_themeLabel());
        this.themeLabel.setTitle(constants.editExchange_themeTooltip());
        this.themeInput.setTitle(constants.editExchange_themeTooltip());

        this.costLabel.setText(constants.editExchange_costLabel());
        this.costLabel.setTitle(constants.editExchange_costTooltip());
        this.costInput.setTitle(constants.editExchange_costTooltip());

        this.organizerNameLabel.setText(constants.editExchange_organizerNameLabel());
        this.organizerNameLabel.setTitle(constants.editExchange_organizerNameTooltip());
        this.organizerNameInput.setTitle(constants.editExchange_organizerNameTooltip());

        this.organizerEmailLabel.setText(constants.editExchange_organizerEmailLabel());
        this.organizerEmailLabel.setTitle(constants.editExchange_organizerEmailTooltip());
        this.organizerEmailInput.setTitle(constants.editExchange_organizerEmailTooltip());

        this.organizerPhoneLabel.setText(constants.editExchange_organizerPhoneLabel());
        this.organizerPhoneLabel.setTitle(constants.editExchange_organizerPhoneTooltip());
        this.organizerPhoneInput.setTitle(constants.editExchange_organizerPhoneTooltip());

        this.extraInfoLabel.setText(constants.editExchange_extraInfoLabel());
        this.extraInfoLabel.setTitle(constants.editExchange_extraInfoTooltip());
        this.extraInfoInput.setTitle(constants.editExchange_extraInfoTooltip());

        this.optionalLabel.setText(constants.editExchange_optional());

        this.instructionsLabel.setText(constants.editExchange_instructions());

        this.saveButton.setText(constants.editExchange_saveButton());
        this.saveButton.setTitle(constants.editExchange_saveTooltip());
        this.saveButton.addClickHandler(new SaveClickHandler(this));

        this.resetButton.setText(constants.editExchange_resetButton());
        this.resetButton.setTitle(constants.editExchange_resetTooltip());
        this.resetButton.addClickHandler(new ResetClickHandler(this));

        this.returnToListButton.setText(constants.editExchange_returnToListButton());
        this.returnToListButton.setTitle(constants.editExchange_returnToListTooltip());
        this.returnToListButton.addClickHandler(new ReturnToListClickHandler(this));

        this.addParticipantButton.setText(constants.editExchange_addParticipantButton());
        this.addParticipantButton.setTitle(constants.editExchange_addParticipantTooltip());
        this.addParticipantButton.addClickHandler(new AddParticipantClickHandler(this));

        this.deleteParticipantButton.setText(constants.editExchange_deleteParticipantButton());
        this.deleteParticipantButton.setTitle(constants.editExchange_deleteParticipantTooltip());
        this.deleteParticipantButton.addClickHandler(new DeleteParticipantClickHandler(this));

        this.sendAllNotificationsButton.setText(constants.editExchange_sendAllNotificationsButton());
        this.sendAllNotificationsButton.setTitle(constants.editExchange_sendAllNotificationsTooltip());
        this.sendAllNotificationsButton.addClickHandler(new SendAllNotificationsClickHandler(this));

        this.resendNotificationButton.setText(constants.editExchange_resendNotificationButton());
        this.resendNotificationButton.setTitle(constants.editExchange_resendNotificationTooltip());
        this.resendNotificationButton.addClickHandler(new ResendNotificationClickHandler(this));

        this.previewButton.setText(constants.editExchange_previewButton());
        this.previewButton.setTitle(constants.editExchange_previewTooltip());
        this.previewButton.addClickHandler(new PreviewClickHandler(this));
    }

    /** Setup the displayed table. */
    private void setupTable() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        ParticipantKeyProvider keyProvider = new ParticipantKeyProvider();
        this.table = new DataTable<Participant>(this.getPageSize(), "550px", keyProvider);
        this.pager = table.getPager();

        this.table.setNoRowsMessage(constants.editExchange_noRows());
        table.addCellPreviewHandler(new RowClickHandler(this));

        this.table.addSelectionColumn(10, Unit.PCT, keyProvider);
        addColumn(new NameColumn(), constants.editExchange_nameTitle(), 35);
        addColumn(new EmailColumn(), constants.editExchange_emailTitle(), 55);

        this.dataProvider.addDataDisplay(this.table);
    }

    /** Add a column to the table. */
    private void addColumn(Column<Participant, ?> column, String title, int width) {
        this.table.addColumn(column, title);
        this.table.setColumnWidth(column, width, Unit.PCT);
    }

    /** Name column. */
    protected static class NameColumn extends Column<Participant, String> {
        public NameColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(Participant item) {
            return item == null || item.getName() == null ? "" : item.getName();
        }
    }

    /** Email column. */
    protected static class EmailColumn extends Column<Participant, String> {
        public EmailColumn() {
            super(new TextCell());
            this.setSortable(false);
        }

        @Override
        public String getValue(Participant item) {
            return item == null || item.getEmailAddress() == null ? "" : item.getEmailAddress();
        }
    }

    /** Save click handler. */
    protected static class SaveClickHandler extends AbstractViewEventClickHandler<EditExchangeTabView> {
        public SaveClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getSaveHandler();
        }
    }

    /** Reset click handler. */
    protected static class ResetClickHandler extends AbstractClickHandler<EditExchangeTabView> {
        public ResetClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public void onClick(ClickEvent event) {
            // Only bother to show confirmation if the parent will actually do something
            if (this.getParent().getResetHandler() != null) {
                this.getParent().showResetConfirmPopup();
                // later, the ResetConfirmHandler will actually invoke the parent's reset event handler
            }
        }
    }

    /** Reset confirm handler. */
    protected static class ResetConfirmHandler extends AbstractViewEventHandler<EditExchangeTabView> {
        public ResetConfirmHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            if (this.getParent().getResetHandler() != null) {
                this.getParent().getResetHandler().handleEvent(event);
            }
        }
    }

    /** ReturnToList click handler. */
    protected static class ReturnToListClickHandler extends AbstractViewEventClickHandler<EditExchangeTabView> {
        public ReturnToListClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getReturnToListHandler();
        }
    }

    /** AddParticipant click handler. */
    protected static class AddParticipantClickHandler extends AbstractViewEventClickHandler<EditExchangeTabView> {
        public AddParticipantClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getAddParticipantHandler();
        }
    }

    /** DeleteParticipant click handler. */
    protected static class DeleteParticipantClickHandler extends AbstractViewEventClickHandler<EditExchangeTabView> {
        public DeleteParticipantClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getDeleteParticipantHandler();
        }
    }

    /** SendAllNotifications click handler. */
    protected static class SendAllNotificationsClickHandler extends AbstractClickHandler<EditExchangeTabView> {
        public SendAllNotificationsClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public void onClick(ClickEvent event) {
            if (this.getParent().getSendAllNotificationsHandler() != null) {
                AssignmentSet assignments = this.getParent().getEditState().getAssignments();
                if (assignments != null && !assignments.isEmpty()) {
                    this.getParent().showResendConfirmPopup();
                    // later, SendAllNotificationsConfirmHandler will invoke the parent event handler
                } else {
                    this.getParent().showSendConfirmPopup();
                    // later, SendAllNotificationsConfirmHandler will invoke the parent event handler
                }
            }
        }
    }

    /** SendAllNotifications confirm handler. */
    protected static class SendAllNotificationsConfirmHandler extends AbstractViewEventHandler<EditExchangeTabView> {
        public SendAllNotificationsConfirmHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public void handleEvent(UnifiedEvent event) {
            if (this.getParent().getSendAllNotificationsHandler() != null) {
                this.getParent().getSendAllNotificationsHandler().handleEvent(event);
            }
        }
    }

    /** ResendNotification click handler. */
    protected static class ResendNotificationClickHandler extends AbstractViewEventClickHandler<EditExchangeTabView> {
        public ResendNotificationClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getResendNotificationHandler();
        }
    }

    /** Preview click handler. */
    protected static class PreviewClickHandler extends AbstractViewEventClickHandler<EditExchangeTabView> {
        public PreviewClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getPreviewHandler();
        }
    }

    /** Row click handler. */
    protected static class RowClickHandler extends AbstractRowClickViewEventHandler<EditExchangeTabView, Participant> {
        public RowClickHandler(EditExchangeTabView parent) {
            super(parent);
        }

        @Override
        protected Integer getSelectionColumn() {
            return 0;
        }

        @Override
        protected ViewEventHandlerWithContext<Participant> getViewEventHandler() {
            return this.getParent().getEditParticipantHandler();
        }
    }
}
