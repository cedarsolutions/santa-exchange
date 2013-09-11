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
package com.cedarsolutions.santa.client.internal.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.module.view.IModuleTabView;
import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;

/**
 * Edit participant tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IEditParticipantTabView extends IModuleTabView, IViewWithValidation {

    /**
     * Set the edit state.
     * @param participant   Participant to be edited
     * @param giftGiver     Giver that is assigned to this participant
     * @param giftReceiver  Receiver that is assigned to this participant
     * @param participants  List of all participants in this exchange
     */
    void setEditState(Participant participant, Participant giftGiver, Participant giftReceiver, ParticipantSet participants);

    /** Get the exchange that is being edited, in its current state. */
    Participant getEditState();

    /** Show a validation error. */
    void showValidationError(InvalidDataException error);

    /** Set the save handler. */
    void setSaveHandler(ViewEventHandler saveHandler);

    /** Get the save handler. */
    ViewEventHandler getSaveHandler();

    /** Set the cancel handler. */
    void setCancelHandler(ViewEventHandler cancelHandler);

    /** Get the cancel handler. */
    ViewEventHandler getCancelHandler();

}
