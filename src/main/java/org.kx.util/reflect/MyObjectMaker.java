package org.kx.util.reflect;

import com.alibaba.fastjson.JSON;

import javax.tools.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class MyObjectMaker<T> {


    public T makeObject(String source) throws IllegalAccessException, InstantiationException, ClassNotFoundException {

        ClassLoader classloader = MyClassLoader.getInstance();
        Map<String, MyJavaFileObject> fileObjects = ((MyClassLoader) classloader).getFileObjects();


        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        JavaFileManager javaFileManager = new MyJavaFileManager(compiler.getStandardFileManager(collector, null, null), fileObjects);

        List<String> options = new ArrayList<>();
        options.add("-target");
        options.add("1.8");

        Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*");
        Matcher matcher = CLASS_PATTERN.matcher(source);
        String cls;
        if (matcher.find()) {
            cls = matcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in " + source);
        }

        JavaFileObject javaFileObject = new MyJavaFileObject(cls, source);
        compiler.getTask(null, javaFileManager, collector, options, null, Arrays.asList(javaFileObject)).call();

        Class<?> clazz = classloader.loadClass(cls);

        return (T) clazz.newInstance();

    }


    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        String code = "public class Man {\n" +
                "\tpublic String  name = \"skx\";" +
                "\tpublic Integer  age = 18;" +
                "\tpublic void hello(){\n" +
                "\t\tSystem.out.println(\"hello world\");\n" +
                "\t}\n" +
                "}";


        Object  man = new MyObjectMaker().makeObject(code);
        System.out.println(JSON.toJSONString(man));
    }

}