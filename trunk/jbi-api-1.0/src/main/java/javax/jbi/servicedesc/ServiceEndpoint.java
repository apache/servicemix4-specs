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
package javax.jbi.servicedesc;

import javax.xml.namespace.QName;

/**
 * Reference to an endpoint, used to refer to an endpoint as well as
 * query information about the endpoint. An endpoint is an addressable
 * entity in the JBI system, used for accessing the provider of a
 * specific service.
 *
 * @author JSR208 Expert Group
 */
public interface ServiceEndpoint {

    /**
     * Get a reference to this endpoint, using an endpoint reference
     * vocabulary that is known to the provider.
     *
     * @param operationName the name of the operation to be performed by a
     *                      consumer of the generated endpoint reference. Set
     *                      to null if this is not applicable.
     * @return endpoint reference as an XML fragment; null if the provider
     *         does not support such references.
     */
    org.w3c.dom.DocumentFragment getAsReference(QName operationName);

    /**
     * Returns the name of this endpoint.
     *
     * @return the endpoint name
     */
    String getEndpointName();

    /**
     * Get the qualified names of all the interfaces implemented by this
     * service endpoint.
     *
     * @return array of all interfaces implemented by this service endpoint;
     *         must be non-null and non-empty.
     */
    javax.xml.namespace.QName[] getInterfaces();

    /**
     * Returns the service name of this endpoint.
     * 
     * @return the qualified service name.
     */
    javax.xml.namespace.QName getServiceName();
}
