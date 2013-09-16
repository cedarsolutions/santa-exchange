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
package com.cedarsolutions.santa.shared.domain.exchange;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.cedarsolutions.shared.domain.email.EmailFormat;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Template configuration.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "senderName", "emailFormat", "templateGroup", "templateName" })
public class TemplateConfig extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Application sender name. */
    private String senderName;

    /** Email format. */
    private EmailFormat emailFormat;

    /** Template group. */
    private String templateGroup;

    /** Template name. */
    private String templateName;

    /** Default constructor. */
    public TemplateConfig() {
    }

    /** Copy constructor(). */
    public TemplateConfig(TemplateConfig source) {
        if (source != null) {
            this.senderName = source.senderName;
            this.emailFormat = source.emailFormat;
            this.templateGroup = source.templateGroup;
            this.templateName = source.templateName;
        }
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        TemplateConfig other = (TemplateConfig) obj;
        return new EqualsBuilder()
                    .append(this.senderName, other.senderName)
                    .append(this.emailFormat, other.emailFormat)
                    .append(this.templateGroup, other.templateGroup)
                    .append(this.templateName, other.templateName)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.senderName)
                    .append(this.emailFormat)
                    .append(this.templateGroup)
                    .append(this.templateName)
                    .toHashCode();
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public EmailFormat getEmailFormat() {
        return this.emailFormat;
    }

    public void setEmailFormat(EmailFormat emailFormat) {
        this.emailFormat = emailFormat;
    }

    public String getTemplateGroup() {
        return this.templateGroup;
    }

    public void setTemplateGroup(String templateGroup) {
        this.templateGroup = templateGroup;
    }

    public String getTemplateName() {
        return this.templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

}
