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

/**
 * The deployment service MBean allows administrative tools to manage
 * service assembly deployments. The tasks supported are:
 * <ul>
 *   <li>Deploying a service assembly.</li>
 *   <li>Undeploying a previously deployed service assembly.</li>
 *   <li>Querying deployed service assemblies:
 *     <ul>
 *       <li>For all components in the system.</li>
 *       <li>For a particular component.</li>
 *     </ul>
 *   </li>
 *   <li>Control the state of deployed service assemblies:
 *     <ul>
 *       <li>Start the service units that contained in the SA.</li>
 *       <li>Stop the service units that contained in the SA. </li>
 *       <li>Shut down the service units that contained in the SA.</li>
 *     </ul>
 *   </li>
 *   <li>Query the service units deployed to a particular component.</li>
 *   <li>Check if a service unit is deployed to a particular component.</li>
 *   <li>Query the deployment descriptor for a particular service assembly.</li>
 * </ul>
 *
 * @author JSR208 Expert Group
 */
public interface DeploymentServiceMBean {

    /**
     * The service assembly is started. This means that the assembly's offered
     * services can accept message exchanges, and it can send exchanges to
     * consume services.
     */
    String STARTED = "Started";

    /**
     * The service assembly has been deployed, or shutdown
     */
     String SHUTDOWN = "Shutdown";

    /**
     * The service assembly is stopped. This means that the assembly's offered
     * services can accept message exchanges, but it will not send any.
     */
    String STOPPED = "Stopped";

    /**
     * Deploys the given Service Assembly to the JBI environment.
     * <p>
     * Note that the implementation must not automatically start the service
     * assembly after deployment; it must wait for the {@link #start(String)}
     * method to be invoked by the administrative tool.
     *
     * @param saZipURL String containing the location URL of the
     *        Service Assembly ZIP file; must be non-null, non-empty, and a
     *        legal URL
     * @return Result/Status of the current deployment; must conform to
     *         JBI management result/status XML schema; must be non-null and
     *         non-empty
     * @exception Exception if complete deployment fails
     */
    String deploy(String saZipURL) throws Exception;

    /**
     * Undeploys the given Service Assembly from the JBI environment.
     *
     * @param saName name of the Service Assembly that is to be 
     *        undeployed; must be non-null and non-empty
     * @return Result/Status of the current undeployment; must conform to
     *         JBI management result/status XML schema; must be non-null and
     *         non-empty
     * @exception Exception if compelete undeployment fails
     */
    String undeploy(String saName) throws Exception;

    /**
     * Returns an array of service unit names that are currently deployed to
     * the named component.
     *
     * @param componentName the name of the component to query; must be
     *        non-null and non-empty
     * @return array of service unit names deployed in the named component;
     *         must be non-null; may be empty
     * @exception Exception if a processing error occurs
     */
    String[] getDeployedServiceUnitList(String componentName) throws Exception;

    /**
     * Returns a list of Service Assemblies deployed to the JBI environment.
     *
     * @return list of Service Assembly names; must be non-null; may be
     *         empty
     * @exception Exception if a processing error occurs
     */
    String[] getDeployedServiceAssemblies() throws Exception;

    /**
     * Returns the deployment descriptor of the Service Assembly that was
     * deployed to the JBI enviroment, serialized to a <code>String</code>.
     *
     * @param saName name of the service assembly to be queried;
     *        must be non-null and non-empty
     * @return descriptor of the Assembly Unit; must be non-null
     * @exception Exception if a processing error occurs
     */
    String getServiceAssemblyDescriptor(String saName) throws Exception;

    /**
     * Returns an array of Service Assembly names, where each assembly contains
     * Service Units for the given component.
     *
     * @param componentName name of the component to query; must be non-null
     *        and non-empty
     * @return array of of Service Assembly names, where each assembly contains
     *         a Service Unit for the named component; must be non-null; may
     *         be empty
     * @exception Exception if a processing error occurs
     */
    String[] getDeployedServiceAssembliesForComponent(String componentName) throws Exception;

    /**
     * Returns an array of component names, where for each the given assembly
     * contains a service unit for the component.
     *
     * @param saName the service assembly to be queried; must be
     *        non-null and non-empty
     * @return array of component names, where for each name the given assembly
     *         contains a service unit from the given service assembly; must
     *         be non-null; may be empty
     * @exception Exception if a processing error occurs
     */
    String[] getComponentsForDeployedServiceAssembly(String saName) throws Exception;

    /**
     * Queries if the named Service Unit is currently deployed to the named
     * component.
     *
     * @param componentName name of the component to query; must be non-null
     *        and non-empty
     * @param suName name of the subject service unit; must be non-null
     *        and non-empty
     * @return <code>true</code> if the named service unit is currently deployed
     *         to the named component
     */
    boolean isDeployedServiceUnit(String componentName, String suName) throws Exception;

    /**
     * Returns <code>true</code> if the the given component accepts the
     * deployment of service units. This is used by admin tools to
     * determine which components can be named in service assembly
     * deployment descriptors.
     *
     * @param componentName name of the component; must be non-null and
     *        non-empty
     * @return <code>true</code> if the named component accepts deployments;
     *         <code>false</code> if the named component does not accept
     *         deployments or it does not exist
     *
     */
    boolean canDeployToComponent(String componentName);

    /**
     * Start the service assembly. This puts the assembly into the {@link
     * #STARTED} state.
     *
     * @param serviceAssemblyName name of the assembly to be started; must be
     *        non-null and non-empty
     * @return result / status string giving the results of starting (and
     *         possibly initializing) each service unit in the assembly; must
     *         be non-null and non-empty
     * @exception Exception if there is no such assembly
     * @exception Exception if the assembly fails to start
     *
     */
    String start(String serviceAssemblyName) throws Exception;

    /**
     * Stop the service assembly. This puts the assembly into the {@link
     * #STOPPED} state.
     *
     * @param serviceAssemblyName name of the assembly to be stopped; must be
     *        non-null and non-empty
     * @return result / status string giving the results of stopping each
     *         service unit in the assembly; must be non-null and non-empty
     * @exception Exception if there is no such assembly
     * @exception Exception if the assembly fails to stop
     */
    String stop(String serviceAssemblyName) throws Exception;

    /**
     * Shut down the service assembly. This puts the assembly back into the
     * {@link #SHUTDOWN} state.
     *
     * @param serviceAssemblyName name of the assembly to be shut down; must be
     *        non-null and non-empty
     * @return result / status string giving the results of shutting down
     *         each service unit in the assembly; must be non-null and non-empty
     * @exception Exception if there is no such assembly
     * @exception Exception if the assembly fails to shut down
     */
    String shutDown(String serviceAssemblyName) throws Exception;

    /**
     * Get the running state of a service assembly. The possible result values
     * of this query are enumerated by the following set of constants:
     * {@link #SHUTDOWN}, {@link #STOPPED}, {@link #STARTED}.
     *
     * @param serviceAssemblyName name of the assembly to query; must be
     *        non-null and non-empty
     * @return the state of the service assembly, as a string value; must be one
     *         of the enumerated string values provided by this interface:
     *         {@link #SHUTDOWN}, {@link #STOPPED}, or {@link #STARTED}
     * @exception Exception if there is no such assembly
     */
    String getState(String serviceAssemblyName) throws Exception;
}
