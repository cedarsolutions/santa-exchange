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
package com.cedarsolutions.santa.shared.domain.exchange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.cedarsolutions.shared.domain.email.EmailFormat;

/**
 * Unit tests for TemplateConfig.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class TemplateConfigTest {

    /** Test the constructors. */
    @Test public void testConstructors() {
        TemplateConfig config = new TemplateConfig();
        assertNotNull(config);
        assertNull(config.getSenderName());
        assertNull(config.getEmailFormat());
        assertNull(config.getTemplateGroup());
        assertNull(config.getTemplateName());

        config = new TemplateConfig(null);
        assertNotNull(config);
        assertNull(config.getSenderName());
        assertNull(config.getEmailFormat());
        assertNull(config.getTemplateGroup());
        assertNull(config.getTemplateName());
    }

    /** Test the getters and setters. */
    @Test public void testGettersSetters() {
        TemplateConfig config = new TemplateConfig();

        config.setEmailFormat(EmailFormat.MULTIPART);
        assertEquals(EmailFormat.MULTIPART, config.getEmailFormat());

        config.setSenderName("sender");
        assertEquals("sender", config.getSenderName());

        config.setTemplateGroup("group");
        assertEquals("group", config.getTemplateGroup());

        config.setTemplateName("name");
        assertEquals("name", config.getTemplateName());
    }

    /** Test equals(). */
    @Test public void testEquals() {
        TemplateConfig config1;
        TemplateConfig config2;

        config1 = createTemplateConfig();
        config2 = createTemplateConfig();
        assertTrue(config1.equals(config2));
        assertTrue(config2.equals(config1));

        try {
            config1 = createTemplateConfig();
            config2 = null;
            config1.equals(config2);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) { }

        try {
            config1 = createTemplateConfig();
            config2 = null;
            config1.equals("blech");
            fail("Expected ClassCastException");
        } catch (ClassCastException e) { }

        config1 = createTemplateConfig();
        config2 = createTemplateConfig();
        config2.setSenderName("X");
        assertFalse(config1.equals(config2));
        assertFalse(config2.equals(config1));

        config1 = createTemplateConfig();
        config2 = createTemplateConfig();
        config2.setEmailFormat(EmailFormat.MULTIPART);
        assertFalse(config1.equals(config2));
        assertFalse(config2.equals(config1));

        config1 = createTemplateConfig();
        config2 = createTemplateConfig();
        config2.setTemplateGroup("X");
        assertFalse(config1.equals(config2));
        assertFalse(config2.equals(config1));

        config1 = createTemplateConfig();
        config2 = createTemplateConfig();
        config2.setTemplateName("X");
        assertFalse(config1.equals(config2));
        assertFalse(config2.equals(config1));
    }

    /** Test hashCode(). */
    @Test public void testHashCode() {
        TemplateConfig config1 = createTemplateConfig();
        config1.setSenderName("X");

        TemplateConfig config2 = createTemplateConfig();
        config2.setEmailFormat(EmailFormat.MULTIPART);

        TemplateConfig config3 = createTemplateConfig();
        config3.setTemplateGroup("X");

        TemplateConfig config4 = createTemplateConfig();
        config4.setTemplateName("X");

        TemplateConfig config5 = createTemplateConfig();
        config5.setSenderName("X");

        Map<TemplateConfig, String> map = new HashMap<TemplateConfig, String>();
        map.put(config1, "ONE");
        map.put(config2, "TWO");
        map.put(config3, "THREE");
        map.put(config4, "FOUR");

        assertEquals("ONE", map.get(config1));
        assertEquals("TWO", map.get(config2));
        assertEquals("THREE", map.get(config3));
        assertEquals("FOUR", map.get(config4));
        assertEquals("ONE", map.get(config5));
    }

    /** Create a TemplateConfig for testing. */
    private static TemplateConfig createTemplateConfig() {
        TemplateConfig config = new TemplateConfig();

        config.setSenderName("sender");
        config.setEmailFormat(EmailFormat.PLAINTEXT);
        config.setTemplateGroup("group");
        config.setTemplateName("name");

        return config;
    }
}
