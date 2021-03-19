package org.kx.util.generate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/11/13
 */

public class ObjectUtil {
    public static Object getObjectFromBytes(byte[] objBytes) throws IOException,
            ClassNotFoundException {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream oi = new ObjectInputStream(bi);
        Object outObject = oi.readObject();
        return outObject;
    }
}
