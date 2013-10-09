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
package com.cedarsolutions.santa.server.service.impl;

import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.NULL;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.server.config.BugReportServiceConfig;
import com.cedarsolutions.santa.server.service.IBugReportService;
import com.cedarsolutions.santa.shared.domain.BugReport;
import com.cedarsolutions.server.service.IEmailService;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.shared.domain.ValidationErrors;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.shared.domain.email.EmailTemplate;
import com.cedarsolutions.util.StringUtils;

/**
 * Bug report functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportService extends AbstractService implements IBugReportService {

    /** Service configuration. */
    private BugReportServiceConfig config;

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
            throw new NotConfiguredException("BugReportService is not properly configured.");
        }
    }

    /**
     * Submit a bug report.
     * @param bugReport  Bug report to submit
     * @throws InvalidDataException If the input data is not valid.
     */
    @Override
    public void submitBugReport(BugReport bugReport) throws InvalidDataException {
        validateBugReport(bugReport);
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("bugReport", bugReport);
        this.generateAndSendEmail(context);
    }

    /**
     * Generate and send an email for a given event.
     * @param type     Notification type
     * @param context  Context to use when generating email
     */
    private void generateAndSendEmail(Map<String, Object> context) {
        EmailTemplate template = new EmailTemplate();
        template.setFormat(EmailFormat.MULTIPART);
        template.setSender(this.config.getSender());
        template.setReplyTo(this.config.getReplyTo());
        template.setRecipients(this.config.getRecipients());
        template.setTemplateGroup(this.config.getTemplateGroup());
        template.setTemplateName(this.config.getTemplateName());
        template.setTemplateContext(context);
        this.emailService.sendEmail(template);
    }

    /** Validate the contents of a bug report. */
    private static void validateBugReport(BugReport bugReport) throws InvalidDataException {
        ValidationErrors details = new ValidationErrors(INVALID, "Bug report is invalid");
        InvalidDataException exception = new InvalidDataException("Bug report is invalid", details);

        if (bugReport == null) {
            details.addMessage(NULL, "Bug report is null");
        } else {
            if (bugReport.getReportDate() == null) {
                details.addMessage(REQUIRED, "reportDate", "Report date is required");
            }

            if (StringUtils.isEmpty(bugReport.getApplicationVersion())) {
                details.addMessage(REQUIRED, "applicationVersion", "Application version is required");
            }

            if (bugReport.getSubmittingUser() == null) {
                details.addMessage(REQUIRED, "submittingUser", "Submitting user is required");
            }

            if (StringUtils.isEmpty(bugReport.getProblemSummary())) {
                details.addMessage(REQUIRED, "problemSummary", "Summary is required");
            }

            if (StringUtils.isEmpty(bugReport.getDetailedDescription())) {
                details.addMessage(REQUIRED, "detailedDescription", "Description is required");
            }
        }

        if (!details.getMessages().isEmpty()) {
            throw exception;
        }
    }

    public BugReportServiceConfig getConfig() {
        return this.config;
    }

    public void setConfig(BugReportServiceConfig config) {
        this.config = config;
    }

    public IEmailService getEmailService() {
        return this.emailService;
    }

    public void setEmailService(IEmailService emailService) {
        this.emailService = emailService;
    }

}
