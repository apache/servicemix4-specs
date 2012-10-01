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
 * The InstallerMBean defines standard installation and uninstallation controls
 * for components. InstallerMBeans are created by the
 * {@link InstallationServiceMBean}. The InstallerMBean offers controls to
 * allow an administrative tool to:
 * <ul>
 *   <li>Install the component from the installation package.</li>
 *   <li>Uninstall the component.</li>
 *   <li>Check the installation status of the component.</li>
 *   <li>Get the file path to the component's installation root directory.</li>
 * </ul>
 *
 * @author JSR208 Expert Group
 */
public interface InstallerMBean {

    /**
     * Get the installation root directory path for this component.
     *
     * @return the full installation path of this component; this must be in
     *         absolute path name form, in platform-specific format; must be
     *         non-null and non-empty
     */
    String getInstallRoot();

    /**
     * Install a component.
     * <p>
     * Note that the implementation must leave the component in its
     * installed, shutdown state. Automatic starting of components during
     * installation by implementations is not allowed.
     *
     * @return JMX ObjectName representing the LifeCycleMBean for the installed
     *         component, or <code>null</code> if the installation did not
     *         complete
     * @exception javax.jbi.JBIException if the installation fails
     */
    ObjectName install() throws javax.jbi.JBIException;

    /**
     * Determine whether or not the component is installed.
     *
     * @return <code>true</code> if this component is currently installed,
     *         otherwise <code>false</code>
     */
    boolean isInstalled();

    /**
     * Uninstall the component. This completely removes the component from the
     * JBI system.
     *
     * @exception javax.jbi.JBIException if the uninstallation fails
     */
    void uninstall() throws javax.jbi.JBIException;

    /**
     * Get the installer configuration MBean name for this component.
     *
     * @return the MBean object name of the Installer Configuration MBean;
     *         <code>null</code> if none is provided by this component
     * @exception javax.jbi.JBIException if the component is not in the
     *            appropriate state (after install() but before life cycle
     *            initialization), or if any error occurs during processing
     */
    ObjectName getInstallerConfigurationMBean() throws javax.jbi.JBIException;

}
