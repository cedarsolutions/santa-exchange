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
package com.cedarsolutions.santa.shared.domain;

/**
 * List of valid message keys to be used in validation errors.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class MessageKeys {

    public static final String NULL = "null";               // The object being validated is null
    public static final String INVALID = "invalid";         // An object or field is invalid
    public static final String REQUIRED = "required";       // A required field is missing
    public static final String FAILURE = "failure";         // A failure occurred due to invalid data

}
