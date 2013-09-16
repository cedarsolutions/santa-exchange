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
package com.cedarsolutions.santa.shared.domain;

import java.util.Date;

import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Bug report information submitted by a user.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class BugReport extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    private Date reportDate;
    private String applicationVersion;
    private FederatedUser submittingUser;
    private String emailAddress;
    private String problemSummary;
    private String detailedDescription;

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        BugReport other = (BugReport) obj;
        return new EqualsBuilder()
                    .append(this.reportDate, other.reportDate)
                    .append(this.applicationVersion, other.applicationVersion)
                    .append(this.submittingUser, other.submittingUser)
                    .append(this.emailAddress, other.emailAddress)
                    .append(this.problemSummary, other.problemSummary)
                    .append(this.detailedDescription, other.detailedDescription)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.reportDate)
                    .append(this.applicationVersion)
                    .append(this.submittingUser)
                    .append(this.emailAddress)
                    .append(this.problemSummary)
                    .append(this.detailedDescription)
                    .toHashCode();
    }

    public Date getReportDate() {
        return this.reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getApplicationVersion() {
        return this.applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public FederatedUser getSubmittingUser() {
        return this.submittingUser;
    }

    public void setSubmittingUser(FederatedUser submittingUser) {
        this.submittingUser = submittingUser;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getProblemSummary() {
        return this.problemSummary;
    }

    public void setProblemSummary(String problemSummary) {
        this.problemSummary = problemSummary;
    }

    public String getDetailedDescription() {
        return this.detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }
}
