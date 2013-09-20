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
package com.cedarsolutions.santa.client.common.widget;

import com.google.gwt.i18n.client.Constants;

/**
 * User interface constants used by widgets.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface WidgetConstants extends Constants {


    /* ***********************************
     *  Constants for DropdownList widget
     * ***********************************/

    @DefaultStringValue("Any")
    String dropdown_Any();


    /* *************************************
     *  Constants for the ErrorPopup widget
     * *************************************/

    @DefaultStringValue("Error Occurred")
    String errorPopup_dialogTitle();

    @DefaultStringValue("Error Message")
    String errorPopup_messageTextHeader();

    @DefaultStringValue("Supporting Information")
    String errorPopup_supportingTextHeader();

    @DefaultStringValue("Exception Stack Trace")
    String errorPopup_exceptionTextHeader();

    @DefaultStringValue("Close")
    String errorPopup_closeButtonText();

    @DefaultStringValue("Close the popup window")
    String errorPopup_closeButtonTooltip();

    @DefaultStringValue("Loading...")
    String progressIndicator_loading();

    @DefaultStringValue("Please wait...")
    String progressIndicator_pleaseWait();


    /* *****************************
     *  Constants for the YesNoList
     * *****************************/

    @DefaultStringValue("Yes")
    String yesNoList_yes();

    @DefaultStringValue("No")
    String yesNoList_no();


    /* ****************************************
     *  Descriptive names for OpenId providers
     * ****************************************/

    @DefaultStringValue("Google")
    String openId_Google();

    @DefaultStringValue("Yahoo")
    String openId_Yahoo();

    @DefaultStringValue("MySpace")
    String openId_MySpace();

    @DefaultStringValue("AOL")
    String openId_AOL();

    @DefaultStringValue("myOpenId")
    String openId_myOpenId();


    /* **************************
     *  Login-related constants
     * **************************/

    @DefaultStringValue("What is OpenId?")
    String login_openIdDisclosureText();

    @DefaultStringValue("You can sign in to this application using any of the OpenId providers shown in " +
                        "the list.")
    String login_openIdExplanation1Text();

    @DefaultStringValue("When you choose to sign in using an OpenId provider, you will be " +
                        "redirected to that provider's web site.  The provider will tell this " +
                        "application who you are and that you have signed in correctly.  Your " +
                        "password is never shared with this application.")
    String login_openIdExplanation2Text();

    @DefaultStringValue("If you have more than one OpenId username, you should make sure to use the same " +
                        "one every time you sign in here.  If you do not have an OpenId username already, " +
                        "the easiest way to get one is to sign up for a gmail account.  Then, sign in here " +
                        "using the Google OpenId provider.")
    String login_openIdExplanation3Text();

    @DefaultStringValue("Sign in using OpenId:")
    String login_openIdInstructions();

    @DefaultStringValue("Sign In")
    String login_openIdButtonText();

    @DefaultStringValue("You're already logged in")
    String login_continueDisclosureText();

    @DefaultStringValue("You have already logged in.  Click the button " +
                        "above to enter the web site and start working " +
                        "on your Secret Santa exchange.")
    String login_continueExplanationText();

    @DefaultStringValue("Enter Site")
    String login_continueButtonText();


    /* *************************
     *  About-related constants
     * *************************/

    @DefaultStringValue("About Santa Exchange")
    String about_title();

    @DefaultStringValue("Close")
    String about_close();

    @DefaultStringValue("A \"Secret Santa\" exchange is a party where people get together " +
                        "to exchange surprise gifts.  The gift assignments are secret, and " +
                        "the gifts are usually fairly small.")
    String about_paragraph1();

    @DefaultStringValue("This application helps you organize a Secret Santa exchange. " +
                        "You enter the names and email addresses of the participants, and provide some " +
                        "information about how the exchange will be organized.  Then, the application " +
                        "randomly generates the assignments and emails off invitations to each of the " +
                        "participants.")
    String about_paragraph2();

    @DefaultStringValue("The configuration that you create for an exchange will be stored in Google's " +
                        "cloud, and no other users will be able to see it.  The information you enter " +
                        "will not be used for any other purpose.")
    String about_paragraph3();

    @DefaultStringValue("To report problems with the application, use the Report a Problem menu option.")
    String about_paragraph4();

    @DefaultStringValue("This is open source software, released under the " +
                        "<a href=\"%s\">Apache v2.0</a> license. " +
                        "Source code and documentation can be found " +
                        "at <a href=\"%s\">Google Code</a>.")
    String about_paragraph5TextFormat();


    /* ***************************************
     *  Constants for the WelcomePopup widget
     * ***************************************/

    @DefaultStringValue("Welcome!")
    String welcome_title();

    @DefaultStringValue("Close")
    String welcome_close();

    @DefaultStringValue("Welcome to SantaExchange!")
    String welcome_paragraph1();

    @DefaultStringValue("Thanks for visiting.  We're glad to have you here.")
    String welcome_paragraph2();

    @DefaultStringValue("If you notice any problems, please use the Report a Problem menu option to tell us.")
    String welcome_paragraph3();


    /* *******************************
     *  Confirmation pop-up constants
     * *******************************/

    @DefaultStringValue("Yes")
    String confirmationPopup_ok();

    @DefaultStringValue("No")
    String confirmationPopup_cancel();


    /* ********************************
     *  Informational pop-up constants
     * ********************************/

    @DefaultStringValue("Ok")
    String informationalPopup_close();

}
