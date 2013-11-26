/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.servicemix.specs.activation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.CommandMap;
import javax.activation.DataContentHandler;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * A bundle activator for activation framework
 */
public class Activator extends org.apache.servicemix.specs.locator.Activator {

    private Map<Long, MailCap> mailcaps = new ConcurrentHashMap<Long, MailCap>();

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        super.start(bundleContext);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        super.stop(bundleContext);
        CommandMap.setDefaultCommandMap(null);
    }

    @Override
    protected void register(Bundle bundle) {
        debugPrintln("checking bundle " + bundle.getBundleId());
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
            } catch (ClassNotFoundException e) {
                // ignored
            }

            try {
                mailcaps.put(bundle.getBundleId(), new MailCap(bundle, url));
            } catch (IOException ex) {
                // ignored
            }
            rebuildCommandMap();
        }
    }

    @Override
    protected void unregister(long bundleId) {
        MailCap mailcap = mailcaps.remove(bundleId);
        if (mailcap != null ){
            debugPrintln("removing mailcap for bundle " + mailcap.bundle.getBundleId());
            rebuildCommandMap();
        }
    }

    private void rebuildCommandMap() {
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            OsgiMailcapCommandMap commandMap = new OsgiMailcapCommandMap();
            for (MailCap mailcap : mailcaps.values()) {
                for (String line : mailcap.lines) {
                    commandMap.addMailcap(line, mailcap.bundle);
                }
            }
            CommandMap.setDefaultCommandMap(commandMap);
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);
        }
    }

    private static class MailCap {
        Bundle bundle;
        List<String> lines;

        private MailCap(Bundle bundle, URL url) throws IOException {
            this.bundle = bundle;
            this.lines = new ArrayList<String>();
            InputStream is = url.openStream();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } finally {
                is.close();
            }
        }
    }

}
