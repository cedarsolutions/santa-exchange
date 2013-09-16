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
package com.cedarsolutions.santa.client.internal.view;

import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.module.view.IModuleTabView;
import com.cedarsolutions.client.gwt.validation.IViewWithValidation;
import com.cedarsolutions.exception.InvalidDataException;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.Participant;
import com.cedarsolutions.santa.shared.domain.exchange.ParticipantSet;
import com.cedarsolutions.shared.domain.email.EmailMessage;

/**
 * Edit exchange tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IEditExchangeTabView extends IModuleTabView, IViewWithValidation {

    /** Set the exchange to be edited. */
    void setEditState(Exchange editState);

    /** Get the exchange that is being edited, in its current state. */
    Exchange getEditState();

    /** Show the "send was successful" popup. */
    void showSendSuccessfulPopup();

    /** Get the selected participants. */
    ParticipantSet getSelectedParticipants();

    /** Show a preview. */
    void showPreview(EmailMessage preview);

    /** Show a validation error. */
    void showValidationError(InvalidDataException error);

    /** Set the save handler. */
    void setSaveHandler(ViewEventHandler saveHandler);

    /** Get the save handler. */
    ViewEventHandler getSaveHandler();

    /** Set the reset handler. */
    void setResetHandler(ViewEventHandler resetEventHandler);

    /** Get the reset handler. */
    ViewEventHandler getResetHandler();

    /** Set the return to list handler. */
    void setReturnToListHandler(ViewEventHandler returnToListHandler);

    /** Get the return to list handler. */
    ViewEventHandler getReturnToListHandler();

    /** Set the add participant handler. */
    void setAddParticipantHandler(ViewEventHandler addParticipantHandler);

    /** Get the add participant handler. */
    ViewEventHandler getAddParticipantHandler();

    /** Set the edit participant handler. */
    void setEditParticipantHandler(ViewEventHandlerWithContext<Participant> editParticipantHandler);

    /** Get the edit participant handler. */
    ViewEventHandlerWithContext<Participant> getEditParticipantHandler();

    /** Set the delete participant handler. */
    void setDeleteParticipantHandler(ViewEventHandler deleteParticipantHandler);

    /** Get the delete participant handler. */
    ViewEventHandler getDeleteParticipantHandler();

    /** Set the send all notifications handler. */
    void setSendAllNotificationsHandler(ViewEventHandler sendAllNotificationsHandler);

    /** Get the send all notifications handler. */
    ViewEventHandler getSendAllNotificationsHandler();

    /** Set the resend notification handler. */
    void setResendNotificationHandler(ViewEventHandler resendNotificationHandler);

    /** Get the resend notification handler. */
    ViewEventHandler getResendNotificationHandler();

    /** Set the preview handler. */
    void setPreviewHandler(ViewEventHandler previewHandler);

    /** Get the preview handler. */
    ViewEventHandler getPreviewHandler();

}
