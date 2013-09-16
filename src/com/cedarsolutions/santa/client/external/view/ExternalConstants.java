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
package com.cedarsolutions.santa.client.external.view;

import com.google.gwt.i18n.client.Constants;

/**
 * User interface constants used by this module.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface ExternalConstants extends Constants {

    /* **************************
     *  Account locked constants
     * **************************/

    @DefaultStringValue("Account Locked")
    String accountLocked_headerText();

    @DefaultStringValue("Your account has been locked by an administrator.  " +
                        "As long a your account remains locked, you will not " +
                        "be allowed to use the Santa Exchange web site.")
    String accountLocked_accountLockedText();

    @DefaultStringValue("Continue")
    String accountLocked_continueButton();

    @DefaultStringValue("Return to the landing page.")
    String accountLocked_continueTooltip();


    /* **************************
     *  Login required constants
     * **************************/

    @DefaultStringValue("Login Required")
    String loginRequired_headerText();

    @DefaultStringValue("The page you requested requires you to be logged in. " +
                        "To continue, please choose an OpenId provider and then " +
                        "click the Sign In button.")
    String loginRequired_loginRequiredText();


    /* ************************
     *  Landing page constants
     * ************************/

    @DefaultStringValue("Secret Santa Exchange")
    String landingPage_headerText();

    @DefaultStringValue("A \"Secret Santa\" exchange is a party where people get together " +
                        "to exchange surprise gifts. The gift assignments are supposed " +
                        "to be secret, so that no one knows ahead of time who they will get " +
                        "their gift from.  Usually, the gifts are fairly small.")
    String landingPage_paragraph1Text();

    @DefaultStringValue("This application helps you organize a Secret Santa exchange.  You enter the " +
                        "names and email addresses of the participants, and provide some information about " +
                        "how the exchange will be organized.  Then, the application randomly generates the " +
                        "assignments and emails off invitations to each of the participants.  You have " +
                        "the chance to customize the invitations and review the emails before they are " +
                        "sent, if you want.  You can also re-send an invitation if someone loses theirs. ")
    String landingPage_paragraph2Text();

    @DefaultStringValue("The configuration that you create for an exchange will be stored in Google's " +
                        "cloud, and no other users will be able to see it.  The information you enter " +
                        "will not be used for any other purpose.")
    String landingPage_paragraph3Text();

    @DefaultStringValue("This is open source software, released under the " +
                        "<a href=\"%s\">Apache v2.0</a> license. " +
                        "Source code and documentation can be found " +
                        "at <a href=\"%s\">Google Code</a>.")
    String landingPage_paragraph4TextFormat();

}
