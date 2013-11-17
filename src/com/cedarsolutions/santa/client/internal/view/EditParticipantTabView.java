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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.handler.AbstractClickHandler;
import com.cedarsolutions.client.gwt.handler.AbstractEventHandler;
import com.cedarsolutions.client.gwt.module.view.ModuleTabView;
import com.cedarsolutions.client.gwt.validation.IValidationErrorWidget;
import com.cedarsolutions.client.gwt.widget.table.DataTable;
import com.cedarsolutions.client.gwt.widget.table.DataTablePager;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.view.ViewUtils;
import com.cedarsolutions.santa.client.common.widget.ParticipantList;
import com.cedarsolutions.santa.client.common.widget.ValidationErrorWidget;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantKeyProvider;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.util.gwt.GwtLocalizationUtils;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;
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
public class EditParticipantTabView extends ModuleTabView implements IEditParticipantTabView {

    /** Reference to the UI binder. */
    private static ViewUiBinder BINDER = GWT.create(ViewUiBinder.class);

    /** Reference to the id handler. */
    private static ViewIdHandler ID_HANDLER = GWT.create(ViewIdHandler.class);

    /** UI binder interface. */
    protected interface ViewUiBinder extends UiBinder<Widget, EditParticipantTabView> {
    }

    /** Id handler interface. */
    protected interface ViewIdHandler extends ElementIdHandler<EditParticipantTabView> {
    }

    // User interface fields fron the UI binder
    @UiField protected ValidationErrorWidget validationErrorWidget;
    @UiField @WithElementId protected Button saveButton;
    @UiField @WithElementId protected Button cancelButton;
    @UiField @WithElementId protected Label nameLabel;
    @UiField @WithElementId protected TextBox nameInput;
    @UiField @WithElementId protected Label nicknameLabel;
    @UiField @WithElementId protected TextBox nicknameInput;
    @UiField @WithElementId protected Label emailLabel;
    @UiField @WithElementId protected TextBox emailInput;
    @UiField @WithElementId protected Label giftGiverLabel;
    @UiField @WithElementId protected Label giftGiverDisplayLabel;
    @UiField @WithElementId protected Label giftReceiverLabel;
    @UiField @WithElementId protected Label giftReceiverDisplayLabel;
    @UiField @WithElementId protected DisclosurePanel giftGiverDisclosure;
    @UiField @WithElementId protected DisclosurePanel giftReceiverDisclosure;
    @UiField @WithElementId protected Button addConflictButton;
    @UiField @WithElementId protected Button deleteConflictButton;
    @UiField @WithElementId protected Label conflictLabel;
    @UiField(provided = true) @WithElementId protected ParticipantList conflictInput;
    @UiField(provided = true) @WithElementId protected DataTable<Participant> table;
    @UiField(provided = true) @WithElementId protected DataTablePager pager;

    // Other instance variables
    private Participant editState; // saves the current edit state for values that are not edited
    private ViewEventHandler saveHandler;
    private ViewEventHandler cancelHandler;
    private ListDataProvider<Participant> dataProvider = new ListDataProvider<Participant>();

    /** Create the view. */
    public EditParticipantTabView() {
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
        return GwtLocalizationUtils.translate(message, constants, "editParticipant");
    }

