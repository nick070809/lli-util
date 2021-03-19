package org.kx.util.io;

import org.junit.Test;
import org.kx.util.FileUtil;

import java.io.*;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Description ：
 * Created by  xianguang.skx
 * Since 2020/9/8
 */

public class IoUtil {

    public static void Input2File(InputStream initialStream, String targetPath) throws IOException {

        // InputStream initialStream = new FileInputStream(new File("src/main/resources/sample.txt"));
        byte[] buffer = new byte[initialStream.available()];
        initialStream.read(buffer);

        File targetFile = new File(targetPath);
        OutputStream outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);

    }


    public static void DataInput2File(InputStream dataInputStream, String targetPath) throws IOException {

        String dirPath = targetPath.substring(0, targetPath.lastIndexOf("/"));
        FileUtil.createDir(dirPath);
        DataOutputStream out = new DataOutputStream(new FileOutputStream(targetPath));
        BufferedInputStream bufferedInput = new BufferedInputStream(dataInputStream);
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(out);

        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        //从文件中按字节读取内容，到文件尾部时read方法将返回-1
        while ((bytesRead = bufferedInput.read(buffer)) != -1) {
            bufferedOutput.write(buffer);
        }


        bufferedOutput.close();
        out.close();
        bufferedInput.close();
        dataInputStream.close();
    }


    public static void writeOrderToFile(Object obj, String filtPath) throws IOException {
        File file = new File(filtPath);
        FileOutputStream out;

        //创建文件
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        out = new FileOutputStream(file);
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        objOut.flush();
        objOut.close();

    }


    public static Object readObjectFromFile(String filePath) throws IOException, ClassNotFoundException {
        //Object temp = null;
        File file = new File(filePath);
        //FileInputStream in = new FileInputStream(file);
       // ObjectInputStream objIn = new ObjectInputStream(in);

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(fileToByteArray(file))));
        Object obj = ois.readObject();

       /* temp = objIn.readObject();
        objIn.close();*/
        return obj;
    }


    public static byte[] fileToByteArray(File src) {
        //选择流
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            //搬运
            bos = new ByteArrayOutputStream();
            fis = new FileInputStream(src);
            int temp;
            //用来暂时存放数据的，FileInputStream 的read方法会重复向里面读数据，
            //接着通过ByteArrayOutputStream写，这是一个重复的过程。直到temp= -1 代表读完。然后return。
            byte[] bt = new byte[1024*10];
            while((temp = fis.read(bt))!= -1) {
                bos.write(bt, 0, temp);
            }
            bos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关流
            try {
                if(null != fis)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }



    /*public    Object testDeserialize() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        BigInteger bi = new BigInteger("0");
        oos.writeObject(bi);
        byte[] str = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(str)));
        return  ois.readObject();
    }*/


    @Test
    public void testDeserializeTest() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        BigInteger bi = new BigInteger("0");
        oos.writeObject(bi);
        String str = baos.toString();
        System.out.println(str);
        ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new ByteArrayInputStream(str.getBytes())));
        Object obj = ois.readObject();
        assertEquals(obj.getClass().getName(), "java.math.BigInteger");
        assertEquals(((BigInteger) obj).intValue(), 0);
    }

    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        BigInteger bi = new BigInteger("0");
        oos.writeObject(bi);
        byte[] str = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(str)));
        Object obj = ois.readObject();
        assertNotNull(obj);
        assertEquals(obj.getClass().getName(), "java.math.BigInteger");
        assertEquals(((BigInteger) obj).intValue(), 0);
    }
}
