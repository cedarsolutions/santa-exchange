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
package com.cedarsolutions.santa.client.rpc.util;


/**
 * Manages system-wide state for RPCs.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class CallerIdManager {

    /** Maximum index, no more than 6 hex digits. */
    private static final int MAX_INDEX = 0xFFFFFF;

    /** Current index. */
    protected int index;

    /** Get the next caller id. */
    // Note that the GWT compiler will ignore synchronized, so this is just informational
    public synchronized String getNextCallerId() {
        this.index += 1;

        if (this.index > MAX_INDEX) {
            this.index = 1;
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("rpc");
        buffer.append(Integer.toHexString(this.index));

        // we're aiming for a caller id with a max length of 9, like "rpc000001".
        for (int i = buffer.length(); i < 9; i++) {
            buffer.insert(3, "0");  // after "rpc"
        }

        return buffer.toString();
    }

}
