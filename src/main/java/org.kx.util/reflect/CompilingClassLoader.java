package org.kx.util.reflect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class CompilingClassLoader extends ClassLoader {
    protected void finalize() {
        System.out.println("finalize this:" + this);
        try {
            super.finalize();
        } catch (Throwable e) {
             e.printStackTrace();
        }

    }

    // 指定一个文件名，从磁盘读取整个文件内容，返回字节数组。
    private byte[] getBytes(String filename) throws IOException {
        // 获得文件大小。
        File file = new File(filename);
        long len = file.length();
        // 创建一个数组刚好可以存放文件的内容。
        byte raw[] = new byte[(int) len];
        // 打开文件
        FileInputStream fin = new FileInputStream(file);
        // 读取所有内容，如果没法读取，表示发生了一个错误。
        int r = fin.read(raw);
        if (r != len)
            throw new IOException("Can't read all, " + r + " != " + len);
        // 别忘了关闭文件。
        fin.close();
        // 返回这个数组。
        return raw;
    }

    // 产生一个进程来编译指定的Java源文件，制定文件参数.如果编译成功返回true，否者,
    // 返回false。
    private boolean compile(String javaFile) throws IOException {
        // 显示当前进度
        System.out.println("CCL: Compiling " + javaFile + "...");
        // 启动编译器
        Process p = Runtime.getRuntime().exec(
                "javac -classpath " + CompilingClassLoader.class.getResource("/").getPath()
                        + " -Xlint:unchecked " + javaFile);
        // 等待编译结束
        try {
            p.waitFor();
        } catch (InterruptedException ie) {
            System.out.println(ie);
        }
        // 检查返回码，看编译是否出错。
        int ret = p.exitValue();
        // 返回编译是否成功。
        return ret == 0;
    }

    // 类加载器的核心代码 -加载类在需要的时候自动编译源文件。
    public Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException {
        // if (!name.startsWith("com.ailk.dynamic")){
        // return getParent().loadClass(name);
        // }
        // 我们的目的是获得一个类对象。
        Class clas = null;
        // 首先，检查是否已经出理过这个类。
        clas = findLoadedClass(name);
        if (clas != null)
            return clas;

         // 通过类名获得路径名 比如：java.lang.Object => java/lang/Object
        String fileStub = name.replace('.', '/');
        // 构建指向源文件和类文件的对象。
        String javaFilename = CompilingClassLoader.class.getResource("/").getPath() + "reload/"
                + fileStub + ".java";
        //System.out.println(javaFilename);
        String classFilename = CompilingClassLoader.class.getResource("/").getPath() + "reload/"
                + fileStub + ".class";
        //System.out.println(classFilename);
        File javaFile = new File(javaFilename);
        File classFile = new File(classFilename);
        // 首先，判断是否需要编译。如果源文件存在而类文件不存在，或者都存在，但是源文件
        // 较新，说明需要编译。
        boolean javaExists = javaFile.exists();
        boolean classExists = classFile.exists();
        if (javaFile.exists()
                && (!classFile.exists() || javaFile.lastModified() > classFile
                .lastModified())) {
            try {
                // 编译，如果编译失败，我们必须声明失败原因（仅仅使用陈旧的类是不够的）。
                if (!compile(javaFilename) || !classFile.exists()) {
                    throw new ClassNotFoundException("Compile failed: "
                            + javaFilename);
                }
            } catch (IOException ie) {
                // 可能编译时出现IO错误。
                throw new ClassNotFoundException(ie.toString());
            }
        }
        // 确保已经正确编译或者不需要编译，我们开始加载原始字节。
        try {
            // 读取字节。
            byte raw[] = getBytes(classFilename);
            // 转化为类对象
            clas = defineClass(name, raw, 0, raw.length);
            System.out.println("load class:" + classFilename + " classloader is:" + this);
        } catch (IOException ie) {
            // 这里并不表示失败，可能我们处理的类在本地类库中，如java.lang.Object。
        }
        // System.out.println( "defineClass: "+clas );
        // 可能在类库中，以默认的方式加载。
        if (clas == null) {
            clas = findSystemClass(name);
            //System.out.println("use define class:"+name);
        }
        // System.out.println( "findSystemClass: "+clas );
        // 如果参数resolve为true，根据需要解释类。
        if (resolve && clas != null)
            resolveClass(clas);
        // 如果还没有获得类，说明出错了。
        if (clas == null)
            throw new ClassNotFoundException(name);
        // 否则，返回这个类对象。
        return clas;
    }
}