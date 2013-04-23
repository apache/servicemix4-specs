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
package org.apache.servicemix.specs.activator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.activation.CommandMap;
import javax.activation.DataContentHandler;

import org.apache.servicemix.specs.locator.OsgiLocator;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

public class Activator implements BundleActivator, SynchronousBundleListener {

    private static boolean debug = false;

    private ConcurrentMap<Long, Map<String, Callable<Class>>> factories = new ConcurrentHashMap<Long, Map<String, Callable<Class>>>();

    private BundleContext bundleContext;
    
    private Map<Long, MailCap> mailcaps = new ConcurrentHashMap<Long, MailCap>();

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
    protected void debugPrintln(String msg) {
        if (debug) {
            System.err.println("Spec(" + bundleContext.getBundle().getBundleId() + "): " + msg);
        }
    }

    public synchronized void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        debugPrintln("activating");
        debugPrintln("adding bundle listener");
        bundleContext.addBundleListener(this);
        debugPrintln("checking existing bundles");
        for (Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getState() == Bundle.RESOLVED || bundle.getState() == Bundle.STARTING ||
                    bundle.getState() == Bundle.ACTIVE || bundle.getState() == Bundle.STOPPING) {
                register(bundle);
            }
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
        CommandMap.setDefaultCommandMap(null);
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
                map.put(factoryId, new BundleFactoryLoader(factoryId, u, bundle));
            }
        }
        if (map != null) {
            for (Map.Entry<String, Callable<Class>> entry : map.entrySet()) {
                debugPrintln("registering service for key " + entry.getKey() + " with value " + entry.getValue());
                OsgiLocator.register(entry.getKey(), entry.getValue());
            }
        }
        
        URL url = bundle.getResource("/META-INF/mailcap");
        if (url != null) {
            debugPrintln("found mailcap at " + url);

            try {
                final Class<?> clazz = bundle
                        .loadClass("javax.activation.DataContentHandler");
                if (!clazz.isAssignableFrom(DataContentHandler.class)) {
                    debugPrintln("incompatible DataContentHandler class in bundle "
                            + bundle.getBundleId());
                    return;
                }
            } catch (ClassNotFoundException ex) {
                // ignored
            }

            mailcaps.put(bundle.getBundleId(), new MailCap(bundle, url));
            rebuildCommandMap();
        }
    }

    protected void unregister(long bundleId) {
        Map<String, Callable<Class>> map = factories.remove(bundleId);
        if (map != null) {
            for (Map.Entry<String, Callable<Class>> entry : map.entrySet()) {
                debugPrintln("unregistering service for key " + entry.getKey() + " with value " + entry.getValue());
                OsgiLocator.unregister(entry.getKey(), entry.getValue());
            }
        }
        MailCap mailcap = mailcaps.remove(bundleId);
        if (mailcap != null ){
            debugPrintln("removing mailcap at " + mailcap.url);
            rebuildCommandMap();
        }
    }

    private class BundleFactoryLoader implements Callable<Class> {
        private final String factoryId;
        private final URL u;
        private final Bundle bundle;
        private volatile Class<?> clazz;

        public BundleFactoryLoader(String factoryId, URL u, Bundle bundle) {
            this.factoryId = factoryId;
            this.u = u;
            this.bundle = bundle;
        }

        public Class call() throws Exception {
            try {
                debugPrintln("loading factory for key: " + factoryId);
                
                if (clazz == null){
                    synchronized (this) {
                        if (clazz == null){
                            debugPrintln("creating factory for key: " + factoryId);
                            BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
                            String factoryClassName = br.readLine();
                            br.close();
                            debugPrintln("factory implementation: " + factoryClassName);
                            clazz = bundle.loadClass(factoryClassName);
                        }
                    }
                }
                return clazz;
            } catch (Exception e) {
                debugPrintln("exception caught while creating factory: " + e);
                throw e;
            } catch (Error e) {
                debugPrintln("error caught while creating factory: " + e);
                throw e;
            }
        }
        
        
        private class MimeFactoryLoader implements Callable<Class> {
            private final String mimeType;
            private final URL u;
            private final Bundle bundle;
            private volatile Class<?> clazz;

            public MimeFactoryLoader(String mimeType, URL u, Bundle bundle) {
                this.mimeType = mimeType;
                this.u = u;
                this.bundle = bundle;
            }

            public Class call() throws Exception {
                try {
                    debugPrintln("loading factory for key: " + mimeType);
                    
                    if (clazz == null){
                        synchronized (this) {
                            if (clazz == null){
                                debugPrintln("creating factory for key: " + factoryId);
                                BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream(), "UTF-8"));
                                String factoryClassName = br.readLine();
                                br.close();
                                debugPrintln("factory implementation: " + factoryClassName);
                                clazz = bundle.loadClass(factoryClassName);
                            }
                        }
                    }
                    return clazz;
                } catch (Exception e) {
                    debugPrintln("exception caught while creating factory: " + e);
                    throw e;
                } catch (Error e) {
                    debugPrintln("error caught while creating factory: " + e);
                    throw e;
                }
            }
        }

        @Override
        public String toString() {
           return u.toString();
        }

        @Override
        public int hashCode() {
           return u.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BundleFactoryLoader) {
                return u.equals(((BundleFactoryLoader) obj).u);
            } else {
                return false;
            }
        }
    }
    
    private void rebuildCommandMap() {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            OsgiMailcapCommandMap commandMap = new OsgiMailcapCommandMap();
            for (MailCap mailcap : mailcaps.values()) {
                try {
                    InputStream is = mailcap.url.openStream();
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = br.readLine()) != null) {
                            commandMap.addMailcap(line, mailcap.bundle);
                        }
                    } finally {
                        is.close();
                    }
                } catch (Exception e) {
                    // Ignore
                }
            }
            CommandMap.setDefaultCommandMap(commandMap);
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);
        }
    }
    
    private static class MailCap {
        Bundle bundle;
        URL url;

        private MailCap(Bundle bundle, URL url) {
            this.bundle = bundle;
            this.url = url;
        }
    }
}
