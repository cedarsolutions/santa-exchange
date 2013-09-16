/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
' *              C E D A R
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
package com.cedarsolutions.santa.server.service.impl;

import static com.cedarsolutions.santa.server.domain.notification.NotificationType.REGISTER;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.junit.gae.EmailTestUtils;
import com.cedarsolutions.santa.server.config.NotificationServiceConfig;
import com.cedarsolutions.santa.server.domain.notification.NotificationType;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.server.service.IEmailService;
import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.cedarsolutions.shared.domain.email.EmailTemplate;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for NotificationService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class NotificationServiceTest {

    /** Directory where templates are stored. */
    private static final String TEMPLATE_DIR = "resources/templates";

    /** Base directory where results are stored. */
    private static final String RESULTS_DIR = "test/com/cedarsolutions/santa/server/service/impl/results/notification";

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        NotificationService service = new NotificationService();
        assertNull(service.getConfig());
        assertNull(service.getEmailService());

        NotificationServiceConfig config = mock(NotificationServiceConfig.class);
        service.setConfig(config);
        assertSame(config, service.getConfig());

        IEmailService emailService = mock(IEmailService.class);
        service.setEmailService(emailService);
        assertSame(emailService, service.getEmailService());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        NotificationServiceConfig config = mock(NotificationServiceConfig.class);
        IEmailService emailService = mock(IEmailService.class);
        NotificationService service = new NotificationService();

        service.setConfig(config);
        service.setEmailService(emailService);
        service.afterPropertiesSet();

        try {
            service.setConfig(null);
            service.setEmailService(emailService);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            service.setConfig(config);
            service.setEmailService(null);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test notifyRegisteredUser(), confirming that the Velocity template renders (empty object). */
    @Test public void testNotifyRegisteredUserTemplateEmpty() throws Exception {
        RegisteredUser registeredUser = new RegisteredUser();
        NotificationService service = createService();

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        service.notifyRegisteredUser(registeredUser);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertSame(registeredUser, template.getValue().getTemplateContext().get("registeredUser"));
        validateTemplateContents(template.getValue(), REGISTER, service);
        validateMessageContents(message.getValue(), REGISTER, "empty");
    }

    /** Test notifyRegisteredUser(), confirming that the Velocity template renders (non-empty object). */
    @Test public void testNotifyRegisteredUserTemplateNotEmpty() throws Exception {
        TimeZone originalZone = TimeZone.getDefault();
        try {
            // This matters because the output has a date in it, formatted in local time
            TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

            RegisteredUser registeredUser = new RegisteredUser();
            registeredUser.setUserId("userId");
            registeredUser.setUserName("userName");
            registeredUser.setRegistrationDate(DateUtils.createDate("2011-06-14T00:00:00,000-0000"));
            registeredUser.setAuthenticationDomain("authenticationDomain");
            registeredUser.setOpenIdProvider(OpenIdProvider.GOOGLE);
            registeredUser.setFederatedIdentity("federatedIdentity");
            registeredUser.setEmailAddress("emailAddress");
            registeredUser.setAdmin(true);

            NotificationService service = createService();

            ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
            ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

            service.notifyRegisteredUser(registeredUser);
            verify(service.getEmailService()).sendEmail(template.capture());
            verify(service.getEmailService()).sendEmail(message.capture());

            assertSame(registeredUser, template.getValue().getTemplateContext().get("registeredUser"));
            validateTemplateContents(template.getValue(), REGISTER, service);
            validateMessageContents(message.getValue(), REGISTER, "notempty");
        } finally {
            TimeZone.setDefault(originalZone);
        }
    }

    /**
     * Validate the contents of an email template.
     * @param template   Template to validate
     * @param type       Type of notification that is expected
     * @param service    Service which generated the template
     */
    private static void validateTemplateContents(EmailTemplate template, NotificationType type, NotificationService service) {
        EmailTestUtils.validateTemplateContents(template,
                                                EmailFormat.MULTIPART,
                                                service.getConfig().getTemplateGroup(),
                                                type.getValue(),
                                                service.getConfig().getSender(),
                                                service.getConfig().getReplyTo(),
                                                service.getConfig().getRecipients());
    }

    /**
     * Validate the contents of an email message.
     * @param message    Message to validate
     * @param type       Type of notification that is expected
     * @param testName   Name of the test, used to find the expected results on disk
     */
    private static void validateMessageContents(EmailMessage message, NotificationType type, String testName) {
        EmailTestUtils.validateMessageContents(message, EmailFormat.MULTIPART, RESULTS_DIR, type.getValue(), testName);
    }

    /** Create a mocked service that actually renders the underlying template. */
    private static NotificationService createService() {
        IEmailService emailService = EmailTestUtils.createPartiallyMockedEmailService(TEMPLATE_DIR);
        NotificationServiceConfig config = mock(NotificationServiceConfig.class);

        List<EmailAddress> recipients = new ArrayList<EmailAddress>();
        recipients.add(new EmailAddress("one@example.com"));
        recipients.add(new EmailAddress("two@example.com"));

        when(config.getTemplateGroup()).thenReturn("notification");
        when(config.getSender()).thenReturn(new EmailAddress("Santa Exchange", "santa@example.com"));
        when(config.getReplyTo()).thenReturn(new EmailAddress("noreply@example.com"));
        when(config.getRecipients()).thenReturn(recipients);

        NotificationService service = new NotificationService();
        service.setConfig(config);
        service.setEmailService(emailService);
        service.afterPropertiesSet();

        return service;
    }

}
