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
package com.cedarsolutions.santa.server.dao.impl;

import com.cedarsolutions.dao.gae.IFilterPredicate;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.util.gwt.GwtStringUtils;

/**
 * Filter predicate based on exchange search criteria.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeFilterPredicate implements IFilterPredicate<Exchange> {

    /** Search criteria. */
    private ExchangeCriteria criteria;

    /** Create a filter predicate in terms of exchange search criteria. */
    public ExchangeFilterPredicate(ExchangeCriteria criteria) {
        this.criteria = criteria;
    }

    /** Evaluate the predicate for the passed-in value. */
    @Override
    public boolean evaluate(Exchange value) {
        return meetsUserIdCriteria(criteria, value) &&
               meetsExchangeIdCriteria(criteria, value);
    }

    /** Indicates whether the passed-in row meets the user id criteria. */
    private static boolean meetsUserIdCriteria(ExchangeCriteria criteria, Exchange exchange) {
        return GwtStringUtils.equals(criteria.getUserId(), exchange.getUserId());
    }

    /** Indicates whether the passed-in row meets the exchange id criteria. */
    private static boolean meetsExchangeIdCriteria(ExchangeCriteria criteria, Exchange exchange) {
        if (criteria.getExchangeIds() == null || criteria.getExchangeIds().isEmpty()) {
            return true;
        } else {
            return criteria.getExchangeIds().contains(exchange.getId());
        }
    }

}
