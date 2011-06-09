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
package javax.script;

import java.io.*;
import java.lang.annotation.AnnotationFormatError;
import java.net.URL;
import java.util.*;

public class ScriptEngineManager {
	
    private final Set engineSpis = new HashSet();
    private final Map<String, ScriptEngineFactory> byName = new HashMap<String, ScriptEngineFactory>();
    private final Map<String, ScriptEngineFactory> registeredByName = new HashMap();
    private final Map<String, ScriptEngineFactory> byExtension = new HashMap<String, ScriptEngineFactory>();
    private final Map<String, ScriptEngineFactory> registeredByExtension = new HashMap<String, ScriptEngineFactory>();
    private final Map<String, ScriptEngineFactory> byMimeType = new HashMap<String, ScriptEngineFactory>();
    private final Map<String, ScriptEngineFactory> registeredByMimeType = new HashMap<String, ScriptEngineFactory>();
    private Bindings globalScope;

	public ScriptEngineManager() {
		this(Thread.currentThread().getContextClassLoader());
	}
	
	public ScriptEngineManager(ClassLoader classLoader) {
        try
        {
            for (Enumeration factoryResources = classLoader.getResources("META-INF/services/javax.script.ScriptEngineFactory"); 
				 factoryResources.hasMoreElements();) {
                URL url = (URL) factoryResources.nextElement();
				for (String className : getClassNames(url)) {
                    try {
                        Class factoryClass = classLoader.loadClass(className);
                        Object object = factoryClass.newInstance();
                        if (object instanceof ScriptEngineFactory) {
                            ScriptEngineFactory factory = (ScriptEngineFactory) object;
                            for (String name : factory.getNames()) {
								byName.put(name, factory);
							}
							for (String extension : factory.getExtensions()) {
								byExtension.put(extension, factory);
							}
							for (String mimeType : factory.getMimeTypes()) {
								byMimeType.put(mimeType, factory);
							}
                            engineSpis.add(factory);
                        }
                    } catch(ClassNotFoundException doNothing) { 
					} catch(IllegalAccessException doNothing) { 
					} catch(InstantiationException doNothing) { 
					} catch(LinkageError doNothing) { 
					} catch(AnnotationFormatError doNothing) { 
					}
                }
            }
        } catch(IOException doNothing) { 
		}
	}

    public Bindings getGlobalScope() {
        return globalScope;
    }

    public void setGlobalScope(Bindings globalScope) {
        this.globalScope = globalScope;
    }

    public void put(String key, Object value) {
        if (globalScope != null) {
            globalScope.put(key, value);
		}
    }

    public Object get(String key) {
        if (globalScope != null) {
            return globalScope.get(key);
        } else {
            return null;
		}
    }

    public ScriptEngine getEngineByName(String shortName) {
		ScriptEngineFactory factory = null;
        List<Class<? extends ScriptEngineFactory>> factoryClasses = org.apache.servicemix.specs.locator.OsgiLocator.locateAll(javax.script.ScriptEngineFactory.class);
        for (Class<? extends ScriptEngineFactory> factoryClass : factoryClasses) {
			try {
				ScriptEngineFactory f = factoryClass.newInstance();
				if (f.getNames().contains(shortName)) {
					factory = f;
					break;
				}
			} catch (Throwable doNothing) {
			}
		}
        if (factory == null) {
			factory = registeredByName.get(shortName);
		}
        if (factory == null) {
            factory = byName.get(shortName);
		}
        if (factory == null) {
            return null;
        } else {
            ScriptEngine engine = factory.getScriptEngine();
            engine.setBindings(globalScope, ScriptContext.GLOBAL_SCOPE);
            return engine;
        }
    }

    public ScriptEngine getEngineByExtension(String extension) {
		ScriptEngineFactory factory = null;
        List<Class<? extends ScriptEngineFactory>> factoryClasses = org.apache.servicemix.specs.locator.OsgiLocator.locateAll(javax.script.ScriptEngineFactory.class);
        for (Class<? extends ScriptEngineFactory> factoryClass : factoryClasses) {
			try {
				ScriptEngineFactory f = factoryClass.newInstance();
				if (f.getExtensions().contains(extension)) {
					factory = f;
					break;
				}
			} catch (Throwable doNothing) {
			}
		}
        if (factory == null) {
			factory = registeredByExtension.get(extension);
		}
        if (factory == null) {
            factory = byExtension.get(extension);
		}
        if (factory == null) {
            return null;
        } else {
            ScriptEngine engine = factory.getScriptEngine();
            engine.setBindings(globalScope, ScriptContext.GLOBAL_SCOPE);
            return engine;
        }
    }

    public ScriptEngine getEngineByMimeType(String mimeType) {
		ScriptEngineFactory factory = null;
        List<Class<? extends ScriptEngineFactory>> factoryClasses = org.apache.servicemix.specs.locator.OsgiLocator.locateAll(javax.script.ScriptEngineFactory.class);
        for (Class<? extends ScriptEngineFactory> factoryClass : factoryClasses) {
			try {
				ScriptEngineFactory f = factoryClass.newInstance();
				if (f.getMimeTypes().contains(mimeType)) {
					factory = f;
					break;
				}
			} catch (Throwable doNothing) {
			}
		}
        if (factory == null) {
			factory = registeredByMimeType.get(mimeType);
		}
        if(factory == null) {
            factory = byMimeType.get(mimeType);
		}
        if (factory == null) {
            return null;
        } else {
            ScriptEngine engine = factory.getScriptEngine();
            engine.setBindings(globalScope, ScriptContext.GLOBAL_SCOPE);
            return engine;
        }
    }

    public List<ScriptEngineFactory> getEngineFactories() {
        List<ScriptEngineFactory> factories = new ArrayList();
        List<Class<? extends ScriptEngineFactory>> factoryClasses = org.apache.servicemix.specs.locator.OsgiLocator.locateAll(javax.script.ScriptEngineFactory.class);
        for (Class<? extends ScriptEngineFactory> factoryClass : factoryClasses) {
			try {
				factories.add((ScriptEngineFactory) factoryClass.newInstance());
			} catch (Throwable doNothing) {
			}
		}
		factories.addAll(engineSpis);
		return factories;
    }

    public void registerEngineName(String name, ScriptEngineFactory factory) {
        registeredByName.put(name, factory);
    }

    public void registerEngineMimeType(String type, ScriptEngineFactory factory) {
        registeredByMimeType.put(type, factory);
    }

    public void registerEngineExtension(String extension, ScriptEngineFactory factory) {
        registeredByExtension.put(extension, factory);
    }

    private Iterable<String> getClassNames(URL url) {
        Stack<String> stack = new Stack<String>();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = r.readLine()) != null) {
                int comment = line.indexOf('#');
                if (comment != -1) {
                    line = line.substring(0, comment);
				}
                stack.push(line.trim());
            }
        } catch(IOException doNothing) { 
		}
        return stack;
    }

}