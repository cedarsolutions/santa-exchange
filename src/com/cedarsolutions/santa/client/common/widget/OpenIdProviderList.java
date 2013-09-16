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

import com.cedarsolutions.shared.domain.OpenIdProvider;
import com.google.gwt.core.client.GWT;

/**
 * List box containing available OpenId providers.
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
public class OpenIdProviderList extends DropdownList<OpenIdProvider> {

    /** Default selection. */
    public static final OpenIdProvider DEFAULT_SELECTION = OpenIdProvider.GOOGLE;

    /** Create the list. */
    public OpenIdProviderList() {
        this(false);
    }

    /** Create the list, optionally adding the "any" choice. */
    public OpenIdProviderList(boolean addAny) {
        if (addAny) {
            this.addDropdownItemAny();
        }

        this.addDropdownItem(OpenIdProvider.AOL);
        this.addDropdownItem(OpenIdProvider.GOOGLE);
        this.addDropdownItem(OpenIdProvider.MYOPENID);
        this.addDropdownItem(OpenIdProvider.MYSPACE);
        this.addDropdownItem(OpenIdProvider.YAHOO);

        this.setVisibleItemCount(1);

        if (addAny) {
            this.setSelectedValue(null);
        } else {
            this.setSelectedValue(DEFAULT_SELECTION);
        }
    }

    /** Get the display value for an item. */
    @Override
    protected String getDisplay(OpenIdProvider value) {
        return getProviderName(value);
    }

    /** Get a user-visible string representing an OpenId provider. */
    public static String getProviderName(OpenIdProvider provider) {
        if (provider == null) {
            // this shouldn't happen, because a null provider is the "any" option, handled elsewhere
            return "null";
        } else {
            WidgetConstants constants = GWT.create(WidgetConstants.class);
            switch(provider) {
            case AOL:
                return constants.openId_AOL();
            case GOOGLE:
                return constants.openId_Google();
            case MYOPENID:
                return constants.openId_myOpenId();
            case MYSPACE:
                return constants.openId_MySpace();
            case YAHOO:
                return constants.openId_Yahoo();
            default:
                return provider.toString();  // don't bother localizing it, but provide something legible
            }
        }
    }
}
