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
package org.apache.servicemix.specs.activator;

import java.util.Map;
import java.util.HashMap;

import javax.activation.MailcapCommandMap;
import javax.activation.CommandInfo;
import javax.activation.DataContentHandler;

import org.osgi.framework.Bundle;

/**
 * An OSGi enabled MailcapCommandMap
 */
public class OsgiMailcapCommandMap extends MailcapCommandMap {

    private Bundle currentBundle;
    private Map<CommandInfo, Bundle> bundles = new HashMap<CommandInfo, Bundle>();

    public void addMailcap(String line, Bundle bundle) {
        currentBundle = bundle;
        addMailcap(line);
    }

    protected void addCommand(Map commandList, String mimeType, CommandInfo command) {
        super.addCommand(commandList, mimeType, command);
        if (currentBundle != null) {
            bundles.put(command, currentBundle);
        }
    }

    @Override
    public DataContentHandler createDataContentHandler(String mimeType) {
        CommandInfo info = getCommand(mimeType, "content-handler");
        if (info == null) {
            return null;
        }

        Bundle bundle = bundles.get(info);
        if (bundle != null) {
            try {
                return (DataContentHandler) bundle.loadClass(info.getCommandClass()).newInstance();
            } catch (ClassNotFoundException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            } catch (InstantiationException e) {
                return null;
            }
        }

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = getClass().getClassLoader();
        }
        try {
            return (DataContentHandler) cl.loadClass(info.getCommandClass()).newInstance();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (InstantiationException e) {
            return null;
        }
    }
}
