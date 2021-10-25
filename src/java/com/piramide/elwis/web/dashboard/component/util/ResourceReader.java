package com.piramide.elwis.web.dashboard.component.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * @author : ivan
 */
public class ResourceReader {
    public static URL getResource(String resourceName, Class c) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (null == url) {
            url = c.getResource(resourceName);
        }
        return url;
    }

    public static Object getClassInstance(String className) {
        Object obj = null;
        try {
            Class clazz = Class.forName(className);
            obj = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static Class getClass(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object executeMethodOfClass(String className, String methodName, Object[] args) {
        Object obj = null;

        try {
            Class clazz = Class.forName(className);
            Object instance = clazz.newInstance();
            Method m;
            if (args.length > 0) {
                m = clazz.getMethod(methodName, Object.class);
            } else {
                m = clazz.getMethod(methodName);
            }

            obj = m.invoke(instance, args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
