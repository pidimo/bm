package com.piramide.elwis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Oct 3, 2005
 * Time: 2:18:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceLoader {
    /**
     * Load a given resource.
     * <p/>
     * This method will try to load the resource using the following methods (in order):
     * <ul>
     * <li>From {@link Thread#getContextClassLoader() Thread.currentThread().getContextClassLoader()}
     * <li>From {@link Class#getClassLoader() ClassLoaderUtil.class.getClassLoader()}
     * <li>From the {@link Class#getResource(String) calling Clazz.getResource() }
     * </ul>
     *
     * @param resourceName The name of the resource to load
     */
    private static URL getResource(String resourceName, Class clazz) {
        URL url = null;
        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);

        if (url == null) {
            url = ResourceLoader.class.getClassLoader().getResource(resourceName);
        }
        if (url == null) {
            url = clazz.getResource(resourceName);
        }
        return url;
    }


    /**
     * This is a convenience method to load a resource as a stream.
     * <p/>
     * The algorithm used to find the resource is given in getResource()
     *
     * @param resourceName The name of the resource to load
     */
    public static InputStream getResourceAsStream(String resourceName, Class clazz) {
        URL url = getResource(resourceName, clazz);
        try {
            return url != null ? url.openStream() : null;
        }
        catch (IOException e) {
            return null;
        }
    }
}
