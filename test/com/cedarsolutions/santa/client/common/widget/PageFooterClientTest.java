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
package com.cedarsolutions.santa.client.common.widget;

import com.cedarsolutions.santa.client.SantaExchangeConfig;
import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.google.gwt.core.client.GWT;

/**
 * Client-side unit tests for PageFooter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class PageFooterClientTest extends ClientTestCase {

    /** Test the constructor. */
    public void testConstructor() {
        SantaExchangeConfig config = GWT.create(SantaExchangeConfig.class);

        PageFooter footer = new PageFooter();
        assertNotNull(footer);

        assertEquals(config.system_copyrightStatement(), footer.copyrightStatement.getText());
        assertEquals(WidgetUtils.getInstance().getApplicationVersion(), footer.applicationVersion.getText());
    }

}
