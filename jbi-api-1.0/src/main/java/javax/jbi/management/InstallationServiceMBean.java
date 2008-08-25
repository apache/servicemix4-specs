/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.jbi.management;

import javax.management.ObjectName;

/**
 * The installation service MBean allows administrative tools to manage
 * component and shared library installations.  The tasks supported are:
 * <ul>
 *   <li>Installing (and uninstalling) a shared library</li>
 *   <li>Creating (loading) and destroying (unloading) a component installer
 *       MBean.</li>
 *   <li>Finding an existing component installer MBean.
 * </ul>
 *
 * Installing and uninstalling components is accomplished using
 * {@link InstallerMBean}s, loaded by this MBean.  An individual installer MBean
 * is needed for each component installation / uninstallation.  This is to support
 * the more complex installation process that some components require.
 *
 * @author JSR208 Expert Group
 */
public interface InstallationServiceMBean {

    /**
     * Load the installer for a new component for the given component
     * installation package.
     *
     * @param installZipURL URL locating a ZIP file containing the
     *        JBI Installation package to be installed; must be non-null,
     *        non-empty, and a legal URL
     * @return the JMX ObjectName of the InstallerMBean loaded from
     *         installZipURL; must be non-null
     */
    ObjectName loadNewInstaller(String installZipURL);

    /**
     * Load the InstallerMBean for a previously installed component.
     * <p>
     * The "component name" refers to the
     * <code>&lt;identification>&lt;name></code> element value from the
     * component's installation package (see {@link #loadNewInstaller(String)}).
     *
     * @param aComponentName the component name identifying the installer to 
     *        load; must be non-null and non-empty
     * @return the JMX ObjectName of the InstallerMBean loaded from an existing
     *         installation context; <code>null</code> if the installer MBean
     *         doesn't exist
     */
    ObjectName loadInstaller(String aComponentName);

    /**
     * Unload an InstallerMBean previously loaded for a component.
     *
     * @param aComponentName the component name identifying the installer to 
     *        unload; must be non-null and non-empty
     * @param isToBeDeleted <code>true</code> if the component is to be deleted
     *        as well
     * @return true if the operation was successful, otherwise false
     */
    boolean unloadInstaller(String aComponentName, boolean isToBeDeleted);

    /**
     * Install a shared library installation package.
     * <p>
     * The return value is the unique name for the shared-library, as found
     * in the the value of the installation descriptor's
     * <code>&lt;identification>&lt;name></code> element.
     *
     * @param aSharedLibURI URL locating a zip file containing a shared library
     *        installation package; must be non-null, non-empty, and a legal
     *        URL
     * @return the unique name of the shared library loaded from slZipURL; must
     *         be non-null and non-empty
     */
    String installSharedLibrary(String aSharedLibURI);

    /**
     * Uninstall a previously installed shared library.
     *
     * @param aSharedLibName the name of the shared name space to uninstall; must be
     *        non-null and non-empty
     * @return true if the uninstall was successful
     */
    boolean uninstallSharedLibrary(String aSharedLibName);
}
