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
package com.cedarsolutions.santa.server.domain.notification;

import com.cedarsolutions.shared.domain.StringEnum;

/**
 * Legal notification types.
 * The value of the enumeration is the Velocity template name.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public enum NotificationType implements StringEnum {

    REGISTER("register");          // A user was registered due to their first login

    private String value;

    NotificationType(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return this.value;
    }

}
