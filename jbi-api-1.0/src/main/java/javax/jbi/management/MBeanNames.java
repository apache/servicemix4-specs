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
 * This interface provides methods to create JMX object names for component-
 * supplied MBeans. This ensures that component-supplied MBeans follow the
 * JBI implementation-determined naming convention.
 *
 * Components obtain instances of this name creator using {@link
 * javax.jbi.component.ComponentContext#getMBeanNames()}.
 *
 * @author JSR208 Expert Group
 */
public interface MBeanNames {

    /** The custom name that must be used for bootstrap extensions */
    String BOOTSTRAP_EXTENSION = "BootstrapExtension";

    /** The custom name that must be used for component life cycle extensions */
    String COMPONENT_LIFE_CYCLE_EXTENSION = "LifeCycleExtension";

    /**
     * Formulate and return an MBean ObjectName for a custom control
     * of this name creator's JBI component.
     * <p>
     * This is used by components to create JMX names for their own JMX
     * controls, allowing the JBI implementation to prefix the created name
     * to fit within the implementation's own naming scheme.
     * <p>
     * Standard extensions must use the following custom name constants:
     * <ul>
     *   <li>Bootstrap (installer) extension: {@link #BOOTSTRAP_EXTENSION}.</li>
     *   <li>Component life cycle extension:
     *       {@link #COMPONENT_LIFE_CYCLE_EXTENSION}.
     *   </li>
     * </ul>
     * All other custom component MBeans must use custom names that do not
     * collide with the standard extension names.
     *
     * @param customName the name of the custom control; must be non-null and
     *        non-empty; must be legal for use in a JMX object name
     * @return the JMX ObjectName of the MBean, or <code>null</code> if
     *         the <code>customName</code> is invalid
     */
    ObjectName createCustomComponentMBeanName(String customName);

    /**
     * Retrieve the default JMX Domain Name for MBeans registered in
     * this instance of the JBI implementation.
     *
     * @return the JMX domain name for this instance of the JBI implemention;
     *         must be non-null and non-empty
     */
    String getJmxDomainName();
}
