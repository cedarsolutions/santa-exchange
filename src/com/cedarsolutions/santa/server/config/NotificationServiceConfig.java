/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2012 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.server.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cedarsolutions.config.PropertyBasedConfig;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Configuration for NotificationService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class NotificationServiceConfig extends PropertyBasedConfig {

    /** Log4j logger. */
    private static final Logger LOGGER = LoggingUtils.getLogger(NotificationServiceConfig.class);

    /** Notification template group. */
    private String templateGroup;

    /** Notification sender. */
    private EmailAddress sender;

    /** Reply-to email address. */
    private EmailAddress replyTo;

    /** List of notification email addresses. */
    private List<EmailAddress> recipients;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        if (!this.isConfigured()) {
            throw new NotConfiguredException("NotificationServiceConfig is not configured.");
        }

        this.templateGroup = this.parseRequiredString("NotificationService.templateGroup");
        String senderName = this.parseRequiredString("NotificationService.senderName");
        String senderAddress = this.parseRequiredString("NotificationService.senderAddress");
        String replyToAddress = this.parseOptionalString("NotificationService.replyToAddress", null);
        List<String> recipientsList = this.parseRequiredStringList("NotificationService.recipients");

        this.sender = new EmailAddress(senderName, senderAddress);

        if (replyToAddress != null) {
            this.replyTo = new EmailAddress(replyToAddress);
        }

        this.recipients = new ArrayList<EmailAddress>();
        for (String value : recipientsList) {
            EmailAddress notificationAddress = new EmailAddress(value);
            this.recipients.add(notificationAddress);
        }

        LOGGER.debug(this.toString());
    }

    public String getTemplateGroup() {
        return this.templateGroup;
    }

    public EmailAddress getSender() {
        return this.sender;
    }

    public EmailAddress getReplyTo() {
        return this.replyTo;
    }

    public List<EmailAddress> getRecipients() {
        return this.recipients;
    }

}
