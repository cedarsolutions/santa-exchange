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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Test;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailFormat;

/**
 * Unit tests for ExchangeServiceConfig.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeServiceConfigTest {

    /** Test constructor. */
    @Test public void testConstructor() {
        ExchangeServiceConfig config = new ExchangeServiceConfig();
        assertNotNull(config);
        assertNull(config.getProperties());
        assertNull(config.getTemplateGroup());
        assertNull(config.getSender());
        assertNull(config.getDefaultEmailFormat());
        assertNull(config.getDefaultTemplateName());
        assertEquals(0, config.getMaxAttempts());

        Properties properties = new Properties();
        config.setProperties(properties);
        assertSame(properties, config.getProperties());
        assertNull(config.getTemplateGroup());
        assertNull(config.getSender());
        assertNull(config.getDefaultEmailFormat());
        assertNull(config.getDefaultTemplateName());
        assertEquals(0, config.getMaxAttempts());
    }

    /** Test afterPropertiesSet(). */
    @Test public void testAfterPropertiesSet() throws Exception {
        Properties properties = null;
        ExchangeServiceConfig config = new ExchangeServiceConfig();

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
        properties.setProperty("ExchangeService.templateGroup", "group");
        properties.setProperty("ExchangeService.senderName", "Sender");
        properties.setProperty("ExchangeService.senderAddress", "sender@example.com");
        properties.setProperty("ExchangeService.defaultEmailFormat", "MULTIPART");
        properties.setProperty("ExchangeService.defaultTemplateName", "standard");
        properties.setProperty("ExchangeService.maxAttempts", "10");
        config.setProperties(properties);
        config.afterPropertiesSet();
        assertEquals("group", config.getTemplateGroup());
        assertEquals(new EmailAddress("Sender", "sender@example.com"), config.getSender());
        assertEquals(EmailFormat.MULTIPART, config.getDefaultEmailFormat());
        assertEquals("standard", config.getDefaultTemplateName());
        assertEquals(10, config.getMaxAttempts());
    }

}
