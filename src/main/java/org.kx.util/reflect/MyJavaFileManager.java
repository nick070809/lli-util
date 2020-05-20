package org.kx.util.reflect;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.util.Map;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class MyJavaFileManager  extends ForwardingJavaFileManager<JavaFileManager> {

    private Map<String, MyJavaFileObject> fileObjects;

    public MyJavaFileManager(JavaFileManager fileManager, Map<String, MyJavaFileObject> fileObjects) {
        super(fileManager);
        this.fileObjects = fileObjects;
    }

    @Override
    public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException {
        JavaFileObject javaFileObject = fileObjects.get(className);
        if (javaFileObject == null) {
            super.getJavaFileForInput(location, className, kind);
        }
        return javaFileObject;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String qualifiedClassName, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        String  className = null;
        if(qualifiedClassName.contains(".")){
            className = qualifiedClassName.substring(qualifiedClassName.lastIndexOf(".")+1);
        }else {
            className = qualifiedClassName ;
        }

        MyJavaFileObject javaFileObject = new MyJavaFileObject(qualifiedClassName, kind);
        fileObjects.put(className, javaFileObject);
        return javaFileObject;
    }
}