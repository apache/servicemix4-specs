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
package javax.jbi.messaging;

import java.util.Set;

import javax.activation.DataHandler;
import javax.security.auth.Subject;
import javax.xml.transform.Source;

/**
 * Represents a JBI Normalized Message.
 *
 * @author JSR208 Expert Group
 */
public interface NormalizedMessage {

    /**
     * Add an attachment to the message.
     *
     * @param id unique identifier for the attachment
     * @param content attachment content
     * @throws MessagingException failed to add attachment
     */
    void addAttachment(String id, DataHandler content) throws MessagingException;

    /**
     * Retrieve the content of the message.
     *
     * @return message content
     */
    Source getContent();

    /**
     * Retrieve attachment with the specified identifier.
     *
     * @param id unique identifier for attachment
     * @return DataHandler representing attachment content, or null if an attachment
     *         with the specified identifier is not found
     */
    DataHandler getAttachment(String id);

    /**
     * Returns a list of identifiers for each attachment to the message.
     *
     * @return iterator over String attachment identifiers
     */
    Set getAttachmentNames();

    /**
     * Removes attachment with the specified unique identifier.
     *
     * @param id attachment identifier
     * @throws MessagingException failed to remove attachment
     */
    void removeAttachment(String id) throws MessagingException;

    /**
     * Set the content of the message.
     *
     * @param content message content
     * @throws MessagingException failed to set content
     */
    void setContent(Source content) throws MessagingException;

    /**
     * Set a property on the message.
     *
     * @param name property name
     * @param value property value
     */
    void setProperty(String name, Object value);

    /**
     * Set the security Subject for the message.
     *
     * @param subject Subject to associated with message.
     */
    void setSecuritySubject(Subject subject);

    /**
     * Retrieve a list of property names for the message.
     *
     * @return list of property names
     */
    Set getPropertyNames();

    /**
     * Retrieve a property from the message.
     *
     * @param name property name
     * @return property value, or null if the property does not exist
     */
    Object getProperty(String name);

    /**
     * Retrieve the security Subject from the message.
     *
     * @return security Subject associated with message, or null.
     */
    Subject getSecuritySubject();
}
