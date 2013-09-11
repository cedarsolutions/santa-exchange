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
package com.cedarsolutions.santa.server.dao.impl;

import java.util.Iterator;

import com.cedarsolutions.dao.IDaoTransaction;
import com.cedarsolutions.dao.domain.PaginatedResults;
import com.cedarsolutions.dao.domain.Pagination;
import com.cedarsolutions.dao.domain.SortOrder;
import com.cedarsolutions.dao.gae.impl.AbstractGaeDao;
import com.cedarsolutions.dao.gae.impl.FilteredResultIterator;
import com.cedarsolutions.dao.gae.impl.GaeDaoTransaction;
import com.cedarsolutions.dao.gae.impl.PaginationUtils;
import com.cedarsolutions.exception.DaoException;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.server.dao.IRegisteredUserDao;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserSortColumn;
import com.cedarsolutions.util.DateUtils;
import com.googlecode.objectify.Query;

/**
 * DAO to handle registered users.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RegisteredUserDao extends AbstractGaeDao implements IRegisteredUserDao {

    /**
     * Insert a registered user into the back-end data store.
     * @param registeredUser  Registered user to insert
     * @throws DaoException If the operation fails.
     */
    @Override
    public void insertRegisteredUser(RegisteredUser registeredUser) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            this.insertRegisteredUser(transaction, registeredUser);
            transaction.commit();
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Insert a registered user into the back-end data store (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to insert
     * @throws DaoException If the operation fails.
     */
    @Override
    public void insertRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            gaeTransaction.getObjectify().put(registeredUser);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error inserting user.", e);
        }
    }

    /**
     * Retrieve a registered user by user id.
     * @param userId  User id of the user to retrieve
     * @return Registered user retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser retrieveRegisteredUser(String userId) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            RegisteredUser result = this.retrieveRegisteredUser(transaction, userId);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Retrieve a registered user by user id (transactional).
     * @param transaction  Transaction to operate within
     * @param userId       User id of the user to retrieve
     * @return Registered user retrieved from the database, possibly null.
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser retrieveRegisteredUser(IDaoTransaction transaction, String userId) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            return gaeTransaction.getObjectify().find(RegisteredUser.class, userId);
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving user.", e);
        }
    }

    /**
     * Delete registered user by user id.
     * @param userId  User id of the user to delete
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteRegisteredUser(String userId) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            this.deleteRegisteredUser(transaction, userId);
            transaction.commit();
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Delete registered user by user id (transactional).
     * @param transaction  Transaction to operate within
     * @param userId       User id of the user to delete
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteRegisteredUser(IDaoTransaction transaction, String userId) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            RegisteredUser registeredUser = gaeTransaction.getObjectify().find(RegisteredUser.class, userId);
            if (registeredUser != null) {
                // Don't bother deleting unless it's still there
                gaeTransaction.getObjectify().delete(registeredUser);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error deleting user.", e);
        }
    }

    /**
     * Delete the passed-in registered user.
     * @param registeredUser  Registered user to delete
     * @throws DaoException If the operation fails.
     */
    @Override
    public void deleteRegisteredUser(RegisteredUser registeredUser) throws DaoException {
        this.deleteRegisteredUser(registeredUser.getUserId());
    }

    /**
     * Update the passed-in registered user.
     * @param registeredUser  Registered user to update
     * @throws DaoException If the operation fails.
     */
    @Override
    public void updateRegisteredUser(RegisteredUser registeredUser) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            this.updateRegisteredUser(transaction, registeredUser);
            transaction.commit();
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Update the passed-in registered user (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @throws DaoException If the operation fails.
     */
    @Override
    public void updateRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            RegisteredUser record = gaeTransaction.getObjectify().find(RegisteredUser.class, registeredUser.getUserId());
            if (record == null) {
                // Make sure that we don't accidentally insert this user instead of updating it
                throw new DaoException("Failed to update record: does not exist");
            } else {
                gaeTransaction.getObjectify().put(registeredUser);
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error updating user.", e);
        }
    }

    /**
     * Lock a registered user.
     * @param registeredUser  Registered user to update
     * @return Registered user that was locked, or null if user no longer exists
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser lockRegisteredUser(RegisteredUser registeredUser) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            RegisteredUser result = this.lockRegisteredUser(transaction, registeredUser);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Lock a registered user (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @return Registered user that was locked.
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser lockRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            RegisteredUser record = gaeTransaction.getObjectify().find(RegisteredUser.class, registeredUser.getUserId());
            if (record == null) {
                // Make sure that we don't accidentally insert this user instead of updating it
                throw new DaoException("Failed to update record: does not exist");
            } else {
                // Update only the relevant fields, to minimize data loss from race conditions
                record.setLocked(true);
                gaeTransaction.getObjectify().put(record);
                return record;
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error locking user.", e);
        }
    }

    /**
     * Unlock a registered user.
     * @param registeredUser  Registered user to update
     * @return Registered user that was unlocked, or null if user no longer exists
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser unlockRegisteredUser(RegisteredUser registeredUser) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            RegisteredUser result = this.unlockRegisteredUser(transaction, registeredUser);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Unlock a registered user (transactional).
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @return Registered user that was unlocked.
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser unlockRegisteredUser(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            RegisteredUser record = gaeTransaction.getObjectify().find(RegisteredUser.class, registeredUser.getUserId());
            if (record == null) {
                // Make sure that we don't accidentally insert this user instead of updating it
                throw new DaoException("Failed to update record: does not exist");
            } else {
                // Update only the relevant fields, to minimize data loss from race conditions
                record.setLocked(false);
                gaeTransaction.getObjectify().put(record);
                return record;
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error unlocking user.", e);
        }
    }

    /**
     * Record a new login for a registered user.
     * This increments the number of logins and sets the last login date.
     * @param registeredUser  Registered user to update
     * @return Registered user that was updated, or null if user no longer exists
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser recordNewLogin(RegisteredUser registeredUser) throws DaoException {
        IDaoTransaction transaction = getDaoTransaction();
        try {
            RegisteredUser result = this.recordNewLogin(transaction, registeredUser);
            transaction.commit();
            return result;
        } finally {
            transaction.rollback();  // only matters if we haven't already committed
        }
    }

    /**
     * Record a new login for a registered user (transactional).
     * This increments the number of logins and sets the last login date.
     * @param transaction     Transaction to operate within
     * @param registeredUser  Registered user to update
     * @return Registered user that was updated.
     * @throws DaoException If the operation fails.
     */
    @Override
    public RegisteredUser recordNewLogin(IDaoTransaction transaction, RegisteredUser registeredUser) throws DaoException {
        try {
            GaeDaoTransaction gaeTransaction = checkTransactionType(transaction);
            RegisteredUser record = gaeTransaction.getObjectify().find(RegisteredUser.class, registeredUser.getUserId());
            if (record == null) {
                // Make sure that we don't accidentally insert this user instead of updating it
                throw new DaoException("Failed to update record: does not exist");
            } else {
                // Update only the relevant fields, to minimize data loss from race conditions
                record.setLogins(record.getLogins() + 1);
                record.setLastLogin(DateUtils.getCurrentDate());
                gaeTransaction.getObjectify().put(record);
                return record;
            }
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error recording new login.", e);
        }
    }

    /**
     * Retrieve the registered users that match search criteria.
     *
     * <p>
     * See documentation on the criteria class for information on how criteria are applied.
     * There is no transactional version of this method because GAE does not allow queries
     * like this to be run within transactions.
     * </p>
     *
     * @param criteria  Search criteria to apply
     *
     * @return Iterator for the results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    @Override
    public Iterator<RegisteredUser> retrieveRegisteredUsers(RegisteredUserCriteria criteria) throws InvalidDataException, DaoException {
        try {
            return buildRetrieveIterator(criteria, null);
        } catch (InvalidDataException e) {
            throw e;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving users.", e);
        }
    }

    /**
     * Retrieve the registered users that match search criteria.
     *
     * <p>
     * See documentation on the criteria class for information on how criteria are applied.
     * There is no transactional version of this method because GAE does not allow queries
     * like this to be run within transactions.
     * </p>
     *
     * @param criteria  Search criteria to apply
     * @param pagination  Pagination to use
     *
     * @return Paginated results.
     * @throws InvalidDataException If the criteria is invalid
     * @throws DaoException If the operation fails.
     */
    @Override
    public PaginatedResults<RegisteredUser> retrieveRegisteredUsers(RegisteredUserCriteria criteria, Pagination pagination)
    throws InvalidDataException, DaoException {
        try {
            FilteredResultIterator<RegisteredUser> iterator = buildRetrieveIterator(criteria, pagination);
            return PaginationUtils.createPaginatedResults(pagination, iterator);
        } catch (InvalidDataException e) {
            throw e;
        } catch (DaoException e) {
            throw e;
        } catch (Exception e) {
            throw new DaoException("Error retrieving users.", e);
        }
    }

    /** Build a query to retrieve matching rows, and return the resulting iterator. */
    private FilteredResultIterator<RegisteredUser> buildRetrieveIterator(RegisteredUserCriteria criteria, Pagination pagination) {
        Query<RegisteredUser> query = getObjectify().query(RegisteredUser.class, pagination);
        setSortField(query, criteria.getSortOrder(), criteria.getSortColumn());
        RegisteredUserFilterPredicate predicate = new RegisteredUserFilterPredicate(criteria);
        return new FilteredResultIterator<RegisteredUser>(query, predicate);
    }

    /** Set the sort field. */
    private static void setSortField(Query<RegisteredUser> query, SortOrder sortOrder, RegisteredUserSortColumn sortColumn) {
        switch(sortColumn) {
        case USER_ID:
            setSort(query, sortOrder, "userId");
            break;
        case USER_NAME:
            setSort(query, sortOrder, "userName");
            break;
        case REGISTRATION_DATE:
            setSort(query, sortOrder, "registrationDate");
            break;
        case OPEN_ID_PROVIDER:
            setSort(query, sortOrder, "openIdProvider");
            break;
        case LOGINS:
            setSort(query, sortOrder, "logins");
            break;
        case LAST_LOGIN:
            setSort(query, sortOrder, "lastLogin");
            break;
        case ADMIN:
            setSort(query, sortOrder, "admin");
            break;
        case LOCKED:
            setSort(query, sortOrder, "locked");
            break;
        }
    }

}
