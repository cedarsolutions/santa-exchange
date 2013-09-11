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

/**
 * Implementations for client-visible GWT RPC services.
 *
 * <p>
 * RPCs are client-visible services.  <i>Only</i> functionality that is useful to
 * clients should be exposed in the RPC interface.
 * </p>
 *
 * <p>
 * RPCs are usually implemented as thin wrappers over other back-end DAO or
 * service implementations.  In some cases, an RPC might directly proxy to the
 * back-end implementation.  In other cases, the RPC's purpose is process
 * orchestration.  RPCs should generally not inject other RPCs.  Back-end
 * functionality that needs to be shared should be in the service layer.
 * </p>
 *
 * <p>
 * Either entire RPC interfaces or specific RPC method calls can be protected
 * using Spring security annotations.  Interfaces or methods annotated with
 * \@Secured("ROLE_USER") can only be invoked by logged-in users.  Interfaces or
 * methods  annotated as \@Secured("ROLE_ADMIN") can only be invoked by logged-in
 * adminstrators.  Other methods or interfaces can be invoked without an
 * existing session (i.e. by anyone in the world), so be careful.  Most
 * RPCs should be limited to either ROLE_USER or ROLE_ADMIN.
 * </p>
 *
 * <p>
 * Some methods may interact with data owned by a specific user.  For security
 * purposes, it is very important that these methods rely on the session to
 * identify the user, rather than accepting credentials or other user identifiers
 * as method arguments.  This way, one logged in user will not be able to spoof
 * another logged in user.  Users will only be able to get at their own data.
 * </p>
 *
 * <p>
 * Classes that implement GWT RPC interfaces are mapped to GWT RPC calls
 * via Spring configuration using <a href="http://code.google.com/p/gwt-sl">GWT-SL</a>.
 * For example, <code>rpc-servlet.xml</code> might include something like this:
 * </p>
 *
 * <pre>
 *     &lt;bean class="org.gwtwidgets.server.spring.GWTHandler"&gt;
 *      &lt;property name="serviceExporterFactory" ref="securedServiceExporterFactory" /&gt;
 *      &lt;property name="mappings"&gt;
 *          &lt;map&gt;
 *              &lt;entry key="/santaexchange/rpc/gaeUserRpc.rpc" value-ref="gaeUserRpc" /&gt;
 *          &lt;/map&gt;
 *      &lt;/property&gt;
 *  &lt;/bean&gt;
 *
 *  &lt;bean id="securedServiceExporterFactory" class="com.cedarsolutions.wiring.gwt.rpc.SecuredServiceExporterFactory" /&gt;
 * </pre>
 *
 * <p>
 * Note that the key above (<code>/santaexchange/rpc/gaeUserRpc.rpc</code>)
 * has a relationship with the RemoteServiceRelativePath annotation on the service
 * interface, but it is not identical. In this case, the annotation will be
 * <code>rpc/gaeUserRpc.rpc</code>, but the Spring configuration must
 * include the application name <code>/santaexchange</code>.
 * </p>
 *
 * @author Kenneth J. Pronovici <pronovic@ieee.org>
 */
package com.cedarsolutions.santa.server.rpc.impl;
