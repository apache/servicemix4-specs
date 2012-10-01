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
 * DeploymentException is an exception thrown by the Deployment Service and
 * the Service Unit Manager.
 *
 * @author JSR208 Expert Group
 */
public class DeploymentException extends javax.jbi.JBIException {

    /**
     * Creates a new instance of DeploymentException with an exception detail
     * message.
     *
     * @param aMessage the detail message for this exception.
     */
    public DeploymentException(String aMessage) {
        super(aMessage);
    }

    /**
     * Creates a new instance of DeploymentException with and exception detail
     * message and a cause.
     *
     * @param aMessage the detail message for this exception.
     * @param aCause <code>Error</code> or <code>Exception</code> which
     *        represents the cause of the problem (<code>null</code> if none,
     *        or if the cause is not known).
     */
    public DeploymentException(String aMessage, Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     * Creates a new instance of DeploymentException with the specified cause.
     *
     * @param aCause <code>Error</code> or <code>Exception</code> which
     *        represents the cause of the problem (<code>null</code> if none,
     *        or if the cause is not known).
     */
    public DeploymentException(Throwable aCause) {
        super(aCause);
    }
}
