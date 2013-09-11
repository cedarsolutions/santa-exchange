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

import com.google.gwt.i18n.client.ConstantsWithLookup;

/**
 * Message-constants used by this module.
 *
 * <p>
 * These are implemented as constants because there is no lookup mechanism for
 * messages.  Screen validation would be painful without lookup, so this is
 * what seems to make the most sense.  The down-side is that there's no
 * built-in argument formatting like with a real Messages interface.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface InternalMessageConstants extends ConstantsWithLookup {

    /* *********************
     *  Bug report messages
     * *********************/

    @DefaultStringValue("Bug report is invalid")
    String bugReport_invalid();

    @DefaultStringValue("Report date is required")
    String bugReport_required_reportDate();

    @DefaultStringValue("Application version is required")
    String bugReport_required_applicationVersion();

    @DefaultStringValue("Submitting user is required")
    String bugReport_required_submittingUser();

    @DefaultStringValue("Summary is required")
    String bugReport_required_problemSummary();

    @DefaultStringValue("Description is required")
    String bugReport_required_detailedDescription();


    /* ************************
     *  Edit exchange messages
     * ************************/

    @DefaultStringValue("Please fix the following problems: ")
    String editExchange_invalid();

    @DefaultStringValue("Failed to generate assignments.  You may have defined so many " +
                        "conflicts that there is no possible solution.  If that does not " +
                        "appear to be the cause, please use the Report a Problem menu option " +
                        "to report a bug, and a developer will look into it.")
    String editExchange_failure_generateAssignments();

    @DefaultStringValue("Exchange name is required")
    String editExchange_required_name();

    @DefaultStringValue("Date and time field is required")
    String editExchange_required_dateAndTime();

    @DefaultStringValue("Theme is required")
    String editExchange_required_theme();

    @DefaultStringValue("Suggested cost is required")
    String editExchange_required_cost();

    @DefaultStringValue("Organizer name is required")
    String editExchange_required_organizerName();

    @DefaultStringValue("Provide either email address, phone number, or both")
    String editExchange_required_organizerEmailAddress();

    @DefaultStringValue("At least 2 participants are required")
    String editExchange_required_participants();

    @DefaultStringValue("All participants must have a name")
    String editExchange_required_participantName();

    @DefaultStringValue("All participants must have an email address")
    String editExchange_required_participantEmailAddress();


    /* ***************************
     *  Edit participant messages
     * ***************************/

    @DefaultStringValue("Please fix the following problems: ")
    String editParticipant_invalid();

    @DefaultStringValue("Participant name is required")
    String editParticipant_required_name();

    @DefaultStringValue("Participant nickname is required")
    String editParticipant_required_nickname();

    @DefaultStringValue("Participant email address is required")
    String editParticipant_required_emailAddress();

}
