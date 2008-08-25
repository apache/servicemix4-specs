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

import javax.jbi.messaging.MessageExchange;
import javax.jbi.servicedesc.ServiceEndpoint;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;

/**
 * This interface, implemented by component implementations, allows
 * the JBI implementation to query the component for various types
 * of information. This includes:
 * <ul>
 *   <li>The component's life cycle control interface.</li>
 *   <li>The component's service unit manager, for handling deployments.</li>
 *   <li>A method for querying service metadata describing services
 *       provided by this component.</li>
 *   <li>"Policy" methods that are called by the JBI implementation to
 *       query if proposed matches of this component to a provider (or
 *       consumer) are acceptable, according to this component's policies.</li>
 *   <li>Endpoint reference (EPR) resolution. Some components will provide
 *       the ability to resolve EPRs (typically binding components). This
 *       ability to resolve EPRs is used by JBI to facilitate resolution of
 *       EPRs received by service consumers.</li>
 *
 * The name of the class that implements this interface for a component is
 * specified in the installation descriptor for that component.
 *
 * @author JSR208 Exert Group
 */
public interface Component {

    /**
     * Get the life cycle control interface for this component. This interface
     * allows the JBI implementation to control the running state of this component.
     *
     * This method must be called before any other methods of this interface are
     * called. In addition, the JBI implementation must call the init() method of
     * the component life cycle returned by this method before calling any other
     * methods on this interface, or the component life cycle interface.
     *
     * @return the life cycle control interface for this component; must be non-null.
     */
    ComponentLifeCycle getLifeCycle();

    /**
     * Get the Service Unit manager for this component. If this component does not
     * support deployments, it must return null.
     *
     * @return the ServiceUnitManager for this component, or null if there is none.
     */
    ServiceUnitManager getServiceUnitManager();

    /**
     * Retrieves a DOM representation containing metadata which describes the service
     * provided by this component, through the given endpoint. The result can use WSDL
     * 1.1 or WSDL 2.0.
     *
     * @param endpoint the service endpoint.
     * @return the description for the specified service endpoint.
     */
    Document getServiceDescription(ServiceEndpoint endpoint);

    /**
     * This method is called by JBI to check if this component, in the role of provider
     * of the service indicated by the given exchange, can actually perform the operation
     * desired.
     *
     * @param endpoint the endpoint to be used by the consumer; must be non-null.
     * @param exchange the proposed message exchange to be performed; must be non-null.
     * @return true if this provider component can interact with the described consumer
     *         to perform the given exchange.
     */
    boolean isExchangeWithConsumerOkay(ServiceEndpoint endpoint, MessageExchange exchange);

    /**
     * This method is called by JBI to check if this component, in the role of consumer
     * of the service indicated by the given exchange, can actually interact with the
     * provider properly. The provider is described by the given endpoint and the service
     * description supplied by that endpoint.
     *
     * @param endpoint the endpoint to be used by the provider; must be non-null.
     * @param exchange the proposed message exchange to be performed; must be non-null.
     * @return true if this consumer component can interact with the described provider
     *         to perform the given exchange.
     */
    boolean isExchangeWithProviderOkay(ServiceEndpoint endpoint, MessageExchange exchange);

    /**
     * Resolve the given endpoint reference. This is called by JBI when it is attempting to
     * resolve the given EPR on behalf of a component.
     *
     * If this component returns a non-null result, it must conform to the following:
     * <ul>
     *   <li>This component implements the ServiceEndpoint returned.</li>
     *   <li>The result must not be registered or activated with the JBI implementation.</li>
     * </ul>
     *
     * Dynamically resolved endpoints are distinct from static ones; they must not be activated
     * (see {@link javax.jbi.component.ComponentContext#activateEndpoint(javax.xml.namespace.QName, String)}),
     * nor registered (see {@link ComponentContext}) by components. They can only be used to address
     * message exchanges; the JBI implementation must deliver such exchanges to the component that
     * resolved the endpoint reference (see
     * {@link javax.jbi.component.ComponentContext#resolveEndpointReference(org.w3c.dom.DocumentFragment)}).
     *
     * @param epr the endpoint reference, in some XML dialect understood by the appropriate component
     *            (usually a binding); must be non-null.
     * @return the service endpoint for the EPR; null if the EPR cannot be resolved by this component.
     */
    ServiceEndpoint resolveEndpointReference(DocumentFragment epr);
}
