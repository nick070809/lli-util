package org.kx.util;

import java.io.InputStream;

/**
 * Created by sunkx on 2017/5/18.
 */
public class SystemClassLoader extends  ClassLoader {
    @Override
    public Class<?> loadClass(String name)
            throws ClassNotFoundException {
        try {
            String className = name
                    .substring(name.lastIndexOf('.') + 1) + ".class";
            InputStream iStream = getClass().getResourceAsStream(
                    className);
            if (iStream == null) {
                return super.loadClass(name);
            }
            byte[] b = new byte[iStream.available()];
            iStream.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (Exception e) {
            throw new ClassNotFoundException(name);
        }
    }
}
