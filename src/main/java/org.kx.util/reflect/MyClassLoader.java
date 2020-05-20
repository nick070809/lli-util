package org.kx.util.reflect;

import javax.tools.JavaFileObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class MyClassLoader extends ClassLoader {
    MyClassLoader myClassLoader = null;
    private  Map<String, JavaFileObject> fileObjects = new ConcurrentHashMap<>();


    private static class MyClassLoadersInstance {
        private static final MyClassLoader instance = new MyClassLoader();
    }

    private MyClassLoader() {
    }

    public static MyClassLoader getInstance() {
        return MyClassLoadersInstance.instance;
    }

    public Map<String, JavaFileObject> getFileObjects() {
        return fileObjects;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        JavaFileObject fileObject = fileObjects.get(name);
        if (fileObject != null) {
            byte[] bytes = ((MyJavaFileObject) fileObject).getCompiledBytes();
            return defineClass(name, bytes, 0, bytes.length);
        }
        try {
            return ClassLoader.getSystemClassLoader().loadClass(name);
        } catch (Exception e) {
            return super.findClass(name);
        }
    }
}
