/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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

import static com.cedarsolutions.santa.shared.domain.MessageKeys.FAILURE;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cedarsolutions.exception.CedarRuntimeException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.server.config.ExchangeServiceConfig;
import com.cedarsolutions.santa.server.service.IExchangeService;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.santa.shared.domain.exchange.TemplateConfig;
import com.cedarsolutions.server.service.IEmailService;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.shared.domain.LocalizableMessage;
import com.cedarsolutions.shared.domain.ValidationErrors;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.cedarsolutions.shared.domain.email.EmailTemplate;
import com.cedarsolutions.util.RandomNumberUtils;
import com.cedarsolutions.util.StringUtils;

/**
 * Functionality manage Secret Santa exchanges.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeService extends AbstractService implements IExchangeService {

    /** Service configuration. */
    private ExchangeServiceConfig config;

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
            throw new NotConfiguredException("ExchangeService is not properly configured.");
        }

        if (this.getConfig().getMaxAttempts() < 1) {
            throw new NotConfiguredException("ExchangeService: maximum attempts must be at least 1");
        }
    }

    /**
     * Send the email message for an assignment.
     * @param exchange       Exchange that assignment is related to
     * @param assignment     Assignment to generate an email for
     * @param organizerOnly  Whether the email should be sent to the organizer only
     * @throws InvalidDataException If the exchange is not valid.
     */
    @Override
    public void sendMessage(Exchange exchange, Assignment assignment, boolean organizerOnly) throws InvalidDataException {
        validateExchange(exchange);
        EmailTemplate message = this.generateMessage(exchange, assignment, organizerOnly);
        this.emailService.sendEmail(message);
    }

    /**
     * Send all email messages for a set of assignments.
     * @param exchange       Exchange that assignment is related to
     * @param assignments    Assignments to generate an email for
     * @param organizerOnly  Whether the email should be sent to the organizer only
     * @return The number of emails that were sent.
     * @throws InvalidDataException If the exchange is not valid.
     */
    @Override
    public int sendMessages(Exchange exchange, AssignmentSet assignments, boolean organizerOnly) throws InvalidDataException {
        validateExchange(exchange);

        for (Assignment assignment : assignments) {
            this.sendMessage(exchange, assignment, organizerOnly);
        }

        return assignments.size();
    }

    /**
     * Generate assignments for the passed-in exchange.
     * @param exchange      Exchange to generate assignments for
     * @param autoConflict  Whether automatic conflict detection should be enabled
     * @return Randomly-generated set of assignment for this exchange.
     * @throws InvalidDataException If the exchange is not valid or assignments could not be generated
     * @throws ServiceException If internal configuration is invalid
     */
    @Override
    public AssignmentSet generateAssignments(Exchange exchange, boolean autoConflict) throws InvalidDataException, ServiceException {
        if (this.getConfig().getMaxAttempts() < 1) {
            throw new ServiceException("Internal error: configured maximum attempts is invalid");
        }

        validateExchange(exchange);
        for (int i = 0; i < this.getConfig().getMaxAttempts(); i++) {
            try {
                return generateAssignments(exchange.getParticipants(), autoConflict);
            } catch (CedarRuntimeException e) { }
        }

        String message = "Unable to generate assignments after " + this.getConfig().getMaxAttempts() + " attempts";
        throw new InvalidDataException(new LocalizableMessage(FAILURE, "generateAssignments", message));
    }

    /**
     * Generate a preview for an exchange notification email.
     * @param exchange      Exchange to operate on
     * @return A preview of the exchange notification email
     * @throws InvalidDataException If the exchange is not valid.
     */
    @Override
    public EmailMessage generatePreview(Exchange exchange) throws InvalidDataException {
        validateExchange(exchange);
        Participant giver = exchange.getParticipants().get(0);
        Participant receiver = exchange.getParticipants().get(1);
        Assignment assignment = new Assignment(giver, receiver);
        EmailTemplate template = this.generateMessage(exchange, assignment, false);
        return this.emailService.generateEmail(template);
    }

    /**
     * Generate assignments for the passed-in set of participants.
     * @param participants           Set of participants to operate on
     * @param autoConflictDetection  Whether automatic conflict detection should be enabled
     * @return Randomly-generated set of assignment for this exchange.
     */
    private static AssignmentSet generateAssignments(ParticipantSet participants, boolean autoConflictDetection)  {
        AssignmentSet assignments = new AssignmentSet();

        Map<Long, Participant> available = new HashMap<Long, Participant>();
        for (Participant participant : participants) {
            available.put(participant.getId(), participant);
        }

        for (Participant giftGiver : participants) {
            Map<Long, Participant> allowable = new HashMap<Long, Participant>(available);

            allowable.remove(giftGiver.getId());
            for (Participant participant : giftGiver.getConflicts()) {
                allowable.remove(participant.getId());
            }

            if (autoConflictDetection) {
                if (assignments.getGiftGiver(giftGiver) != null) {
                    allowable.remove(assignments.getGiftGiver(giftGiver).getId());
                }
            }

            if (allowable.isEmpty()) {
                throw new ServiceException("No allowable partipants remain.");
            }

            int index = RandomNumberUtils.generateRandomInteger(0, allowable.size() - 1);
            Participant giftReceiver = new ArrayList<Participant>(allowable.values()).get(index);  // turn it into a map which can be indexed
            assignments.add(new Assignment(giftGiver, giftReceiver));

            available.remove(giftReceiver.getId());
        }

        return assignments;
    }

    /**
     * Generate an email message for an assignment.
     * @param exchange       Exchange that assignment is related to
     * @param assignment     Assignment to generate an email for
     * @param organizerOnly  Whether the email should be sent to the organizer only
     * @return Email template suitable for passing to IEmailService.sendEmail().
     */
    protected EmailTemplate generateMessage(Exchange exchange, Assignment assignment, boolean organizerOnly) {
        TemplateConfig templateConfig = this.deriveTemplateConfig(exchange, assignment);

        exchange = new Exchange(exchange);  // operate on a copy, so we don't modify the caller's object

        // Wrap the input data, but use Windows newline because that's what the
        // templates use.  There should be a smarter way to do this (i.e. load
        // the templates and convert the line endings), but I haven't gotten
        // that working yet.
        exchange.setExtraInfo(StringUtils.wrapLine(exchange.getExtraInfo(), 75, "\r\n"));

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("exchange", exchange);
        context.put("assignment", assignment);

        EmailAddress sender = new EmailAddress(templateConfig.getSenderName(), this.config.getSender().getAddress());
        EmailAddress replyTo = new EmailAddress(exchange.getOrganizer().getName(), exchange.getOrganizer().getEmailAddress());

        // If we're doing organizer-only testing, send the emails to the organizer instead
        EmailAddress recipient = new EmailAddress(assignment.getGiftGiver().getName(), assignment.getGiftGiver().getEmailAddress());
        if (organizerOnly) {
            recipient.setAddress(exchange.getOrganizer().getEmailAddress());
        }

        EmailTemplate template = new EmailTemplate();
        template.setFormat(templateConfig.getEmailFormat());
        template.setTemplateContext(context);
        template.setSender(sender);
        template.setReplyTo(replyTo);
        template.setRecipients(recipient);
        template.setTemplateGroup(templateConfig.getTemplateGroup());
        template.setTemplateName(templateConfig.getTemplateName());

        return template;
    }

    /**
     * Derive the proper template configuration to use.
     *
     * <p>
     * This is pulled out into its own method so we can make decisions
     * based on other criteria if needed.  For instance, an exchange
     * might set a preferred language, and that could (somehow?) change
     * the sender name.
     * </p>
     *
     * <p>
     * Each of the template configuration values has a default. The values
     * can be overridden on either the exchange level or the assignment level.
     * If both exchange an assignment override a value, the assignment takes
     * precedence.
     * </p>
     *
     * @param exchange    Exchange to reference
     * @param assignment  Assignment to reference
     *
     * @return Template configuration to use.
     */
    protected TemplateConfig deriveTemplateConfig(Exchange exchange, Assignment assignment) {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setSenderName(this.config.getSender().getName());
        templateConfig.setTemplateGroup(this.config.getTemplateGroup());
        templateConfig.setEmailFormat(this.config.getDefaultEmailFormat());
        templateConfig.setTemplateName(this.config.getDefaultTemplateName());

        if (assignment.getGiftReceiver().getTemplateOverrides().getSenderName() != null) {
            templateConfig.setSenderName(assignment.getGiftReceiver().getTemplateOverrides().getSenderName());
        } else if (exchange.getTemplateOverrides().getSenderName() != null) {
            templateConfig.setSenderName(exchange.getTemplateOverrides().getSenderName());
        }

        if (assignment.getGiftReceiver().getTemplateOverrides().getEmailFormat() != null) {
            templateConfig.setEmailFormat(assignment.getGiftReceiver().getTemplateOverrides().getEmailFormat());
        } else if (exchange.getTemplateOverrides().getEmailFormat() != null) {
            templateConfig.setEmailFormat(exchange.getTemplateOverrides().getEmailFormat());
        }

        if (assignment.getGiftReceiver().getTemplateOverrides().getTemplateName() != null) {
            templateConfig.setTemplateName(assignment.getGiftReceiver().getTemplateOverrides().getTemplateName());
        } else if (exchange.getTemplateOverrides().getTemplateName() != null) {
            templateConfig.setTemplateName(exchange.getTemplateOverrides().getTemplateName());
        }

        return templateConfig;
    }

    /** Validate the contents of an exchange. */
    protected static void validateExchange(Exchange exchange) throws InvalidDataException {
        ValidationErrors details = new ValidationErrors(INVALID, "Exchange is invalid");
        InvalidDataException exception = new InvalidDataException("Exchange is invalid", details);

        if (StringUtils.isEmpty(exchange.getName())) {
            details.addMessage(REQUIRED, "name", "Exchange name is required");
        }

        if (StringUtils.isEmpty(exchange.getDateAndTime())) {
            details.addMessage(REQUIRED, "dateAndTime", "Date and time field is required");
        }

        if (StringUtils.isEmpty(exchange.getTheme())) {
            details.addMessage(REQUIRED, "theme", "Theme is required");
        }

        if (StringUtils.isEmpty(exchange.getCost())) {
            details.addMessage(REQUIRED, "cost", "Suggested cost is required");
        }

        if (StringUtils.isEmpty(exchange.getOrganizer().getName())) {
            details.addMessage(REQUIRED, "organizerName", "Organizer name is required");
        }

        if (StringUtils.isEmpty(exchange.getOrganizer().getEmailAddress()) && StringUtils.isEmpty(exchange.getOrganizer().getPhoneNumber())) {
            details.addMessage(REQUIRED, "organizerEmailAddress", "Provide either email address, phone number, or both");
        }

        if (exchange.getParticipants().size() < 2) {
            details.addMessage(REQUIRED, "participants", "At least 2 participants are required");
        }

        validateParticipants(exchange.getParticipants(), details);

        if (!details.getMessages().isEmpty()) {
            throw exception;
        }
    }

    /** Validate a set of participants. */
    private static void validateParticipants(ParticipantSet participants, ValidationErrors details) {
        boolean nameInvalid = false;
        boolean nicknameInvalid = false;
        boolean emailInvalid = false;

        for (Participant participant : participants) {
            if (StringUtils.isEmpty(participant.getName())) {
                nameInvalid = true;
            }

            if (StringUtils.isEmpty(participant.getNickname())) {
                nicknameInvalid = true;
            }

            if (StringUtils.isEmpty(participant.getEmailAddress())) {
                emailInvalid = true;
            }
        }

        if (nameInvalid) {
            details.addMessage(REQUIRED, "participantName", "All participants must have a name");
        }

        if (nicknameInvalid) {
            details.addMessage(REQUIRED, "participantNickname", "All participants must have a nickname");
        }

        if (emailInvalid) {
            details.addMessage(REQUIRED, "participantEmailAddress", "All participants must have an email address");
        }
    }

    public ExchangeServiceConfig getConfig() {
        return this.config;
    }

    public void setConfig(ExchangeServiceConfig config) {
        this.config = config;
    }

    public IEmailService getEmailService() {
        return this.emailService;
    }

    public void setEmailService(IEmailService emailService) {
        this.emailService = emailService;
    }

}
