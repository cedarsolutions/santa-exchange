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
package com.cedarsolutions.santa.client.internal.presenter;

import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.google.inject.Singleton;

/**
 * Edit manager for exchanges, to hold edit state.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Singleton
public class ExchangeEditManager {

    /** The exchange that we'll fall back to on "undo". */
    private Exchange undoState;

    /** The current state of the exchange, which may not have been saved yet. */
    private Exchange editState;

    /** Default constructor. */
    public ExchangeEditManager() {
        this.undoState = null;
        this.editState = null;
    }

    /** Initialize the edit manager in terms of an exchange. */
    public void initialize(Exchange exchange) {
        this.undoState = new Exchange(exchange);
        this.editState = new Exchange(exchange);
    }

    /** Clear the edit manager. */
    public void clear() {
        this.undoState = null;
        this.editState = null;
    }

    /** Undo any edits. */
    public void undo() {
        if (this.isActive()) {
            this.editState.refresh(this.undoState);
        }
    }

    /** Whether an exchange is actively being edited. */
    public boolean isActive() {
        return this.undoState != null && this.editState != null;
    }

    /** Whether there are any changes in the edit state vs. the undo state. */
    public boolean hasChanges() {
        if (this.isActive()) {
            return !this.undoState.equals(this.editState);
        } else {
            return false;
        }
    }

    /** Get the current undo state. */
    public Exchange getUndoState() {
        return this.undoState;
    }

    /** Get the current edit state. */
    public Exchange getEditState() {
        return this.editState;
    }

}
