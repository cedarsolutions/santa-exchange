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

/**
 * Widgets that may be used by any module.
 *
 * <p>
 * In the context of this code, a widget is some sort of user interface
 * component that does not require a presenter.  Because widgets do
 * not have a presenter, they can be included directly in .ui.xml files
 * and no other configuration is needed (i.e. they do not need to be
 * injected into the view constructor).  User interface components that
 * require a presenter live in the <code>views</code> package for each
 * module.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
package com.cedarsolutions.santa.client.common.widget;
