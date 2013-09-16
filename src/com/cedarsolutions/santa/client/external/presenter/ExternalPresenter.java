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
package com.cedarsolutions.santa.client.external.presenter;

import com.cedarsolutions.client.gwt.module.presenter.ModuleTemplatePresenter;
import com.cedarsolutions.santa.client.external.ExternalEventBus;
import com.cedarsolutions.santa.client.external.view.ExternalView;
import com.cedarsolutions.santa.client.external.view.IExternalView;
import com.mvp4g.client.annotation.Presenter;

/**
 * Presenter for this module's template view.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
@Presenter(view = ExternalView.class)
public class ExternalPresenter extends ModuleTemplatePresenter<IExternalView, ExternalEventBus> {
}
