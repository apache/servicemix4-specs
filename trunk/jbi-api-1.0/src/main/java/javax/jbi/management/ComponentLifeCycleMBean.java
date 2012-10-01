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
 * ComponentLifeCycleMBean defines the standard life cycle controls for
 * JBI Installable Components.
 * <ul>
 *   <li>Initialize the component, preparing it to receive service requests.</li>
 *   <li>Start the component, allowing it to initiate service requests.</li>
 *   <li>Stop the component from initiating any more service requests.</li>
 *   <li>Shut down the component, returning it to the uninitialized state
 *       where it cannot receive service requests.</li>
 *   <li>Query the JMX object name of the extension MBean for the component.</li>
 * </ul>
 *
 * @author JSR208 Expert Group
 */
public interface ComponentLifeCycleMBean extends LifeCycleMBean {

    /**
     * Get the JMX ObjectName for the life cycle extension MBean for this
     * component. If there is none, return <code>null</code>.
     * Note that this MBean may serve as a container for multiple MBeans,
     * as required by the component implementation.
     *
     * @return ObjectName the JMX object name of the additional MBean
     *         or <code>null</code> if there is no additional MBean.
     * @exception javax.jbi.JBIException if there is a failure getting component
     *            information for the component to which this life cycle
     *            applies.
     */
    ObjectName getExtensionMBeanName() throws javax.jbi.JBIException;

}
