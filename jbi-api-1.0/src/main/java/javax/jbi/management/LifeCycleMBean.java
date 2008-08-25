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

import javax.jbi.JBIException;

/**
 * LifeCycleMBean is a base interface that defines standard life cycle controls
 * for JBI implementation services (which are implementation-specific), and JBI
 * components (bindings and engines).
 *
 * @author JSR208 Expert Group
 */
public interface LifeCycleMBean {

    /** Value returned by {@link #getCurrentState()} for a shutdown component. */
    String SHUTDOWN = "Shutdown";

    /** Value returned by {@link #getCurrentState()} for a stopped component. */
    String STOPPED = "Stopped";

    /** Value returned by {@link #getCurrentState()} for a running component. */
     String STARTED = "Started";

    /** Value returned by {@link #getCurrentState()} for a component in an unknown state. */
    String UNKNOWN = "Unknown";

    /**
     * Start the item.
     *
     * @exception javax.jbi.JBIException if the item fails to start.
     */
    void start() throws JBIException;

    /**
     * Stop the item. This suspends current messaging activities.
     *
     * @exception javax.jbi.JBIException if the item fails to stop.
     */
    void stop() throws JBIException;

    /**
     * Shut down the item. This releases resources and returns the item
     * to an uninitialized state.
     *
     * @exception javax.jbi.JBIException if the item fails to shut down.
     */
    void shutDown() throws JBIException;

    /**
     * Get the current state of this managed compononent.
     *
     * @return the current state of this managed component (must be one of the
     *         string constants defined by this interface)
     */
     String getCurrentState();

}
