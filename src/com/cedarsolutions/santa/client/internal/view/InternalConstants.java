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

import com.google.gwt.i18n.client.Constants;

/**
 * User interface constants used by this module.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface InternalConstants extends Constants {

    /* **************************
     *  Header-related constants
     * **************************/

    @DefaultStringValue("Santa Exchange")
    String header_title();

    @DefaultStringValue("Admin Landing Page")
    String header_adminLandingPage();

    @DefaultStringValue("About Santa Exchange")
    String header_about();

    @DefaultStringValue("Report a Problem")
    String header_bugReport();

    @DefaultStringValue("Source Code")
    String header_sourceCode();

    @DefaultStringValue("Sign Out")
    String header_logout();


    /* ************************
     *  Landing page constants
     * ************************/

    @DefaultStringValue("Manage Your Exchanges")
    String landing_manageTitle();


    /* *************************
     *  Exchange list constants
     * *************************/

    @DefaultIntValue(10)
    int exchangeList_pageSize();

    @DefaultStringValue("Below are all of your exchanges.  To work with an exchange, " +
                        "click on it.  To create a new exchange, click the Create " +
                        "New Exchange button.  You can also delete exchanges that you " +
                        "are no longer using.")
    String exchangeList_paragraph1();

    @DefaultStringValue("Create New")
    String exchangeList_createButton();

    @DefaultStringValue("Delete")
    String exchangeList_deleteButton();

    @DefaultStringValue("You do not have any exchanges.  Click \"Create New\" to create one.")
    String exchangeList_noRows();

    @DefaultStringValue("Exchange Name")
    String exchangeList_nameTitle();

    @DefaultStringValue("State")
    String exchangeList_stateTitle();

    @DefaultStringValue("My New Exchange")
    String exchangeList_newExchangeName();

    @DefaultStringValue("Confirm Delete")
    String exchangeList_confirmDeleteTitle();

    @DefaultStringValue("Are you sure you want to delete this exchange?")
    String exchangeList_confirmDeleteMessageOne();

    @DefaultStringValue("Are you sure you want to delete these exchanges?")
    String exchangeList_confirmDeleteMessageSeveral();


    /* ***************************
     *  Available exchange states
     * ***************************/

    @DefaultStringValue("New")
    String state_new();

    @DefaultStringValue("Started")
    String state_started();

    @DefaultStringValue("Assignments Made")
    String state_generated();

    @DefaultStringValue("Invitations Sent")
    String state_sent();


    /* *************************
     *  Edit exchange constants
     * *************************/

    @DefaultIntValue(10)
    int editExchange_pageSize();

    @DefaultStringValue("Save")
    String editExchange_saveButton();

    @DefaultStringValue("Revert Changes")
    String editExchange_resetButton();

    @DefaultStringValue("<< Return To List")
    String editExchange_returnToListButton();

    @DefaultStringValue("Add")
    String editExchange_addParticipantButton();

    @DefaultStringValue("Remove")
    String editExchange_deleteParticipantButton();

    @DefaultStringValue("Send Invitations")
    String editExchange_sendAllNotificationsButton();

    @DefaultStringValue("Resend Invitations")
    String editExchange_resendNotificationButton();

    @DefaultStringValue("Preview")
    String editExchange_previewButton();

    @DefaultStringValue("Save your changes.")
    String editExchange_saveTooltip();

    @DefaultStringValue("Revert all changes (including participants) since you saved last.")
    String editExchange_resetTooltip();

    @DefaultStringValue("Return to your list of exchanges.")
    String editExchange_returnToListTooltip();

    @DefaultStringValue("Add a new participant to this exchange.")
    String editExchange_addParticipantTooltip();

    @DefaultStringValue("Delete the selected participant(s) from this exchange.")
    String editExchange_deleteParticipantTooltip();

    @DefaultStringValue("Generate secret assignments and send email invitations to all participants.")
    String editExchange_sendAllNotificationsTooltip();

    @DefaultStringValue("Resend email invitations for the selected participant(s).")
    String editExchange_resendNotificationTooltip();

    @DefaultStringValue("Preview what an assignment email will look like.  The preview will use the first two recipients as an example.")
    String editExchange_previewTooltip();

    @DefaultStringValue("New Participant")
    String editExchange_newParticipantName();

    @DefaultStringValue("There are no participants.  Click \"Add\" to add one.")
    String editExchange_noRows();

    @DefaultStringValue("Name")
    String editExchange_nameTitle();

    @DefaultStringValue("Nickname")
    String editExchange_nicknameTitle();

    @DefaultStringValue("Email Address")
    String editExchange_emailTitle();

    @DefaultStringValue("Extra Information **")
    String editExchange_extraInfoLabel();

    @DefaultStringValue("Extra information that will be added to the bottom of the email invitations")
    String editExchange_extraInfoTooltip();

    @DefaultStringValue("Organizer Phone **")
    String editExchange_organizerPhoneLabel();

    @DefaultStringValue("The organizer's phone number.  Provide either phone number or email address (or both).")
    String editExchange_organizerPhoneTooltip();

    @DefaultStringValue("Organizer Email")
    String editExchange_organizerEmailLabel();

    @DefaultStringValue("The organizer's email address.  Provide either phone number or email address (or both).")
    String editExchange_organizerEmailTooltip();

    @DefaultStringValue("Organizer Name")
    String editExchange_organizerNameLabel();

    @DefaultStringValue("The organizer's full name, like \"Robert Smith\".")
    String editExchange_organizerNameTooltip();

    @DefaultStringValue("Suggested Cost")
    String editExchange_costLabel();

    @DefaultStringValue("The suggested amount that participants should spend, like \"$10-$15\".")
    String editExchange_costTooltip();

    @DefaultStringValue("Theme")
    String editExchange_themeLabel();

    @DefaultStringValue("A descriptive theme for the exchange like \"Chocolate!\" or \"White Elephant\"")
    String editExchange_themeTooltip();

    @DefaultStringValue("Date and Time")
    String editExchange_dateTimeLabel();

    @DefaultStringValue("The date and time the party will be held, like \"Christmas Eve\" or \"Saturday, December 4 @ 2:00pm\".")
    String editExchange_dateTimeTooltip();

    @DefaultStringValue("Exchange Name")
    String editExchange_exchangeNameLabel();

    @DefaultStringValue("A short, descriptive name for this exchange, like \"Smith Family Party\"")
    String editExchange_exchangeNameTooltip();

    @DefaultStringValue("Status")
    String editExchange_stateLabel();

    @DefaultStringValue("Are you sure?")
    String editExchange_resetConfirmTitle();

    @DefaultStringValue("Are you sure that you want to discard all of your changes?")
    String editExchange_resetConfirmMessage();

    @DefaultStringValue("Are you sure?")
    String editExchange_sendConfirmTitle();

    @DefaultStringValue("This will generate and send invitations to all participants. " +
                        "You should only do this after you have previewed the invitation " +
                        "and you are happy with it. Are you sure you want to continue?")
    String editExchange_sendConfirmMessage();

    @DefaultStringValue("Are you sure?")
    String editExchange_resendConfirmTitle();

    @DefaultStringValue("You have already sent emails for this exchange. " +
                        "If you do this, new assignments will be generated and " +
                        "participants will all get new email invitations. " +
                        "If you want to keep the existing assignments, but " +
                        "re-send invitations to one or more participants, " +
                        "check the checkbox for each participant and choose " +
                        "Resend Invitation.  Are you sure you want to continue?")
    String editExchange_resendConfirmMessage();

    @DefaultStringValue("Invitations sent successfully.")
    String editExchange_sendSuccessfulTitle();

    @DefaultStringValue("Invitations were sent successfully.")
    String editExchange_sendSuccessfulMessage();

    @DefaultStringValue("** These fields are optional")
    String editExchange_optional();

    @DefaultStringValue("The exchange you requested was not found.")
    String editExchange_exchangeNotFound();

    @DefaultStringValue("Choose another exchange from your list.")
    String editExchange_chooseAnotherExchange();

    @DefaultStringValue("Click Add and Remove to add and remove participants. " +
                        "Click Preview to see what your invitations will look like. " +
                        "When you are ready, click Send Invitations to generate assignments " +
                        "and send emails to all of the participants.  You can also resend an " +
                        "invitation to one or more particpants using the Resend Invitations button.")
    String editExchange_instructions();


    /* ****************************
     *  Edit participant constants
     * ****************************/

    @DefaultIntValue(10)
    int editParticipant_pageSize();

    @DefaultStringValue("Name")
    String editParticipant_nameLabel();

    @DefaultStringValue("The full name of the participant, like \"Robert Smith\"")
    String editParticipant_nameTooltip();

    @DefaultStringValue("Nickname")
    String editParticipant_nicknameLabel();

    @DefaultStringValue("The nickname or familiar name of the participant, like \"Bob\"")
    String editParticipant_nicknameTooltip();

    @DefaultStringValue("Email Address")
    String editParticipant_emailAddressLabel();

    @DefaultStringValue("The email address of the participant")
    String editParticipant_emailAddressTooltip();

    @DefaultStringValue("Receives gift from: ")
    String editParticipant_giftGiverLabel();

    @DefaultStringValue("The other participant that this participant will receive a gift from")
    String editParticipant_giftGiverTooltip();

    @DefaultStringValue("Gives gift to: ")
    String editParticipant_giftReceiverLabel();

    @DefaultStringValue("The other participant that this participant will give a gift to")
    String editParticipant_giftReceiverTooltip();

    @DefaultStringValue("Save")
    String editParticipant_saveButton();

    @DefaultStringValue("Save changes to this participant.")
    String editParticipant_saveTooltip();

    @DefaultStringValue("Cancel")
    String editParticipant_cancelButton();

    @DefaultStringValue("Discard changes to this participant.")
    String editParticipant_cancelTooltip();

    @DefaultStringValue("<none>")
    String editParticipant_noAssignment();

    @DefaultStringValue("Reveal who participant receives gift from")
    String editParticipant_revealGiftGiver();

    @DefaultStringValue("Reveal who participant gives gift to")
    String editParticipant_revealGiftReceiver();

    @DefaultStringValue("Add Conflict")
    String editParticipant_addConflictButton();

    @DefaultStringValue("Add the participant from the drop-down as a conflict.\n" +
                        "A conflict is someone that this participant is not allowed to give a gift to.")
    String editParticipant_addConflictTooltip();

    @DefaultStringValue("Remove Selected")
    String editParticipant_deleteConflictButton();

    @DefaultStringValue("Remove the conflict(s) which are selected in the table.\n" +
                        "A conflict is someone that this participant is not allowed to give a gift to.")
    String editParticipant_deleteConflictTooltip();

    @DefaultStringValue("Name")
    String editParticipant_nameTitle();

    @DefaultStringValue("Email Address")
    String editParticipant_emailTitle();

    @DefaultStringValue("There are no conflicts.  Click \"Add Conflict\" to add one.")
    String editParticipant_noConflicts();

    @DefaultStringValue("Conflict to add: ")
    String editParticipant_conflictLabel();


    /* *************************
     *  Preview popup constants
     * *************************/

    @DefaultStringValue("Email Notification Preview")
    String preview_title();

    @DefaultStringValue("HTML Version")
    String preview_htmlTitle();

    @DefaultStringValue("Text Version")
    String preview_textTitle();

    @DefaultStringValue("Close")
    String preview_close();


    /* ******************************
     *  Bug report-related constants
     * ******************************/

    @DefaultStringValue("Report a Problem")
    String bugReport_title();

    @DefaultStringValue("To report a problem, fill in the information below.")
    String bugReport_instructions();

    @DefaultStringValue("Report Date")
    String bugReport_reportDate();

    @DefaultStringValue("Submitting User")
    String bugReport_submittingUser();

    @DefaultStringValue("Application Version")
    String bugReport_applicationVersion();

    @DefaultStringValue("Email Address **")
    String bugReport_emailAddress();

    @DefaultStringValue("Problem Summary")
    String bugReport_problemSummary();

    @DefaultStringValue("Detailed Description")
    String bugReport_detailedDescription();

    @DefaultStringValue("** These fields are optional")
    String bugReport_optional();

    @DefaultStringValue("Submit")
    String bugReport_submit();

    @DefaultStringValue("Cancel")
    String bugReport_cancel();

}