    /** Get a mapping from field name to input element. */
    @Override
    public Map<String, UIObject> getValidatedFieldMap() {
        Map<String, UIObject> map = new HashMap<String, UIObject>();
        map.put("name", this.nameInput);
        map.put("nickname", this.nicknameInput);
        map.put("emailAddress", this.emailInput);
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

    /** Set the cancel handler. */
    @Override
    public void setCancelHandler(ViewEventHandler cancelHandler) {
        this.cancelHandler = cancelHandler;
    }

    /** Get the cancel handler. */
    @Override
    public ViewEventHandler getCancelHandler() {
        return this.cancelHandler;
    }

    /** Get the configured page size. */
    protected int getPageSize() {
        InternalConstants constants = GWT.create(InternalConstants.class);
        return constants.editParticipant_pageSize();
    }

    /** Add the selected conflict to the current edit state, and refresh the display. */
    protected void addSelectedConflict() {
        Participant conflict = this.conflictInput.getSelectedValue();
        if (conflict != null) {
            this.editState.addConflict(conflict);
            this.conflictInput.removeDropdownItem(conflict);
            this.dataProvider.setList(this.editState.getConflicts());
            this.table.selectNone();
            this.addConflictButton.setEnabled(this.conflictInput.getItemCount() > 0);
        }
    }

    /** Delete the selected conflicts from the current edit state, and refresh the display. */
    protected void deleteSelectedConflicts() {
        List<Participant> selected = this.table.getSelectedRecords();

        if (selected != null && !selected.isEmpty()) {
            for (Participant conflict : this.table.getSelectedRecords()) {
                this.editState.removeConflict(conflict);
                this.conflictInput.addDropdownItem(conflict);
            }
        }

        this.dataProvider.setList(this.editState.getConflicts());
        this.table.selectNone();
        this.addConflictButton.setEnabled(this.conflictInput.getItemCount() > 0);
    }

    /**
     * Set the edit state.
     * @param participant   Participant to be edited
     * @param giftGiver     Giver that is assigned to this participant
     * @param giftReceiver  Receiver that is assigned to this participant
     * @param participants  List of all participants in this exchange
     */
    @Override
    public void setEditState(Participant participant, Participant giftGiver, Participant giftReceiver, ParticipantSet participants) {
        InternalConstants constants = GWT.create(InternalConstants.class);
        String giverName = giftGiver == null ? constants.editParticipant_noAssignment() : giftGiver.getName();
        String receiverName = giftReceiver == null ? constants.editParticipant_noAssignment() : giftReceiver.getName();

        this.editState = new Participant(participant);

        this.nameInput.setText(this.editState.getName());
        this.nicknameInput.setText(this.editState.getNickname());
        this.emailInput.setText(this.editState.getEmailAddress());

        this.giftGiverDisplayLabel.setText(giverName);
        this.giftReceiverDisplayLabel.setText(receiverName);

        ViewUtils.getInstance().clearValidationErrors(this);
        this.giftGiverDisclosure.setOpen(false);
        this.giftReceiverDisclosure.setOpen(false);

        this.dataProvider.setList(this.editState.getConflicts());
        this.table.setVisibleRangeAndClearData(new Range(0, this.getPageSize()), true);
        this.table.selectNone();

        this.conflictInput.clear();
        if (participants != null) {
            this.conflictInput.setParticipants(participants);
            this.conflictInput.removeDropdownItem(participant);
            for (Participant conflict : participant.getConflicts()) {
                this.conflictInput.removeDropdownItem(conflict);
            }
        }

        this.addConflictButton.setEnabled(this.conflictInput.getItemCount() > 0);
    }

    /** Get the participant that is being edited, in its current state. */
    @Override
    public Participant getEditState() {
        Participant participant = new Participant(this.editState);

        participant.setName(this.nameInput.getText());
        participant.setNickname(this.nicknameInput.getText());
        participant.setEmailAddress(this.emailInput.getText());

        return participant;
    }

    /** Create and set up the provided widgets that aren't controlled by the UI binder. */
    private void createProvidedWidgets() {
        this.conflictInput = new ParticipantList();
        this.setupTable();
    }

    /** Set up the widgets that are controlled by the UI binder. */
    private void setupBinderWidgets() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        this.giftGiverDisclosure.getHeaderTextAccessor().setText(constants.editParticipant_revealGiftGiver());
        this.giftGiverDisclosure.setOpen(false);

        this.giftReceiverDisclosure.getHeaderTextAccessor().setText(constants.editParticipant_revealGiftReceiver());
        this.giftReceiverDisclosure.setOpen(false);

        this.nameLabel.setText(constants.editParticipant_nameLabel());
        this.nameLabel.setTitle(constants.editParticipant_nameTooltip());
        this.nameInput.setTitle(constants.editParticipant_nameTooltip());

        this.nicknameLabel.setText(constants.editParticipant_nicknameLabel());
        this.nicknameLabel.setTitle(constants.editParticipant_nicknameTooltip());
        this.nicknameInput.setTitle(constants.editParticipant_nicknameTooltip());

        this.emailLabel.setText(constants.editParticipant_emailAddressLabel());
        this.emailLabel.setTitle(constants.editParticipant_emailAddressTooltip());
        this.emailInput.setTitle(constants.editParticipant_emailAddressTooltip());

        this.giftGiverLabel.setText(constants.editParticipant_giftGiverLabel());
        this.giftGiverLabel.setTitle(constants.editParticipant_giftGiverTooltip());
        this.giftGiverDisplayLabel.setTitle(constants.editParticipant_giftGiverTooltip());

        this.giftReceiverLabel.setText(constants.editParticipant_giftReceiverLabel());
        this.giftReceiverLabel.setTitle(constants.editParticipant_giftReceiverTooltip());
        this.giftReceiverDisplayLabel.setTitle(constants.editParticipant_giftReceiverTooltip());

        this.saveButton.setText(constants.editParticipant_saveButton());
        this.saveButton.setTitle(constants.editParticipant_saveTooltip());
        this.saveButton.addClickHandler(new SaveClickHandler(this));

        this.cancelButton.setText(constants.editParticipant_cancelButton());
        this.cancelButton.setTitle(constants.editParticipant_cancelTooltip());
        this.cancelButton.addClickHandler(new CancelClickHandler(this));

        this.addConflictButton.setText(constants.editParticipant_addConflictButton());
        this.addConflictButton.setTitle(constants.editParticipant_addConflictTooltip());
        this.addConflictButton.addClickHandler(new AddConflictClickHandler(this));
        this.addConflictButton.setEnabled(false);

        this.deleteConflictButton.setText(constants.editParticipant_deleteConflictButton());
        this.deleteConflictButton.setTitle(constants.editParticipant_deleteConflictTooltip());
        this.deleteConflictButton.addClickHandler(new DeleteConflictClickHandler(this));

        this.conflictLabel.setText(constants.editParticipant_conflictLabel());
    }

