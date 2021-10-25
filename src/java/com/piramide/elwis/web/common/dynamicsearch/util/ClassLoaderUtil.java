package com.piramide.elwis.web.common.dynamicsearch.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class ClassLoaderUtil {
    private static URL getResource(String resourceName, Class clazz) {
        URL url;
        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = clazz.getClassLoader().getResource(resourceName);
        }

        if (url == null) {
            url = ClassLoaderUtil.class.getClassLoader().getResource(resourceName);
        }

        return url;
    }

    public static InputStream getResourceAsStream(String resourceName, Class clazz) throws IOException {
        URL url = getResource(resourceName, clazz);
        try {
            return url != null ? url.openStream() : null;
        } catch (IOException e) {
            throw new RuntimeException("Resource not found:" + resourceName, e);
        }

    }

    private static Class loadClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            try {
                //return Class.forName("com.jatun.titus.listgenerator.structure.TableContainerImplementation");
                return Class.forName(className);
            } catch (ClassNotFoundException ex) {
                try {
                    return ClassLoaderUtil.class.getClassLoader().loadClass(className);
                } catch (ClassNotFoundException exc) {
                    return null;
                }
            }
        }
    }

    public static Object newInstance(String className) throws ObjectInstanceException {
        Class clazz = loadClass(className);
        if (clazz == null) {
            throw new ObjectInstanceException("Class not found:" + className);
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new ObjectInstanceException(e.getMessage());
        }
    }
}
