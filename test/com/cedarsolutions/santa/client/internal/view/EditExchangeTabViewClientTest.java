/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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

import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.EmailColumn;
import com.cedarsolutions.santa.client.internal.view.EditExchangeTabView.NameColumn;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.cedarsolutions.santa.shared.domain.exchange.Organizer;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.santa.shared.domain.exchange.TemplateConfig;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for EditExchangeTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class EditExchangeTabViewClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        EditExchangeTabView view = new EditExchangeTabView();
        assertWidgetsSetProperly(view);
        assertTableConfiguredProperly(view);
    }


    /** Test getSelectedParticipants(). */
    public void testGetSelectedParticipants() {
        EditExchangeTabView view = new EditExchangeTabView();
        assertNotNull(view.getSelectedParticipants()); // just make sure it doesn't blow up; not easy to control the table
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        EditExchangeTabView view = new EditExchangeTabView();

        StubbedViewEventHandler saveHandler = new StubbedViewEventHandler();
        view.setSaveHandler(saveHandler);
        assertSame(saveHandler, view.getSaveHandler());
        clickButton(view.saveButton);
        assertTrue(saveHandler.handledEvent());

        StubbedViewEventHandler resetHandler = new StubbedViewEventHandler();
        view.setResetHandler(resetHandler);
        assertSame(resetHandler, view.getResetHandler());
        clickButton(view.resetButton);
        clickButton(view.resetConfirmPopup.getCancelButton());
        assertFalse(resetHandler.handledEvent());
        clickButton(view.resetButton);
        clickButton(view.resetConfirmPopup.getOkButton());
        assertTrue(resetHandler.handledEvent());

        StubbedViewEventHandler returnToListHandler = new StubbedViewEventHandler();
        view.setReturnToListHandler(returnToListHandler);
        assertSame(returnToListHandler, view.getReturnToListHandler());
        clickButton(view.returnToListButton);
        assertTrue(returnToListHandler.handledEvent());

        StubbedViewEventHandler addParticipantHandler = new StubbedViewEventHandler();
        view.setAddParticipantHandler(addParticipantHandler);
        assertSame(addParticipantHandler, view.getAddParticipantHandler());
        clickButton(view.addParticipantButton);
        assertTrue(addParticipantHandler.handledEvent());

        StubbedViewEventHandler deleteParticipantHandler = new StubbedViewEventHandler();
        view.setDeleteParticipantHandler(deleteParticipantHandler);
        assertSame(deleteParticipantHandler, view.getDeleteParticipantHandler());
        clickButton(view.deleteParticipantButton);
        assertTrue(deleteParticipantHandler.handledEvent());

        // This is the case where they get the "are you sure you want to send" message
        view.editState = new Exchange();
        view.editState.setAssignments(null);
        StubbedViewEventHandler sendAllNotificationsHandler = new StubbedViewEventHandler();
        view.setSendAllNotificationsHandler(sendAllNotificationsHandler);
        assertSame(sendAllNotificationsHandler, view.getSendAllNotificationsHandler());
        clickButton(view.sendAllNotificationsButton);
        assertFalse(sendAllNotificationsHandler.handledEvent());
        clickButton(view.sendConfirmPopup.getCancelButton());
        assertFalse(sendAllNotificationsHandler.handledEvent());
        clickButton(view.sendAllNotificationsButton);
        assertFalse(sendAllNotificationsHandler.handledEvent());
        clickButton(view.sendConfirmPopup.getOkButton());
        assertTrue(sendAllNotificationsHandler.handledEvent());

        // This is the case where they get the "are you sure you want to re-send" message
        AssignmentSet assignments = new AssignmentSet();
        assignments.add(new Assignment());
        view.editState = new Exchange();
        view.editState.setAssignments(assignments);
        sendAllNotificationsHandler = new StubbedViewEventHandler();
        view.setSendAllNotificationsHandler(sendAllNotificationsHandler);
        assertSame(sendAllNotificationsHandler, view.getSendAllNotificationsHandler());
        clickButton(view.sendAllNotificationsButton);
        assertFalse(sendAllNotificationsHandler.handledEvent());
        clickButton(view.resendConfirmPopup.getCancelButton());
        assertFalse(sendAllNotificationsHandler.handledEvent());
        clickButton(view.sendAllNotificationsButton);
        assertFalse(sendAllNotificationsHandler.handledEvent());
        clickButton(view.resendConfirmPopup.getOkButton());
        assertTrue(sendAllNotificationsHandler.handledEvent());

        StubbedViewEventHandler resendNotificationHandler = new StubbedViewEventHandler();
        view.setResendNotificationHandler(resendNotificationHandler);
        assertSame(resendNotificationHandler, view.getResendNotificationHandler());
        clickButton(view.resendNotificationButton);
        assertTrue(resendNotificationHandler.handledEvent());

        StubbedViewEventHandler previewHandler = new StubbedViewEventHandler();
        view.setPreviewHandler(previewHandler);
        assertSame(previewHandler, view.getPreviewHandler());
        clickButton(view.previewButton);
        assertTrue(previewHandler.handledEvent());

        ViewEventHandlerWithContext<Participant> editParticipantHandler = new StubbedViewEventHandlerWithContext<Participant>();
        view.setEditParticipantHandler(editParticipantHandler);
        assertSame(editParticipantHandler, view.getEditParticipantHandler());
    }

    /** Test the validation functionality. */
    public void testValidation() {
        EditExchangeTabView view = new EditExchangeTabView();
        assertSame(view.validationErrorWidget, view.getValidationErrorWidget());

        assertSame(view.dateTimeInput, view.getValidatedFieldMap().get("dateAndTime"));
        assertSame(view.themeInput, view.getValidatedFieldMap().get("theme"));
        assertSame(view.costInput, view.getValidatedFieldMap().get("cost"));
        assertSame(view.organizerNameInput, view.getValidatedFieldMap().get("organizerName"));
        assertSame(view.organizerEmailInput, view.getValidatedFieldMap().get("organizerEmailAddress"));
        assertSame(view.organizerPhoneInput, view.getValidatedFieldMap().get("organizerPhoneNumber"));
        assertSame(view.extraInfoInput, view.getValidatedFieldMap().get("extraInfo"));

        InternalMessageConstants constants = GWT.create(InternalMessageConstants.class);
        assertEquals(constants.editExchange_required_dateAndTime(),
                     view.translate(new LocalizableMessage(REQUIRED, "dateAndTime", "dateAndTime invalid")));

        view.showValidationError(new InvalidDataException("whatever")); // make sure it doesn't blow up
    }

    /** Test get/set edit state. */
    public void testEditState() {
        Exchange empty = new Exchange();
        empty.setName("");
        empty.setDateAndTime("");
        empty.setTheme("");
        empty.setCost("");
        empty.setExtraInfo("");
        empty.getOrganizer().setName("");
        empty.getOrganizer().setEmailAddress("");
        empty.getOrganizer().setPhoneNumber("");

        EditExchangeTabView view = new EditExchangeTabView();
        assertEquals(empty, view.getEditState());
        assertTrue(view.table.getSelectedRecords().isEmpty());

        Exchange exchange = createExchange();
        view.setEditState(exchange);
        assertEquals(exchange, view.getEditState());
        assertNotSame(exchange, view.getEditState());
        assertTrue(view.table.getSelectedRecords().isEmpty());

        view.setEditState(null);
        assertEquals(empty, view.getEditState());
        assertTrue(view.table.getSelectedRecords().isEmpty());
    }

    /** Test showPreview(). */
    public void testShowPreview() {
        EmailMessage message = new EmailMessage();
        message.setFormat(EmailFormat.MULTIPART);
        message.setHtml("HTML");
        message.setPlaintext("TEXT");

        EditExchangeTabView view = new EditExchangeTabView();
        assertFalse(view.previewPopup.isShowing());

        view.showPreview(message);
        assertTrue(view.previewPopup.isShowing());
        // remaining functionality is tested in PreviewPopup tests
    }

    /** Test showSendSuccessfulPopup(). */
    public void testShowSendSuccessfulPopup() {
        EmailMessage message = new EmailMessage();
        message.setFormat(EmailFormat.MULTIPART);
        message.setHtml("HTML");
        message.setPlaintext("TEXT");

        EditExchangeTabView view = new EditExchangeTabView();
        assertFalse(view.sendSuccessfulPopup.isShowing());

        view.showSendSuccessfulPopup();
        assertTrue(view.sendSuccessfulPopup.isShowing());
    }

    /** Assert that all of the widgets are configured properly. */
    private static void assertWidgetsSetProperly(EditExchangeTabView view) {
        InternalConstants constants = GWT.create(InternalConstants.class);

        assertEquals(constants.editExchange_saveButton(), view.saveButton.getText());
        assertEquals(constants.editExchange_saveTooltip(), view.saveButton.getTitle());

        assertEquals(constants.editExchange_resetButton(), view.resetButton.getText());
        assertEquals(constants.editExchange_resetTooltip(), view.resetButton.getTitle());

        assertEquals(constants.editExchange_returnToListButton(), view.returnToListButton.getText());
        assertEquals(constants.editExchange_returnToListTooltip(), view.returnToListButton.getTitle());

        assertEquals(constants.editExchange_addParticipantButton(), view.addParticipantButton.getText());
        assertEquals(constants.editExchange_addParticipantTooltip(), view.addParticipantButton.getTitle());

        assertEquals(constants.editExchange_deleteParticipantButton(), view.deleteParticipantButton.getText());
        assertEquals(constants.editExchange_deleteParticipantTooltip(), view.deleteParticipantButton.getTitle());

        assertEquals(constants.editExchange_sendAllNotificationsButton(), view.sendAllNotificationsButton.getText());
        assertEquals(constants.editExchange_sendAllNotificationsTooltip(), view.sendAllNotificationsButton.getTitle());

        assertEquals(constants.editExchange_resendNotificationButton(), view.resendNotificationButton.getText());
        assertEquals(constants.editExchange_resendNotificationTooltip(), view.resendNotificationButton.getTitle());

        assertEquals(constants.editExchange_previewButton(), view.previewButton.getText());
        assertEquals(constants.editExchange_previewTooltip(), view.previewButton.getTitle());

        assertEquals(constants.editExchange_stateLabel(), view.stateTitleLabel.getText());

        assertEquals(constants.editExchange_exchangeNameLabel(), view.exchangeNameLabel.getText());
        assertEquals(constants.editExchange_exchangeNameTooltip(), view.exchangeNameLabel.getTitle());
        assertEquals(constants.editExchange_exchangeNameTooltip(), view.exchangeNameInput.getTitle());

        assertEquals(constants.editExchange_dateTimeLabel(), view.dateTimeLabel.getText());
        assertEquals(constants.editExchange_dateTimeTooltip(), view.dateTimeLabel.getTitle());
        assertEquals(constants.editExchange_dateTimeTooltip(), view.dateTimeInput.getTitle());

        assertEquals(constants.editExchange_themeLabel(), view.themeLabel.getText());
        assertEquals(constants.editExchange_themeTooltip(), view.themeLabel.getTitle());
        assertEquals(constants.editExchange_themeTooltip(), view.themeInput.getTitle());

        assertEquals(constants.editExchange_costLabel(), view.costLabel.getText());
        assertEquals(constants.editExchange_costTooltip(), view.costLabel.getTitle());
        assertEquals(constants.editExchange_costTooltip(), view.costInput.getTitle());

        assertEquals(constants.editExchange_organizerNameLabel(), view.organizerNameLabel.getText());
        assertEquals(constants.editExchange_organizerNameTooltip(), view.organizerNameLabel.getTitle());
        assertEquals(constants.editExchange_organizerNameTooltip(), view.organizerNameInput.getTitle());

        assertEquals(constants.editExchange_organizerEmailLabel(), view.organizerEmailLabel.getText());
        assertEquals(constants.editExchange_organizerEmailTooltip(), view.organizerEmailLabel.getTitle());
        assertEquals(constants.editExchange_organizerEmailTooltip(), view.organizerEmailInput.getTitle());

        assertEquals(constants.editExchange_organizerPhoneLabel(), view.organizerPhoneLabel.getText());
        assertEquals(constants.editExchange_organizerPhoneTooltip(), view.organizerPhoneLabel.getTitle());
        assertEquals(constants.editExchange_organizerPhoneTooltip(), view.organizerPhoneInput.getTitle());

        assertEquals(constants.editExchange_extraInfoLabel(), view.extraInfoLabel.getText());
        assertEquals(constants.editExchange_extraInfoTooltip(), view.extraInfoLabel.getTitle());
        assertEquals(constants.editExchange_extraInfoTooltip(), view.extraInfoInput.getTitle());

        assertEquals(constants.editExchange_optional(), view.optionalLabel.getText());
        assertEquals(constants.editExchange_instructions(), view.instructionsLabel.getText());
    }

    /** Assert that the table is configured properly. */
    private static void assertTableConfiguredProperly(EditExchangeTabView view) {
        InternalConstants constants = GWT.create(InternalConstants.class);

        assertEquals(constants.editExchange_pageSize(), view.getPageSize());

        assertNotNull(view.table);
        assertNotNull(view.pager);
        assertTrue(view.table.hasSelectionColumn());
        assertEquals(constants.editExchange_noRows(), view.table.getNoRowsMessage());

        assertEquals(3, view.table.getColumnCount());
        assertSelectionColumnAtIndex(view.table, 0);
        assertTableColumnValid(view.table, 1, NameColumn.class, constants.editExchange_nameTitle());
        assertTableColumnValid(view.table, 2, EmailColumn.class, constants.editExchange_emailTitle());
        assertTrue(view.table.getSelectedRecords().isEmpty());
    }

    /** Create a Exchange for testing. */
    private static Exchange createExchange() {
        Organizer organizer = new Organizer();
        organizer.setName("organizer");
        organizer.setEmailAddress("organizer@example.com");
        organizer.setPhoneNumber("555-1212");

        TemplateConfig overrides = new TemplateConfig();
        overrides.setSenderName("Santa Exchange");
        overrides.setEmailFormat(EmailFormat.MULTIPART);
        overrides.setTemplateGroup("group");
        overrides.setTemplateGroup("name");

        ParticipantSet conflicts = new ParticipantSet();
        conflicts.add(new Participant(3L));

        Participant participant1 = new Participant(1L, "p1", "p1n", "p1@example.com", conflicts);
        Participant participant2 = new Participant(1L, "p2", "p2n", "p2@example.com", null);

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant1);
        participants.add(participant2);

        Assignment assignment1 = new Assignment(participant1, participant2);
        Assignment assignment2 = new Assignment(participant2, participant1);

        AssignmentSet assignments = new AssignmentSet();
        assignments.add(assignment1);
        assignments.add(assignment2);

        Exchange exchange = new Exchange();
        exchange.setId(12L);
        exchange.setUserId("userId");
        exchange.setExchangeState(ExchangeState.NEW);
        exchange.setName("name");
        exchange.setDateAndTime("dateAndTime");
        exchange.setTheme("theme");
        exchange.setCost("cost");
        exchange.setExtraInfo("extraInfo");
        exchange.setOrganizer(organizer);
        exchange.setTemplateOverrides(overrides);
        exchange.setParticipants(participants);
        exchange.setAssignments(assignments);

        return exchange;
    }
}
