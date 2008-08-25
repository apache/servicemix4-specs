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
package javax.jbi.component;

import javax.jbi.JBIException;

import javax.management.ObjectName;

/**
 * This interface is implemented by a JBI Component to provide any
 * special processing required at install/uninstall time. The methods
 * defined here are called by the JBI implementation during the installation
 * (or uninstallation) of the component that, among other things, supplies
 * an implementation of this interface.
 *
 * Initialization/cleanup tasks such as creation/deletion of directories,
 * files, and database tables can be done by the onInstall() and onUninstall()
 * methods, respectively. This also allows the component to terminate the
 * installation or uninstallation in the event of an error.
 *
 * After calling onInstall() or onUninstall(), regardless of outcome, the JBI
 * implementation must call the cleanUp() method afterwards. Similarly, if
 * init(InstallationContext) fails with an exception, the JBI implementation
 * must call the cleanUp() method.
 *
 * Component implementors should note that there is no guarantee that the same
 * instance of its Bootstrap implementation will be used during both install
 * and uninstall operations on the component. Data that need to be retained
 * between installation-time and uninstallation-time must be persisted in such
 * as fashion that a separate instance of the bootstrap class can find them,
 * despite component or system shutdown.
 *
 * @author JSR208 Exert Group
 */
public interface Bootstrap {

    /**
     * Initializes the installation environment for a component. This method is
     * expected to save any information from the installation context that may
     * be needed by other methods.
     *
     * If the component needs to register an optional installer configuration MBean,
     * it MUST do so during execution of this method, or the getExtensionMBean()
     * method.
     *
     * This method must be called after the installation root (available through
     * the installContext parameter) is prepared. 

     * @param installContext the context containing information from the install
     *                       command and from the component installation ZIP file;
     *                       this must be non-null.
     * @throws JBIException  when there is an error requiring that the installation
     *                       be terminated
     */
    void init(InstallationContext installContext) throws JBIException;

    /**
     * Cleans up any resources allocated by the bootstrap implementation,
     * including performing deregistration of the extension MBean, if applicable.
     *
     * This method must be called after the onInstall() or onUninstall() method
     * is called, whether it succeeds or fails. It must be called after init() is
     * called, if init() fails by throwing an exception.
     * 
     * @throws JBIException if the bootstrap cannot clean up allocated resources
     */
    void cleanUp() throws JBIException;

    /**
     * Obtains the ObjectName of the optional installer configuration MBean. If
     * none is provided by this component, this method must return null.
     *
     * This method must be called before onInstall() (or onUninstall()) is called
     * by the JBI implementation.
     *
     * @return ObjectName of the optional installer configuration MBean; returns null
     *         if there is no such MBean
     */
    ObjectName getExtensionMBeanName();

    /**
     * Called at the beginning of installation of a component to perform any special
     * installation tasks required by the component.
     *
     * This method must not be called if the init() method failed with an exception.
     *  
     * @throws JBIException when there is an error requiring that the installation be
     *         terminated
     */
    void onInstall() throws JBIException;

    /**
     * Called at the beginning of uninstallation of a component to perform any special
     * uninstallation tasks required by the component.
     *
     * This method must not be called if the init() method failed with an exception.
     *
     * @throws JBIException when there is an error requiring that the uninstallation be
     *         terminated.
     */
    void onUninstall() throws JBIException;
}
