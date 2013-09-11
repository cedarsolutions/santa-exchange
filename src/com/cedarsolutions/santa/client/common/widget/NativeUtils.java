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
package com.cedarsolutions.santa.client.common.widget;

/**
 * Utility methods implemented in native Javascript.
 *
 * <p>
 * Generally speaking, methods in here are intended to be used only
 * by other utility classes, i.e. WidgetUtils.  Calling these directly
 * in your code makes it difficult to unit test, because they can't
 * really be mocked.  That's why they're all kept together here in
 * this utility class.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class NativeUtils {

    /** Get $wnd.location.href from the browser's DOM. */
    public static native String getWndLocationHref() /*-{
        return $wnd.location.href;
    }-*/;

    /** Redirect the current page to an external URL. */
    public static native void redirect(String url) /*-{
        $wnd.location = url;
    }-*/;

}
