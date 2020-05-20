package org.kx.util.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class MyClassLoader extends ClassLoader {
    MyClassLoader myClassLoader = null;
    private Map<String, MyJavaFileObject> fileObjects = new ConcurrentHashMap<>();


    private static class MyClassLoadersInstance {
        private static final MyClassLoader instance = new MyClassLoader();
    }

    private MyClassLoader() {
    }

    public static MyClassLoader getInstance() {
        return MyClassLoadersInstance.instance;
    }

    public Map<String, MyJavaFileObject> getFileObjects() {
        return fileObjects;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        MyJavaFileObject fileObject = fileObjects.get(name);
        if (fileObject != null) {
            byte[] bytes = fileObject.getCompiledBytes();
            return defineClass(fileObject.getQualifiedClassName(), bytes, 0, bytes.length);
        }
        try {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            return super.findClass(name);
        }
    }
}
