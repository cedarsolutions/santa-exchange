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
package com.cedarsolutions.santa.client.internal.view;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.cedarsolutions.santa.client.junit.StubbedClientTestCase;
import com.cedarsolutions.shared.domain.email.EmailAddress;
import com.cedarsolutions.shared.domain.email.EmailMessage;

/**
 * Unit tests for PreviewPopup.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PreviewPopupTest extends StubbedClientTestCase {

    /** Test generateTextPreview() for a null email message. */
    @Test public void testGenerateTextPreviewNull() {
        try {
            PreviewPopup.generateTextPreview(null);
        } catch (NullPointerException e) { }
    }

    /** Test generateTextPreview() for an empty email message. */
    @Test public void testGenerateTextPreviewEmpty() {
        EmailMessage email = new EmailMessage();
        String result = PreviewPopup.generateTextPreview(email);
        assertEquals("<html>\n<body>\n<pre>\nFrom: \nTo: \nSubject: \n</pre>\n</body>\n</html>\n", result);
    }

    /** Test generateTextPreview() for an email message with a valid plaintext part and other data. */
    @Test public void testGenerateTextPreviewValid() {
        EmailMessage email = new EmailMessage();
        email.setSender(new EmailAddress("Sender", "sender@example.com"));
        email.setRecipients(new EmailAddress("Recipient", "recipient@example.com"));
        email.setSubject("The Subject");
        email.setPlaintext("The plaintext body");
        email.setHtml(null);

        String result = PreviewPopup.generateTextPreview(email);
        assertEquals("<html>\n<body>\n<pre>\n" +
                     "From: &quot;Sender&quot; &lt;sender@example.com&gt;\n" +
                     "To: &quot;Recipient&quot; &lt;recipient@example.com&gt;\n" +
                     "Subject: The Subject\n" +
                     "\nThe plaintext body\n" +
                     "</pre>\n" +
                     "</body>\n</html>\n", result);
    }

    /** Test generateHtmlPreview() for a null email message. */
    @Test public void testGenerateHtmlPreviewNull() {
        try {
            PreviewPopup.generateHtmlPreview(null);
        } catch (NullPointerException e) { }
    }

    /** Test generateHtmlPreview() for an empty email message. */
    @Test public void testGenerateHtmlPreviewEmpty() {
        EmailMessage email = new EmailMessage();
        String result = PreviewPopup.generateHtmlPreview(email);
        assertEquals("<html>\n<body>\n<pre>\nFrom: \nTo: \nSubject: \n</pre>\n</body>\n</html>\n", result);
    }

    /** Test generateHtmlPreview() for an email message with a valid HTML part and other data. */
    @Test public void testGenerateHtmlPreviewValid() {
        EmailMessage email = new EmailMessage();
        email.setSender(new EmailAddress("Sender", "sender@example.com"));
        email.setRecipients(new EmailAddress("Recipient", "recipient@example.com"));
        email.setSubject("The Subject");
        email.setPlaintext(null);
        email.setHtml("The HTML body");

        String result = PreviewPopup.generateHtmlPreview(email);
        assertEquals("<html>\n<body>\n<pre>\n" +
                     "From: &quot;Sender&quot; &lt;sender@example.com&gt;\n" +
                     "To: &quot;Recipient&quot; &lt;recipient@example.com&gt;\n" +
                     "Subject: The Subject\n" +
                     "</pre>\n" +
                     "The HTML body" +
                     "</body>\n</html>\n", result);
    }

}
