/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2017 Kenneth J. Pronovici.
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
package com.cedarsolutions.santa.server.service.impl;

import java.util.Date;
import java.util.List;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.server.service.IAuditEventService;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;
import com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.util.DateUtils;

/**
 * Log auditable events in the system.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class AuditEventService extends AbstractService implements IAuditEventService {

    /** Client session service. */
    private IClientSessionService clientSessionService;

    /** Audit event DAO. */
    private IAuditEventDao auditEventDao;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.clientSessionService == null || auditEventDao == null) {
            throw new NotConfiguredException("AuditEventService is not properly configured.");
        }
    }

    /**
     * Log the passed-in audit event to the datastore
     * Required context (i.e. client session) will automatically be added to the event.
     * @param auditEvent    Audit event to log
     */
    @Override
    public void logAuditEvent(AuditEvent auditEvent) {
        this.auditEventDao.insertAuditEvent(auditEvent);
    }

    /**
     * Build an audit event, including context information.
     * @param eventType   Event type
     * @param extraData   Extra event data
     * @return Newly-created audit event.
     */
    @Override
    public AuditEvent buildAuditEvent(AuditEventType eventType, ExtraData... extraData) {
        AuditEvent auditEvent = new AuditEvent();

        auditEvent.setEventType(eventType);
        auditEvent.setExtraData(extraData);
        this.addContextInformation(auditEvent);

        return auditEvent;
    }

    /**
     * Build an audit event, including context information.
     * @param eventType   Event type
     * @param extraData   Extra event data
     * @return Newly-created audit event.
     */
    @Override
    public AuditEvent buildAuditEvent(AuditEventType eventType, List<ExtraData> extraData) {
        AuditEvent auditEvent = new AuditEvent();

        auditEvent.setEventType(eventType);
        auditEvent.setExtraData(extraData);
        this.addContextInformation(auditEvent);

        return auditEvent;
    }

    /**
     * Build a delete user event.
     * @param userId        User id of the user that was deleted
     * @param emailAddress  Email address of the user that was deleted
     */
    @Override
    public AuditEvent buildDeleteUserEvent(String userId, String emailAddress) {
        ExtraData userIdData = new ExtraData(ExtraDataKey.USER_ID, userId);
        ExtraData emailAddressData = new ExtraData(ExtraDataKey.EMAIL_ADDRESS, emailAddress);
        return this.buildAuditEvent(AuditEventType.DELETE_USER, userIdData, emailAddressData);
    }

    /**
     * Build a lock user event.
     * @param userId        User id of the user that was locked
     * @param emailAddress  Email address of the user that was locked
     */
    @Override
    public AuditEvent buildLockUserEvent(String userId, String emailAddress) {
        ExtraData userIdData = new ExtraData(ExtraDataKey.USER_ID, userId);
        ExtraData emailAddressData = new ExtraData(ExtraDataKey.EMAIL_ADDRESS, emailAddress);
        return this.buildAuditEvent(AuditEventType.LOCK_USER, userIdData, emailAddressData);
    }

    /**
     * Build an unlock user event.
     * @param userId        User id of the user that was unlocked
     * @param emailAddress  Email address of the user that was locked
     */
    @Override
    public AuditEvent buildUnlockUserEvent(String userId, String emailAddress) {
        ExtraData userIdData = new ExtraData(ExtraDataKey.USER_ID, userId);
        ExtraData emailAddressData = new ExtraData(ExtraDataKey.EMAIL_ADDRESS, emailAddress);
        return this.buildAuditEvent(AuditEventType.UNLOCK_USER, userIdData, emailAddressData);
    }

    /**
     * Build a create exchange event.
     * @param exchangeId  Exchange id of the exchange that was created
     */
    @Override
    public AuditEvent buildCreateExchangeEvent(Long exchangeId) {
        ExtraData exchangeIdData = new ExtraData(ExtraDataKey.EXCHANGE_ID, exchangeId);
        return this.buildAuditEvent(AuditEventType.CREATE_EXCHANGE, exchangeIdData);
    }

    /**
     * Build a delete exchange event.
     * @param exchangeId  Exchange id of the exchange that was deleted
     */
    @Override
    public AuditEvent buildDeleteExchangeEvent(Long exchangeId) {
        ExtraData exchangeIdData = new ExtraData(ExtraDataKey.EXCHANGE_ID, exchangeId);
        return this.buildAuditEvent(AuditEventType.DELETE_EXCHANGE, exchangeIdData);
    }

    /**
     * Build a exchange email event.
     * @param exchangeId  Exchange id of the exchange for which emails were sent
     * @param recipients  Number of recipients that emails were generated for
     */
    @Override
    public AuditEvent buildExchangeEmailEvent(Long exchangeId, int recipients) {
        ExtraData exchangeIdData = new ExtraData(ExtraDataKey.EXCHANGE_ID, exchangeId);
        ExtraData recipientsData = new ExtraData(ExtraDataKey.RECIPIENTS, recipients);
        return this.buildAuditEvent(AuditEventType.EXCHANGE_EMAIL, exchangeIdData, recipientsData);
    }

    /**
     * Build a resend emails event.
     * @param exchangeId  Exchange id of the exchange for which emails were sent
     * @param recipients  Number of recipients that emails were generated for
     */
    @Override
    public AuditEvent buildResendEmailsEvent(Long exchangeId, int recipients) {
        ExtraData exchangeIdData = new ExtraData(ExtraDataKey.EXCHANGE_ID, exchangeId);
        ExtraData recipientsData = new ExtraData(ExtraDataKey.RECIPIENTS, recipients);
        return this.buildAuditEvent(AuditEventType.RESEND_EMAILS, exchangeIdData, recipientsData);
    }

    /**
     * Add context (i.e. event timestamp, client session) to the passed-in event.
     * @param auditEvent    Audit event, which will be modified in-place
     */
    private void addContextInformation(AuditEvent auditEvent) {
        Date eventTimestamp = DateUtils.getCurrentDate();
        ClientSession clientSession = this.clientSessionService.retrieveClientSession();
        String userId = deriveUserId(clientSession);

        auditEvent.setEventTimestamp(eventTimestamp);
        auditEvent.setUserId(userId);
        auditEvent.setSessionId(clientSession.getSessionId());
    }

    /** Derive a user id based on a client session. */
    protected static String deriveUserId(ClientSession clientSession) {
        if (clientSession != null) {
            if (clientSession.getCurrentUser() != null) {
                return clientSession.getCurrentUser().getUserId();
            }
        }

        return null;
    }

    public IClientSessionService getClientSessionService() {
        return this.clientSessionService;
    }

    public void setClientSessionService(IClientSessionService clientSessionService) {
        this.clientSessionService = clientSessionService;
    }

    public IAuditEventDao getAuditEventDao() {
        return this.auditEventDao;
    }

    public void setAuditEventDao(IAuditEventDao auditEventDao) {
        this.auditEventDao = auditEventDao;
    }

}
