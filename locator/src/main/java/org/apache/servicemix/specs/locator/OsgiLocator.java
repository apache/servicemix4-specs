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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class OsgiLocator {

    private static Map<String, List<Callable<Class>>> factories;
    
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    private OsgiLocator() {
    }

    public static void unregister(String id, Callable<Class> factory) {
        lock.writeLock().lock();
        try {
            if (factories != null) {
                List<Callable<Class>> l = factories.get(id);
                if (l != null) {
                    l.remove(factory);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void register(String id, Callable<Class> factory) {
        lock.writeLock().lock();
        try {
            if (factories == null) {
                factories = new HashMap<String, List<Callable<Class>>>();
            }
            List<Callable<Class>> l = factories.get(id);
            if (l ==  null) {
                l = new ArrayList<Callable<Class>>();
                factories.put(id, l);
            }
            l.add(0, factory);
        } finally {
            lock.writeLock().unlock();
        }
    }


    public static <T> Class<? extends T> locate(Class<T> factoryId) {
        return locate(factoryId, factoryId.getName());
    }

    public static <T> Class<? extends T> locate(Class<T> factoryClass, String factoryId) {
        lock.readLock().lock();
        try {
            if (factories != null) {
                List<Callable<Class>> l = factories.get(factoryId);
                if (l != null && !l.isEmpty()) {
                    // look up the System property first
                    String factoryClassName = System.getProperty(factoryId);
                    try {
                        for (Callable<Class> i : l) {
                            Class c = null;
                            try {
                                c = i.call();
                            } catch (Exception ex) {
                                // do nothing here
                            }
                            if (c != null && factoryClass == c.getClassLoader().loadClass(factoryClass.getName())
                                     && (factoryClassName == null || c.getName().equals(factoryClassName)))
                            {
                                return c;
                            }
                        }
                    } catch (Exception ex) {
                        // do nothing here
                    }  
                }
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static <T> List<Class<? extends T>> locateAll(Class<T> factoryId) {
        return locateAll(factoryId, factoryId.getName());
    }

    public static <T> List<Class<? extends T>> locateAll(Class<T> factoryClass, String factoryId) {
        lock.readLock().lock();
        try {
            List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>();
            if (factories != null) {
                List<Callable<Class>> l = factories.get(factoryId);
                if (l != null) {
                    for (Callable<Class> i : l) {
                    	try {
                            Class c = i.call();
                            if (c != null && factoryClass.isAssignableFrom(c)) {
                        	    classes.add(c);
                            }
                    	} catch (Exception e) {
                    	}
    				}
                }
            }
            return classes;
        } finally {
            lock.readLock().unlock();
        }
    }

}
