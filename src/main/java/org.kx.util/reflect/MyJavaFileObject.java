package org.kx.util.reflect;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/5/20
 */

public class MyJavaFileObject extends SimpleJavaFileObject {

    private String source;
    private ByteArrayOutputStream outPutStream;
    private String qualifiedClassName;

    public MyJavaFileObject(String name, String source) {
        super(URI.create("String:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
        this.source = source;
    }

    public MyJavaFileObject(String name, Kind kind) {
        super(URI.create("String:///" + name + kind.extension), kind);
        source = null;
        this.qualifiedClassName = name;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        return source;
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        outPutStream = new ByteArrayOutputStream();
        return outPutStream;
    }

    public byte[] getCompiledBytes() {
        return outPutStream.toByteArray();
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }
}
