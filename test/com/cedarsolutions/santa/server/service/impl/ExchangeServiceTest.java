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

import static com.cedarsolutions.junit.util.Assertions.assertOnlyMessage;
import static com.cedarsolutions.junit.util.Assertions.assertSummary;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.INVALID;
import static com.cedarsolutions.santa.shared.domain.MessageKeys.REQUIRED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.junit.gae.EmailTestUtils;
import com.cedarsolutions.santa.server.config.ExchangeServiceConfig;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Organizer;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.santa.shared.domain.exchange.TemplateConfig;
import com.cedarsolutions.server.service.IEmailService;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.cedarsolutions.shared.domain.email.EmailTemplate;
import com.cedarsolutions.util.StringUtils;

/**
 * Unit tests for ExchangeService.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeServiceTest {

    /** Maximum attempts that we should give the algorithm. */
    private static final int MAX_ATTEMPTS = 20;

    /** Number of times tests should be repeated. */
    private static final int REPEAT = 100;

    /** Directory where templates are stored. */
    private static final String TEMPLATE_DIR = "resources/templates";

    /** Base directory where results are stored. */
    private static final String RESULTS_DIR = "test/com/cedarsolutions/santa/server/service/impl/results/exchange";

    /** Test the constructor, getters and setters. */
    @Test public void testConstructorGettersSetters() {
        ExchangeService service = new ExchangeService();
        assertNull(service.getConfig());
        assertNull(service.getEmailService());

        ExchangeServiceConfig config = mock(ExchangeServiceConfig.class);
        service.setConfig(config);
        assertSame(config, service.getConfig());

        IEmailService emailService = mock(IEmailService.class);
        service.setEmailService(emailService);
        assertSame(emailService, service.getEmailService());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() {
        ExchangeServiceConfig config = mock(ExchangeServiceConfig.class);
        IEmailService emailService = mock(IEmailService.class);
        ExchangeService service = new ExchangeService();

        when(config.getMaxAttempts()).thenReturn(1);
        service.setConfig(config);
        service.setEmailService(emailService);
        service.afterPropertiesSet();

        try {
            when(config.getMaxAttempts()).thenReturn(0);
            service.setConfig(config);
            service.setEmailService(emailService);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            when(config.getMaxAttempts()).thenReturn(-1);
            service.setConfig(config);
            service.setEmailService(emailService);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            when(config.getMaxAttempts()).thenReturn(1);
            service.setConfig(null);
            service.setEmailService(emailService);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            when(config.getMaxAttempts()).thenReturn(1);
            service.setConfig(config);
            service.setEmailService(null);
            service.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }
    }

    /** Test sendMessage() for a plaintext message with extra info. */
    @Test public void testSendMessagePlaintextWithExtraInfo() {
        Exchange exchange = generateExchange(EmailFormat.PLAINTEXT);
        Assignment assignment = generateAssignment();

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.PLAINTEXT, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender(), template.getValue().getSender());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.PLAINTEXT, RESULTS_DIR, "standard", "extrainfo");
    }

    /** Test sendMessage() for a plaintext message with extra info, no email. */
    @Test public void testSendMessagePlaintextWithExtraInfoNoEmail() {
        Exchange exchange = generateExchange(EmailFormat.PLAINTEXT);
        Assignment assignment = generateAssignment();

        exchange.getOrganizer().setEmailAddress(null);

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.PLAINTEXT, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender(), template.getValue().getSender());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.PLAINTEXT, RESULTS_DIR, "standard", "noemail");
    }

    /** Test sendMessage() for a plaintext message with extra info, no phone. */
    @Test public void testSendMessagePlaintextWithExtraInfoNoPhone() {
        Exchange exchange = generateExchange(EmailFormat.PLAINTEXT);
        Assignment assignment = generateAssignment();

        exchange.getOrganizer().setPhoneNumber("");

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.PLAINTEXT, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender(), template.getValue().getSender());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.PLAINTEXT, RESULTS_DIR, "standard", "nophone");
    }

    /** Test sendMessage() for a plaintext message without extra info. */
    @Test public void testSendMessagePlaintextNoExtraInfo() {
        Exchange exchange = generateExchange(EmailFormat.PLAINTEXT);
        Assignment assignment = generateAssignment();

        exchange.setExtraInfo("");

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.PLAINTEXT, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender(), template.getValue().getSender());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.PLAINTEXT, RESULTS_DIR, "standard", "noextrainfo");
    }

    /** Test sendMessage() for a plaintext message, organizer only. */
    @Test public void testSendMessagePlaintextOrganizerOnly() {
        Exchange exchange = generateExchange(EmailFormat.PLAINTEXT);
        Assignment assignment = generateAssignment();

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, true);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.PLAINTEXT, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        // Don't bother to validate the actual HTML here... it's different due to organizerOnly, and we already know the template works
    }

    /** Test sendMessage() for a multipart message with extra info. */
    @Test public void testSendMessageMultipartWithExtraInfo() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        Assignment assignment = generateAssignment();

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.MULTIPART, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.MULTIPART, RESULTS_DIR, "standard", "extrainfo");
    }

    /** Test sendMessage() for a multipart message with extra info that is longer than a single line. */
    @Test public void testSendMessageMultipartWithExtraInfoLong() {
        Assignment assignment = generateAssignment();

        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setExtraInfo("This is example extra info that is really really long and is indeed longer than 75 characters and will be wrapped.");

        Exchange expected = new Exchange(exchange);
        expected.setExtraInfo("This is example extra info that is really really long and is indeed longer" +
                              StringUtils.LINE_ENDING + "than 75 characters and will be wrapped.");

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.MULTIPART, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(expected, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.MULTIPART, RESULTS_DIR, "standard", "extrainfolong");
    }

    /** Test sendMessage() for a multipart message with extra info, no email address. */
    @Test public void testSendMessageMultipartWithExtraInfoNoEmail() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        Assignment assignment = generateAssignment();

        exchange.getOrganizer().setEmailAddress("");

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.MULTIPART, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.MULTIPART, RESULTS_DIR, "standard", "noemail");
    }

    /** Test sendMessage() for a multipart message with extra info, no phone. */
    @Test public void testSendMessageMultipartWithExtraInfoNoPhone() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        Assignment assignment = generateAssignment();

        exchange.getOrganizer().setPhoneNumber(null);

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.MULTIPART, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.MULTIPART, RESULTS_DIR, "standard", "nophone");
    }

    /** Test sendMessage() for a multipart message without extra info. */
    @Test public void testSendMessageMultipartNoExtraInfo() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        Assignment assignment = generateAssignment();

        exchange.setExtraInfo(null);

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, false);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.MULTIPART, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(assignment.getGiftGiver().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        EmailTestUtils.validateMessageContents(message.getValue(), EmailFormat.MULTIPART, RESULTS_DIR, "standard", "noextrainfo");
    }

    /** Test sendMessage() for a multipart message, organizer only. */
    @Test public void testSendMessageMultipartOrganizerOnly() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        Assignment assignment = generateAssignment();

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessage(exchange, assignment, true);
        verify(service.getEmailService()).sendEmail(template.capture());
        verify(service.getEmailService()).sendEmail(message.capture());

        assertEquals(EmailFormat.MULTIPART, template.getValue().getFormat());
        assertEquals(2, template.getValue().getTemplateContext().size());
        assertEquals(exchange, template.getValue().getTemplateContext().get("exchange"));
        assertEquals(assignment, template.getValue().getTemplateContext().get("assignment"));
        assertEquals(service.getConfig().getSender().getName(), template.getValue().getSender().getName());
        assertEquals("sender@example.com", template.getValue().getSender().getAddress());
        assertEquals(exchange.getOrganizer().getName(), template.getValue().getReplyTo().getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getReplyTo().getAddress());
        assertEquals(1, template.getValue().getRecipients().size());
        assertEquals(assignment.getGiftGiver().getName(), template.getValue().getRecipients().get(0).getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getValue().getRecipients().get(0).getAddress());
        assertEquals(service.getConfig().getDefaultTemplateName(), template.getValue().getTemplateName());

        // Don't bother to validate the actual HTML here... it's different due to organizerOnly, and we already know the template works
    }

    /** Test sendMessages(). */
    @Test public void testSendMessages() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);

        Assignment assignment1 = generateAssignment("1");
        Assignment assignment2 = generateAssignment("2");

        AssignmentSet assignments = new AssignmentSet();
        assignments.add(assignment1);
        assignments.add(assignment2);

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessages(exchange, assignments, false);
        verify(service.getEmailService(), times(2)).sendEmail(template.capture());
        verify(service.getEmailService(), times(2)).sendEmail(message.capture());

        // Just spot-check enough so we can tell that everything got called properly
        assertEquals(exchange, template.getAllValues().get(0).getTemplateContext().get("exchange"));
        assertEquals(assignment1, template.getAllValues().get(0).getTemplateContext().get("assignment"));
        assertEquals(assignment1.getGiftGiver().getName(), template.getAllValues().get(0).getRecipients().get(0).getName());
        assertEquals(assignment1.getGiftGiver().getEmailAddress(), template.getAllValues().get(0).getRecipients().get(0).getAddress());
        assertEquals(exchange, template.getAllValues().get(1).getTemplateContext().get("exchange"));
        assertEquals(assignment2, template.getAllValues().get(1).getTemplateContext().get("assignment"));
        assertEquals(assignment2.getGiftGiver().getName(), template.getAllValues().get(1).getRecipients().get(0).getName());
        assertEquals(assignment2.getGiftGiver().getEmailAddress(), template.getAllValues().get(1).getRecipients().get(0).getAddress());
    }

    /** Test sendMessages(), organizer only. */
    @Test public void testSendMessagesOrganizerOnly() {
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);

        Assignment assignment1 = generateAssignment("1");
        Assignment assignment2 = generateAssignment("2");

        AssignmentSet assignments = new AssignmentSet();
        assignments.add(assignment1);
        assignments.add(assignment2);

        ArgumentCaptor<EmailTemplate> template = ArgumentCaptor.forClass(EmailTemplate.class);
        ArgumentCaptor<EmailMessage> message = ArgumentCaptor.forClass(EmailMessage.class);

        ExchangeService service = createService();
        service.sendMessages(exchange, assignments, true);
        verify(service.getEmailService(), times(2)).sendEmail(template.capture());
        verify(service.getEmailService(), times(2)).sendEmail(message.capture());

        // Just spot-check enough so we can tell that everything got called properly
        assertEquals(exchange, template.getAllValues().get(0).getTemplateContext().get("exchange"));
        assertEquals(assignment1, template.getAllValues().get(0).getTemplateContext().get("assignment"));
        assertEquals(assignment1.getGiftGiver().getName(), template.getAllValues().get(0).getRecipients().get(0).getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getAllValues().get(0).getRecipients().get(0).getAddress());
        assertEquals(exchange, template.getAllValues().get(1).getTemplateContext().get("exchange"));
        assertEquals(assignment2, template.getAllValues().get(1).getTemplateContext().get("assignment"));
        assertEquals(assignment2.getGiftGiver().getName(), template.getAllValues().get(1).getRecipients().get(0).getName());
        assertEquals(exchange.getOrganizer().getEmailAddress(), template.getAllValues().get(1).getRecipients().get(0).getAddress());
    }

    /** Test deriveTemplateConfig() with no overrides in exchange or assignment. */
    @Test public void testDeriveTemplateConfigNoOverrides() {
        ExchangeService service = createService();

        Exchange exchange = new Exchange();

        Assignment assignment = new Assignment();
        assignment.setGiftReceiver(new Participant());

        TemplateConfig templateConfig = service.deriveTemplateConfig(exchange, assignment);
        assertNotNull(templateConfig);
        assertEquals(service.getConfig().getTemplateGroup(), templateConfig.getTemplateGroup());
        assertEquals(service.getConfig().getDefaultEmailFormat(), templateConfig.getEmailFormat());
        assertEquals(service.getConfig().getSender().getName(), templateConfig.getSenderName());
        assertEquals(service.getConfig().getDefaultTemplateName(), templateConfig.getTemplateName());
    }

    /** Test deriveTemplateConfig() with overrides in exchange only. */
    @Test public void testDeriveTemplateConfigExchangeOverrides() {
        ExchangeService service = createService();

        Exchange exchange = new Exchange();
        exchange.getTemplateOverrides().setEmailFormat(EmailFormat.PLAINTEXT);
        exchange.getTemplateOverrides().setSenderName("1");
        exchange.getTemplateOverrides().setTemplateName("2");

        Assignment assignment = new Assignment();
        assignment.setGiftReceiver(new Participant());

        TemplateConfig templateConfig = service.deriveTemplateConfig(exchange, assignment);
        assertNotNull(templateConfig);
        assertEquals(service.getConfig().getTemplateGroup(), templateConfig.getTemplateGroup());
        assertEquals(EmailFormat.PLAINTEXT, templateConfig.getEmailFormat());
        assertEquals("1", templateConfig.getSenderName());
        assertEquals("2", templateConfig.getTemplateName());
    }

    /** Test deriveTemplateConfig() with overrides in assignment only. */
    @Test public void testDeriveTemplateConfigAssignmentOverrides() {
        ExchangeService service = createService();

        Exchange exchange = new Exchange();

        Assignment assignment = new Assignment();
        assignment.setGiftReceiver(new Participant());
        assignment.getGiftReceiver().getTemplateOverrides().setEmailFormat(EmailFormat.PLAINTEXT);
        assignment.getGiftReceiver().getTemplateOverrides().setSenderName("1");
        assignment.getGiftReceiver().getTemplateOverrides().setTemplateName("2");

        TemplateConfig templateConfig = service.deriveTemplateConfig(exchange, assignment);
        assertNotNull(templateConfig);
        assertEquals(service.getConfig().getTemplateGroup(), templateConfig.getTemplateGroup());
        assertEquals(EmailFormat.PLAINTEXT, templateConfig.getEmailFormat());
        assertEquals("1", templateConfig.getSenderName());
        assertEquals("2", templateConfig.getTemplateName());
    }

    /** Test deriveTemplateConfig() with overrides in both exchange and assignment. */
    @Test public void testDeriveTemplateConfigBothOverrides() {
        ExchangeService service = createService();

        Exchange exchange = new Exchange();
        exchange.getTemplateOverrides().setEmailFormat(EmailFormat.MULTIPART);
        exchange.getTemplateOverrides().setSenderName("A");
        exchange.getTemplateOverrides().setTemplateName("B");

        Assignment assignment = new Assignment();
        assignment.setGiftReceiver(new Participant());
        assignment.getGiftReceiver().getTemplateOverrides().setEmailFormat(EmailFormat.PLAINTEXT);
        assignment.getGiftReceiver().getTemplateOverrides().setSenderName("1");
        assignment.getGiftReceiver().getTemplateOverrides().setTemplateName("2");

        TemplateConfig templateConfig = service.deriveTemplateConfig(exchange, assignment);
        assertNotNull(templateConfig);
        assertEquals(service.getConfig().getTemplateGroup(), templateConfig.getTemplateGroup());
        assertEquals(EmailFormat.PLAINTEXT, templateConfig.getEmailFormat());
        assertEquals("1", templateConfig.getSenderName());
        assertEquals("2", templateConfig.getTemplateName());
    }

    /** Test generateAssignments() for an empty participant set. */
    @Test public void testGenerateAssignmentsNoMembers() {
        ExchangeService service = createService();
        Exchange exchange = new Exchange();

        ParticipantSet participants = new ParticipantSet();
        exchange.setParticipants(participants);

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, false);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, true);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        try {
            // Empty set of participants is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(1);
            service.generateAssignments(exchange, false);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) { }

        try {
            // Empty set of participants is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(1);
            service.generateAssignments(exchange, true);
            fail("Expected InvalidDataException");
        } catch (InvalidDataException e) { }
    }

    /** Test generateAssignments() for a participant set with 1 member. */
    @Test public void testGenerateAssignmentsOneMember() {
        ExchangeService service = createService();
        Exchange exchange = new Exchange();

        ParticipantSet participants = new ParticipantSet();
        participants.add(new Participant(1L));
        exchange.setParticipants(participants);

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, false);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, true);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            try {
                // With only 1 member, there are no legal assignments
                when(service.getConfig().getMaxAttempts()).thenReturn(1);
                service.generateAssignments(exchange, false);
                fail("Expected InvalidDataException");
            } catch (InvalidDataException e) { }
        }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            try {
                // With only 1 member, there are no legal assignments
                when(service.getConfig().getMaxAttempts()).thenReturn(1);
                service.generateAssignments(exchange, true);
                fail("Expected InvalidDataException");
            } catch (InvalidDataException e) { }
        }
    }

    /** Test generateAssignments() for a participant set with 2 members. */
    @Test public void testGenerateAssignmentsTwoMembers() {
        ExchangeService service = createService();
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().clear();

        Participant p1 = new Participant(1L, "name1", "nick1", "email1");
        Participant p2 = new Participant(2L, "name2", "nick2", "email2");

        ParticipantSet participants = new ParticipantSet();
        participants.add(p1);
        participants.add(p2);
        exchange.setParticipants(participants);

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, false);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, true);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            try {
                // With auto-conflict enabled, there are no legal assignments
                when(service.getConfig().getMaxAttempts()).thenReturn(1);
                service.generateAssignments(exchange, true);
                fail("Expected InvalidDataException");
            } catch (InvalidDataException e) { }
        }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            // With auto-conflict not enabled, the two participants are assigned to each other
            when(service.getConfig().getMaxAttempts()).thenReturn(1);
            AssignmentSet assignments = service.generateAssignments(exchange, false);
            assertEquals(2, assignments.size());
            assertEquals(p1, assignments.getGiftGiver(p2));
            assertEquals(p2, assignments.getGiftGiver(p1));
            assertEquals(p1, assignments.getGiftReceiver(p2));
            assertEquals(p2, assignments.getGiftReceiver(p1));
        }
    }

    /** Test generateAssignments() for a participant set with 3 members, no conflicts. */
    @Test public void testGenerateAssignmentsThreeMembersNoConflicts() {
        ExchangeService service = createService();
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().clear();

        Participant p1 = new Participant(1L, "name1", "nick1", "email1");
        Participant p2 = new Participant(2L, "name2", "nick2", "email2");
        Participant p3 = new Participant(3L, "name3", "nick3", "email3");

        ParticipantSet participants = new ParticipantSet();
        participants.add(p1);
        participants.add(p2);
        participants.add(p3);
        exchange.setParticipants(participants);

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, false);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, true);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            // For this set of participants, there is no definitive solution
            when(service.getConfig().getMaxAttempts()).thenReturn(MAX_ATTEMPTS);
            AssignmentSet assignments = service.generateAssignments(exchange, false);  // give it a few attempts
            assertEquals(3, assignments.size());
            assertTrue(assignments.getGiftGiver(p1).equals(p2) || assignments.getGiftGiver(p1).equals(p3));
            assertTrue(assignments.getGiftGiver(p2).equals(p1) || assignments.getGiftGiver(p2).equals(p3));
            assertTrue(assignments.getGiftGiver(p3).equals(p1) || assignments.getGiftGiver(p3).equals(p2));
            assertTrue(assignments.getGiftReceiver(p1).equals(p2) || assignments.getGiftReceiver(p1).equals(p3));
            assertTrue(assignments.getGiftReceiver(p2).equals(p1) || assignments.getGiftReceiver(p2).equals(p3));
            assertTrue(assignments.getGiftReceiver(p3).equals(p1) || assignments.getGiftReceiver(p3).equals(p2));
        }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            // For this set of participants, there is no definitive solution
            when(service.getConfig().getMaxAttempts()).thenReturn(MAX_ATTEMPTS);
            AssignmentSet assignments = service.generateAssignments(exchange, true);  // give it a few attempts
            assertEquals(3, assignments.size());
            assertTrue(assignments.getGiftGiver(p1).equals(p2) || assignments.getGiftGiver(p1).equals(p3));
            assertTrue(assignments.getGiftGiver(p2).equals(p1) || assignments.getGiftGiver(p2).equals(p3));
            assertTrue(assignments.getGiftGiver(p3).equals(p1) || assignments.getGiftGiver(p3).equals(p2));
            assertTrue(assignments.getGiftReceiver(p1).equals(p2) || assignments.getGiftReceiver(p1).equals(p3));
            assertTrue(assignments.getGiftReceiver(p2).equals(p1) || assignments.getGiftReceiver(p2).equals(p3));
            assertTrue(assignments.getGiftReceiver(p3).equals(p1) || assignments.getGiftReceiver(p3).equals(p2));
            assertFalse(assignments.getGiftGiver(p1).equals(assignments.getGiftReceiver(p1)));
            assertFalse(assignments.getGiftGiver(p2).equals(assignments.getGiftReceiver(p2)));
            assertFalse(assignments.getGiftGiver(p3).equals(assignments.getGiftReceiver(p3)));
        }
    }

    /** Test generateAssignments() for a participant set with 3 members, with conflicts. */
    @Test public void testGenerateAssignmentsThreeMemberWithConflicts() {
        ExchangeService service = createService();
        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().clear();

        Participant p1 = new Participant(1L, "name1", "nick1", "email1");
        Participant p2 = new Participant(2L, "name2", "nick2", "email2");
        Participant p3 = new Participant(3L, "name3", "nick3", "email3");

        p1.getConflicts().add(p2);
        p2.getConflicts().add(p3);

        ParticipantSet participants = new ParticipantSet();
        participants.add(p1);
        participants.add(p2);
        participants.add(p3);
        exchange.setParticipants(participants);

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, false);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        try {
            // Zero attempts is not valid
            when(service.getConfig().getMaxAttempts()).thenReturn(0);
            service.generateAssignments(exchange, true);
            fail("Expected ServiceException");
        } catch (ServiceException e) { }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            // For this set of participants and their conflicts, there is only one legal assignment set
            when(service.getConfig().getMaxAttempts()).thenReturn(MAX_ATTEMPTS);
            AssignmentSet assignments = service.generateAssignments(exchange, false);  // give it a few attempts
            assertEquals(3, assignments.size());
            assertTrue(assignments.getGiftReceiver(p1).equals(p3));
            assertTrue(assignments.getGiftReceiver(p2).equals(p1));
            assertTrue(assignments.getGiftReceiver(p3).equals(p2));
        }

        // Try it a bunch of times, to make sure it wasn't just chance that the test passed
        for (int i = 0; i < REPEAT; i++) {
            // For this set of participants and their conflicts, there is only one legal assignment set
            when(service.getConfig().getMaxAttempts()).thenReturn(MAX_ATTEMPTS);
            AssignmentSet assignments = service.generateAssignments(exchange, true);  // give it a few attempts
            assertEquals(3, assignments.size());
            assertTrue(assignments.getGiftReceiver(p1).equals(p3));
            assertTrue(assignments.getGiftReceiver(p2).equals(p1));
            assertTrue(assignments.getGiftReceiver(p3).equals(p2));
        }
    }

    /** Test generatePreview(). */
    @Test public void testGeneratePreview() {
        ExchangeService service = createService();

        Exchange exchange = generateExchange(EmailFormat.MULTIPART);
        Assignment assignment = new Assignment(exchange.getParticipants().get(0), exchange.getParticipants().get(1));
        EmailTemplate template = service.generateMessage(exchange, assignment, false);  // we've tested this elsewhere

        EmailMessage message = new EmailMessage();
        when(service.getEmailService().generateEmail(template)).thenReturn(message);

        assertSame(message, service.generatePreview(exchange));
    }

    /** Test validateExchange(). */
    @Test public void testValidateExchange() {
        Exchange exchange = null;

        exchange = generateExchange(EmailFormat.MULTIPART);
        ExchangeService.validateExchange(exchange);

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setId(null);
        assertFieldIsOptional(exchange, "id");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setExchangeState(null);
        assertFieldIsOptional(exchange, "exchangeState");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setName("");
        assertFieldIsRequired(exchange, "name");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setDateAndTime("");
        assertFieldIsRequired(exchange, "dateAndTime");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setTheme("");
        assertFieldIsRequired(exchange, "theme");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setCost("");
        assertFieldIsRequired(exchange, "cost");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.setExtraInfo("");
        assertFieldIsOptional(exchange, "extraInfo");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getOrganizer().setName("");
        assertFieldIsRequired(exchange, "organizerName");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getOrganizer().setPhoneNumber("");
        assertFieldIsOptional(exchange, "organizerPhoneNumber");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getOrganizer().setEmailAddress("");
        assertFieldIsOptional(exchange, "organizerEmailAddress");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getOrganizer().setEmailAddress("");
        exchange.getOrganizer().setPhoneNumber("");
        assertFieldIsRequired(exchange, "organizerEmailAddress");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().clear();
        assertFieldIsRequired(exchange, "participants");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().remove(1);
        assertFieldIsRequired(exchange, "participants");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().get(0).setName("");
        assertFieldIsRequired(exchange, "participantName");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().get(0).setNickname("");
        assertFieldIsRequired(exchange, "participantNickname");

        exchange = generateExchange(EmailFormat.MULTIPART);
        exchange.getParticipants().get(0).setEmailAddress("");
        assertFieldIsRequired(exchange, "participantEmailAddress");

        try {
            exchange = null;
            ExchangeService.validateExchange(exchange);
            fail("Expected NPE");
        } catch (NullPointerException e) { }

        try {
            exchange = generateExchange(EmailFormat.MULTIPART);
            exchange.setParticipants(null);
            ExchangeService.validateExchange(exchange);
            fail("Expected NPE");
        } catch (NullPointerException e) { }

        try {
            exchange = generateExchange(EmailFormat.MULTIPART);
            exchange.setParticipants(null);
            ExchangeService.validateExchange(exchange);
            fail("Expected NPE");
        } catch (NullPointerException e) { }
    }

    /** Assert that a field is required. */
    private static void assertFieldIsRequired(Exchange exchange, String field) {
        try {
            ExchangeService.validateExchange(exchange);
            fail("Expected InvalidDataException for field [" + field + "]");
        } catch (InvalidDataException e) {
            assertSummary(e, INVALID);
            assertOnlyMessage(e, REQUIRED, field);
        }
    }

    /** Assert that a field is optional. */
    private static void assertFieldIsOptional(Exchange exchange, String field) {
        try {
            ExchangeService.validateExchange(exchange);
        } catch (InvalidDataException e) {
            fail("Got unexpected InvalidDataException for field [" + field + "]");
        }
    }

    /** Create a mocked service that actually renders the underlying template. */
    private static ExchangeService createService() {
        IEmailService emailService = EmailTestUtils.createPartiallyMockedEmailService(TEMPLATE_DIR);
        ExchangeServiceConfig config = mock(ExchangeServiceConfig.class);
        when(config.getMaxAttempts()).thenReturn(1);

        List<EmailAddress> recipients = new ArrayList<EmailAddress>();
        recipients.add(new EmailAddress("one@example.com"));
        recipients.add(new EmailAddress("two@example.com"));

        when(config.getTemplateGroup()).thenReturn("exchange");
        when(config.getSender()).thenReturn(new EmailAddress("Santa Exchange", "sender@example.com"));
        when(config.getDefaultEmailFormat()).thenReturn(EmailFormat.MULTIPART);
        when(config.getDefaultTemplateName()).thenReturn("standard");

        ExchangeService service = new ExchangeService();
        service.setConfig(config);
        service.setEmailService(emailService);
        service.afterPropertiesSet();

        return service;
    }

    /** Generate an Exchange for testing. */
    private static Exchange generateExchange(EmailFormat emailFormat) {
        Exchange exchange = new Exchange();
        exchange.getTemplateOverrides().setEmailFormat(emailFormat);
        exchange.setName("Example Exchange");
        exchange.setTheme("Theme");
        exchange.setDateAndTime("February 3, 2011");
        exchange.setCost("$5-$10");
        exchange.setExtraInfo("This is example extra info.");
        exchange.setOrganizer(new Organizer());
        exchange.getOrganizer().setName("Organizer Dude");
        exchange.getOrganizer().setPhoneNumber("(651) 555-1212");
        exchange.getOrganizer().setEmailAddress("organizer@example.com");
        exchange.getParticipants().add(new Participant(1L, "Name1", "Nickname1", "email1"));
        exchange.getParticipants().add(new Participant(2L, "Name2", "Nickname2", "email2"));
        return exchange;
    }

    /** Generate an assignment for testing. */
    private static Assignment generateAssignment() {
        return generateAssignment("");
    }

    /**
     * Generate an assignment for testing.
     * @param suffix  Suffix to append to every value, so we can tell assignments apart
     */
    private static Assignment generateAssignment(String suffix) {
        Assignment assignment = new Assignment(new Participant(), new Participant());

        assignment.getGiftGiver().setName("Gift Giver" + suffix);
        assignment.getGiftGiver().setNickname("Giver" + suffix);
        assignment.getGiftGiver().setEmailAddress("giver@example.com" + suffix);

        assignment.getGiftReceiver().setName("Gift Receiver" + suffix);
        assignment.getGiftReceiver().setNickname("Receiver" + suffix);

        return assignment;
    }

}
