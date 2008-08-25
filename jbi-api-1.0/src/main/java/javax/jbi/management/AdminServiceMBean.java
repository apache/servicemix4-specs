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
 * This interface defines a set of administrative methods allowing a JMX-based
 * administrative tool to perform a variety of administrative tasks.
 * <ul>
 *   <li>Find component lifecycle MBean names for:
 *     <ul>
 *       <li>Individual components, by name</li>
 *       <li>All binding components</li>
 *       <li>All service engines</li>
 *     </ul>
 *   </li>
 *   <li>Find lifecyle MBeans names for implementation-defined services:
 *     <ul>
 *       <li>Individual implementation services, by name</li>
 *       <li>All implementation services</li>
 *     </ul>
 *   </li>
 *   <li>Query whether an individual component is a binding or engine</li>
 *   <li>Query implementation information (version etc.)</li>
 * </ul>
 *
 * @author JSR208 Expert Group
 */
public interface AdminServiceMBean {

    /**
     * Get a list of {@link ComponentLifeCycleMBean}s for all binding components
     * currently installed in the JBI system.
     *
     * @return array of JMX object names of component life cycle MBeans for all
     *         installed binding components; must be non-null; may be empty
     */
    ObjectName[] getBindingComponents();

    /**
     * Find the {@link ComponentLifeCycleMBean} of a JBI Installable Component
     * by its unique name.
     *
     * @param name the name of the engine or binding component; must be non-
     *        null and non-empty
     * @return the JMX object name of the component's life cycle MBean, or
     *         <code>null</code> if there is no such component with the given
     *         <code>name</code>
     */
    ObjectName getComponentByName(String name);

    /**
     * Get a list of {@link ComponentLifeCycleMBean}s for all service engines
     * currently installed in the JBI system.
     *
     * @return array of JMX object names of component life cycle MBeans for all
     *         installed service engines; must be non-null; may be empty
     */
    ObjectName[] getEngineComponents();

    /**
     * Return current version and other info about this JBI implementation. The
     * contents of the returned string are implementation dependent.
     *
     * @return information string about the JBI implementation, including
     *         version information; must be non-null and non-empty
     */
    String getSystemInfo();

    /**
     * Lookup a system service {@link LifeCycleMBean} by name. System services
     * are implementation-defined services which can administered through JMX,
     * and have a life cycle.
     * <p>
     * System services are not related to service engines.
     *
     * @param serviceName name of the system service; must be non-null and non-
     *        empty; values are implementation-dependent
     * @return JMX object name of the system service's LifeCycleMBean, or
     *         <code>null</code> if there is no system service with the given
     *         <code>name</code>.
     */
    ObjectName getSystemService(String serviceName);

    /**
     * Looks up all JBI system services {@link LifeCycleMBean}'s currently
     * installed. System services are implementation-defined services which can
     * administered through JMX. System services are not related to service
     * engines.
     *
     * @return array of LifecycleMBean JMX object names of system services
     *         currently installed in the JBI implementation; must be non-null;
     *         may be empty
     */
    ObjectName[] getSystemServices();

    /**
     * Check if a given JBI component is a Binding Component.
     *
     * @param componentName the unique name of the component; must be non-null
     *        and non-empty
     * @return <code>true</code> if the component is a binding component;
     *         <code>false</code> if the component is a service engine or if
     *         there is no component with the given <code>componentName</code>
     *         installed in the JBI system
     */
    boolean isBinding(String componentName);

    /**
     * Check if a given JBI component is a Service Engine.
     *
     * @param componentName the unique name of the component; must be non-null
     *        and non-empty
     * @return <code>true</code> if the component is a service engine;
     *         <code>false</code> if the component is a binding component, or if
     *         there is no component with the given <code>componentName</code>
     *         installed in the JBI system
     */
    boolean isEngine(String componentName);

}
