/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2011-2013 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.client.rpc;

import java.util.List;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous version of IExchangeRpc.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IExchangeRpcAsync {

    /**
     * Retrieve an exchange by exchange id.
     * @param exchangeId  Exchange id.
     * @param callback    Callback to be invoked after method call completes
     */
    void retrieveExchange(Long exchangeId, AsyncCallback<Exchange> callback);

    /**
     * Get a set of exchanges that match the passed-in criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @param callback    Callback to be invoked after method call completes
     */
    void getExchanges(ExchangeCriteria criteria, Pagination pagination, AsyncCallback<PaginatedResults<Exchange>> callback);

    /**
     * Create a new exchange.
     * @param name     Name of the exchange
     * @param callback Callback to be invoked after method call completes
     */
    void createExchange(String name, AsyncCallback<Exchange> callback);

    /**
     * Delete a set of exchanges.
     * @param records  Records to delete
     * @param callback Callback to be invoked after method call completes
     */
    void deleteExchanges(List<Exchange> records, AsyncCallback<Void> callback);

    /**
     * Save changes to an exchange.
     * @param exchange  Exchange to save
     * @param callback  Callback to be invoked after method call completes
     */
    void saveExchange(Exchange exchange, AsyncCallback<Exchange> callback);

    /**
     * Generate assignments and send notifications for an exchange, and also save the exchange.
     * @param exchange  Exchange to send notifications for
     * @param callback  Callback to be invoked after method call completes
     */
    void sendNotifications(Exchange exchange, AsyncCallback<Exchange> callback);

    /**
     * Re-send notifications for a specific set of participants, and also save the exchange.
     * @param exchange      Exchange to operate on
     * @param participants  Set of participants to re-send notifications for
     * @param callback      Callback to be invoked after method call completes
     */
    void resendNotification(Exchange exchange, ParticipantSet participants, AsyncCallback<Exchange> callback);

    /**
     * Generate a preview for an exchange notification email.
     * @param exchange      Exchange to operate on
     * @param callback      Callback to be invoked after method call completes
     */
    void generatePreview(Exchange exchange, AsyncCallback<EmailMessage> callback);

}
