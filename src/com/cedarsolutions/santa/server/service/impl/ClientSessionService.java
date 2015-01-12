/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *              C E D A R
 *          S O L U T I O N S       "Software done right."
 *           S O F T W A R E
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * Copyright (c) 2013,2015 Kenneth J. Pronovici.
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cedarsolutions.exception.NotConfiguredException;
import com.cedarsolutions.santa.server.dao.IAuditEventDao;
import com.cedarsolutions.santa.server.dao.IRegisteredUserDao;
import com.cedarsolutions.santa.server.service.IClientSessionService;
import com.cedarsolutions.santa.server.service.INotificationService;
import com.cedarsolutions.santa.shared.domain.ClientSession;
import com.cedarsolutions.santa.shared.domain.Module;
import com.cedarsolutions.santa.shared.domain.audit.AuditEvent;
import com.cedarsolutions.santa.shared.domain.audit.AuditEventType;
import com.cedarsolutions.santa.shared.domain.audit.ExtraData;
import com.cedarsolutions.santa.shared.domain.audit.ExtraDataKey;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.server.service.IGaeUserService;
import com.cedarsolutions.server.service.ISpringContextService;
import com.cedarsolutions.server.service.impl.AbstractService;
import com.cedarsolutions.shared.domain.FederatedUser;
import com.cedarsolutions.util.DateUtils;
import com.cedarsolutions.util.LoggingUtils;

