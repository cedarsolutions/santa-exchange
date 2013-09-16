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
package com.cedarsolutions.santa.shared.domain.audit;

import com.cedarsolutions.shared.domain.TranslatableDomainObject;
import com.flipthebird.gwthashcodeequals.EqualsBuilder;
import com.flipthebird.gwthashcodeequals.HashCodeBuilder;

/**
 * Extra data stored on an audit event as a key/value pair.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExtraData extends TranslatableDomainObject {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    private ExtraDataKey key;
    private String value;

    public ExtraData() {
    }

    public ExtraData(ExtraDataKey key, String value) {
        this.key = key;
        this.value = value;
    }

    public ExtraData(ExtraDataKey key, Integer value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public ExtraData(ExtraDataKey key, Long value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    /** Compare this object to another object. */
    @Override
    public boolean equals(Object obj) {
        ExtraData other = (ExtraData) obj;
        return new EqualsBuilder()
                    .append(this.key, other.key)
                    .append(this.value, other.value)
                    .isEquals();
    }

    /** Generate a hash code for this object. */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(this.key)
                    .append(this.value)
                    .toHashCode();
    }

    public ExtraDataKey getKey() {
        return this.key;
    }

    public void setKey(ExtraDataKey key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
