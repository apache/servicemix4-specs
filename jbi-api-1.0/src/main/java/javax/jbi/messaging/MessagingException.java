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

/**
 * Generic exception used to report messaging related errors
 * in the Normalized Message Service.
 *
 * @author JSR208 Expert Group
 */
public class MessagingException extends javax.jbi.JBIException {

    /**
     * Create a new MessagingException.
     *
     * @param msg error detail
     */
    public MessagingException(String msg) {
        super(msg);
    }

    /**
     * Create a new MessagingException with the specified cause and error text.
     *
     * @param msg error detail
     * @param cause underlying error
     */
    public MessagingException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create a new MessagingException with the specified cause.
     * 
     * @param cause underlying error
     */
    public MessagingException(Throwable cause) {
        super(cause);
    }
}
