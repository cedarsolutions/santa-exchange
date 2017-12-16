/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013-2014,2017 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.server.rpc.impl;

import static com.cedarsolutions.util.ServiceExceptionUtils.createServiceException;

import java.util.List;

import org.apache.log4j.Logger;

import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.exception.RpcSecurityException;
import com.cedarsolutions.exception.ServiceException;
import com.cedarsolutions.santa.client.rpc.IExchangeRpc;
import com.cedarsolutions.santa.server.dao.IExchangeDao;
import com.cedarsolutions.santa.server.service.IAuditEventService;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.server.service.IExchangeService;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.exchange.Assignment;
import com.cedarsolutions.santa.shared.domain.exchange.AssignmentSet;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeState;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.shared.domain.email.EmailMessage;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Functionality for maintaining exchanges.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeRpc extends AbstractService implements IExchangeRpc {

    /** Logger instance. */
    private static Logger LOGGER = LoggingUtils.getLogger(ExchangeRpc.class);

    /** Audit event service. */
    private IAuditEventService auditEventService;

    /** Exchange service. */
    private IExchangeService exchangeService;

    /** Client session service. */
    private IClientSessionService clientSessionService;

    /** Exchange DAO. */
    private IExchangeDao exchangeDao;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.auditEventService == null ||
                this.exchangeService == null ||
                this.clientSessionService == null ||
                this.exchangeDao == null) {
            throw new NotConfiguredException("ExchangeRpc is not properly configured.");
        }
    }

    /**
     * Retrieve an exchange by exchange id.
     * @param exchangeId  Exchange id.
     * @return Exchange retrieved from the back-end, or null if the exchange id is not known.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public Exchange retrieveExchange(Long exchangeId) throws ServiceException {
        try  {
            Exchange exchange = this.exchangeDao.retrieveExchange(exchangeId);
            if (exchange != null) {
                this.validateUserId(exchange);
            }

            return exchange;
        } catch (RpcSecurityException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error retrieving exchange: " + e.getMessage(), e);
            throw createServiceException("Error retrieving exchange: " + e.getMessage(), e);
        }
    }

    /**
     * Get a set of exchanges that match the passed-in criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public PaginatedResults<Exchange> getExchanges(ExchangeCriteria criteria, Pagination pagination) throws ServiceException {
        try  {
            this.validateUserId(criteria);
            criteria.setUserId(this.getUserId()); // so we definitely only get back rows for this user
            return this.exchangeDao.retrieveExchanges(criteria, pagination);
        } catch (RpcSecurityException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error getting exchanges: " + e.getMessage(), e);
            throw createServiceException("Error getting exchanges: " + e.getMessage(), e);
        }
    }

    /**
     * Create a new exchange.
     * @param name  Name of the exchange
     * @return The newly-created exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public Exchange createExchange(String name) throws ServiceException {
        try  {
            String userId = this.getUserId();

            Exchange exchange = new Exchange();
            exchange.setUserId(userId);
            exchange.setName(name);
            exchange.setExchangeState(ExchangeState.NEW);

            long id = this.exchangeDao.insertExchange(exchange);
            exchange.setId(id);

            AuditEvent auditEvent = this.auditEventService.buildCreateExchangeEvent(id);
            this.auditEventService.logAuditEvent(auditEvent);

            return exchange;
        } catch (RpcSecurityException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error creating exchange: " + e.getMessage(), e);
            throw createServiceException("Error creating exchange: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a set of exchanges.
     * @param records  Records to delete
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public void deleteExchanges(List<Exchange> records) throws ServiceException {
        try {
            this.validateUserId(records);
            for (Exchange record : records) {
                this.exchangeDao.deleteExchange(record);
                AuditEvent auditEvent = this.auditEventService.buildDeleteExchangeEvent(record.getId());
                this.auditEventService.logAuditEvent(auditEvent);
            }
        } catch (RpcSecurityException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error deleting exchanges: " + e.getMessage(), e);
            throw createServiceException("Error deleting exchanges: " + e.getMessage(), e);
        }
    }

    /**
     * Save changes to an exchange.
     * @param exchange  Exchange to save
     * @return The newly-saved exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     */
    @Override
    public Exchange saveExchange(Exchange exchange) throws ServiceException {
        try {
            this.validateUserId(exchange);

            if (ExchangeState.NEW.equals(exchange.getExchangeState())) {
                exchange.setExchangeState(ExchangeState.STARTED);
            }

            this.exchangeDao.updateExchange(exchange);
            return exchange;
        } catch (RpcSecurityException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error saving exchange: " + e.getMessage(), e);
            throw createServiceException("Error saving exchange: " + e.getMessage(), e);
        }
    }

    /**
     * Generate assignments and send notifications for an exchange, and also save the exchange.
     * @param exchange       Exchange to send notifications for
     * @return The newly-saved exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the exchange is not valid.
     */
    @Override
    public Exchange sendNotifications(Exchange exchange) throws ServiceException, InvalidDataException  {
        try {
            this.validateUserId(exchange);

            AssignmentSet assignments = null;
            try {
                // First pass with auto-conflict set to true, since that yields a better result (if it works)
                assignments = this.exchangeService.generateAssignments(exchange, true);
            } catch (InvalidDataException e) {
                // Second pass with auto-conflict set to false, since it's more likely to succeed
                assignments = this.exchangeService.generateAssignments(exchange, false);
            }

            int recipients = this.exchangeService.sendMessages(exchange, assignments, false);

            exchange.setAssignments(assignments);
            exchange.setExchangeState(ExchangeState.SENT);
            exchange = this.saveExchange(exchange);

            AuditEvent auditEvent = this.auditEventService.buildExchangeEmailEvent(exchange.getId(), recipients);
            this.auditEventService.logAuditEvent(auditEvent);

            return exchange;
        } catch (RpcSecurityException e) {
            throw e;
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error sending notifications: " + e.getMessage(), e);
            throw createServiceException("Error sending notifications: " + e.getMessage(), e);
        }
    }

    /**
     * Re-send notifications for a specific set of participants, and also save the exchange.
     * @param exchange      Exchange to operate on
     * @param participants  Set of participants to re-send notifications for
     * @return The newly-saved exchange.
     * @throws RpcSecurityException If there is a security or permissions problem.
     * @throws ServiceException If there is a problem with the method call.
     * @throws InvalidDataException If the exchange is not valid.
     */
    @Override
    public Exchange resendNotification(Exchange exchange, ParticipantSet participants) throws ServiceException, InvalidDataException {
        try {
            this.validateUserId(exchange);

            AssignmentSet assignments = new AssignmentSet();
            for (Participant participant : participants) {
                Participant receiver = exchange.getAssignments().getGiftReceiver(participant);
                if (receiver != null) {
                    Assignment assignment = new Assignment(participant, receiver);
                    assignments.add(assignment);
                }
            }

            if (!assignments.isEmpty()) {
                this.exchangeService.sendMessages(exchange, assignments, false);

                AuditEvent auditEvent = this.auditEventService.buildResendEmailsEvent(exchange.getId(), assignments.size());
                this.auditEventService.logAuditEvent(auditEvent);
            }

            return exchange;
        } catch (RpcSecurityException e) {
            throw e;
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error resending notification: " + e.getMessage(), e);
            throw createServiceException("Error resending notification: " + e.getMessage(), e);
        }
    }

    /**
     * Generate a preview for an exchange notification email.
     * @param exchange      Exchange to operate on
     * @return A preview of the exchange notification email
     * @throws ServiceException If there is a problem with the method call
     * @throws InvalidDataException If the exchange is not valid.
     */
    @Override
    public EmailMessage generatePreview(Exchange exchange) throws ServiceException, InvalidDataException {
        try {
            this.validateUserId(exchange);
            return this.exchangeService.generatePreview(exchange);
        } catch (RpcSecurityException e) {
            throw e;
        } catch (InvalidDataException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error generating preview: " + e.getMessage(), e);
            throw createServiceException("Error generating preview: " + e.getMessage(), e);
        }
    }

    /**
     * Validate the user id on search criteria.
     * @throws RpcSecurityException If there current user does not own one of the passed-in records.
     */
    private void validateUserId(ExchangeCriteria... records) throws ServiceException {
        String userId = this.getUserId();
        for (ExchangeCriteria record : records) {
            if (!userId.equals(record.getUserId())) {
                throw new RpcSecurityException("User may only operate on exchanges that they own");
            }
        }
    }

    /**
     * Validate the user id on a list of records.
     * @throws RpcSecurityException If there current user does not own one of the passed-in records.
     */
    private void validateUserId(Exchange... records) {
        String userId = this.getUserId();
        if (records != null) {
            for (Exchange record : records) {
                if (!userId.equals(record.getUserId())) {
                    throw new RpcSecurityException("User may only operate on exchanges that they own");
                }
            }
        }
    }

    /**
     * Validate the user id on a list of records.
     * @throws RpcSecurityException If there current user does not own one of the passed-in records.
     */
    private void validateUserId(List<Exchange> records) {
        String userId = this.getUserId();
        if (records != null) {
            for (Exchange record : records) {
                if (!userId.equals(record.getUserId())) {
                    throw new RpcSecurityException("User may only operate on exchanges that they own");
                }
            }
        }
    }

    /** Get the user id from the current session. */
    private String getUserId() {
        return this.clientSessionService.retrieveClientSession().getCurrentUser().getUserId();
    }

    public IAuditEventService getAuditEventService() {
        return this.auditEventService;
    }

    public void setAuditEventService(IAuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    public IExchangeService getExchangeService() {
        return this.exchangeService;
    }

    public void setExchangeService(IExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    public IClientSessionService getClientSessionService() {
        return this.clientSessionService;
    }

    public void setClientSessionService(IClientSessionService clientSessionService) {
        this.clientSessionService = clientSessionService;
    }

    public IExchangeDao getExchangeDao() {
        return this.exchangeDao;
    }

    public void setExchangeDao(IExchangeDao exchangeDao) {
        this.exchangeDao = exchangeDao;
    }

}
