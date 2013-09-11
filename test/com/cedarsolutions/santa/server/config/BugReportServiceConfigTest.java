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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Test;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.shared.domain.email.EmailAddress;

/**
 * Unit tests for BugReportServiceConfig.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReportServiceConfigTest {

    /** Test constructor. */
    @Test public void testConstructor() {
        BugReportServiceConfig config = new BugReportServiceConfig();
        assertNotNull(config);
        assertNull(config.getProperties());
        assertNull(config.getTemplateGroup());
        assertNull(config.getTemplateName());
        assertNull(config.getSender());
        assertNull(config.getReplyTo());
        assertNull(config.getRecipients());

        Properties properties = new Properties();
        config.setProperties(properties);
        assertSame(properties, config.getProperties());
        assertNull(config.getTemplateGroup());
        assertNull(config.getTemplateName());
        assertNull(config.getSender());
        assertNull(config.getReplyTo());
        assertNull(config.getRecipients());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() throws Exception {
        Properties properties = null;
        BugReportServiceConfig config = new BugReportServiceConfig();

        try {
            config.setProperties(null);
            config.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        try {
            config.setProperties(properties);
            config.afterPropertiesSet();
            fail("Expected NotConfiguredException");
        } catch (NotConfiguredException e) { }

        properties = new Properties();
        properties.setProperty("BugReportService.templateGroup", "group");
        properties.setProperty("BugReportService.templateName", "name");
        properties.setProperty("BugReportService.senderName", "Secret Santa");
        properties.setProperty("BugReportService.senderAddress", "santa@example.com");
        properties.setProperty("BugReportService.recipients", "");
        config.setProperties(properties);
        config.afterPropertiesSet();
        assertEquals("group", config.getTemplateGroup());
        assertEquals("name", config.getTemplateName());
        assertEquals(new EmailAddress("Secret Santa", "santa@example.com"), config.getSender());
        assertEquals(null, config.getReplyTo());
        assertEquals(0, config.getRecipients().size());

        properties = new Properties();
        properties.setProperty("BugReportService.templateGroup", "group");
        properties.setProperty("BugReportService.templateName", "name");
        properties.setProperty("BugReportService.senderName", "Secret Santa");
        properties.setProperty("BugReportService.senderAddress", "santa@example.com");
        properties.setProperty("BugReportService.replyToAddress", "noreply@example.com");
        properties.setProperty("BugReportService.recipients", "one@example.com,two@example.com, three@example.com");
        config.setProperties(properties);
        config.afterPropertiesSet();
        assertEquals("group", config.getTemplateGroup());
        assertEquals("name", config.getTemplateName());
        assertEquals(new EmailAddress("Secret Santa", "santa@example.com"), config.getSender());
        assertEquals(new EmailAddress("noreply@example.com"), config.getReplyTo());
        assertEquals(3, config.getRecipients().size());
        assertEquals(new EmailAddress("one@example.com"), config.getRecipients().get(0));
        assertEquals(new EmailAddress("two@example.com"), config.getRecipients().get(1));
        assertEquals(new EmailAddress("three@example.com"), config.getRecipients().get(2));
    }

}
