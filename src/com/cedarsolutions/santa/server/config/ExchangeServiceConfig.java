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
package com.cedarsolutions.santa.server.config;

import org.apache.log4j.Logger;

import com.cedarsolutions.config.PropertyBasedConfig;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Configuration for ExchangeService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeServiceConfig extends PropertyBasedConfig {

    /** Log4j logger. */
    private static final Logger LOGGER = LoggingUtils.getLogger(ExchangeServiceConfig.class);

    /** Template group used for all exchange emails. */
    private String templateGroup;

    /** Exchange email sender. */
    private EmailAddress sender;

    /** Default email format, if none is set on the exchange or the participant. */
    private EmailFormat defaultEmailFormat;

    /** Default email template, found within the template group. */
    private String defaultTemplateName;

    /** Maximum number of attempts to make when generating assignments. */
    private int maxAttempts;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        if (!this.isConfigured()) {
            throw new NotConfiguredException("ExchangeServiceConfig is not configured.");
        }

        this.templateGroup = this.parseRequiredString("ExchangeService.templateGroup");
        String senderName = this.parseRequiredString("ExchangeService.senderName");
        String senderAddress = this.parseRequiredString("ExchangeService.senderAddress");
        this.defaultEmailFormat = this.parseRequiredEmailFormat("ExchangeService.defaultEmailFormat");
        this.defaultTemplateName = this.parseRequiredString("ExchangeService.defaultTemplateName");
        this.maxAttempts = this.parseRequiredInteger("ExchangeService.maxAttempts");

        this.sender = new EmailAddress(senderName, senderAddress);

        LOGGER.debug(this.toString());
    }

    public String getTemplateGroup() {
        return this.templateGroup;
    }

    public EmailAddress getSender() {
        return this.sender;
    }

    public EmailFormat getDefaultEmailFormat() {
        return this.defaultEmailFormat;
    }

    public String getDefaultTemplateName() {
        return this.defaultTemplateName;
    }

    public int getMaxAttempts() {
        return this.maxAttempts;
    }

}
