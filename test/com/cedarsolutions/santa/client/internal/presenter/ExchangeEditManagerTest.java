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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cedarsolutions.santa.shared.domain.exchange.Exchange;

/**
 * Unit tests for ExchangeEditManager.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class ExchangeEditManagerTest {

    /** Test the constructor. */
    @Test public void testConstructor() {
        ExchangeEditManager manager = new ExchangeEditManager();
        assertNull(manager.getUndoState());
        assertNull(manager.getEditState());
        assertFalse(manager.isActive());
        assertFalse(manager.hasChanges());
    }

    /** Test initialize() and clear. */
    @Test public void testInitializeClear() {
        ExchangeEditManager manager = new ExchangeEditManager();

        Exchange exchange = new Exchange();
        exchange.setId(2L);

        manager.initialize(exchange);
        assertEquals(exchange, manager.getUndoState());
        assertNotSame(exchange, manager.getUndoState());
        assertEquals(exchange, manager.getEditState());
        assertNotSame(exchange, manager.getEditState());
        assertTrue(manager.isActive());
        assertFalse(manager.hasChanges());

        manager.clear();
        assertNull(manager.getUndoState());
        assertNull(manager.getEditState());
        assertFalse(manager.isActive());
        assertFalse(manager.hasChanges());
    }

    /** Test undo() and hasChanges(). */
    @Test public void testUndo() {
        ExchangeEditManager manager = new ExchangeEditManager();

        Exchange exchange = new Exchange();
        exchange.setId(2L);

        manager.initialize(exchange);

        manager.getEditState().setDateAndTime("whenever");
        assertFalse(manager.getUndoState().equals(manager.getEditState()));
        assertTrue(manager.hasChanges());

        manager.undo();
        assertTrue(manager.getUndoState().equals(manager.getEditState()));
        assertFalse(manager.hasChanges());
    }

}
