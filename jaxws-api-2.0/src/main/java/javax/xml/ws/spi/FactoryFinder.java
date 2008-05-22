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
package javax.xml.ws.spi;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.xml.ws.WebServiceException;

class FactoryFinder {
	static Object locate(String factoryId) {
		return locate(factoryId, null);
	}

	static Object locate(String factoryId, String altClassName) {
		return locate(factoryId, altClassName, Thread.currentThread().getContextClassLoader());
	}

	static Object locate(String factoryId, String altClassName, ClassLoader classLoader) {
        try {
            // If we are deployed into an OSGi environment, leverage it
            Class spiClass = org.apache.servicemix.specs.locator.OsgiLocator.locate(factoryId);
            if (spiClass != null) {
                return spiClass.newInstance();
            }
        } catch (Throwable e) {
        }

		String serviceId = "META-INF/services/" + factoryId;
		try {
			InputStream is = null;

			if (classLoader == null) {
				is = ClassLoader.getSystemResourceAsStream(serviceId);
			} else {
				is = classLoader.getResourceAsStream(serviceId);
			}

			if (is != null) {
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String factoryClassName = br.readLine();
				br.close();

				if (factoryClassName != null && !"".equals(factoryClassName)) {
					return loadFactory(factoryClassName, classLoader);
				}
			}
		} catch (Exception ex) {
		}

        try {
            String configFile = System.getProperty("java.home")
                    + File.separator + "lib" + File.separator
                    + "jaxws.properties";
            File f = new File(configFile);
            if (f.exists()) {
                Properties props = new Properties();
                props.load(new FileInputStream(f));
                String factoryClassName = props.getProperty(factoryId);
                return loadFactory(factoryClassName, classLoader);
            }
        } catch (Exception e) {
        }

        try {
			String prop = System.getProperty(factoryId);
			if (prop != null) {
				return loadFactory(prop, classLoader);
			}
		} catch (Exception e) {
		}

		if (altClassName == null) {
			throw new WebServiceException("Unable to locate factory for " + factoryId + ".", null);
		}
		return loadFactory(altClassName, classLoader);
	}

	private static Object loadFactory(String className, ClassLoader classLoader) {
		try {
			Class factoryClass = classLoader == null ? Class.forName(className)
					                                 : classLoader.loadClass(className);
			return factoryClass.newInstance();
		} catch (ClassNotFoundException x) {
			throw new WebServiceException("Requested factory "
					+ className + " cannot be located.  Classloader ="
					+ classLoader.toString(), x);
		} catch (Exception x) {
			throw new WebServiceException("Requested factory "
					+ className + " could not be instantiated: " + x, x);
		}
	}
}
