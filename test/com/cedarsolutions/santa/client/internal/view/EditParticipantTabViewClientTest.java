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

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.client.common.widget.ParticipantList;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.EmailColumn;
import com.cedarsolutions.santa.client.internal.view.EditParticipantTabView.NameColumn;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.santa.shared.domain.exchange.TemplateConfig;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for EditParticipantTabView.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class EditParticipantTabViewClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        EditParticipantTabView view = new EditParticipantTabView();
        assertNotNull(view);
        assertTableConfiguredProperly(view);
        assertWidgetsSetProperly(view);
    }

    /** Test the getters and setters for event handlers. */
    public void testEventHandlers() {
        EditParticipantTabView view = new EditParticipantTabView();

        StubbedViewEventHandler saveHandler = new StubbedViewEventHandler();
        view.setSaveHandler(saveHandler);
        assertSame(saveHandler, view.getSaveHandler());
        clickButton(view.saveButton);
        assertTrue(saveHandler.handledEvent());

        StubbedViewEventHandler cancelHandler = new StubbedViewEventHandler();
        view.setCancelHandler(cancelHandler);
        assertSame(cancelHandler, view.getCancelHandler());
        clickButton(view.cancelButton);
        assertTrue(cancelHandler.handledEvent());
    }

    /** Test the validation functionality. */
    public void testValidation() {
        EditParticipantTabView view = new EditParticipantTabView();
        assertSame(view.validationErrorWidget, view.getValidationErrorWidget());

        assertSame(view.nameInput, view.getValidatedFieldMap().get("name"));
        assertSame(view.nicknameInput, view.getValidatedFieldMap().get("nickname"));
        assertSame(view.emailInput, view.getValidatedFieldMap().get("emailAddress"));

        InternalMessageConstants constants = GWT.create(InternalMessageConstants.class);
        assertEquals(constants.editParticipant_required_name(), view.translate(new LocalizableMessage(REQUIRED, "name", "name invalid")));

        view.showValidationError(new InvalidDataException("whatever")); // make sure it doesn't blow up
    }

    /** Test get/set edit state. */
    public void testEditState() {
        InternalConstants constants = GWT.create(InternalConstants.class);

        Participant giver = new Participant();
        giver.setId(10L);
        giver.setName("giver");

        Participant receiver = new Participant(20L);
        receiver.setId(20L);
        receiver.setName("receiver");

        Participant empty = new Participant();
        empty.setId(null);
        empty.setName("");
        empty.setNickname("");
        empty.setEmailAddress("");

        Participant other1 = new Participant();
        other1.setId(201L);

        Participant other2 = new Participant();
        other2.setId(202L);

        Participant participant = createParticipant();

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant);
        participants.addAll(participant.getConflicts());
        participants.add(other1);
        participants.add(other2);

        EditParticipantTabView view = new EditParticipantTabView();
        assertEquals(empty, view.getEditState());
        assertEquals("", view.giftGiverDisplayLabel.getText());
        assertEquals("", view.giftReceiverDisplayLabel.getText());
        assertEquals(0, view.conflictInput.getItemCount());
        assertTrue(view.table.getSelectedRecords().isEmpty());
        assertFalse(view.addConflictButton.isEnabled());

        view.setEditState(participant, giver, receiver, participants);
        assertEquals(participant, view.getEditState());
        assertNotSame(participant, view.getEditState());
        assertEquals(giver.getName(), view.giftGiverDisplayLabel.getText());
        assertEquals(receiver.getName(), view.giftReceiverDisplayLabel.getText());
        assertContainsItems(view.getEditState().getConflicts(), 101L);
        assertContainsItems(view.conflictInput, "201", "202");
        assertTrue(view.table.getSelectedRecords().isEmpty());
        assertTrue(view.addConflictButton.isEnabled());

        view.setEditState(null, null, null, null);
        assertEquals(empty, view.getEditState());
        assertEquals(constants.editParticipant_noAssignment(), view.giftGiverDisplayLabel.getText());
        assertEquals(constants.editParticipant_noAssignment(), view.giftReceiverDisplayLabel.getText());
        assertEquals(0, view.conflictInput.getItemCount());
        assertTrue(view.table.getSelectedRecords().isEmpty());
        assertFalse(view.addConflictButton.isEnabled());
    }

    /** Test add and remove of conflicts. */
    public void testAddRemoveConflicts() {
        Participant giver = new Participant();
        giver.setId(10L);
        giver.setName("giver");

        Participant receiver = new Participant(20L);
        receiver.setId(20L);
        receiver.setName("receiver");

        Participant other1 = new Participant();
        other1.setId(201L);

        Participant other2 = new Participant();
        other2.setId(202L);

        Participant participant = createParticipant();

        ParticipantSet participants = new ParticipantSet();
        participants.add(participant);
        participants.addAll(participant.getConflicts());
        participants.add(other1);
        participants.add(other2);

        EditParticipantTabView view = new EditParticipantTabView();
        view.setEditState(participant, giver, receiver, participants);
        assertContainsItems(view.getEditState().getConflicts(), 101L);
        assertContainsItems(view.conflictInput, "201", "202");
        assertTrue(view.addConflictButton.isEnabled());

        view.conflictInput.setSelectedValue(other2);
        clickButton(view.addConflictButton);
        assertContainsItems(view.getEditState().getConflicts(), 101L, 202L);
        assertContainsItems(view.conflictInput, "201");
        assertTrue(view.addConflictButton.isEnabled());

        view.conflictInput.setSelectedValue(other1);
        clickButton(view.addConflictButton);
        assertContainsItems(view.getEditState().getConflicts(), 101L, 202L, 201L);
        assertEquals(0, view.conflictInput.getItemCount());
        assertFalse(view.addConflictButton.isEnabled());

        view.table.selectNone();
        clickButton(view.deleteConflictButton);
        assertContainsItems(view.getEditState().getConflicts(), 101L, 202L, 201L);
        assertEquals(0, view.conflictInput.getItemCount());
        assertFalse(view.addConflictButton.isEnabled());

        view.table.selectItem(view.getEditState().getConflicts().get(0));
        clickButton(view.deleteConflictButton);
        assertContainsItems(view.getEditState().getConflicts(), 202L, 201L);
        assertContainsItems(view.conflictInput, "101");
        assertTrue(view.addConflictButton.isEnabled());

        view.table.selectItem(view.getEditState().getConflicts().get(0));
        view.table.selectItem(view.getEditState().getConflicts().get(1));
        clickButton(view.deleteConflictButton);
        assertTrue(view.getEditState().getConflicts().isEmpty());
        assertContainsItems(view.conflictInput, "101", "202", "201");
        assertTrue(view.addConflictButton.isEnabled());
    }

    /** Create a Participant for testing. */
    private static Participant createParticipant() {
        Participant conflict = new Participant();
        conflict.setId(101L);
        ParticipantSet conflicts = new ParticipantSet();
        conflicts.add(conflict);

        Participant participant = new Participant();
        participant.setId(1L);
        participant.setName("name");
        participant.setNickname("nick");
        participant.setEmailAddress("email");
        participant.setTemplateOverrides(new TemplateConfig());
        participant.setConflicts(conflicts);

        return participant;
    }

    /** Assert that a participant list contains specific items (in order). */
    private static void assertContainsItems(ParticipantList list, String ... items) {
        assertEquals(items.length, list.getItemCount());
        for (int i = 0; i < items.length; i++) {
            assertEquals(items[i], list.getValue(i));
        }
    }

    /** Assert that a participant list contains specific items (in order). */
    private static void assertContainsItems(ParticipantSet list, Long ... items) {
        assertEquals(items.length, list.size());
        for (int i = 0; i < items.length; i++) {
            assertEquals(items[i], list.get(i).getId());
        }
    }

    /** Assert that all of the widgets are configured properly. */
    private static void assertWidgetsSetProperly(EditParticipantTabView view) {
        InternalConstants constants = GWT.create(InternalConstants.class);

        assertEquals(constants.editParticipant_nameLabel(), view.nameLabel.getText());
        assertEquals(constants.editParticipant_nameTooltip(), view.nameLabel.getTitle());
        assertEquals(constants.editParticipant_nameTooltip(), view.nameInput.getTitle());

        assertEquals(constants.editParticipant_nicknameLabel(), view.nicknameLabel.getText());
        assertEquals(constants.editParticipant_nicknameTooltip(), view.nicknameLabel.getTitle());
        assertEquals(constants.editParticipant_nicknameTooltip(), view.nicknameInput.getTitle());

        assertEquals(constants.editParticipant_emailAddressLabel(), view.emailLabel.getText());
        assertEquals(constants.editParticipant_emailAddressTooltip(), view.emailLabel.getTitle());
        assertEquals(constants.editParticipant_emailAddressTooltip(), view.emailInput.getTitle());

        assertEquals(constants.editParticipant_giftGiverLabel(), view.giftGiverLabel.getText());
        assertEquals(constants.editParticipant_giftGiverTooltip(), view.giftGiverLabel.getTitle());
        assertEquals(constants.editParticipant_giftGiverTooltip(), view.giftGiverDisplayLabel.getTitle());

        assertEquals(constants.editParticipant_giftReceiverLabel(), view.giftReceiverLabel.getText());
        assertEquals(constants.editParticipant_giftReceiverTooltip(), view.giftReceiverLabel.getTitle());
        assertEquals(constants.editParticipant_giftReceiverTooltip(), view.giftReceiverDisplayLabel.getTitle());

        assertEquals(constants.editParticipant_saveButton(), view.saveButton.getText());
        assertEquals(constants.editParticipant_saveTooltip(), view.saveButton.getTitle());

        assertEquals(constants.editParticipant_cancelButton(), view.cancelButton.getText());
        assertEquals(constants.editParticipant_cancelTooltip(), view.cancelButton.getTitle());

        assertEquals(constants.editParticipant_addConflictButton(), view.addConflictButton.getText());
        assertEquals(constants.editParticipant_addConflictTooltip(), view.addConflictButton.getTitle());

        assertEquals(constants.editParticipant_deleteConflictButton(), view.deleteConflictButton.getText());
        assertEquals(constants.editParticipant_deleteConflictTooltip(), view.deleteConflictButton.getTitle());

        assertEquals(constants.editParticipant_conflictLabel(), view.conflictLabel.getText());

        // Can't check the text of the disclosure panels, unfortunately
    }

    /** Assert that the table is configured properly. */
    private static void assertTableConfiguredProperly(EditParticipantTabView view) {
        InternalConstants constants = GWT.create(InternalConstants.class);

        assertEquals(constants.editParticipant_pageSize(), view.getPageSize());

        assertNotNull(view.table);
        assertNotNull(view.pager);
        assertTrue(view.table.hasSelectionColumn());
        assertEquals(constants.editParticipant_noConflicts(), view.table.getNoRowsMessage());

        assertEquals(3, view.table.getColumnCount());
        assertSelectionColumnAtIndex(view.table, 0);
        assertTableColumnValid(view.table, 1, NameColumn.class, constants.editParticipant_nameTitle());
        assertTableColumnValid(view.table, 2, EmailColumn.class, constants.editParticipant_emailTitle());
        assertTrue(view.table.getSelectedRecords().isEmpty());
    }
}
