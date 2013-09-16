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
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * An assignment for the exchange.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "giftGiver", "giftReceiver" })
public class Assignment extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Participant that is giving the gift. */
    private Participant giftGiver;

    /** Participant that is receiving the gift. */
    private Participant giftReceiver;

    /** Default constructor. */
    public Assignment() {
        this(null, null);
    }

    /** Values constructor. */
    public Assignment(Participant giftGiver, Participant giftReceiver) {
        this.giftGiver = giftGiver;
        this.giftReceiver = giftReceiver;
    }

    /** Copy constructor. */
    public Assignment(Assignment source) {
        if (source != null) {
            this.giftGiver = source.giftGiver == null ? null : new Participant(source.giftGiver);
            this.giftReceiver = source.giftReceiver == null ? null : new Participant(source.giftReceiver);
        }
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        Assignment other = (Assignment) obj;
        return new EqualsBuilder()
                    .append(this.giftGiver, other.giftGiver)
                    .append(this.giftReceiver, other.giftReceiver)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.giftGiver)
                    .append(this.giftReceiver)
                    .toHashCode();
    }

    public Participant getGiftGiver() {
        return giftGiver;
    }

    public void setGiftGiver(Participant giftGiver) {
        this.giftGiver = giftGiver;
    }

    public Participant getGiftReceiver() {
        return giftReceiver;
    }

    public void setGiftReceiver(Participant giftReceiver) {
        this.giftReceiver = giftReceiver;
    }

}
