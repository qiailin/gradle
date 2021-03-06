/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;

public class ClassLoaderBackedClasspathSource implements ClasspathSource {
    private final ClassLoader classLoader;

    public ClassLoaderBackedClasspathSource(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void collectClasspath(Collection<? super URL> classpath) {
        ClassLoader stopAt = ClassLoader.getSystemClassLoader() == null ? null : ClassLoader.getSystemClassLoader().getParent();
        for (ClassLoader cl = classLoader; cl != null && cl != stopAt; cl = cl.getParent()) {
            if (cl instanceof ClasspathSource) {
                ClasspathSource classpathSource = (ClasspathSource) cl;
                classpathSource.collectClasspath(classpath);
                break;
            }
            if (cl instanceof URLClassLoader) {
                classpath.addAll(Arrays.asList(((URLClassLoader) cl).getURLs()));
            }
        }
    }
}
