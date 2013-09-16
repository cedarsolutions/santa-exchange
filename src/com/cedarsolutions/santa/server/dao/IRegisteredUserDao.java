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
package com.cedarsolutions.santa.server.dao;

import java.util.Iterator;

import com.cedarsolutions.dao.IDaoTransaction;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;

/**
 * DAO to handle registered users.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IRegisteredUserDao {

    /**
     * Insert a registered user into the back-end data store.
     * @param registeredUser  Registered user to insert
     * @throws DaoException If the operation fails.
     */
    void insertRegisteredUser(RegisteredUser registeredUser) throws DaoException;

    /**
     * Insert a registered user into the back-end data store (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to insert
     * @throws DaoException If the operation fails.
     */
    void insertRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException;

    /**
     * Retrieve a registered user by user id.
     * @param userId  User id of the user to retrieve
     * @return Registered user retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser retrieveRegisteredUser(String userId) throws DaoException;

    /**
     * Retrieve a registered user by user id (transactional).
     * @param transaction  Transaction to operate within
     * @param userId       User id of the user to retrieve
     * @return Registered user retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser retrieveRegisteredUser(IDaoTransaction transaction, String userId) throws DaoException;

    /**
     * Delete registered user by user id.
     * @param userId  User id of the user to delete
     * @throws DaoException If the operation fails.
     */
    void deleteRegisteredUser(String userId) throws DaoException;

    /**
     * Delete registered user by user id (transactional).
     * @param transaction  Transaction to operate within
     * @param userId       User id of the user to delete
     * @throws DaoException If the operation fails.
     */
    void deleteRegisteredUser(IDaoTransaction transaction, String userId) throws DaoException;

    /**
     * Delete the passed-in registered user.
     * @param registeredUser  Registered user to delete
     * @throws DaoException If the operation fails.
     */
    void deleteRegisteredUser(RegisteredUser registeredUser) throws DaoException;

    /**
     * Update the passed-in registered user.
     * @param registeredUser  Registered user to update
     * @throws DaoException If the operation fails.
     */
    void updateRegisteredUser(RegisteredUser registeredUser) throws DaoException;

    /**
     * Update the passed-in registered user (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @throws DaoException If the operation fails.
     */
    void updateRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException;

    /**
     * Lock a registered user.
     * @param registeredUser  Registered user to update
     * @return Registered user that was locked.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser lockRegisteredUser(RegisteredUser registeredUser) throws DaoException;

    /**
     * Lock a registered user (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @return Registered user that was locked.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser lockRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException;

    /**
     * Unlock a registered user.
     * @param registeredUser  Registered user to update
     * @return Registered user that was unlocked.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser unlockRegisteredUser(RegisteredUser registeredUser) throws DaoException;

    /**
     * Unlock a registered user (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @return Registered user that was unlocked.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser unlockRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException;

    /**
     * Record a new login for a registered user.
     * This increments the number of logins and sets the last login date.
     * @param registeredUser  Registered user to update
     * @return Registered user that was updated.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser recordNewLogin(RegisteredUser registeredUser) throws DaoException;

    /**
     * Record a new login for a registered user (transactional).
     * This increments the number of logins and sets the last login date.
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @return Registered user that was updated.
     * @throws DaoException If the operation fails.
     */
    RegisteredUser recordNewLogin(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException;

    /**
     * Retrieve the registered users that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria  Search criteria to apply
     * @return Iterator for the results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    Iterator<RegisteredUser> retrieveRegisteredUsers(RegisteredUserCriteria criteria)
    throws InvalidDataException, DaoException;

    /**
     * Retrieve the registered users that match search criteria.
     * See documentation on the criteria class for information on how criteria are applied.
     * @param criteria    Search criteria to apply
     * @param pagination  Pagination to use
     * @return Paginated results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    PaginatedResults<RegisteredUser> retrieveRegisteredUsers(RegisteredUserCriteria criteria, Pagination pagination)
    throws InvalidDataException, DaoException;

}
