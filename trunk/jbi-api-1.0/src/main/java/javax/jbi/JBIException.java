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
package javax.jbi;

/**
 * JBIException is the top-level exception thrown by all JBI system components.
 */
public class JBIException extends Exception {

    /**
     * Creates a new instance of JBIException with an exception message.
     * @param aMessage String describing this exception.
     */
    public JBIException(String aMessage) {
        super(aMessage);
    }

    /**
     * Creates a new instance of JBIException with the specified message and cause.
     * @param aMessage String describing this exception.
     * @param aCause Throwable which represents an underlying problem (or null).
     */
    public JBIException(String aMessage, Throwable aCause) {
        super(aMessage, aCause);
    }

    /**
     * Creates a new instance of JBIException with the specified cause.
     * @param aCause Throwable which represents an underlying problem (or null).
     */
    public JBIException(Throwable aCause) {
        super(aCause);
    }
}
