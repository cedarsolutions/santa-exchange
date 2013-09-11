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
package com.cedarsolutions.santa.client.admin.view;

import java.util.List;

import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.IModuleTabView;
import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUser;
import com.cedarsolutions.santa.shared.domain.user.RegisteredUserCriteria;


/**
 * Tab that contains user maintenance functionality.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IUserTabView extends IModuleTabView, IViewWithValidation, IBackendDataRenderer<RegisteredUser, RegisteredUserCriteria> {

    /** Set the history token. */
    void setHistoryToken(String historyToken);

    /** Show the search criteria. */
    void showSearchCriteria();

    /** Hide the search criteria. */
    void hideSearchCriteria();

    /** Get the delete event handler. */
    ViewEventHandler getDeleteEventHandler();

    /** Set the delete event handler. */
    void setDeleteEventHandler(ViewEventHandler deleteEventHandler);

    /** Get the lock event handler. */
    ViewEventHandler getLockEventHandler();

    /** Set the lock event handler. */
    void setLockEventHandler(ViewEventHandler lockEventHandler);

    /** Get the unlock event handler. */
    ViewEventHandler getUnlockEventHandler();

    /** Set the unlock event handler. */
    void setUnlockEventHandler(ViewEventHandler unlockEventHandler);

    /** Get the selected records. */
    List<RegisteredUser> getSelectedRecords();

}
