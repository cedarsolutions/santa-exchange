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
package com.cedarsolutions.santa.server.service.impl;

import static com.cedarsolutions.santa.server.domain.notification.NotificationType.REGISTER;

import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.server.config.NotificationServiceConfig;
import com.cedarsolutions.santa.server.domain.notification.NotificationType;
import com.cedarsolutions.santa.server.service.INotificationService;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.server.service.IEmailService;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.shared.domain.email.EmailTemplate;

/**
 * Server-side notifications.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class NotificationService extends AbstractService implements INotificationService {

    /** Service configuration. */
    private NotificationServiceConfig config;

    /** Email service. */
    private IEmailService emailService;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.config == null || this.emailService == null) {
            throw new NotConfiguredException("NotificationService is not properly configured.");
        }
    }

    /**
     * Notify that a user has been registered.
     * @param registeredUser  Newly-registered user.
     */
    @Override
    public void notifyRegisteredUser(RegisteredUser registeredUser) {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("registeredUser", registeredUser);
        this.generateAndSendEmail(REGISTER, context);
    }

    /**
     * Generate and send an email for a given event.
     * @param type     Notification type
     * @param context  Context to use when generating email
     */
    private void generateAndSendEmail(NotificationType type, Map<String, Object> context) {
        EmailTemplate template = new EmailTemplate();
        template.setFormat(EmailFormat.MULTIPART);
        template.setSender(this.config.getSender());
        template.setReplyTo(this.config.getReplyTo());
        template.setRecipients(this.config.getRecipients());
        template.setTemplateGroup(this.config.getTemplateGroup());
        template.setTemplateName(type.getValue());
        template.setTemplateContext(context);
        this.emailService.sendEmail(template);
    }

    public NotificationServiceConfig getConfig() {
        return this.config;
    }

    public void setConfig(NotificationServiceConfig config) {
        this.config = config;
    }

    public IEmailService getEmailService() {
        return this.emailService;
    }

    public void setEmailService(IEmailService emailService) {
        this.emailService = emailService;
    }

}
