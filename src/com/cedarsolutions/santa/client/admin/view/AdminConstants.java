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
package com.cedarsolutions.santa.client.admin.view;

import com.google.gwt.i18n.client.Constants;

/**
 * User interface constants used by this module.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface AdminConstants extends Constants {

    /* **************************
     *  Header-related constants
     * **************************/

    @DefaultStringValue("Santa Exchange Administrator Interface")
    String header_title();

    @DefaultStringValue("Application Dashboard")
    String header_dashboard();

    @DefaultStringValue("Internal Landing Page")
    String header_internalLandingPage();

    @DefaultStringValue("About Santa Exchange")
    String header_about();

    @DefaultStringValue("Report a Problem")
    String header_bugReport();

    @DefaultStringValue("Source Code")
    String header_sourceCode();

    @DefaultStringValue("Sign Out")
    String header_logout();


    /* ********************
     *  Home tab constants
     * ********************/

    @DefaultStringValue("Admin Home")
    String homeTab_tabTitle();

    @DefaultStringValue("This is the Santa Exchange Administrator Interface.  " +
                        "On the other tabs, you can find functionality that is " +
                        "useful to site administrators.  For instance, you can " +
                        "review audit events and manage user accounts.")
    String homeTab_paragraph1();

    @DefaultStringValue("Administrators can still use the normal Santa Exchange " +
                        "functionality, too.  On the menu at the upper right of " +
                        "your screen, use the Internal Landing Page menu option. " +
                        "This will take you to the landing page for normal users. " +
                        "From there, switch back to the administrator interface " +
                        "using the Admin Landing Page menu option.")
    String homeTab_paragraph2();


    /* *********************
     *  Audit tab constants
     * *********************/

    @DefaultStringValue("Auditing")
    String auditTab_tabTitle();

    @DefaultIntValue(15)
    int auditTab_pageSize();

    @DefaultStringValue("Filter")
    String auditTab_refreshButton();

    @DefaultStringValue("Clear")
    String auditTab_clearButton();

    @DefaultStringValue("Filter results")
    String auditTab_filterResults();

    @DefaultStringValue("Event Id")
    String auditTab_eventIdTitle();

    @DefaultStringValue("Event Type")
    String auditTab_eventTypeTitle();

    @DefaultStringValue("Event Timestamp")
    String auditTab_eventTimestampTitle();

    @DefaultStringValue("User Id")
    String auditTab_userIdTitle();

    @DefaultStringValue("Session Id")
    String auditTab_sessionIdTitle();

    @DefaultStringValue("Extra Data")
    String auditTab_extraDataTitle();

    @DefaultStringValue("Yes")
    String auditTab_yes();

    @DefaultStringValue("No")
    String auditTab_no();

    @DefaultStringValue("Event Date Range")
    String auditTab_eventDateCriteria();

    @DefaultStringValue("to")
    String auditTab_to();

    @DefaultStringValue("Event Type")
    String auditTab_eventTypeCriteria();

    @DefaultStringValue("User Id")
    String auditTab_userIdCriteria();

    @DefaultStringValue("No audit events match your filter criteria")
    String auditTab_noRows();


    /* ********************
     *  User tab constants
     * ********************/

    @DefaultStringValue("User Maintenance")
    String userTab_tabTitle();

    @DefaultIntValue(15)
    int userTab_pageSize();

    @DefaultStringValue("Delete")
    String userTab_deleteButton();

    @DefaultStringValue("Lock")
    String userTab_lockButton();

    @DefaultStringValue("Unlock")
    String userTab_unlockButton();

    @DefaultStringValue("Filter")
    String userTab_refreshButton();

    @DefaultStringValue("Clear")
    String userTab_clearButton();

    @DefaultStringValue("Filter results")
    String userTab_filterResults();

    @DefaultStringValue("")
    String userTab_actionTitle();

    @DefaultStringValue("User Id")
    String userTab_userIdTitle();

    @DefaultStringValue("User Name")
    String userTab_userNameTitle();

    @DefaultStringValue("Registered")
    String userTab_registrationDateTitle();

    @DefaultStringValue("Provider")
    String userTab_openIdProviderTitle();

    @DefaultStringValue("Logins")
    String userTab_loginsTitle();

    @DefaultStringValue("Last Login")
    String userTab_lastLoginTitle();

    @DefaultStringValue("Admin")
    String userTab_adminTitle();

    @DefaultStringValue("Locked")
    String userTab_lockedTitle();

    @DefaultStringValue("Yes")
    String userTab_yes();

    @DefaultStringValue("No")
    String userTab_no();

    @DefaultStringValue("Registration Date Range")
    String userTab_registrationDateCriteria();

    @DefaultStringValue("to")
    String userTab_to();

    @DefaultStringValue("User Name")
    String userTab_userNameCriteria();

    @DefaultStringValue("User Id")
    String userTab_userIdCriteria();

    @DefaultStringValue("Provider")
    String userTab_openIdProviderCriteria();

    @DefaultStringValue("Admin")
    String userTab_adminCriteria();

    @DefaultStringValue("Locked")
    String userTab_lockedCriteria();

    @DefaultStringValue("No users match your filter criteria")
    String userTab_noRows();

    @DefaultStringValue("Confirm Delete")
    String userTab_confirmDeleteTitle();

    @DefaultStringValue("Are you sure you want to delete these record(s)?")
    String userTab_confirmDeleteMessage();

}
