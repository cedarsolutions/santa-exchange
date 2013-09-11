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
package com.cedarsolutions.santa.client.root.presenter;

import com.cedarsolutions.santa.client.junit.ClientTestCase;
import com.cedarsolutions.santa.client.root.view.RootView;
import com.cedarsolutions.shared.domain.ErrorDescription;

/**
 * Client-side unit tests for RootPresenter.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class RootPresenterClientTest extends ClientTestCase {

    /** Test onReplaceRootBody(). */
    public void testOnReplaceRootBody() {
        RootPresenter presenter = new RootPresenter();
        presenter.onReplaceRootBody(new RootView()); // just make sure it doesn't blow up
    }

    /** Test onShowAboutPopup(). */
    public void testShowAboutPopup() {
        RootPresenter presenter = new RootPresenter();
        presenter.onShowAboutPopup();
        // just make sure it doesn't blow up
    }

    /** Test onShowWelcomePopup(). */
    public void testShowWelcomePopup() {
        RootPresenter presenter = new RootPresenter();
        presenter.onShowWelcomePopup();
        // just make sure it doesn't blow up
    }

    /** Test onShowErrorPopup(). */
    public void testOnShowErrorPopup() {
        // We can't really verify much; just make sure that the method call doesn't blow up
        ErrorDescription error = new ErrorDescription("Hello, world");
        RootPresenter presenter = new RootPresenter();
        presenter.onShowErrorPopup(error);
    }

    /** Test onShowBookmarkNotFoundError(). */
    public void testOnShowBookmarkNotFoundError() {
        // We can't really verify much; just make sure that the method call doesn't blow up
        RootPresenter presenter = new RootPresenter();
        presenter.onShowBookmarkNotFoundError();
    }

}
