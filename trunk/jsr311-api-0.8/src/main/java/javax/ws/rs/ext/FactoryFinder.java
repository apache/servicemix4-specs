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

package javax.ws.rs.ext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

class FactoryFinder {

    /**
     * Creates an instance of the specified class using the specified 
     * <code>ClassLoader</code> object.
     *
     * @exception ClassNotFoundException if the given class could not be found
     *            or could not be instantiated
     */
    private static Object newInstance(String className,
            ClassLoader classLoader) throws ClassNotFoundException {
        try {
            Class spiClass;
            if (classLoader == null) {
                spiClass = Class.forName(className);
            } else {
                spiClass = classLoader.loadClass(className);
            }
            return spiClass.newInstance();
        } catch (ClassNotFoundException x) {
            throw x;
        } catch (Exception x) {
            throw new ClassNotFoundException(
                    "Provider " + className + " could not be instantiated: " + x,
                    x);
        }
    }

    /**
     * Finds the implementation <code>Class</code> object for the given
     * factory name, or if that fails, finds the <code>Class</code> object
     * for the given fallback class name. The arguments supplied MUST be
     * used in order. If using the first argument is successful, the second
     * one will not be used.
     * <P>
     * This method is package private so that this code can be shared.
     *
     * @return the <code>Class</code> object of the specified message factory;
     *         may not be <code>null</code>
     *
     * @param factoryId             the name of the factory to find, which is
     *                              a system property
     * @param fallbackClassName     the implementation class name, which is
     *                              to be used only if nothing else
     *                              is found; <code>null</code> to indicate that
     *                              there is no fallback class name
     * @exception ClassNotFoundException if there is an error
     */
    static Object find(String factoryId, String fallbackClassName) throws ClassNotFoundException {
        try {
            // If we are deployed into an OSGi environment, leverage it
            Class factoryClass = FactoryFinder.class.getClassLoader().loadClass(factoryId);
            Class spiClass = org.apache.servicemix.specs.locator.OsgiLocator.locate(factoryClass, factoryId);
            if (spiClass != null) {
                return spiClass.newInstance();
            }
        } catch (Throwable e) {
        }

        ClassLoader classLoader;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception x) {
            throw new ClassNotFoundException(x.toString(), x);
        }

        String serviceId = "META-INF/services/" + factoryId;
        // try to find services in CLASSPATH
        try {
            InputStream is = null;
            if (classLoader == null) {
                is = ClassLoader.getSystemResourceAsStream(serviceId);
            } else {
                is = classLoader.getResourceAsStream(serviceId);
            }

            if (is != null) {
                BufferedReader rd =
                        new BufferedReader(new InputStreamReader(is, "UTF-8"));

                String factoryClassName = rd.readLine();
                rd.close();

                if (factoryClassName != null &&
                        !"".equals(factoryClassName)) {
                    return newInstance(factoryClassName, classLoader);
                }
            }
        } catch (Exception ex) {
        }


        // try to read from $java.home/lib/jaxrs.properties
        try {
            String javah = System.getProperty("java.home");
            String configFile = javah + File.separator +
                    "lib" + File.separator + "jaxrs.properties";
            File f = new File(configFile);
            if (f.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(f));
                String factoryClassName = props.getProperty(factoryId);
                return newInstance(factoryClassName, classLoader);
            }
        } catch (Exception ex) {
        }


        // Use the system property
        try {
            String systemProp =
                    System.getProperty(factoryId);
            if (systemProp != null) {
                return newInstance(systemProp, classLoader);
            }
        } catch (SecurityException se) {
        }

        if (fallbackClassName == null) {
            throw new ClassNotFoundException(
                    "Provider for " + factoryId + " cannot be found", null);
        }

        return newInstance(fallbackClassName, classLoader);
    }
}
