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

import static com.cedarsolutions.junit.util.Assertions.EMPTY_STRINGS;
import static com.cedarsolutions.junit.util.Assertions.assertContainsMessage;
import static com.cedarsolutions.junit.util.Assertions.assertOnlyMessage;
import static com.cedarsolutions.junit.util.Assertions.assertSummary;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.NULL;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.junit.gae.EmailTestUtils;
import com.cedarsolutions.santa.server.config.BugReportServiceConfig;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.server.service.IEmailService;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.cedarsolutions.shared.domain.email.EmailTemplate;
import com.cedarsolutions.util.DateUtils;

/**
 * Unit tests for BugReportService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportServiceTest {

    /** Directory where templates are stored. */
    private static final String TEMPLATE_DIR = "resources/templates";

    /** Base directory where results are stored. */
    private static final String RESULTS_DIR = "test/com/cedarsolutions/santa/server/service/impl/results/bugreport";

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        BugReportService service = new BugReportService();
        assertNull(service.getConfig());
        assertNull(service.getEmailService());

        BugReportServiceConfig config = mock(BugReportServiceConfig.class);
        service.setConfig(config);
        assertSame(config, service.getConfig());

        IEmailService emailService = mock(IEmailService.class);
        service.setEmailService(emailService);
        assertSame(emailService, service.getEmailService());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        BugReportServiceConfig config = mock(BugReportServiceConfig.class);
        IEmailService emailService = mock(IEmailService.class);
        BugReportService service = new BugReportService();

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

    /** Test submitBugReport(), confirming that the Velocity template renders (non-empty object). */
    @Test public void testSubmitBugReportTemplate() throws Exception {
        BugReport bugReport = createValidBugReport();
        BugReportService service = createService();

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        service.submitBugReport(bugReport);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertSame(bugReport, template.getValue().getTemplateContext().get("bugReport"));
        validateTemplateContents(template.getValue(), service);
        validateMessageContents(message.getValue(), "notempty");
    }

    /** Test validation, null bug report. */
    @Test public void testValidationNullBugReport() {
        try {
            BugReport bugReport = null;
            BugReportService service = createService();
            service.submitBugReport(bugReport);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertOnlyMessage(e, NULL);
        }
    }

    /** Test validation, empty bug report. */
    @Test public void testValidationEmptyBugReport() {
        try {
            BugReport bugReport = new BugReport();
            BugReportService service = createService();
            service.submitBugReport(bugReport);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertContainsMessage(e, REQUIRED, "reportDate");
            assertContainsMessage(e, REQUIRED, "applicationVersion");
            assertContainsMessage(e, REQUIRED, "submittingUser");
            assertContainsMessage(e, REQUIRED, "problemSummary");
            assertContainsMessage(e, REQUIRED, "detailedDescription");
        }
    }

    /** Test validation, empty report date. */
    @Test public void testValidationEmptyReportDate() {
        BugReport bugReport = createValidBugReport();
        bugReport.setReportDate(null);
        assertFieldIsRequired(bugReport, "reportDate", null);
    }

    /** Test validation, empty application version. */
    @Test public void testValidationEmptyApplicationVersion() {
        BugReport bugReport = createValidBugReport();
        for (String value : EMPTY_STRINGS) {
            bugReport.setApplicationVersion(value);
            assertFieldIsRequired(bugReport, "applicationVersion", value);
        }
    }

    /** Test validation, empty email address. */
    @Test public void testValidationEmptyEmailAddress() {
        BugReport bugReport = createValidBugReport();
        for (String value : EMPTY_STRINGS) {
            bugReport.setEmailAddress(value);
            assertFieldIsOptional(bugReport, "emailAddress", value);
        }
    }

    /** Test validation, empty submitting user. */
    @Test public void testValidationEmptySubmittingUser() {
        BugReport bugReport = createValidBugReport();
        bugReport.setSubmittingUser(null);
        assertFieldIsRequired(bugReport, "submittingUser", null);
    }

    /** Test validation, empty summary. */
    @Test public void testValidationEmptySummary() {
        BugReport bugReport = createValidBugReport();
        for (String value : EMPTY_STRINGS) {
            bugReport.setProblemSummary(value);
            assertFieldIsRequired(bugReport, "problemSummary", value);
        }
    }

    /** Test validation, empty description. */
    @Test public void testValidationEmptyDescription() {
        BugReport bugReport = createValidBugReport();
        for (String value : EMPTY_STRINGS) {
            bugReport.setDetailedDescription(value);
            assertFieldIsRequired(bugReport, "detailedDescription", value);
        }
    }

    /**
     * Validate the contents of an email template.
     * @param template   Template to validate
     * @param type       Type of bugreport that is expected
     * @param service    Service which generated the template
     */
    private static void validateTemplateContents(EmailTemplate template, BugReportService service) {
        EmailTestUtils.validateTemplateContents(template,
                                                EmailFormat.MULTIPART,
                                                service.getConfig().getTemplateGroup(),
                                                service.getConfig().getTemplateName(),
                                                service.getConfig().getSender(),
                                                service.getConfig().getReplyTo(),
                                                service.getConfig().getRecipients());
    }

    /**
     * Validate the contents of an email message.
     * @param message    Message to validate
     * @param type       Type of bugreport that is expected
     * @param testName   Name of the test, used to find the expected results on disk
     */
    private static void validateMessageContents(EmailMessage message, String testName) {
        EmailTestUtils.validateMessageContents(message, EmailFormat.MULTIPART, RESULTS_DIR, "bug", testName);
    }

    /** Assert that a field is required. */
    private static void assertFieldIsRequired(BugReport bugReport, String field, String value) {
        try {
            BugReportService service = createService();
            service.submitBugReport(bugReport);
            fail("Expected InvalidDataException for value [" + value + "]");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertOnlyMessage(e, REQUIRED, field);
        }
    }

    /** Assert that a field is optional. */
    private static void assertFieldIsOptional(BugReport bugReport, String field, String value) {
        try {
            BugReportService service = createService();
            service.submitBugReport(bugReport);
        } catch (InvalidDataException e) {
            fail("Got unexpected InvalidDataException for value [" + value + "]");
        }
    }

    /** Create a mocked service that actually renders the underlying template. */
    private static BugReportService createService() {
        IEmailService emailService = EmailTestUtils.createPartiallyMockedEmailService(TEMPLATE_DIR);
        BugReportServiceConfig config = mock(BugReportServiceConfig.class);

        List<EmailAddress> recipients = new ArrayList<EmailAddress>();
        recipients.add(new EmailAddress("one@example.com"));
        recipients.add(new EmailAddress("two@example.com"));

        when(config.getTemplateGroup()).thenReturn("bugreport");
        when(config.getTemplateName()).thenReturn("bug");
        when(config.getSender()).thenReturn(new EmailAddress("Santa Exchange", "santa@example.com"));
        when(config.getReplyTo()).thenReturn(new EmailAddress("noreply@example.com"));
        when(config.getRecipients()).thenReturn(recipients);

        BugReportService service = new BugReportService();
        service.setConfig(config);
        service.setEmailService(emailService);
        service.afterPropertiesSet();

        return service;
    }

    /** Create a valid bug report for testing. */
    private static BugReport createValidBugReport() {
        FederatedUser submittingUser = new FederatedUser();
        submittingUser.setUserId("id");
        submittingUser.setUserName("name");

        BugReport bugReport = new BugReport();
        bugReport.setReportDate(DateUtils.createDate(2011, 11, 12, 14, 36));
        bugReport.setApplicationVersion("version");
        bugReport.setSubmittingUser(submittingUser);
        bugReport.setEmailAddress("email");
        bugReport.setProblemSummary("summary");
        bugReport.setDetailedDescription("detailed description of the problem");
        return bugReport;
    }
}
