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
 * Typesafe enumeration containing status values for a message exchange.
 *
 * @author JSR208 Expert Group
 */
public final class ExchangeStatus {

    /**
     * Indicates that an ME has not been processed to completion.
     */
    public static final ExchangeStatus ACTIVE = new ExchangeStatus("Active");

    /**
     * Indicates that an ME has terminated abnormally within the JBI environment.
     */
    public static final ExchangeStatus ERROR = new ExchangeStatus("Error");

    /**
     * Indicates that an ME has been processed to completion.
     */
    public static final ExchangeStatus DONE = new ExchangeStatus("Done");

    /** String representation of status. */
    private String mStatus;

    /**
     * Private constructor used to create a new ExchangeStatus type.
     *
     * @param status value
     */
    private ExchangeStatus(String status) {
        mStatus = status;
    }

    /**
     * Returns string value of enumerated type.
     *
     * @return String representation of status value.
     */
    public String toString() {
        return mStatus;
    }

    /**
     * Returns instance of ExchangeStatus that corresponds to given string.
     *
     * @param status string value of status
     * @return ExchangeStatus
     * @throws java.lang.IllegalArgumentException if string can't be translated
     */
    public static ExchangeStatus valueOf(String status) {
        ExchangeStatus instance;

        //
        //  Convert symbolic name to object reference.
        //
        if (status.equals(DONE.toString())) {
            instance = DONE;
        } else if (status.equals(ERROR.toString())) {
            instance = ERROR;
        } else if (status.equals(ACTIVE.toString())) {
            instance = ACTIVE;

        } else {
            //
            //  Someone has a problem.
            //
            throw new java.lang.IllegalArgumentException(status);
        }

        return instance;
    }

    /**
     * Returns hash code value for this object.
     *
     * @return hash code value
     */
    public int hashCode() {
        return mStatus.hashCode();
    }
}
