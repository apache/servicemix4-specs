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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

public class Activator implements BundleActivator, SynchronousBundleListener {

    private static boolean debug = false;

    private ConcurrentMap<Long, Map<String, Callable<Class>>> factories = new ConcurrentHashMap<Long, Map<String, Callable<Class>>>();

    private BundleContext bundleContext;

    static {
        try {
            String prop = System.getProperty("org.apache.servicemix.specs.debug");
            debug = prop != null && !"false".equals(prop);
        } catch (Throwable t) { }
    }

    /**
     * <p>Output debugging messages.</p>
     *
     * @param msg <code>String</code> to print to <code>stderr</code>.
     */
    private void debugPrintln(String msg) {
        if (debug) {
            System.err.println("Spec(" + bundleContext.getBundle().getBundleId() + "): " + msg);
        }
    }

    public synchronized void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        debugPrintln("activating");
        bundleContext.addBundleListener(this);
        for (Bundle bundle : bundleContext.getBundles()) {
            register(bundle);
        }
        debugPrintln("activated");
    }

    public synchronized void stop(BundleContext bundleContext) throws Exception {
        debugPrintln("deactivating");
        bundleContext.removeBundleListener(this);
        while (!factories.isEmpty()) {
            unregister(factories.keySet().iterator().next());
        }
        debugPrintln("deactivated");
        this.bundleContext = null;
    }

    public void bundleChanged(BundleEvent event) {
        if (event.getType() == BundleEvent.RESOLVED) {
            register(event.getBundle());
        } else if (event.getType() == BundleEvent.UNRESOLVED || event.getType() == BundleEvent.UNINSTALLED) {
            unregister(event.getBundle().getBundleId());
        }
    }

    protected void register(final Bundle bundle) {
        debugPrintln("checking bundle " + bundle.getBundleId());
        Map<String, Callable<Class>> map = factories.get(bundle.getBundleId());
        Enumeration e = bundle.findEntries("META-INF/services/", "*", false);
        if (e != null) {
            while (e.hasMoreElements()) {
                final URL u = (URL) e.nextElement();
                final String url = u.toString();
                if (url.endsWith("/")) {
                    continue;
                }
                final String factoryId = url.substring(url.lastIndexOf("/") + 1);
                if (map == null) {
                    map = new HashMap<String, Callable<Class>>();
                    factories.put(bundle.getBundleId(), map);
                }
                map.put(factoryId, new Callable<Class>() {
                    public Class call() throws Exception {
                        try {
                            debugPrintln("creating factory for key: " + factoryId);
                            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
                            String factoryClassName = br.readLine();
                            br.close();
                            debugPrintln("factory implementation: " + factoryClassName);
                            return bundle.loadClass(factoryClassName);
                        } catch (Exception e) {
                            debugPrintln("exception caught while creating factory: " + e);
                            throw e;
                        } catch (Error e) {
                            debugPrintln("error caught while creating factory: " + e);
                            throw e;
                        }
                    }
                });
            }
        }
        if (map != null) {
            for (Map.Entry<String, Callable<Class>> entry : map.entrySet()) {
                debugPrintln("registering service for key " + entry.getKey());
                OsgiLocator.register(entry.getKey(), entry.getValue());
            }
        }
    }

    protected void unregister(long bundleId) {
        Map<String, Callable<Class>> map = factories.remove(bundleId);
        if (map != null) {
            for (Map.Entry<String, Callable<Class>> entry : map.entrySet()) {
                debugPrintln("unregistering service for key " + entry.getKey());
                OsgiLocator.unregister(entry.getKey(), entry.getValue());
            }
        }
    }
}
