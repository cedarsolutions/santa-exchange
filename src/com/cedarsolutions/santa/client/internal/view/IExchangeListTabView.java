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
package com.cedarsolutions.santa.client.internal.view;

import java.util.List;

import com.cedarsolutions.client.gwt.datasource.IBackendDataRenderer;
import com.cedarsolutions.client.gwt.event.ViewEventHandler;
import com.cedarsolutions.client.gwt.event.ViewEventHandlerWithContext;
import com.cedarsolutions.client.gwt.module.view.IModuleTabView;
import com.cedarsolutions.santa.shared.domain.exchange.Exchange;
import com.cedarsolutions.santa.shared.domain.exchange.ExchangeCriteria;

/**
 * Exchange list tab.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public interface IExchangeListTabView extends IModuleTabView, IBackendDataRenderer<Exchange, ExchangeCriteria> {

    /** Get the delete handler. */
    ViewEventHandler getDeleteHandler();

    /** Set the delete handler. */
    void setDeleteHandler(ViewEventHandler deleteHandler);

    /** Get the create handler. */
    ViewEventHandler getCreateHandler();

    /** Set the create handler. */
    void setCreateHandler(ViewEventHandler createHandler);

    /** Get the edit selected row handler. */
    ViewEventHandlerWithContext<Exchange> getEditSelectedRowHandler();

    /** Set the edit selected row handler. */
    void setEditSelectedRowHandler(ViewEventHandlerWithContext<Exchange> editSelectedRowHandler);

    /** Get the selected records. */
    List<Exchange> getSelectedRecords();

}