    /** Save click handler. */
    protected static class SaveClickHandler extends AbstractClickHandler<EditParticipantTabView> {
        public SaveClickHandler(EditParticipantTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getSaveHandler();
        }
    }

    /** Cancel click handler. */
    protected static class CancelClickHandler extends AbstractClickHandler<EditParticipantTabView> {
        public CancelClickHandler(EditParticipantTabView parent) {
            super(parent);
        }

        @Override
        public ViewEventHandler getViewEventHandler() {
            return this.getParent().getCancelHandler();
        }
    }

    /** Setup the displayed table. */
    private void setupTable() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        ParticipantKeyProvider keyProvider = new ParticipantKeyProvider();
        this.table = new DataTable<Participant>(this.getPageSize(), "550px", keyProvider);
        this.pager = table.getPager();

        this.table.setNoRowsMessage(constants.editParticipant_noConflicts());

        this.table.addSelectionColumn(10, Unit.PCT, keyProvider);
        addColumn(new NameColumn(), constants.editParticipant_nameTitle(), 35);
        addColumn(new EmailColumn(), constants.editParticipant_emailTitle(), 55);

        this.dataProvider.addDataDisplay(this.table);
    }

    /** Add a column to the table. */
    private void addColumn(Column<Participant, ?> column, String title, int width) {
        this.table.addColumn(column, title);
        this.table.setColumnWidth(column, width, Unit.PCT);
    }

    /** Add conflict click handler. */
    protected static class AddConflictClickHandler extends AbstractEventHandler<EditParticipantTabView> implements ClickHandler {
        public AddConflictClickHandler(EditParticipantTabView parent) {
            super(parent);
        }

        /** Handle a click event. */
        @Override
        public void onClick(ClickEvent event) {
            this.getParent().addSelectedConflict();
        }
    }

    /** Delete conflict click handler. */
    protected static class DeleteConflictClickHandler extends AbstractEventHandler<EditParticipantTabView> implements ClickHandler {
        public DeleteConflictClickHandler(EditParticipantTabView parent) {
            super(parent);
        }

        @Override
        public void onClick(ClickEvent event) {
            this.getParent().deleteSelectedConflicts();
        }
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

}
