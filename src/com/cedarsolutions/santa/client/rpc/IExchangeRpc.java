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

import org.springframework.security.annotation.Secured;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.RpcSecurityException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.server.rpc.XsrfProtect;

/**
 * Functionality for maintaining exchanges.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@RemoteServiceRelativePath("rpc/exchangeRpc.rpc")
@Secured({"ROLE_USER", "ROLE_ENABLED" })  // only enabled, logged in users can call these methods
@XsrfProtect  // Apply protection to prevent CSRF/XSRF attacks
public interface IExchangeRpc extends RemoteService {

    /**
     * Retrieve an exchange by exchange id.
     * @param exchangeId  Exchange id.
     * @return Exchange retrieved from the back-end, or null if the exchange id is not known.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    Exchange retrieveExchange(Long exchangeId) throws RpcSecurityException, ServiceException;

    /**
     * Get a set of exchanges that match the passed-in criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    PaginatedResults<Exchange> getExchanges(ExchangeCriteria criteria, Pagination pagination) throws RpcSecurityException, ServiceException;

    /**
     * Create a new exchange.
     * @param name  Name of the exchange
     * @return The newly-created exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    Exchange createExchange(String name) throws RpcSecurityException, ServiceException;

    /**
     * Delete a set of exchanges.
     * @param records  Records to delete
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    void deleteExchanges(List<Exchange> records) throws RpcSecurityException, ServiceException;

    /**
     * Save changes to an exchange.
     * @param exchange  Exchange to save
     * @return The newly-saved exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    Exchange saveExchange(Exchange exchange) throws RpcSecurityException, ServiceException;

    /**
     * Generate assignments and send notifications for an exchange, and also save the exchange.
     * @param exchange  Exchange to send notifications for
     * @return The newly-saved exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the exchange is not valid.
     */
    Exchange sendNotifications(Exchange exchange) throws RpcSecurityException, ServiceException, InvalidDataException;

    /**
     * Re-send notifications for a specific set of participants, and also save the exchange.
     * @param exchange      Exchange to operate on
     * @param participants  Set of participants to re-send notifications for
     * @return The newly-saved exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the exchange is not valid.
     */
    Exchange resendNotification(Exchange exchange, ParticipantSet participants) throws RpcSecurityException, ServiceException, InvalidDataException;

    /**
     * Generate a preview for an exchange notification email.
     * @param exchange      Exchange to operate on
     * @return A preview of the exchange notification email
     * @throws ServiceException If there is a problem with the method call
     * @throws InvalidDataException If the exchange is not valid.
     */
    EmailMessage generatePreview(Exchange exchange) throws ServiceException, InvalidDataException;

}
