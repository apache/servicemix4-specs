/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.servicemix.specs.locator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class OsgiLocatorTest extends Assert {
    
    @BeforeClass
    public static void setup() {
        OsgiLocator.register("Factory", new MockCallable());
        OsgiLocator.register("Factory", new MockCallable2());
    }
    
    @Test
    public void testLocatorWithSystemProperty() {
        System.setProperty("Factory", "org.apache.servicemix.specs.locator.MockCallable");
        Class clazz = OsgiLocator.locate(Object.class, "Factory");
        assertNotNull("Except to find the class", clazz);
        assertEquals("Get a wrong class.", MockCallable.class.getName(), clazz.getName());
        
        System.setProperty("Factory", "org.apache.servicemix.specs.locator");
        clazz = OsgiLocator.locate(Object.class, "Factory");
        assertNull("Except to find the class", clazz);
        System.clearProperty("Factory");
    }
    
    @Test
    public void testLocatorWithoutSystemProperty() {
        Class clazz = OsgiLocator.locate(Object.class, "Factory");
        assertNotNull("Except to find the class", clazz);
        assertEquals("Get a wrong class.", MockCallable2.class.getName(), clazz.getName());
    }

}