/**
 * Client session functionality.
 *
 * <p>
 * Most other services will inject the audit event service and use its
 * interface methods to log audit events.  That's not possible here,
 * because AuditEventService itself needs to inject this service, to
 * get at the client session.  So, this service uses the audit event
 * DAO directly.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ClientSessionService extends AbstractService implements IClientSessionService {

    /** The ROLE_ENABLED client-side role, which ties into the authentication framework. */
    public static final String ROLE_ENABLED = "ROLE_ENABLED";

    /** Log4j logger. */
    private static final Logger LOGGER = LoggingUtils.getLogger(ClientSessionService.class);

    /** GAE user service. */
    private IGaeUserService gaeUserService;

    /** Spring context service. */
    private ISpringContextService springContextService;

    /** Notification service. */
    private INotificationService notificationService;

    /** Audit event DAO. */
    private IAuditEventDao auditEventDao;

    /** Registered user DAO. */
    private IRegisteredUserDao registeredUserDao;

    /**
     * Invoked by a bean factory after it has set all bean properties.
     * @throws NotConfiguredException In the event of misconfiguration.
     */
    @Override
    public void afterPropertiesSet() throws NotConfiguredException {
        super.afterPropertiesSet();
        if (this.gaeUserService == null ||
                this.springContextService == null ||
                this.notificationService == null ||
                this.auditEventDao == null ||
                this.registeredUserDao == null) {
            throw new NotConfiguredException("ClientSessionService is not properly configured.");
        }
    }

    /**
     * Establish a client session on the back-end.
     * @param module                Module that the session is being established for
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @return Newly-established client session as retrieved from the back-end.
     */
    @Override
    public ClientSession establishClientSession(Module module, String logoutDestinationUrl) {
        LOGGER.debug("Established client session in module " + module);
        ClientSession session = this.retrieveClientSession(logoutDestinationUrl);
        this.handleLogin(module, session);
        return session;
    }

    /**
     * Invalidate the current session at the back-end.
     * @param module  Module that the session is being invalidated within
     */
    @Override
    public void invalidateClientSession(Module module) {
        LOGGER.debug("Invalidated client session in module " + module);
        this.springContextService.invalidateCurrentSession();
    }

    /**
     * Retrieve a client session from the back-end, with no destination URL set.
     * @return Client session retrieved from the back-end.
     */
    @Override
    public ClientSession retrieveClientSession() {
        return retrieveClientSession(null);
    }

    /**
     * Retrieve a client session from the back-end.
     * @param logoutDestinationUrl  Destination URL to use when generating the logout URL
     * @return Client session retrieved from the back-end.
     */
    @Override
    public ClientSession retrieveClientSession(String logoutDestinationUrl) {
        FederatedUser currentUser = this.gaeUserService.getCurrentUser();
        String sessionId = normalizeSessionId(this.springContextService.getCurrentSessionId());

        String logoutUrl = null;
        if (currentUser != null && logoutDestinationUrl != null) {
            logoutUrl = this.gaeUserService.getGoogleAccountsLogoutUrl(logoutDestinationUrl);
        }

        ClientSession clientSession = new ClientSession();
        clientSession.initialize();
        clientSession.setCurrentUser(currentUser);
        clientSession.setSessionId(sessionId);
        clientSession.setLogoutUrl(logoutUrl);

        return clientSession;
    }

    /**
     * Handle the login event, registering the user if needed.
     * @param module   Module that the session is being established for
     * @param session  Client session which was just established
     */
    private void handleLogin(Module module, ClientSession session) {
        if (session.isLoggedIn()) {
            this.handleUserRegistration(module, session);
            this.setClientRoles(session);
            this.logUserLoginEvent(module, session);  // mark them as logged in after they are registered
            LOGGER.debug("Logged in user: " + session.getCurrentUser().getUserId() + " (" + session.getCurrentUser().getUserName() + ")");
        }
    }

    /** Set client roles based on the session. */
    private void setClientRoles(ClientSession session) {
        if (session.isLoggedIn() && !session.isLocked()) {
            this.gaeUserService.addClientRoles(ROLE_ENABLED);
            LOGGER.debug("Working with session id: " + this.springContextService.getCurrentSessionId());
        }
    }

    /**
     * Handle user-registration tasks (either register the user, or update their record).
     * @param module   Module that the session is being established for
     * @param session  Client session which was just established
     */
    private void handleUserRegistration(Module module, ClientSession session) {
        String userId = session.getCurrentUser().getUserId();
        RegisteredUser registeredUser = this.registeredUserDao.retrieveRegisteredUser(userId);
        if (registeredUser != null) {
            registeredUser = this.registeredUserDao.recordNewLogin(registeredUser);
            session.setRegisteredUser(registeredUser);
        } else {
            session.getCurrentUser().setFirstLogin(true);  // so the application can tell they're new
            registeredUser = createRegisteredUser(session.getCurrentUser());
            this.registeredUserDao.insertRegisteredUser(registeredUser);
            this.notificationService.notifyRegisteredUser(registeredUser);
            session.setRegisteredUser(registeredUser);
            this.logRegisteredUserEvent(module, session);
            LOGGER.debug("Registered user: " + session.getCurrentUser().getUserId());
        }
    }

    /** Normalize session id. */
    private static String normalizeSessionId(String sessionId) {
        return StringUtils.isEmpty(sessionId) ? "<none>" : sessionId;
    }

    /** Log a user login event. */
    private void logUserLoginEvent(Module module, ClientSession session) {
        if (session.getCurrentUser().isAdmin()) {
            AuditEvent auditEvent = buildAuditEvent(AuditEventType.ADMIN_LOGIN, module, session);
            auditEventDao.insertAuditEvent(auditEvent);
        } else {
            AuditEvent auditEvent = buildAuditEvent(AuditEventType.USER_LOGIN, module, session);
            auditEventDao.insertAuditEvent(auditEvent);
        }
    }

    /** Log a register user event. */
    private void logRegisteredUserEvent(Module module, ClientSession session) {
        AuditEvent auditEvent = buildAuditEvent(AuditEventType.REGISTER_USER, module, session);
        auditEventDao.insertAuditEvent(auditEvent);
    }

    /**
     * Create an audit event.
     * @param eventType Audit event type to use
     * @param module    Module in which the session was invalidated
     * @param session   Client session
     * @return Newly-created event.
     */
    private static AuditEvent buildAuditEvent(AuditEventType eventType, Module module, ClientSession session) {
        Date eventTimestamp = DateUtils.getCurrentDate();
        String userId = deriveUserId(session);
        String emailAddress = deriveEmailAddress(session);
        String moduleName = module == null ? null : module.toString();

        AuditEvent auditEvent = new AuditEvent();
        auditEvent.setEventType(eventType);
        auditEvent.setEventTimestamp(eventTimestamp);
        auditEvent.setUserId(userId);
        auditEvent.setSessionId(session.getSessionId());

        if (eventType == AuditEventType.REGISTER_USER) {
            ExtraData moduleData = new ExtraData(ExtraDataKey.MODULE, moduleName);
            ExtraData emailAddressData = new ExtraData(ExtraDataKey.EMAIL_ADDRESS, emailAddress);
            auditEvent.setExtraData(moduleData, emailAddressData);
        } else {
            ExtraData moduleData = new ExtraData(ExtraDataKey.MODULE, moduleName);
            auditEvent.setExtraData(moduleData);
        }

        return auditEvent;
    }

    /**
     * Derive a user id based on a client session.
     * @param session  Client session
     * @return User id, possibly null.
     */
    protected static String deriveUserId(ClientSession session) {
        if (session != null) {
            if (session.getCurrentUser() != null) {
                return session.getCurrentUser().getUserId();
            }
        }

        return null;
    }

    /**
     * Derive an email address based on a client session.
     * @param session  Client session
     * @return Email address, possibly null.
     */
    protected static String deriveEmailAddress(ClientSession session) {
        if (session != null) {
            if (session.getCurrentUser() != null) {
                return session.getCurrentUser().getEmailAddress();
            }
        }

        return null;
    }

    /** Create a new registered user. */
    protected static RegisteredUser createRegisteredUser(FederatedUser currentUser) {
        RegisteredUser registeredUser = new RegisteredUser();

        registeredUser.setUserId(currentUser.getUserId());
        registeredUser.setUserName(currentUser.getUserName());
        registeredUser.setRegistrationDate(DateUtils.getCurrentDate());
        registeredUser.setAuthenticationDomain(currentUser.getAuthenticationDomain());
        registeredUser.setOpenIdProvider(currentUser.getOpenIdProvider());
        registeredUser.setFederatedIdentity(currentUser.getFederatedIdentity());
        registeredUser.setEmailAddress(currentUser.getEmailAddress());
        registeredUser.setLogins(1);
        registeredUser.setLastLogin(registeredUser.getRegistrationDate());
        registeredUser.setAdmin(currentUser.isAdmin());
        registeredUser.setLocked(false);

        return registeredUser;
    }

    public IGaeUserService getGaeUserService() {
        return this.gaeUserService;
    }

    public void setGaeUserService(IGaeUserService gaeUserService) {
        this.gaeUserService = gaeUserService;
    }

    public ISpringContextService getSpringContextService() {
        return this.springContextService;
    }

    public void setSpringContextService(ISpringContextService springContextService) {
        this.springContextService = springContextService;
    }

    public INotificationService getNotificationService() {
        return this.notificationService;
    }

    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public IAuditEventDao getAuditEventDao() {
        return this.auditEventDao;
    }

    public void setAuditEventDao(IAuditEventDao auditEventDao) {
        this.auditEventDao = auditEventDao;
    }

    public IRegisteredUserDao getRegisteredUserDao() {
        return this.registeredUserDao;
    }

    public void setRegisteredUserDao(IRegisteredUserDao registeredUserDao) {
        this.registeredUserDao = registeredUserDao;
    }

}
