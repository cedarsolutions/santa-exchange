/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2014 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.server.dao.impl;

import javax.jdo.annotations.Index;
import javax.persistence.Id;

import com.cedarsolutions.dao.gae.domain.IContainer;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.util.JaxbUtils;

/**
 * Container object used for storing serialized (XML) Exchange data in GAE's datastore.
 *
 * <p>
 * This implementation assumes that the Exchange object can be serialized to
 * XML and deserialized from XML via the methods on JaxbUtils.  The Exchange
 * object must be annotated with the proper JAXB annotations.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeContainer implements IContainer<Exchange> {

    /** Serialization version number, which can be important to the GAE back-end. */
    private static final long serialVersionUID = 1L;

    /** Unique identifier for the exchange. */
    @Id private Long id;

    /** User which owns this exchange. */
    @Index private String userId;

    /** Serialized (XML) exchange data. */
    private String serialized;

    /** Initialize the container based on a value. */
    @Override
    public void fromValue(Exchange value) {
        if (value == null) {
            throw new NullPointerException("exchange");
        }

        this.id = value.getId();
        this.userId = value.getUserId();
        this.serialized = JaxbUtils.getInstance().marshalDocument(value);
    }

    /** Turn the container into a value. */
    @Override
    public Exchange toValue() {
        if (this.getSerialized() == null) {
            return null;
        } else {
            // There's a JAXB bug that gets confused by the Exchange object structure
            // when using schema validation like implemented by unmarshalDocument().
            // See the bottom of: http://stackoverflow.com/questions/3658378
            // Because of this bug, we pass validate=false and don't validate the XML.
            Exchange exchange = JaxbUtils.getInstance().unmarshalDocument(Exchange.class, this.serialized, false);
            exchange.setId(this.getId());
            return exchange;
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSerialized() {
        return this.serialized;
    }

    public void setSerialized(String serialized) {
        this.serialized = serialized;
    }

}
