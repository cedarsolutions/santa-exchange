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
package com.cedarsolutions.santa.client;

import com.cedarsolutions.web.metadata.HttpStatusCode;
import com.google.gwt.i18n.client.Messages;

/**
 * Localized messages used project-wide.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface SantaExchangeMessages extends Messages {

    /* ***************************
     *  Bookmark-related messages
     * ***************************/

    @DefaultMessage("Your bookmark was not found.")
    String bookmark_bookmarkNotFound();

    @DefaultMessage("You will be redirected to a landing page.")
    String bookmark_youWillBeRedirected();


    /* **************************************
     *  Messages for AbstractServiceCallback
     * **************************************/

    @DefaultMessage("There was an error invoking a remote service.")
    String abstractRpcCallback_generalRpcErrorMessage();

    @DefaultMessage("There was an error invoking a remote service: you are not authorized.")
    String abstractRpcCallback_notAuthorizedRpcErrorMessage();

    @DefaultMessage("There was an error invoking a remote service: your request was blocked.")
    String abstractRpcCallback_requestBlockedMessage();

    @DefaultMessage("There was an error invoking a remote service: your request timed out.")
    String abstractRpcCallback_requestTimedOut();

    @DefaultMessage("There was an error invoking a remote service: no response was received from the server.")
    String abstractRpcCallback_noResponse();

    @DefaultMessage("There was an error invoking a remote service: the backend has been upgraded.")
    String abstractRpcCallback_backendUpgraded();

    @DefaultMessage("The remote service call was: {0}")
    String abstractRpcCallback_rpcName(String serviceMethod);

    @DefaultMessage("The HTTP/1.1 status code was: {1} ({0})")
    String abstractRpcCallback_httpStatusCode(HttpStatusCode name, int value);

    @DefaultMessage("The configured timeout was: {0} ms")
    String abstractRpcCallback_timeoutMs(int timeoutMs);

    @DefaultMessage("There was a security exception that prevented your request from being processed.")
    String abstractRpcCallback_securityException();

    @DefaultMessage("A timeout or lack of response can have several causes. For instance, there may " +
                    "have been a problem with your network connection, the back-end might be so " +
                    "busy that it did not respond in time, or the back-end might be completely down. " +
                    "Some of these problems are intermittent and your action might work if you try " +
                    "it again.  If the problem continues, it is probably best to close your browser " +
                    "and try again later.")
    String abstractRpcCallback_timeoutExplanation();

    @DefaultMessage("The backend has been upgraded and is incompatable with the version of " +
                    "the application you are running.  Close your browser and log back in to " +
                    "to get a new version of the application.")
    String abstractRpcCallback_closeBrowserDueToUpgrade();

    @DefaultMessage("If this behavior continues, you may want to log out and try again later.")
    String abstractRpcCallback_logOutAndTryAgain();

    @DefaultMessage("Message was: {0}")
    String abstractRpcCallback_message(String message);


    /* **********************
     *  Messages for filters
     * **********************/

    @DefaultMessage("The page you attempted to view requires admin access.")
    String filter_adminAccessRequired();

    @DefaultMessage("You will be redirected to a landing page.")
    String filter_youWillBeRedirected();

}
